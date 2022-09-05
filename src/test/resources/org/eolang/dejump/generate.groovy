package org.eolang.dejump

import com.jcabi.xml.XML
import org.eolang.parser.XMIR

/**
 * Script that generates EO junit tests
 * from EO sources at 'src/test/eo/org/eolang/dejump/'
 */

def dir = new File("${project.basedir}/target/eo-after/")
dir.mkdir()

new File("${project.basedir}/src/test/eo/org/eolang/dejump/").eachFile {
    String testName = it.name.substring(0, it.name.lastIndexOf("."))

    def todo = [
        "easy_test_case",     // consider @name in "if." object
    ]

    if (!(testName in todo)) {
        String src = it.getText()
        String srcTransformed = new XMIR(
            RemoveGOTO.applyTrain(
                RemoveGOTO.getParsedXML(src)
            )
        ).toEO()
        srcTransformed = String.format(
            "%sTest%s",
            srcTransformed.substring(0, srcTransformed.indexOf("\n")),
            srcTransformed.substring(srcTransformed.indexOf("\n"))
        )
        File file = new File(String.format(
            "%s/target/eo-after/process_%s.eo",
            "${project.basedir}", "${testName}"
        ))
        file.createNewFile()
        file.append("+alias org.eolang.hamcrest.assert-that\n")
        file.append("+junit\n")
        file.append("+package org.eolang.dejump\n\n")
        file.append("[] > process_" + testName + "\n")
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