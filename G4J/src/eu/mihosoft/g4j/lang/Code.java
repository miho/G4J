/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.g4j.lang;

import java.nio.file.Path;

/**
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class Code {

    private final Path file;
    private final String code;
    private final String templateArguments;

    public Code(Path file, String code, String templateArguments) {
        this.file = file;
        this.code = code;
        this.templateArguments = templateArguments;
    }
    
        public Code(Path file, String code) {
        this.file = file;
        this.code = code;
        this.templateArguments = "";
    }

    public Path getFile() {
        return file;
    }

    public String getCode() {
        return code;
    }

    public String getTemplateArguments() {
        return templateArguments;
    }
    
    

}
