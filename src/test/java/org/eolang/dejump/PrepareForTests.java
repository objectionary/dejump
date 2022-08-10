package org.eolang.dejump;


import com.jcabi.xml.XML;
import org.cactoos.io.ResourceOf;
import org.cactoos.text.TextOf;
import org.cactoos.text.UncheckedText;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.LinkedList;

import static org.eolang.dejump.RemoveGOTO.getParsedXML;

public final class PrepareForTests {

    @ParameterizedTest
    @MethodSource("getEOsrc")
    public void apply(final String name) throws IOException {
        File curFile = new File(name);
        XML xml = RemoveGOTO.applyTrain(getParsedXML(Files.readString(curFile.toPath())));

    }

    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private static Collection<String> getEOsrc() {
        return PrepareForTests.getEO("eo/org/eolang/dejump/");
    }

    private static Collection<String> getEO(final String path) {
        final Collection<String> out = new LinkedList<>();
        final String[] paths = new UncheckedText(
                new TextOf(new ResourceOf(path))
        ).asString().split("\n");
        for (final String cur : paths) {
            File check = new File(cur);
            if (!check.isDirectory()) {
                out.add(cur);
            }
        }
        return out;
    }

}
