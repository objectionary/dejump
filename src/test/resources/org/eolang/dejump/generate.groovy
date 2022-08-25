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
    String testName = it.getName().substring(0, it.getName().lastIndexOf("."))
    if (testName != "empty_goto") {
        String src = it.getText()
        XML xml = RemoveGOTO.applyTrain(RemoveGOTO.getParsedXML(src))
        String srcTransformed = new XMIR(xml).toEO()
        srcTransformed = srcTransformed.substring(0, srcTransformed.indexOf("\n")) + "Test" + srcTransformed.substring(srcTransformed.indexOf("\n"))
        File file = new File(new String("${project.basedir}/target/eo-after/process_" + "${testName}" + ".eo"))
        file.createNewFile()
        file.append("+junit\n")
        file.append("+package org.eolang.dejump\n")
        file.append("+alias org.eolang.hamcrest.assert-that\n\n")
        file.append("[] > process_" + testName + "\n")
        file.append("  assert-that > @\n")
        src.eachLine{
            file.append("    " + it + "\n")
        }
        file.append('    $.equal-to\n')
        srcTransformed.eachLine{
            file.append("      " + it + "\n")
        }
    }

}