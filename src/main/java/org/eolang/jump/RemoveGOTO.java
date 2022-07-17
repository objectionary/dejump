package org.eolang.jump;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.jcabi.xml.XSLDocument;
import com.yegor256.xsline.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RemoveGOTO {
    public void exec(String path) throws IOException, ClassNotFoundException {
        final Train<Shift> train = new TrDefault<Shift>()
                .with(new StXSL(new XSLDocument(this.getClass().getResource("/SG.xsl"))));
                //.with(new StXSL(new XSLDocument(this.getClass().getResource("/GF.xsl"))));
                //.with(new StXSL(new XSLDocument(this.getClass().getResource("/GB.xsl"))));
                //.with(new StXSL(new XSLDocument(this.getClass().getResource("/TW.xsl"))));

        File in = new File(path);
        XML input = new XMLDocument(in);
        /*XML input = new XMLDocument(this.getClass().getResource("/hello.xmir"));*/
        XML output = new Xsline(train).pass(input);

        File ret = new File(path.substring(0, path.lastIndexOf('\\')) + "\\converted_" + in.getName());
        ret.createNewFile();
        FileWriter out = new FileWriter(ret);
        out.write(output.toString());
        System.out.println(output);
    }
}
