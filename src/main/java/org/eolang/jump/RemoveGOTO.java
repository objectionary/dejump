package org.eolang.jump;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.jcabi.xml.XSLDocument;
import com.yegor256.xsline.*;

import java.io.IOException;

public class RemoveGOTO {
    public void exec() throws IOException, ClassNotFoundException {
        final Train<Shift> train = new TrDefault<Shift>()
                .with(new StXSL(new XSLDocument(this.getClass().getResource("/SG.xsl"))))
                .with(new StXSL(new XSLDocument(this.getClass().getResource("/GF.xsl"))))
                .with(new StXSL(new XSLDocument(this.getClass().getResource("/GB.xsl"))))
                .with(new StXSL(new XSLDocument(this.getClass().getResource("/TW.xsl"))));

        XML input = new XMLDocument(this.getClass().getResource("/hello.xmir"));
        XML output = new Xsline(train).pass(input);

        System.out.println(output);
    }
}
