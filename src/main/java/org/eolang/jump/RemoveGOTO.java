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

    public RemoveGOTO(String pth) {
        this.path = new File(pth).getAbsolutePath();
    }

    public void exec() {
        File curDir = new File(this.path.substring(0, this.path.lastIndexOf('\\')) + "\\generated");
        String curName = new File(this.path).getName().substring(0, new File(this.path).getName().lastIndexOf('.'));
        File input = new File(this.path);

        if (curDir.exists()) {
            deleteDirectory(curDir);
        }
        curDir.mkdir();
        XML xmlIn, xmlOut;
        try {
            xmlIn = getParsedXML(Files.readString(input.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Train<Shift> train = new TrDefault<Shift>()
                .with(new StEndless(new StClasspath("/org/eolang/jump/simple-goto.xsl")))
                .with(new StEndless(new StClasspath("/org/eolang/jump/change-condition-of-jump.xsl")))
                .with(new StEndless(new StClasspath("/org/eolang/jump/add-fl.xsl")))
                .with(new StEndless(new StClasspath("/org/eolang/jump/add-order-for-while.xsl")))
                .with(new StEndless(new StClasspath("/org/eolang/jump/wrap-other-objects.xsl")))
                .with(new StEndless(new StClasspath("/org/eolang/jump/terminating-while.xsl")))
                .with(new StEndless(new StClasspath("/org/eolang/jump/goto-to-while.xsl")))
                .with(new StClasspath("/org/eolang/jump/flags-to-memory.xsl"))
                .with(new StEndless(new StClasspath("/org/eolang/jump/rmv-meaningless.xsl")));

        xmlOut = new Xsline(train).pass(xmlIn);
        System.out.println(xmlOut);

        String ret = new XMIR(xmlOut).toEO();
        File output = new File(curDir.getPath() + '\\' + curName + "_transformed.eo");
        try {
            output.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (FileWriter out = new FileWriter(output.getPath())) {
            out.write(ret);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
