package com.binhtv.apigateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallbacks")
public class GatewayFallback {

    @GetMapping("/product")
    public ResponseEntity<String> productFallback(){
        return new ResponseEntity<>("Something went wrong for this operation. Please try later...", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @PostMapping("/product")
    public ResponseEntity<String> productPostFallback(){
        return new ResponseEntity<>("Something went wrong for this operation. Please try later...", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping("/order")
    public ResponseEntity<String> orderFallback(){
        return new ResponseEntity<>("Something went wrong for this operation. Please try later...", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @PostMapping("/order")
    public ResponseEntity<String> orderPostFallback(){
        return new ResponseEntity<>("Something went wrong for this operation. Please try later...", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping("/auth")
    public ResponseEntity<String> authFallback(){
        return new ResponseEntity<>("Something went wrong for this operation. Please try later...", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @PostMapping("/auth")
    public ResponseEntity<String> authPostFallback(){
        return new ResponseEntity<>("Something went wrong for this operation. Please try later...", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping("/user")
    public ResponseEntity<String> userFallback(){
        return new ResponseEntity<>("Something went wrong for this operation. Please try later...", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @PostMapping("/user")
    public ResponseEntity<String> userPostFallback(){
        return new ResponseEntity<>("Something went wrong for this operation. Please try later...", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
