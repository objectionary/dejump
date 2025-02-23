/*
 * SPDX-FileCopyrightText: Copyright (c) 2022 Mikhail Lipanin
 * SPDX-License-Identifier: MIT
 */
package org.eolang.dejump;

import com.jcabi.log.Logger;
import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.yegor256.xsline.Shift;
import com.yegor256.xsline.StClasspath;
import com.yegor256.xsline.StEndless;
import com.yegor256.xsline.TrDefault;
import com.yegor256.xsline.Train;
import com.yegor256.xsline.Xsline;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import org.cactoos.io.InputOf;
import org.cactoos.io.OutputTo;
import org.eolang.parser.Syntax;
import org.eolang.parser.XMIR;

/*
@todo #54:30min add RemoveGotoTest
 */
/**
 * The main logic of application.
 *
 * @since 0.0.1
 */
public final class RemoveGoto {

    /**
     * Path to file to be transformed.
     */
    private final String path;

    /**
     * Format of file to transform ("xmir" or "eo").
     */
    private final boolean format;

    /**
     * Directory separator.
     */
    private final char sep;

    /**
     * Ctor.
     *
     * @param pth Path to input file
     * @param fmt True, if input is EO-program
     */
    public RemoveGoto(final String pth, final boolean fmt) {
        this.path = new File(pth).getAbsolutePath();
        this.format = fmt;
        this.sep = File.separatorChar;
    }

    /**
     * Applies train of XSL-transformations.
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

    /**
     * Applies XSL-transformations and saves to output file.
     *
     * @throws IOException If fails
     */
    public void exec() throws IOException {
        final File dir = new File(
            String.format(
                "%s%cgenerated",
                this.path.substring(0, this.path.lastIndexOf(this.sep)), this.sep
            )
        );
        final String filename = new File(this.path).getName()
            .substring(0, new File(this.path).getName().lastIndexOf('.'));
        final File input = new File(this.path);
        if (dir.exists()) {
            RemoveGoto.deleteDirectory(dir);
        }
        dir.mkdir();
        final XML before;
        if (this.format) {
            before = RemoveGoto.getParsedXml(Files.readString(input.toPath()));
        } else {
            before = RemoveGoto.getParsedXml(new XMLDocument(Files.readString(input.toPath())));
        }
        Logger.info(this, "XMIR before transformations:\n%s", before);
        final XML after = RemoveGoto.applyTrain(before);
        Logger.info(this, "XMIR after transformations:\n%s", after);
        final String ret;
        final File output;
        if (this.format) {
            ret = new XMIR(after).toEO();
            output = new File(
                String.format(
                    "%s%c%s_transformed.%s",
                    dir.getPath(), this.sep, filename, "eo"
                )
            );
        } else {
            ret = after.toString();
            output = new File(
                String.format(
                    "%s%c%s_transformed.%s",
                    dir.getPath(), this.sep, filename, "xmir"
                )
            );
        }
        output.createNewFile();
        try (FileWriter out = new FileWriter(output.getPath())) {
            out.write(ret);
            out.flush();
        }
    }

    /**
     * Takes XMIR-source as input,
     * converts it to ".eo" and calls overridden method.
     *
     * @param xml XML ".xmir" source
     * @return XML
     * @throws IOException When Parsing EO fails
     */
    public static XML getParsedXml(final XML xml) throws IOException {
        return RemoveGoto.getParsedXml(
            new XMIR(xml).toEO()
        );
    }

    /**
     * Takes EO-source as input,
     * converts it to ".xmir" and applies "wrap-method-calls.xsl".
     *
     * @param source String EO-source
     * @return XML
     * @throws IOException When Parsing EO fails
     */
    public static XML getParsedXml(final String source) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new Syntax(
            "scenario",
            new InputOf(String.format("%s\n", source)),
            new OutputTo(baos)
        ).parse();
        final XML xml = new XMLDocument(baos.toByteArray());
        baos.close();
        return new Xsline(
            new TrDefault<Shift>()
                .with(new StClasspath("/org/eolang/parser/wrap-method-calls.xsl"))
        ).pass(xml);
    }

    /**
     * Recursively deletes given directory.
     *
     * @param directory File directory to delete
     * @return True if given directory has been deleted successfully
     */
    public static boolean deleteDirectory(final File directory) {
        boolean ret = true;
        if (directory.isDirectory()) {
            final File[] files = directory.listFiles();
            if (files != null) {
                for (final File file : files) {
                    ret &= RemoveGoto.deleteDirectory(file);
                }
            }
        }
        ret &= directory.delete();
        return ret;
    }

}
