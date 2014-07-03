/* 
 * G4J.java
 * 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2009–2012 Steinbeis Forschungszentrum (STZ Ölbronn),
 * Copyright (c) 2006–2012 by Michael Hoffer
 * 
 * This file is part of Visual Reflection Library (VRL).
 *
 * VRL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * see: http://opensource.org/licenses/LGPL-3.0
 *      file://path/to/VRL/src/eu/mihosoft/vrl/resources/license/lgplv3.txt
 *
 * VRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * This version of VRL includes copyright notice and attribution requirements.
 * According to the LGPL this information must be displayed even if you modify
 * the source code of VRL. Neither the VRL Canvas attribution icon nor any
 * copyright statement/attribution may be removed.
 *
 * Attribution Requirements:
 *
 * If you create derived work you must do three things regarding copyright
 * notice and author attribution.
 *
 * First, the following text must be displayed on the Canvas:
 * "based on VRL source code". In this case the VRL canvas icon must be removed.
 * 
 * Second, the copyright notice must remain. It must be reproduced in any
 * program that uses VRL.
 *
 * Third, add an additional notice, stating that you modified VRL. In addition
 * you must cite the publications listed below. A suitable notice might read
 * "VRL source code modified by YourName 2012".
 * 
 * Note, that these requirements are in full accordance with the LGPL v3
 * (see 7. Additional Terms, b).
 *
 * Publications:
 *
 * M. Hoffer, C.Poliwoda, G.Wittum. Visual Reflection Library -
 * A Framework for Declarative GUI Programming on the Java Platform.
 * Computing and Visualization in Science, 2011, in press.
 */
package eu.mihosoft.g4j.lang;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class G4J implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String id = "Processor:g4j";

    String globalCode = "";

    public List<Code> process(List<Code> codes) {

        globalCode = "";

        for (Code code : codes) {
            globalCode+="//"+code.getFile().toString() + "\n" + code.getCode()+"\n";
        }

        List<TemplateClass> templateClasses = new ArrayList<TemplateClass>();
        List<TemplateClass> templateInstances = new ArrayList<TemplateClass>();

        List<Code> finalCodes = new ArrayList<>();

        for (Code code : codes) {
            System.out.println(" --- processing codefile: " + code.getFile() + " ---");
            finalCodes.addAll(
                    process(code,
                            templateClasses,
                            templateInstances));
        }

        return finalCodes;
    }

//    public String process(String code) {
//
//        List<TemplateClass> templateClasses = new ArrayList<TemplateClass>();
//        List<TemplateClass> templateInstances = new ArrayList<TemplateClass>();
//
//        return process(code, templateClasses, templateInstances);
//    }
    private List<Code> process(Code codeObj,
            List<TemplateClass> templateClasses,
            List<TemplateClass> templateInstances) {

        List<Code> codes = new ArrayList<Code>();

        TemplateClassProcessor tP
                = new TemplateClassProcessor(templateClasses, templateInstances);

        String code = codeObj.getCode();

        tP.process(code);

        // filter template instances, e.g., classes that seem to be
        // template instances based on the view limited to a single file
        // but which are template classes that are already defined in other
        // files
        List<TemplateClass> instancesBeforeProcess = new ArrayList<TemplateClass>(templateInstances);
        List<TemplateClass> delList = new ArrayList<TemplateClass>();
        for (TemplateClass tCls : templateClasses) {
            for (TemplateClass tInst : instancesBeforeProcess) {
                if (tInst.getName().equals(tCls.getName())) {
                    delList.add(tCls);
                }
            }
        }
        templateInstances.removeAll(delList);

        String finalCode = "// processed code\n\n";

        int oldLength = 0;
        int counter = 0;

        while (finalCode.length() > oldLength) {

            System.out.println("\n >> --- G4J Pass " + counter + " ---\n");
            oldLength = finalCode.length();

            finalCode = "// processed code\n" + "// --> passes: " + counter + "\n\n";

            counter++;

            System.out.println("Template Classes:");

            for (TemplateClass tC : tP.getTemplateClasses()) {
                System.out.println(tC);
            }

            System.out.println("Template Instances:");

            for (TemplateClass tC : tP.getTemplateInstances()) {
                System.out.println(tC);
            }

            for (TemplateClass tC : tP.getTemplateClasses()) {

//                System.out.println(">> tC : " + tC);
                Collection<TemplateClass> instances
                        = new ArrayList<TemplateClass>();

                for (TemplateClass t : tP.getTemplateInstances()) {
//                    System.out.println(" --> search: " + t);
                    if (tC.getName().equals(t.getName())) {
                        instances.add(t);
                    }
                }

                ClassCodeExtractor cE = new ClassCodeExtractor(tC);

                String templateClassCode = cE.process(code);

                for (TemplateClass tI : instances) {

//                     System.out.println("Code: " + tI.getClsHeader());
//                    System.out.println(" --> tI: " + tI);
//                    System.out.println("Code: " + tI.getClsHeader());
                    TemplateInstanceCodeCreator tIC
                            = new TemplateInstanceCodeCreator(tC, tI,
                                    templateClasses, templateInstances);

//                    System.out.println(tIC.process(templateClassCode));
                    
                    String localCode = tIC.process(templateClassCode);
                    finalCode += localCode;
               
                    codes.add(new Code(codeObj.getFile(), convertTemplateArgs(localCode), tI.getTemplateArguments()));
                }
            }

            tP.process(finalCode);
        }

        convertTemplateArgs(finalCode);

        return codes;
    }

    private String convertTemplateArgs(String finalCode) {
        // finally convert <<T>,V> to T_V notation
        Matcher m = Patterns.TEMPLATE_ARGUMENT.matcher(finalCode);

        while (m.find()) {

            String templateArgs = m.group();

            int start = m.start();
            int end = m.end();

            templateArgs = templateArgs.
                    replaceFirst("<<", "").
                    replaceFirst(">>", "").trim();

            String codeBefore = finalCode.substring(0, start).trim();
            String replacement = TemplateClassProcessor.
                    replaceTemplateArguments(templateArgs);
            String codeAfter = finalCode.substring(end);

            finalCode = codeBefore + replacement + codeAfter;

            m = Patterns.TEMPLATE_ARGUMENT.matcher(finalCode);
        }
        
        return finalCode;
    }

}
