package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/transfers")
@PreAuthorize("isAuthenticated()")
public class TransferController {
    private final TransferDao transferDao;
    private final UserDao userDao;


    public TransferController(TransferDao transferDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{transferId}")
    public Transfer getTransferById(@PathVariable int transferId, Principal principal) {
        try {
            int userId = getCurrentUserId(principal);
            return transferDao.getTransferById(transferId);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving transfer", e);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping()
    public Transfer createNewTransfer(@Valid @RequestBody Transfer newTransfer, Principal principal) {
        try {
            return transferDao.createNewTransfer(newTransfer);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving transfer", e);
        }

    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/pending")
    public List<Transfer> getPendingTransfers(Principal principal) {
        try {
            int userId = getCurrentUserId(principal);
            return transferDao.getPendingTransfers(true, userId);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving transfer", e);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/approved")
    public List<Transfer> getApprovedTransfers(Principal principal) {
        try {
            int userId = getCurrentUserId(principal);
            return transferDao.getApprovedTransfers(true, userId);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving transfer", e);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/approved/{transferId}")
    public void approveTransfer(@PathVariable int transferId, Principal principal) {
        try {
            int rowsAffected = transferDao.approveTransfer(transferId);
            if (rowsAffected != 1) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error approving transfer");
            }
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving transfer", e);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/rejected")
    public List<Transfer> getRejectedTransfers(Principal principal) {
        try {
            int userId = getCurrentUserId(principal);
            return transferDao.getRejectedTransfers(true, userId);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving transfer", e);
        }
    }

    private int getCurrentUserId(Principal principal) {
        return userDao.getUserByUsername(principal.getName()).getId();
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/rejected/{transferId}")
    public void rejectTransfer(@PathVariable int transferId, Principal principal) {
        try {
            int rowsAffected = transferDao.rejectTransfer(transferId);
            if (rowsAffected != 1) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error rejecting transfer");
            }
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving transfer", e);
        }
    }

}










