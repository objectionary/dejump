package org.eolang.dejump;

import picocli.CommandLine;
import java.io.File;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "dejump",
    mixinStandardHelpOptions = true,
    version = "version 0.0.1",
    description = "Replaces objects GOTO with semantically equivalent")
public final class Main implements Callable<Integer> {
    @CommandLine.Parameters(index = "0",
        description = "Absolute path of file to transform")
    private File file;

    @Override
    public Integer call() {
        new RemoveGOTO(this.file.toString()).exec();
        return 0;
    }

    public static void main(final String[] args) {
        new CommandLine(new Main()).execute(args);
    }
}