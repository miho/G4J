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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miho
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String code = IOUtils.readSampleCode("/eu/mihosoft/g4j/lang/samples/MathSample01.g4j");
        code+="\n"+IOUtils.readSampleCode("/eu/mihosoft/g4j/lang/samples/Main.g4j");
        
       
        TemplateClassProcessor tP = new TemplateClassProcessor();
        String result = tP.process(code);
      
        
        System.out.println("Template Classes:");
        
        for (TemplateClass tC : tP.getTemplateClasses()) {
            System.out.println(tC);
        }
        
        System.out.println("Template Instances:");
        
        for (TemplateClass tC : tP.getTemplateInstances()) {
            System.out.println(tC);
        }


//        CodeAnalyzerImpl analyzer = new CodeAnalyzerImpl();
//
//        FilterComments rC = new FilterComments();
//        code = rC.process(code);
//
//        Collection<ClassEntry> classes = analyzer.analyze(new CodeEntry(code));
//
//        for (ClassEntry cE : classes) {
//            System.out.println("Class (" + cE.getName() + "): \n" + cE.getCode());
//        }


        
        
    }

}
