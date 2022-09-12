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

    /**
     * Format of file to transform ("xmir" or "eo")
     */
    private final boolean format;

    public RemoveGOTO(final String pth, final boolean fmt) {
        this.path = new File(pth).getAbsolutePath();
        this.format = fmt;
    }

    /**
     * Applies train of XSL-transformations
     *
     * @param xml XML
     * @return XML
     */
    public static XML applyTrain(final XML xml) {
        final Train<Shift> train = new TrDefault<Shift>()
            .with(new StEndless(new StClasspath("/org/eolang/dejump/simple-goto.xsl")))
            .with(new StEndless(new StClasspath("/org/eolang/dejump/change-condition-of-jump.xsl")))
            .with(new StEndless(new StClasspath("/org/eolang/dejump/add-fl.xsl")))
            .with(new StEndless(new StClasspath("/org/eolang/dejump/recalculate-flags.xsl")))
            .with(new StEndless(new StClasspath("/org/eolang/dejump/add-order-for-while.xsl")))
            .with(new StEndless(new StClasspath("/org/eolang/dejump/wrap-other-objects.xsl")))
            .with(new StClasspath("/org/eolang/dejump/add-temp-flags.xsl"))
            .with(new StClasspath("/org/eolang/dejump/return-value.xsl"))
            .with(new StEndless(new StClasspath("/org/eolang/dejump/terminating-while.xsl")))
            .with(new StEndless(new StClasspath("/org/eolang/dejump/goto-to-while.xsl")))
            .with(new StClasspath("/org/eolang/dejump/flags-to-memory.xsl"))
            .with(new StEndless(new StClasspath("/org/eolang/dejump/rmv-meaningless.xsl")));
        return new Xsline(train).pass(xml);
    }

    public void exec() throws IOException {
        final File dir = new File(this.path.substring(0, this.path.lastIndexOf('\\')) + "\\generated");
        final String filename = new File(this.path).getName().substring(0, new File(this.path).getName().lastIndexOf('.'));
        final File input = new File(this.path);
        if (dir.exists()) {
            RemoveGOTO.deleteDirectory(dir);
        }
        dir.mkdir();
        final XML before;
        if (this.format) {
            before = RemoveGOTO.getParsedXML(Files.readString(input.toPath()));
        } else {
            before = RemoveGOTO.getParsedXML(new XMLDocument(Files.readString(input.toPath())));
        }
        final XML after = RemoveGOTO.applyTrain(before);
        System.out.println(after);

        final String ret;
        if (this.format) {
            ret = new XMIR(after).toEO();
        } else {
            ret = after.toString();
        }
        final File output = new File(
            String.format(
                "%s\\%s_transformed.%s",
                dir.getPath(), filename, this.format
            )
        );
        output.createNewFile();
        try (final FileWriter out = new FileWriter(output.getPath())) {
            out.write(ret);
            out.flush();
        }
    }

    /**
     * Takes XMIR-source as input,
     * converts it to ".eo" and calls overridden method
     *
     * @param xml XML ".xmir" source
     * @return XML
     */
    public static XML getParsedXML(final XML xml) throws IOException {
        return RemoveGOTO.getParsedXML(
            new XMIR(xml).toEO()
        );
    }

    /**
     * Takes EO-source as input,
     * converts it to ".xmir" and applies 'wrap-method-calls.xsl'
     *
     * @param source String EO-source
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
        return new Xsline(
            new TrDefault<Shift>().with(new StClasspath("/org/eolang/parser/wrap-method-calls.xsl"))
        ).pass(xml);
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
