package org.eolang.dejump

import com.jcabi.xml.XML
import org.eolang.parser.XMIR

new File("${project.basedir}/src/test/eo/org/eolang/dejump/").eachFile {
    String testName = it.getName().substring(0, it.getName().lastIndexOf("."))
    String src = it.getText()
    XML xml = RemoveGOTO.applyTrain(RemoveGOTO.getParsedXML(src))
    String srcTransformed = new XMIR(xml).toEO()
    String ret = "+junit\n"
    ret += "+package org.eolang.dejump\n\n"
    ret += "[] > test_" + testName + "\n"
    ret += "  assert-that > @"

    println ret + "\n\n"
}