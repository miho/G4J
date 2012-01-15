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

    public TemplateClass(
            String name, String templateArguments, String clsHeader) {
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

    @Override
    public boolean equals(Object obj) {
        
        // null or instance of incompatible class
        if (!(obj instanceof TemplateClass)) {
            return false;
        }
        
        TemplateClass other  = (TemplateClass) obj;
        
        if (!getName().equals(other.getName())) {
            return false;
        }
        
        if (!getTemplateArguments().equals(other.getTemplateArguments())) {
            return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 23 * hash + (this.templateArguments != null ? this.templateArguments.hashCode() : 0);
//        hash = 23 * hash + (this.clsHeader != null ? this.clsHeader.hashCode() : 0);
        return hash;
    }
    
    
}
