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


/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class FilterComments implements StringProcessor {

    private static final String id = "FilterComments";

    @Override
    public String process(String code) {
        
        StringBuilder result = new StringBuilder();

        String[] lines = code.split("\n");

        boolean multiLineComment = false;
        boolean insideComment = false;

        for (String l : lines) {

            char lastChar = 0;
            boolean doubleQuotes = false;
            boolean singleLineComment = false;
            boolean multiLineCommentStop = false;

            for (int i = 0; i < l.length(); i++) {

                char ch = l.charAt(i);

                // find double quotes (handles encapsulated quotes correctly)
                if (ch == '\"' && lastChar != '\\') {
                    doubleQuotes = !doubleQuotes;
                }

                // single-line comment
                if (ch == '/' && lastChar == '/') {
                    singleLineComment = !doubleQuotes || singleLineComment;
                }

                // multi-line comment start
                if (lastChar == '/' && ch == '*') {
                    multiLineComment = (!doubleQuotes || multiLineComment)
                            && !multiLineCommentStop;
                }

                // multi-line comment stop
                if (lastChar == '*' && ch == '/') {
                    multiLineComment = doubleQuotes && multiLineComment;
                    multiLineCommentStop = true && !doubleQuotes;
                }

                insideComment = singleLineComment || multiLineComment;

                // if we are in a single-line comment there is nothing to check,
                // we can break
                if (singleLineComment) {
                    break;
                }

                // if we are not inside of a comment, we write characters
                if (!insideComment) {
                    // we did read ahead last time to check whether we are
                    // inside of a comment. as we were not, we write the
                    // last char now.
                    if (lastChar == '/' && !multiLineCommentStop) {
                        result.append(lastChar);
                    }
                    // if the current character is a '/' we do not write this
                    // character as we do not know if this is the beginning of
                    // a comment
                    if (ch != '/') {
                        multiLineCommentStop = false;
                        result.append(ch);
                    }
                }

                lastChar = ch;
            }

            if (!insideComment && lastChar!='/') {
                result.append('\n');
            }

        } // end for lines

        return result.toString();
    }

    @Override
    public String getID() {
        return id;
    }
}
