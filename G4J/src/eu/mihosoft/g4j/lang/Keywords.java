// Error reading included file Templates/Classes/../Licenses/license-gplv3classpath.txt
package eu.mihosoft.g4j.lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * mIndicates whether the specified word is a reserved language keyword
   (currently only java and groovy are supported).
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Keywords {

    private static Set<String> keywords = new HashSet<String>();

    static {
        
        // Java Keywords
        String keywordString = readKeyWords(
                "/eu/mihosoft/vrl/lang/keywords_java6");
        keywords.addAll(Arrays.asList(keywordString.split("\n")));
        
        // Groovy Keywords
        keywordString = readKeyWords(
                "/eu/mihosoft/vrl/lang/keywords_groovy");
        
        keywords.addAll(Arrays.asList(keywordString.split("\n")));
    }

    /**
     * Indicates whether the specified word is a reserved language keyword
     * (currently only java and groovy are supported).
     * @param word word to check
     * @return <code>true</code> if the specified word is a reserved
     *         language keyword; <code>false</code> otherwise
     */
    public static boolean isKeyword(String word) {

        for (String s : keywords) {
            if (s.equals(word)) {
                return true;
            }
        }

        return false;
    }
    
    /**
     * Returns all reserved language keywords
     * (currently only java and groovy are supported).
     * @return all reserved language keywords
     */
    public static Iterable<String> getKeywords() {
        return keywords;
    }

    private static String readKeyWords(String codeName) {
        // load Sample Code
        InputStream iStream = Keywords.class.getResourceAsStream(
                codeName);

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(iStream));

        String code = "";

        try {
            while (reader.ready()) {
                String line = reader.readLine();
                // lines starting with # and empty lines are ignored
                if (line.trim().startsWith("#") || line.trim().isEmpty()) {
                    continue;
                }
                code += line + "\n";
            }
        } catch (IOException ex) {
            Logger.getLogger(Keywords.class.getName()).
                    log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(Keywords.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }
        return code;
    }
}
