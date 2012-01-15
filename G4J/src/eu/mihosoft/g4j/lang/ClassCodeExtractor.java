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

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class ClassCodeExtractor implements StringProcessor {

    private static final String id = "Processor:classcode";
    private TemplateClass clazz;

    public ClassCodeExtractor(TemplateClass clazz) {
        this.clazz = clazz;
    }

    public String process(String code) {

        // filter comments, chars and strings
        FilterComments fC = new FilterComments();
        code = fC.process(code);
        FilterChars fCh = new FilterChars();
        code = fCh.process(code);
        FilterStrings fS = new FilterStrings();
        code = fS.process(code);

        StringBuilder result = new StringBuilder();

        String[] lines = code.split("\n");

        boolean insideOfClassImplementation = false;
        int blockCount = 0;


        for (String l : lines) {

            if (!insideOfClassImplementation
                    && Patterns.TEMPLATE_CLS_HEADER.matcher(l).find()) {
                insideOfClassImplementation = 
                        LangUtils.classNameFromTemplateClsHeader(l).
                        equals(clazz.getName());
            }

            if (insideOfClassImplementation) {
                for (int i = 0; i < l.length(); i++) {

                    char ch = l.charAt(i);

                    if (ch=='{') {
                        blockCount++;
                    }
                    
                    if (ch=='}') {
                        blockCount--;
                    }
                }

                if (insideOfClassImplementation) {
                    result.append(l).append("\n");
                }

                if (blockCount == 0) {
                    break;
                }
            }
        }

        return result.toString();
    }

    public String getID() {
        return id;
    }
}
