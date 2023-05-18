package com.iablonski.mynetwork.controller;

import com.iablonski.mynetwork.dto.UserDTO;
import com.iablonski.mynetwork.entity.User;
import com.iablonski.mynetwork.facade.UserFacade;
import com.iablonski.mynetwork.service.UserService;
import com.iablonski.mynetwork.validation.ResponseErrorValid;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/user")
@CrossOrigin
public class UserController {
    private UserService userService;
    private UserFacade userFacade;
    private ResponseErrorValid responseErrorValid;

    @Autowired
    public UserController(UserService userService, UserFacade userFacade, ResponseErrorValid responseErrorValid) {
        this.userService = userService;
        this.userFacade = userFacade;
        this.responseErrorValid = responseErrorValid;
    }

    @GetMapping("/")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal){
        User user = userService.getUserFromPrincipal(principal);
        UserDTO userDTO = userFacade.userToUserDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable("userId") String userId){
        User user = userService.getUserById(Long.parseLong(userId));
        UserDTO userDTO = userFacade.userToUserDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO userDTO,
                                             BindingResult bindingResult,
                                             Principal principal){
        ResponseEntity<Object> errors = responseErrorValid.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        User user = userService.updateUserProfile(userDTO, principal);
        UserDTO updatedUser = userFacade.userToUserDTO(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }


}
