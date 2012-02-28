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
public class TemplateClassProcessor implements StringProcessor {

    private static final String id = "Processor:templates";
    private Collection<TemplateClass> templateClasses;
    private Collection<TemplateClass> templateInstances;

    public TemplateClassProcessor(Collection<TemplateClass> templateClasses,
            Collection<TemplateClass> templateInstances) {
        this.templateClasses = templateClasses;
        this.templateInstances = templateInstances;
    }

    public static String replaceTemplateArguments(String args) {
        String[] templateArgs = args.split(",");

        StringBuilder result = new StringBuilder();

        for (String arg : templateArgs) {

            arg = arg.trim();

            result.append("_").append(arg);
        }

        return result.toString();
    }

    public String process(String code) {

        String originalCode = code;

        // filter comments, chars and strings
        FilterComments fC = new FilterComments();
        code = fC.process(code);
        FilterChars fCh = new FilterChars();
        code = fCh.process(code);
        FilterStrings fS = new FilterStrings();
        code = fS.process(code);

        StringBuilder result = new StringBuilder();

        TemplateArgumentsExtractor tAE = new TemplateArgumentsExtractor();

        Matcher m = Patterns.TEMPLATE_CLS_HEADER.matcher(code);

        while (m.find()) {

            String templateClsHeader = m.group();

            String templateArguments = tAE.process(templateClsHeader);

            String newTemplateClsHeader =
                    templateClsHeader.replaceAll(
                    "\\s*<<\\s*" + templateArguments + "\\s*>>",
                    replaceTemplateArguments(templateArguments));

            originalCode = originalCode.replaceFirst(
                    templateClsHeader, newTemplateClsHeader);

            result.append(templateClsHeader).append("\n");

            TemplateClass tC = new TemplateClass(
                    LangUtils.classNameFromTemplateClsHeader(templateClsHeader),
                    templateArguments, code);

            if (!getTemplateClasses().contains(tC)) {
                getTemplateClasses().add(tC);
            }
        }

        Matcher m2 = Patterns.TEMPLATE_CLS.matcher(code);

        while (m2.find()) {

            String templateInstance = m2.group();

            if (templateInstance.contains("class ")) {
                continue;
            }

            String templateArguments = tAE.process(templateInstance);

            TemplateClass tI = new TemplateClass(
                    templateInstance.replaceAll("<<.*>>", "").trim(),
                    templateArguments, code);

            if (!getTemplateInstances().contains(tI) 
                    && !getTemplateClasses().contains(tI)) {
                getTemplateInstances().add(tI);
            }
        }

        return originalCode;
    }

    public String getID() {
        return id;
    }

    /**
     * @return the classes
     */
    public Collection<TemplateClass> getTemplateClasses() {
        return templateClasses;
    }

    /**
     * @return the templateInstances
     */
    public Collection<TemplateClass> getTemplateInstances() {
        return templateInstances;
    }
}
