package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/account")
@PreAuthorize("isAuthenticated()")
public class AccountController {
    private final TransferDao transferDao;
    private final AccountDao accountDao;
    private final UserDao userDao;

    private int getCurrentUserId(Principal principal) {

        return userDao.getUserByUsername(principal.getName()).getId();
    }

    public AccountController(AccountDao accountDao, UserDao userDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDao = transferDao;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{accountId}")
    public Account getAccountById(@PathVariable int accountId, Principal principal) {
        try {
            int userId = getCurrentUserId(principal);
            return accountDao.getAccountById(accountId, userId);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving account", e);
        }
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/balance/{accountId}")
    public BigDecimal getAccountBalance(@PathVariable int accountId, Principal principal) {
        try {
            int userId = getCurrentUserId(principal);
            return accountDao.getAccountBalance(accountId, userId);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving account balance", e);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/transfers/{accountFrom}") //remember accountFrom corresponds to account_id in DB.
    public List<Transfer> getTransfersFromAccount(@PathVariable int accountFrom, Principal principal) {
        try {
            int userId = getCurrentUserId(principal);
            return transferDao.getTransfersFromAccount(accountFrom, userId);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving account transfers", e);
        }
    }




}
