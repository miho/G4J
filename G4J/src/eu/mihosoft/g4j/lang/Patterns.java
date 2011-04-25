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

import java.util.regex.Pattern;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Patterns {

    /**
     * Regular expression for a valid identifier (variable, class or method names).
     */
    public static final String IDENTIFIER_STRING = "[a-zA-Z$_][a-zA-Z$_0-9]*";
    /**
     * Regular expression for a list of valid identifiers.
     */
    public static final String IDENTIFIER_LIST_STRING =
            "(" + IDENTIFIER_STRING + "\\s*,\\s*)*"
            + IDENTIFIER_STRING;
    /**
     * Regular expression for a valid template argument, e.g,
     * <code>&lt;T,V&gt;</code>.
     */
    public static final String TEMPLATE_ARGUMENT_STRING =
            "<\\s*" + IDENTIFIER_LIST_STRING + "\\s*>";
    /**
     * Pattern to match template class headers. Example:
     * <code>
     * public class Sample01 &lt;Type, Type2&gt; extends Base01
     * </code>
     */
    public static final Pattern TEMPLATE_CLS_HEADER = Pattern.compile(
            "(\\s+|^)class\\s+"
            + IDENTIFIER_STRING
            + "\\s*" + TEMPLATE_ARGUMENT_STRING,
            Pattern.MULTILINE);
    /**
     * Pattern to match template arguments, e.g.,
     * <code>&lt;T,V&gt;</code>.
     */
    public static final Pattern TEMPLATE_ARGUMENT = Pattern.compile(TEMPLATE_ARGUMENT_STRING);
    /**
     * Pattern to match an identifier.
     */
    public static final Pattern IDENTIFIER = Pattern.compile(IDENTIFIER_STRING);
    /**
     * Pattern to match an identifier list.
     */
    public static final Pattern IDENTIFIER_LIST = Pattern.compile(IDENTIFIER_LIST_STRING);
}
