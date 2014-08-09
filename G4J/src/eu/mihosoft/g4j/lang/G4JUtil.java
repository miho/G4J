/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.g4j.lang;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
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

//    public static boolean processFile(
//            Path srcFile,
//            Path destFile,
//            String packageName) throws IOException {
//
//        String code = new String(Files.readAllBytes(srcFile), "UTF-8");
//
//        G4J g4j = new G4J();
//        code = g4j.process(code);
//
//        Files.write(destFile, code.getBytes("UTF-8"));
//
//        return false;
//    }

//    public static void processDir(
//            Path inputDir,
//            Path outputDir) {
//            processDir(inputDir, outputDir);
//    }
    
//    public static void processDirAndMerge(
//            Path inputDir,
//            Path outputFile, String packageName) {
//        areDirs(inputDir);
//
//        G4JVisitor g4jVisitor
//                = new G4JVisitor();
//        try {
//            Files.walkFileTree(inputDir, g4jVisitor);
//        } catch (IOException ex) {
//            Logger.getLogger(G4JUtil.class.getName()).
//                    log(Level.SEVERE, null, ex);
//        }
//
//        String code = g4jVisitor.getMergedCode();
//
//        G4J g4j = new G4J();
//        code = g4j.process(code);
//        
//        code = "package " + packageName + ";\n\n" + code;
//
//        if (!Files.exists(outputFile.toAbsolutePath().getParent())) {
//            try {
//                Files.createDirectories(outputFile.getParent());
//            } catch (IOException ex) {
//                Logger.getLogger(G4JUtil.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//        try {
//            Files.write(outputFile, code.getBytes("UTF-8"));
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(G4JUtil.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(G4JUtil.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
    public static void processDir(
            Path inputDir,
            Path outputDir, String packageName) {
        areDirs(inputDir);

        G4JVisitor g4jVisitor
                = new G4JVisitor();
        try {
            Files.walkFileTree(inputDir, g4jVisitor);
        } catch (IOException ex) {
            Logger.getLogger(G4JUtil.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        G4J g4j = new G4J();
        List<Code> codes = g4j.process(g4jVisitor.getCodes());
        
        for(Code c : codes) {
            
            System.out.println("c: " + c.getFile());
        
            String code = "package " + packageName + ";\n\n" + c.getCode();

            String fName = c.getFile().getFileName().toString();
            fName = fName.substring(0,fName.length()-4) + "_"+c.getTemplateArguments() + ".java";
            
            File outputFile = new File(outputDir.toAbsolutePath().toString(), fName);
//
//            if (!Files.exists(outputDir)) {
//                try {
//                    Files.createDirectories(outputDir);
//                } catch (IOException ex) {
//                    Logger.getLogger(G4JUtil.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }

            try {
                Files.write(outputFile.toPath(), code.getBytes("UTF-8"));
                
                System.out.println("written: " + outputFile.toPath());
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(G4JUtil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(G4JUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        
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

    private String mergedCode = "";
    private final List<Code> codes = new ArrayList<Code>();

    public G4JVisitor() {
        //
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        if (file.toString().endsWith(".g4j")) {
            System.out.println(" -> processing: " + file.toString());

            String code = new String(Files.readAllBytes(file), "UTF-8");
            
            mergedCode += code;
            
            codes.add(new Code(file, code));
        }

        return FileVisitResult.CONTINUE;
    }

    /**
     * @return the finalCode
     */
    public String getMergedCode() {
  
        return mergedCode;
    }

    public List<Code> getCodes() {
        return codes;
    }
    
    

}
