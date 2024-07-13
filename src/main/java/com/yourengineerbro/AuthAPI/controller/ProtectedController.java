package com.yourengineerbro.AuthAPI.controller;

// ProtectedController.java

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.yourengineerbro.AuthAPI.util.Constants.Messages.MESSAGE;
import static com.yourengineerbro.AuthAPI.util.Constants.Path.PROTECTED_PATH;

@RestController
@RequestMapping(PROTECTED_PATH)
public class ProtectedController {

    @GetMapping
    public String getProtectedResource() {
        return MESSAGE;
    }
}
