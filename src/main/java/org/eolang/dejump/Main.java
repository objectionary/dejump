package org.eolang.dejump;

import java.io.IOException;
import picocli.CommandLine;
import java.io.File;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "dejump",
    mixinStandardHelpOptions = true,
    version = "Version 0.0.1",
    description = "Replaces objects GOTO with semantically equivalent")
public final class Main implements Callable<Integer> {

    @CommandLine.Option(names = { "--eo" },
        defaultValue = "false",
        description = "treat input file as EO program (not XMIR)")
    boolean asEO;

    @CommandLine.Parameters(index = "0",
        description = "Absolute path of file to transform")
    File file;

    @Override
    public Integer call() throws IOException {
        new RemoveGOTO(this.file.toString(), this.asEO).exec();
        return 0;
    }

    public static void main(final String[] args) {
        new CommandLine(new Main()).execute(args);
    }
}