// Error reading included file Templates/Classes/../Licenses/license-2-clause-bsd.txt
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

    public String getID() {
        return id;
    }
}
