package com.backend.test.backendtest.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/hello") // Endpoint controller para hello world
class HelloWorldController {

    @GetMapping
    fun helloWorld(): String = "Hello, this is a REST endpoint!"
}