package com.sipios.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = { "protected" })
public class TransferController {
    @RequestMapping
    @ApiOperation(value = "Hello World", notes = "A resource that returns hello world")
    public ResponseEntity<String> transferFunds() {
        return ResponseEntity.ok("Transfer Accepted");
    }
}
