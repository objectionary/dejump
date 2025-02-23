/*
 * The MIT License (MIT)
 *
 * SPDX-FileCopyrightText: Copyright (c) 2022 Mikhail Lipanin
 * SPDX-License-Identifier: MIT
 */
package org.eolang.dejump;

import com.jcabi.manifests.Manifests;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import picocli.CommandLine;

/**
 * Main entrance.
 *
 * @since 0.0.1
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@CommandLine.Command(
    name = "dejump",
    mixinStandardHelpOptions = true,
    description = "Replaces objects GOTO with semantically equivalent",
    versionProvider = Main.Version.class
)
public final class Main implements Callable<Integer> {

    /**
     * Flag indicating whether the input file is EO-program.
     */
    @CommandLine.Option(names = { "--eo" },
        defaultValue = "false",
        description = "treat input file as EO program (not XMIR)")
    private boolean eolang;

    /**
     * Absolute path to input file.
     */
    @CommandLine.Parameters(index = "0",
        description = "Absolute path of file to transform")
    private File file;

    @Override
    public Integer call() throws IOException {
        new RemoveGoto(this.file.toString(), this.eolang).exec();
        return 0;
    }

    /**
     * Main entrance for Java command line.
     * @param args The args from the command line.
     */
    public static void main(final String[] args) {
        new CommandLine(new Main()).execute(args);
    }

    /**
     * Version.
     * @since 0.0.2
     */
    static final class Version implements CommandLine.IVersionProvider {
        @Override
        public String[] getVersion() {
            return new String[]{
                String.format(
                    "Dejump version is %s\nEO version is %s",
                    Manifests.read("Dejump-Version"),
                    Manifests.read("EO-Version")
                ),
            };
        }
    }

}
