package com.example.sandbox.service;

import com.example.sandbox.entity.MyEntity;
import com.example.sandbox.repository.MyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService")
class MyServiceTest {

    @Mock
    private MyRepository userRepository;

    @InjectMocks
    private MyService userService;

    private MyEntity entity;

    @BeforeEach
    void setUp() {

    }


}
