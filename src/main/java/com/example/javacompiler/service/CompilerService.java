package com.example.javacompiler.service;

import org.springframework.stereotype.Service;

import javax.tools.*;
import java.io.*;
import java.util.Arrays;

@Service
public class CompilerService {

    public String compileAndRun(String code) {
        return compileAndRunWithArgs(code, new String[]{});
    }

    public String compileAndRunWithArgs(String code, String[] args) {
        String className = "Main";
        String javaFile = className + ".java";
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

        try {
            ProcessBuilder pb = new ProcessBuilder("java", className);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            for (String arg : args) {
                writer.write(arg + "\n");
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
}
