package org.eolang.jump;

import org.cactoos.io.ResourceOf;
import org.cactoos.text.TextOf;
import org.cactoos.text.UncheckedText;
import org.hamcrest.Matchers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.LinkedList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Test case for XSL-transformations packs.
 */
public final class TFSTest {

    @ParameterizedTest
    @MethodSource("getYamls")
    public void testTFS(final String tf) throws Exception {
        assertThat(
                new CheckTF(
                        new TextOf(
                                new ResourceOf(
                                        String.format("org/eolang/jump/packs/%s", tf)
                                )
                        ).asString()
                ).failures(),
                is(true)
        );
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private static Collection<String> getYamls() {
        return TFSTest.yamls("org/eolang/jump/packs/", "");
    }

    private static Collection<String> yamls(final String path,
                                            final String prefix) {
        final Collection<String> out = new LinkedList<>();
        final String[] paths = new UncheckedText(
                new TextOf(new ResourceOf(path))
        ).asString().split("\n");
        for (final String sub : paths) {
            if (sub.endsWith(".yaml")) {
                out.add(String.format("%s%s", prefix, sub));
            } else {
                out.addAll(
                        TFSTest.yamls(
                                String.format("%s%s/", path, sub),
                                String.format("%s/", sub)
                        )
                );
            }
        }
        return out;
    }

}
