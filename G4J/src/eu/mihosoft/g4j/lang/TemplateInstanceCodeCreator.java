/* 
 * TemplateInstanceCodeCreator.java
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class TemplateInstanceCodeCreator implements StringProcessor {

    private static final String id = "Processor:codecreator";
    private TemplateClass templateClass;
    private TemplateClass templateInstance;
    private Collection<TemplateClass> templateClasses;
    private Collection<TemplateClass> templateInstances;

    public TemplateInstanceCodeCreator(TemplateClass templateClass,
            TemplateClass templateInstance,
            Collection<TemplateClass> templateClasses,
            Collection<TemplateClass> templateInstances) {
        this.templateClass = templateClass;
        this.templateInstance = templateInstance;
        this.templateClasses = templateClasses;
        this.templateInstances = templateInstances;
    }

    private String replaceTemplateArguments(String args) {
        String[] templateArgs = args.split(",");

        StringBuilder result = new StringBuilder();

        for (String arg : templateArgs) {

            arg = arg.trim();

            result.append("_").append(arg);
        }

        return result.toString();
    }

    @Override
    public String process(String code) {

        String originalCode = code;

        // filter comments, chars and strings
        FilterComments fC = new FilterComments();
        code = fC.process(code);
//        FilterChars fCh = new FilterChars();
//        code = fCh.process(code);
//        FilterStrings fS = new FilterStrings();
//        code = fS.process(code);

        StringBuilder result = new StringBuilder();

        TemplateArgumentsExtractor tAE = new TemplateArgumentsExtractor();

        Matcher m = Patterns.TEMPLATE_CLS_HEADER.matcher(code);

        while (m.find()) {

            String templateClsHeader = m.group();

            String templateArguments = tAE.process(templateClsHeader);

            String newTemplateClsHeader =
                    templateClsHeader.replaceAll(
                    "\\s*<<\\s*" + templateArguments + "\\s*>>",
                    replaceTemplateArguments(
                    templateInstance.getTemplateArguments()));

            originalCode = originalCode.replaceFirst(
                    templateClsHeader, newTemplateClsHeader);

            result.append(templateClsHeader).append("\n");
        }

        String[] templateArguments =
                templateClass.getTemplateArguments().split(",");
        String[] templateInstanceArguments =
                templateInstance.getTemplateArguments().split(",");

        for (int i = 0; i < templateArguments.length; i++) {
//            Matcher m2 = Pattern.compile("\\b"+arg+"\\b").matcher(originalCode);

            String arg = templateArguments[i];
            String instanceArg = templateInstanceArguments[i];
            originalCode = originalCode.replaceAll(
                    "\\b" + arg + "\\b", instanceArg);
        }

        return originalCode;
    }

    @Override
    public String getID() {
        return id;
    }
}
