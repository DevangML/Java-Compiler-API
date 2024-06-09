package com.example.javacompiler.controller;

import com.example.javacompiler.dto.CodeRequest;
import com.example.javacompiler.dto.InputRequest;
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

    @PostMapping("/start")
    public ResponseEntity<String> startInteractiveSession(@RequestBody CodeRequest codeRequest) {
        String sessionId = compilerService.startInteractiveSession(codeRequest.getCode());
        return ResponseEntity.ok(sessionId);
    }

    @PostMapping("/interact/{sessionId}")
    public ResponseEntity<String> runInteractiveSession(@PathVariable String sessionId, @RequestBody InputRequest inputRequest) {
        String output = compilerService.runInteractiveSession(sessionId, inputRequest.getInputs());
        return ResponseEntity.ok(output);
    }

    @GetMapping("/prompt/{sessionId}")
    public ResponseEntity<String> getInteractivePrompt(@PathVariable String sessionId) {
        String prompt = compilerService.getInteractivePrompt(sessionId);
        return ResponseEntity.ok(prompt);
    }
}
