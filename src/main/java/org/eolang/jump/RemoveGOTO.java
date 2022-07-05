package org.eolang.jump;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.jcabi.xml.XSLDocument;
import com.yegor256.xsline.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RemoveGOTO {
    public void exec() throws IOException {
        final Train<Shift> train = new TrDefault<Shift>()
                .with(new StXSL(new XSLDocument(this.getClass().getResource("add-brackets.xsl"))));
        
        XML input = new XMLDocument(this.getClass().getResource("test.xml"));
        XML output = new Xsline(train).pass(input);
    }
}
