/*
 * Copyright 2011 Michael Hoffer <info@michaelhoffer.de>. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 * 
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 * 
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY Michael Hoffer <info@michaelhoffer.de> "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL Michael Hoffer <info@michaelhoffer.de> OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of Michael Hoffer <info@michaelhoffer.de>.
 */

package eu.mihosoft.g4j.lang;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
class CodeAnalyzerImpl implements CodeAnalyzer {
    private static final String id = "ClassExtractor";

    public String getID() {
        return id;
    }

    public Collection<ClassEntry> analyze(CodeEntry code) {

        int depth = 0;

        Collection<ClassEntry> result = new ArrayList<ClassEntry>();

        String text = code.getCode();
        String header;
        
        Matcher classHeaderMatcher = Patterns.TEMPLATE_CLS_HEADER.matcher(text);

        if (classHeaderMatcher.find()) {

            int posBegin = classHeaderMatcher.start();

            System.out.println(">> class found!");

            int posEnd = classHeaderMatcher.end();

            header = text.substring(posBegin, posEnd);

            text = text.substring(posEnd);

            for (int i = 0; i < text.length();i++) {
                char c = text.charAt(i);

                boolean braceFound = false;

                if (c == '{') {
                    depth++;
                    braceFound = true;
                } else if (c == '}') {
                    depth--;
                    braceFound = true;
                }

                if (depth == 0 && braceFound) {
                    posEnd = i+1;
                    break;
                }
            }

            if (depth>0) {
                System.err.println(">> too many \"{\"!");
            } else if (depth<0) {
                System.err.println(">> too many \"}\"!");
            }

            text = header+text.substring(0,posEnd);

            result.add(new ClassEntryImpl("class",text));
        }

        return result;
    }

}

class ClassEntryImpl implements ClassEntry {
    private String code;
    private String name;
    private Collection<MethodEntry> methods = new ArrayList<MethodEntry>();
    private Collection<ClassEntry> classes = new ArrayList<ClassEntry>();
    private Collection<String> templateArguments = new ArrayList<String>();

    public ClassEntryImpl(String name, String code ) {
        this.code = code;
        this.name = name;
    }

    

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Collection<MethodEntry> getMethods() {
        return methods;
    }

    public Collection<String> getTemplateArguments() {
        return templateArguments;
    }

    public Collection<ClassEntry> getClasses() {
       return classes;
    }

    /**
     * @param methods the methods to set
     */
    public void setMethods(Collection<MethodEntry> methods) {
        this.methods = methods;
    }

    /**
     * @param classes the classes to set
     */
    public void setClasses(Collection<ClassEntry> classes) {
        this.classes = classes;
    }

    /**
     * @param templateArguments the templateArguments to set
     */
    public void setTemplateArguments(Collection<String> templateArguments) {
        this.templateArguments = templateArguments;
    }
    
}
