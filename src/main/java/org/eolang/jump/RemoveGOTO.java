package org.eolang.jump;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.yegor256.xsline.*;
import org.cactoos.io.InputOf;
import org.cactoos.io.OutputTo;
import org.eolang.parser.ParsingTrain;
import org.eolang.parser.Syntax;
import org.eolang.parser.XMIR;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public final class RemoveGOTO {
    private final String path;
    private XML xmlIn;
    private XML xmlOut;
    public RemoveGOTO(String pth) {
        this.path = new File(pth).getAbsolutePath();
    }
    public void exec() throws IOException {
        File curDir = new File(this.path.substring(0, this.path.lastIndexOf('\\')) + "\\generated");
        String curName = new File(this.path).getName().substring(0, new File(this.path).getName().lastIndexOf('.'));
        File input = new File(this.path);

        if (curDir.exists() && !deleteDirectory(curDir)) throw new IOException("Can't delete a 'generated' repository");
        if (!(curDir.mkdir())) throw new IOException("Can't create a 'generated' repository");

        this.xmlIn = getParsedXML(Files.readString(input.toPath()));
        Train<Shift> train = new TrDefault<Shift>()
                .with(new StClasspath("/org/eolang/jump/SG.xsl"))
                .with(new StClasspath("/org/eolang/jump/GF_1.xsl"));
                //.with(new StXSL(new XSLDocument(this.getClass().getResource("/org/eolang/jump/GF_2.xsl"))));
                //.with(new StXSL(new XSLDocument(this.getClass().getResource("/GB.xsl"))));
                //.with(new StXSL(new XSLDocument(this.getClass().getResource("/TW.xsl"))));

        this.xmlOut = new Xsline(train).pass(xmlIn);
        System.out.println(this.xmlOut);

        String ret = new XMIR(xmlOut).toEO();
        File output = new File(curDir.getPath() + '\\' + curName + "_transformed.eo");
        if (!(output.createNewFile())) throw new IOException("Can't create an '../generated/*_transformed.eo' file");
        try (FileWriter out = new FileWriter(output.getPath())) {
            out.write(ret);
            out.flush();
        }
    }

    public static XML getParsedXML(String source) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new Syntax(
                "scenario",
                new InputOf(String.format("%s\n", source)),
                new OutputTo(baos)
        ).parse();
        final XML xml = new XMLDocument(baos.toByteArray());
        baos.close();
        Train<Shift> train = new ParsingTrain();
        return new Xsline(train).pass(xml);
    }

    public static boolean deleteDirectory(File directory) {
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
