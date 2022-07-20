package org.eolang.jump;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.jcabi.xml.XSLDocument;
import com.yegor256.xsline.*;
import org.cactoos.io.InputOf;
import org.cactoos.io.OutputTo;
import org.eolang.parser.Syntax;
import org.eolang.parser.XMIR;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RemoveGOTO {
    private final String path;
    XML xmlIn;
    XML xmlOut;
    public RemoveGOTO(String pth) {
        this.path = new File(pth).getAbsolutePath();
    }
    public void exec() throws IOException, ClassNotFoundException {
        File curDir = new File(this.path.substring(0, this.path.lastIndexOf('\\')) + "\\generated");
        String curName = new File(this.path).getName().substring(0, new File(this.path).getName().lastIndexOf('.'));
        File input = new File(curDir.getPath() + '\\' + curName + ".xmir");

        if (curDir.exists() && !deleteDirectory(curDir)) throw new IOException("Can't delete a 'generated' repository");
        if (!(curDir.mkdir())) throw new IOException("Can't create a 'generated' repository");
        if (!(input.createNewFile())) throw new IOException("Can't create an '../generated/*.xmir' file");

        new Syntax(
                this.path,
                new InputOf(new File(this.path)),
                new OutputTo(input)
        ).parse();
        final Train<Shift> train = new TrDefault<Shift>()
                .with(new StXSL(new XSLDocument(this.getClass().getResource("/org/eolang/jump/SG.xsl"))))
                .with(new StXSL(new XSLDocument(this.getClass().getResource("/org/eolang/jump/GF_1.xsl"))));
                //.with(new StXSL(new XSLDocument(this.getClass().getResource("/org/eolang/jump/GF_2.xsl"))));
                //.with(new StXSL(new XSLDocument(this.getClass().getResource("/GB.xsl"))));
                //.with(new StXSL(new XSLDocument(this.getClass().getResource("/TW.xsl"))));

        this.xmlIn = new XMLDocument(input);
        this.xmlOut = new Xsline(train).pass(xmlIn);
        String ret = new XMIR(xmlOut).toEO();
        File output = new File(curDir.getPath() + '\\' + curName + "_transformed.eo");
        if (!(output.createNewFile())) throw new IOException("Can't create an '../generated/*_transformed.xmir' file");
        try (FileWriter out = new FileWriter(output.getPath())) {
            out.write(ret);
            out.flush();
        }
    }
    public static Boolean deleteDirectory(File directory) {
        boolean ret = true;
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    ret &= deleteDirectory(file);
                }
            }
        }
        ret &= directory.delete();
        return ret;
    }
}
