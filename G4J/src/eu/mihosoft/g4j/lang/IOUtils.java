// Error reading included file Templates/Classes/../Licenses/license-2-clause-bsd.txt
package eu.mihosoft.g4j.lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */


public class IOUtils {
    public static String readSampleCode(String codeName) {

        BufferedReader reader = null;
        String code = "";

        try {
            // load Sample Code
            InputStream iStream = IOUtils.class.getResourceAsStream(
                    codeName);

            reader = new BufferedReader(new InputStreamReader(iStream));

            while (reader.ready()) {
                code += reader.readLine() + "\n";
            }
        } catch (Exception ex) {
            Logger.getLogger(IOUtils.class.getName()).
                    log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(IOUtils.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }
        return code;
    }
}
