package com.softserve.controller.home.api;

import com.softserve.dto.UserDTO;
import com.softserve.exceptions.CustomException;
import com.softserve.exceptions.UserException;
import com.softserve.security.user.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user/")
public class HomeRestController {

    @Autowired
    private UserServices userServices;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(value = "create-account", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAccountSave(@RequestBody Map<String, String> json) {
        return this.userServices.saveAccount(json);
    }
}
