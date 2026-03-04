package com.example.sandbox.controller;

import com.example.sandbox.entity.MyEntity;
import com.example.sandbox.service.MyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class MyController {

    private final MyService userService;

}
