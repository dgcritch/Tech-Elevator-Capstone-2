package com.techelevator.tenmo.controller;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.RegisterUserDto;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

    @RestController
    @RequestMapping("/users")
    @PreAuthorize("isAuthenticated()")
    public class UserController {
        private final UserDao userDao;
        private int getCurrentUserId(Principal principal) {
            return userDao.getUserByUsername(principal.getName()).getId();
        }
        public UserController(UserDao userDao) {
            this.userDao = userDao;

        }

        @PreAuthorize("hasRole('USER')")
        @GetMapping
        public List<User> getUsers(Principal principal) {
            try {
                int userId = getCurrentUserId(principal);
                return userDao.getUsers();
            } catch (DaoException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving transfer", e);
            }
        }

        @PreAuthorize("hasRole('USER')")
        @GetMapping("/currentuser")
        public User getUserById(Principal principal) {
            try {
                int userId = getCurrentUserId(principal);
                return userDao.getUserById(userId);
            } catch (DaoException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving transfer", e);
            }
        }

        @PreAuthorize("hasRole('USER')")
        @GetMapping("/username")
        public User getUserByUsername(@PathVariable String username, Principal principal) {
            try {
                int userId = getCurrentUserId(principal);
                return userDao.getUserByUsername(username);
            } catch (DaoException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving transfer", e);
            }
        }

        @PreAuthorize("hasRole('USER')")
        @ResponseStatus(HttpStatus.CREATED)
        @PostMapping
        public User createUser(@Valid @RequestBody RegisterUserDto user, Principal principal) {
            try {
                int userId = getCurrentUserId(principal);
                return userDao.createUser(user);
            } catch (DaoException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving transfer", e);
            }
        }


    }


