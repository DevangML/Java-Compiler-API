package com.example.javacompiler.controller;

import com.example.javacompiler.dto.CodeRequest;
import com.example.javacompiler.service.CompilerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compile")
public class CompilerController {

    private final CompilerService compilerService;

    public CompilerController(CompilerService compilerService) {
        this.compilerService = compilerService;
    }

    @PostMapping
    public ResponseEntity<String> compileCode(@RequestBody CodeRequest codeRequest) {
        String output = compilerService.compileAndRun(codeRequest.getCode());
        return ResponseEntity.ok(output);
    }

    @PostMapping("/interactive")
    public ResponseEntity<String> compileCodeWithArgs(@RequestBody CodeRequest codeRequest) {
        String output = compilerService.compileAndRunWithArgs(codeRequest.getCode(), codeRequest.getArgs());
        return ResponseEntity.ok(output);
    }
}
