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
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> OR
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

import java.util.regex.Pattern;

/**
 * Language utils provides several code related methods to analyze and verify
 * source code.
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class LangUtils {

    // no instanciation allowed
    private LangUtils() {
        throw new AssertionError(); // not in this class either!
    }

    /**
     * Get class names of all classes defined in the given source code.
     * @param aCode code to analyze
     * @return class names of all classes defined in the given source code
     */
    public static String classNameFromCode(CodeEntry aCode) {
        String result = null;

        String[] lines = aCode.getCode().split("\\n");

        // match example: ^public final class TestClass {$
        Pattern p1 =
                Pattern.compile(
                "^.*\\s+class\\s+.*$", Pattern.DOTALL);

        // match example: ^class TestClass {$
        // or
        // match example: ^     class TestClass {$
        Pattern p2 =
                Pattern.compile(
                "^class\\s+.*$", Pattern.DOTALL);

        for (String l : lines) {

            l = l.trim();

            if (p1.matcher(l).matches() || p2.matcher(l).matches()) {
                result = l.replaceFirst("^.*class\\s+", "").split(" ")[0];
                break;
            }
        }

        return result;
    }

    /**
     * Get class names of all classes defined in the given source code.
     * @param aCode code to analyze
     * @return class names of all classes defined in the given source code
     */
    public static String interfaceNameFromCode(CodeEntry aCode) {
        String result = null;

        String[] lines = aCode.getCode().split("\\n");

        // match example: ^public final interface TestClass {$
        Pattern p1 =
                Pattern.compile(
                "^.*\\s+interface\\s+.*$", Pattern.DOTALL);

        // match example: ^interface TestClass {$
        // or
        // match example: ^     interface TestClass {$
        Pattern p2 =
                Pattern.compile(
                "^interface\\s+.*$", Pattern.DOTALL);

        for (String l : lines) {

            l = l.trim();

            if (p1.matcher(l).matches() || p2.matcher(l).matches()) {
                result = l.replaceFirst("^.*interface\\s+", "").split(" ")[0];
                break;
            }
        }

        return result;
    }

    /**
     * Indicates whether the specified class name is valid. Currently only
     * unqualified names are supported.
     * @param className class name to check
     * @return <code>true</code> if the class name is valid; <code>false</code>
     *         otherwise
     */
    public static boolean isClassNameValid(String className) {

        if (className == null) {
            className = "";
        }

        return isIdentifierValid(className);
    }

    /**
     * Indicates whether the specified method name is valid. Currently only
     * unqualified names are supported.
     * @param methodName method name to check
     * @return <code>true</code> if the variable name is valid; 
     *         <code>false</code> otherwise
     */
    public static boolean isMethodNameValid(String methodName) {

        if (methodName == null) {
            methodName = "";
        }

        return isIdentifierValid(methodName);
    }

    /**
     * Indicates whether the specified variable name is valid. Currently only
     * unqualified names are supported.
     * @param varName variable name to check
     * @return <code>true</code> if the variable name is valid; 
     *         <code>false</code> otherwise
     */
    public static boolean isVariableNameValid(String varName) {

        if (varName == null) {
            varName = "";
        }

        return isIdentifierValid(varName);
    }

    /**
     * Indicates whether the specified identifier name is valid.
     * @param varName identifier name to check
     * @return <code>true</code> if the identifier name is valid;
     *         <code>false</code> otherwise
     */
    private static boolean isIdentifierValid(String varName) {
        if (varName == null) {
            varName = "";
        }

        // same as class name (currently, this may change soon)
        Pattern p = Pattern.compile(getIdentifierRegex());

        return p.matcher(varName).matches();
    }

    /**
     * Returns the regular expression that is used to match a valid identifier.
     * @return the regular expression that is used to match a valid identifier
     */
    public static String getIdentifierRegex() {
        return "[a-zA-Z$_][a-zA-Z$_0-9]*";
    }

    /**
     * Indicates whether the specified package name is valid.
     * @param varName package name to check
     * @return <code>true</code> if the package name is valid;
     *         <code>false</code> otherwise
     */
    public static boolean isPackageNameValid(String packageName) {
        if (packageName == null) {
            packageName = "";
        }
        // same as class name (currently, this may change soon)
        Pattern p = Pattern.compile(
                "(" + getIdentifierRegex() + ")" + "(\\." + getIdentifierRegex() + ")*");

        return p.matcher(packageName).matches();
    }

    /**
     * Adds escape characters to all occurences of <code>"</code>.
     * @param code code
     * @return code with escape characters
     */
    public static String addEscapeCharsToCode(String code) {
        if (code == null) {
            code = "";
        }
        return code.replace("\"", "\\\"");
    }

    /**
     * Adds escape characters to all occurences of <code>\n</code>.
     * @param code code
     * @return code with escape characters
     */
    public static String addEscapeNewLinesToCode(String code) {
        if (code == null) {
            code = "";
        }

        String lines[] = code.split("\n"); // split at new line

        String result = "";

        for (String l : lines) {
            result+=l+"\\n";
        }

        return result;
    }
}
