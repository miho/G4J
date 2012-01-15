// Error reading included file Templates/Classes/../Licenses/license-2-clause-bsd.txt
package eu.mihosoft.g4j.lang;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */


public class TemplateClass {
    private String name;
    private String templateArguments;
    private String code;

    public TemplateClass(String name, String templateArguments, String code) {
        this.name = name;
        this.templateArguments = templateArguments;
        this.code = code;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the templateArguments
     */
    public String getTemplateArguments() {
        return templateArguments;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }


    @Override
    public String toString() {
        return "Name: " + getName() + ", TemplateArguments: " + getTemplateArguments();
    }
    
    
}
