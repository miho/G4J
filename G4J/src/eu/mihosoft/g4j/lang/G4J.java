// Error reading included file Templates/Classes/../Licenses/license-2-clause-bsd.txt
package eu.mihosoft.g4j.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */


public class G4J implements StringProcessor{
    
    private static final String id = "Processor:g4j";

    public String process(String code) {

        ArrayList<TemplateClass> templateClasses = new ArrayList<TemplateClass>();
        ArrayList<TemplateClass> templateInstances = new ArrayList<TemplateClass>();


        TemplateClassProcessor tP =
                new TemplateClassProcessor(templateClasses, templateInstances);

        tP.process(code);

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

                Collection<TemplateClass> instances =
                        new ArrayList<TemplateClass>();

                for (TemplateClass t : tP.getTemplateInstances()) {
//                    System.out.println(" --> search: " + t);
                    if (tC.getName().equals(t.getName())) {
                        instances.add(t);
                    }
                }

                ClassCodeExtractor cE = new ClassCodeExtractor(tC);
                String templateClassCode = cE.process(code);

                for (TemplateClass tI : instances) {

//                    System.out.println(" --> tI: " + tI);

//                    System.out.println("Code: " + tI);

                    TemplateInstanceCodeCreator tIC =
                            new TemplateInstanceCodeCreator(tC, tI,
                            templateClasses, templateInstances);

//                    System.out.println(tIC.process(templateClassCode));

                    finalCode += tIC.process(templateClassCode);
                }
            }

            tP.process(finalCode);
        }


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

    public String getID() {
        return id;
    }
}
