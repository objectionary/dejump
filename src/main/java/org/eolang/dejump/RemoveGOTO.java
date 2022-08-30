package org.eolang.dejump;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.yegor256.xsline.Shift;
import com.yegor256.xsline.StClasspath;
import com.yegor256.xsline.StEndless;
import com.yegor256.xsline.TrDefault;
import com.yegor256.xsline.Train;
import com.yegor256.xsline.Xsline;
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

    /**
     * Path to file to be transformed
     */
    private final String path;

    public RemoveGOTO(final String pth) {
        this.path = new File(pth).getAbsolutePath();
    }

    /**
     * Applies train of XSL-transformations
     *
     * @param in XML
     * @return XML
     */
    public static XML applyTrain(final XML in) {
        final Train<Shift> train = new TrDefault<Shift>()
            .with(new StEndless(new StClasspath("/org/eolang/dejump/simple-goto.xsl")))
            .with(new StEndless(new StClasspath("/org/eolang/dejump/change-condition-of-jump.xsl")))
            .with(new StEndless(new StClasspath("/org/eolang/dejump/add-fl.xsl")))
            .with(new StEndless(new StClasspath("/org/eolang/dejump/recalculate-flags.xsl")))
            .with(new StEndless(new StClasspath("/org/eolang/dejump/add-order-for-while.xsl")))
            .with(new StEndless(new StClasspath("/org/eolang/dejump/wrap-other-objects.xsl")))
            .with(new StClasspath("/org/eolang/dejump/return-value.xsl"))
            .with(new StEndless(new StClasspath("/org/eolang/dejump/terminating-while.xsl")))
            .with(new StEndless(new StClasspath("/org/eolang/dejump/goto-to-while.xsl")))
            .with(new StClasspath("/org/eolang/dejump/flags-to-memory.xsl"))
            .with(new StEndless(new StClasspath("/org/eolang/dejump/rmv-meaningless.xsl")));
        return new Xsline(train).pass(in);
    }

    public void exec() {
        final File curDir = new File(this.path.substring(0, this.path.lastIndexOf('\\')) + "\\generated");
        final String curName = new File(this.path).getName().substring(0, new File(this.path).getName().lastIndexOf('.'));
        final File input = new File(this.path);
        if (curDir.exists()) {
            RemoveGOTO.deleteDirectory(curDir);
        }
        curDir.mkdir();
        final XML xmlIn;
        try {
            xmlIn = RemoveGOTO.getParsedXML(Files.readString(input.toPath()));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        final XML xmlOut = RemoveGOTO.applyTrain(xmlIn);
        System.out.println(xmlOut);

        final String ret = new XMIR(xmlOut).toEO();
        final File output = new File(curDir.getPath() + '\\' + curName + "_transformed.eo");
        try {
            output.createNewFile();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        try (final FileWriter out = new FileWriter(output.getPath())) {
            out.write(ret);
            out.flush();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Takes EO-source as input,
     * converts it to .xmir and applies ParsingTrain
     *
     * @param source String - EO source
     * @return XML
     */
    public static XML getParsedXML(final String source) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new Syntax(
            "scenario",
            new InputOf(String.format("%s\n", source)),
            new OutputTo(baos)
        ).parse();
        final XML xml = new XMLDocument(baos.toByteArray());
        baos.close();
        final Train<Shift> train = new ParsingTrain();
        return new Xsline(train).pass(xml);
    }

    /**
     * Recursively deletes given directory
     *
     * @param directory File - directory to delete
     * @return boolean - True if given directory has been deleted successfully
     */
    public static boolean deleteDirectory(final File directory) {
        boolean ret = true;
        if (directory.isDirectory()) {
            final File[] files = directory.listFiles();
            if (files != null) {
                for (final File file : files) {
                    ret &= RemoveGOTO.deleteDirectory(file);
                }
            }
        }
        ret &= directory.delete();
        return ret;
    }

}
