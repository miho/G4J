package eu.mihosoft.g4j.lang;

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


/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class FilterChars implements StringProcessor {

    private static final String id = "FilterChars";

    @Override
    public String process(String code) {

        StringBuilder result = new StringBuilder();

        String[] lines = code.split("\n");

        for (String l : lines) {

            boolean insideString = false;
            boolean insideChar = false;
            boolean insideEscape = false;

            for (int i = 0; i < l.length(); i++) {

                char ch = l.charAt(i);

                if (ch == '\\') {
                    insideEscape = !insideEscape;
                }
                
                // find double quotes (handles encapsulated quotes correctly)
                if (ch == '\"' && !(insideEscape || insideChar)) {
                    insideString = !insideString;
                }

                // find quotes (handles encapsulated quotes correctly)
                if (ch == '\'' && !(insideEscape || insideString)) {
                    insideChar = !insideChar;
                }

                // we print the character if we are not inside escape or inside char
                if (!insideChar && (ch != '\'' || insideString || insideEscape)) {
                    result.append(ch);
                }

                // current char is no \ (backslash).
                // thus, we are defenitely not "inside escape"
                if (ch != '\\') {
                    insideEscape = false;
                }
            }

            if (!insideString) {
                result.append('\n');
            }

        }

        return result.toString();
    }

    @Override
    public String getID() {
        return id;
    }
}
