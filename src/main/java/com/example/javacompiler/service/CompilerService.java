package com.example.javacompiler.service;

import org.springframework.stereotype.Service;

import javax.tools.*;
import java.io.*;
import java.util.UUID;

@Service
public class CompilerService {

    private static final String BASE_DIR = "interactive_sessions/";

    public String startInteractiveSession(String code) {
        String sessionId = UUID.randomUUID().toString();
        String className = "Main";
        String javaFile = BASE_DIR + sessionId + "/" + className + ".java";
        File sessionDir = new File(BASE_DIR + sessionId);
        if (!sessionDir.exists()) {
            sessionDir.mkdirs();
        }

        File sourceFile = new File(javaFile);
        try (PrintWriter out = new PrintWriter(sourceFile)) {
            out.println(code);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "Error: Unable to write source file.";
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            return "Error: Java compiler not available.";
        }

        int compilationResult = compiler.run(null, null, null, javaFile);
        if (compilationResult != 0) {
            return "Error: Compilation failed.";
        }

        return sessionId;
    }

    public String runInteractiveSession(String sessionId, String[] inputs) {
        String className = "Main";
        String javaFile = BASE_DIR + sessionId + "/" + className + ".java";

        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-cp", BASE_DIR + sessionId, className);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            for (String input : inputs) {
                writer.write(input + "\n");
            }
            writer.flush();
            writer.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            reader.close();

            return output.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: Unable to execute compiled class.";
        }
    }

    public String getInteractivePrompt(String sessionId) {
        String className = "Main";
        String javaFile = BASE_DIR + sessionId + "/" + className + ".java";

        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-cp", BASE_DIR + sessionId, className);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    break;
                }
                output.append(line).append("\n");
            }
            reader.close();

            return output.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: Unable to execute compiled class.";
        }
    }
}
