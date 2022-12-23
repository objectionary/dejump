/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022 Mikhail Lipanin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.eolang.dejump

import org.eolang.parser.XMIR

/**
 * Script that generates EO junit tests
 * from EO sources at 'src/test/eo/org/eolang/dejump/',
 * which checks, that result of dataization EO-code BEFORE
 * transformations are equal to result of dataization same code,
 * but AFTER transformations.
 */

def dir = new File("${project.basedir}/target/eo-after/")
dir.mkdir()

new File("${project.basedir}/src/test/eo/org/eolang/dejump/").eachFile {
    String testName = it.name.substring(0, it.name.lastIndexOf("."))

    def todo = [
        "multiple_returns", // converting negative numbers to "bytes" attribute ?
        "backward_jump" // new logic for "while" object ?
    ]

    if (!(testName in todo)) {
        String src = it.getText()
        String srcBefore = String.format(
            "%s_Before%s",
            src.substring(0, src.indexOf("\n")),
            src.substring(src.indexOf("\n"))
        )
        String srcTransformed = new XMIR(
            RemoveGoto.applyTrain(
                RemoveGoto.getParsedXml(src)
            )
        ).toEO()
        srcTransformed = String.format(
            "%sTest%s",
            srcTransformed.substring(0, srcTransformed.indexOf("\n")),
            srcTransformed.substring(srcTransformed.indexOf("\n"))
        )
        File file = new File(
            String.format(
                "%s/target/eo-after/process_%s.eo",
                "${project.basedir}", "${testName}"
            )
        )
        file.createNewFile()
        file.append(
            String.format(
                "%s\n%s\n%s\n\n",
                "+alias org.eolang.hamcrest.assert-that",
                "+junit",
                "+package org.eolang.dejump"
            )
        )
        srcBefore.eachLine {
            file.append(it + "\n")
        }
        file.append(
            String.format(
                "\n[] > process_%s\n",
                testName
            )
        )
        file.append("  assert-that > @\n")
        src.eachLine {
            file.append("    " + it + "\n")
        }
        file.append('    $.equal-to\n')
        srcTransformed.eachLine {
            file.append("      " + it + "\n")
        }
    }
}
