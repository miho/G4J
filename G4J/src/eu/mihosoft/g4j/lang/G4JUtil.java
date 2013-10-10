/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.g4j.lang;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This utility class provides methods for easily add/replace license header
 * information in .java source files.
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class G4JUtil {

    private G4JUtil() {
        throw new AssertionError("Don't instanciate me!");
    }

    public static boolean processFile(
            Path srcFile,
            Path destFile,
            String packageNanme) throws IOException {

        String code = new String(Files.readAllBytes(srcFile), "UTF-8");

        G4J g4j = new G4J();
        code = g4j.process(code);

        Files.write(destFile, code.getBytes("UTF-8"));

        return false;
    }

//    public static void processDir(
//            Path inputDir,
//            Path outputDir) {
//            processDir(inputDir, outputDir);
//    }
    public static void processDir(
            Path inputDir,
            Path outputFile, String packageName) {
        areDirs(inputDir);

        G4JVisitor g4jVisitor
                = new G4JVisitor();
        try {
            Files.walkFileTree(inputDir, g4jVisitor);
        } catch (IOException ex) {
            Logger.getLogger(G4JUtil.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        String code = g4jVisitor.getFinalCode();

        G4J g4j = new G4J();
        code = g4j.process(code);
        
        try {
            Files.write(outputFile, code.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(G4JUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(G4JUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void areDirs(Path... paths) {
        for (Path path : paths) {
            Objects.requireNonNull(path);
            if (!Files.isDirectory(path)) {
                throw new IllegalArgumentException(
                        String.format("%s is not a directory", path.toString()));
            }
        }
    }
}

/**
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
class G4JVisitor extends SimpleFileVisitor<Path> {

    private String packageName;
    private String finalCode = "";

    public G4JVisitor() {
        //
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        if (file.toString().endsWith(".g4j")) {
            System.out.println(" -> processing: " + file.toString());

            finalCode += new String(Files.readAllBytes(file), "UTF-8");
        }

        return FileVisitResult.CONTINUE;
    }

    /**
     * @return the finalCode
     */
    public String getFinalCode() {
        return finalCode;
    }

}
