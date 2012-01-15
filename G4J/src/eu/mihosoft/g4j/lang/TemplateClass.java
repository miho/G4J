// Error reading included file Templates/Classes/../Licenses/license-2-clause-bsd.txt
package eu.mihosoft.g4j.lang;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */


public class TemplateClass {
    private String name;
    private String templateArguments;
    private String clsHeader;

    public TemplateClass(String name, String templateArguments, String clsHeader) {
        this.name = name;
        this.templateArguments = templateArguments;
        this.clsHeader = clsHeader;
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
    public String getClsHeader() {
        return clsHeader;
    }


    @Override
    public String toString() {
        return "Name: " + getName() + ", TemplateArguments: " + getTemplateArguments();
    }
    
    
}
