/*
 * SPDX-FileCopyrightText: Copyright (c) 2022 Mikhail Lipanin
 * SPDX-License-Identifier: MIT
 */
package org.eolang.dejump

import org.eolang.parser.XMIR

/**
 * Script that generates EO junit tests
 * from EO sources at 'src/test/eo/org/eolang/dejump/',
 * which checks, that result of dataization EO-code BEFORE
 * transformations are equal to result of dataization same code,
 * but AFTER transformations.
 *
 * @todo #5:90min Resolve all tests, that are blocked due "todo" array.
 */

def dir = new File("${project.basedir}/target/eo-after/")
dir.mkdir()

new File("${project.basedir}/src/test/eo/org/eolang/dejump/").eachFile {
    String testName = it.name.substring(0, it.name.lastIndexOf("."))

    def todo = [
        "multiple-returns", // converting negative numbers to "bytes" attribute ?
        "backward-jump", // new logic for "while" object ?
        "goto-inside-try" // check, how "try" object dataizes now ?
    ]

    if (!(testName in todo)) {
        String src = it.getText()
        String srcBefore = String.format(
            "%s-Before%s",
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
                "%s/target/eo-after/process-%s.eo",
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
