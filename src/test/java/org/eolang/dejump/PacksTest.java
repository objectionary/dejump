/*
 * SPDX-FileCopyrightText: Copyright (c) 2022 Mikhail Lipanin
 * SPDX-License-Identifier: MIT
 */
package org.eolang.dejump;

import java.util.Collection;
import java.util.LinkedList;
import org.cactoos.io.ResourceOf;
import org.cactoos.text.TextOf;
import org.cactoos.text.UncheckedText;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test case for XSL-transformations packs.
 *
 * @since 0.0.1
 */
public final class PacksTest {

    @ParameterizedTest
    @MethodSource("getYamls")
    public void testTfs(final String pack) throws Exception {
        MatcherAssert.assertThat(
            new CheckPack(
                new TextOf(
                    new ResourceOf(
                        String.format("org/eolang/dejump/packs/%s", pack)
                    )
                ).asString()
            ).failures(),
            Matchers.is(true)
        );
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private static Collection<String> getYamls() {
        return PacksTest.yamls("org/eolang/dejump/packs/", "");
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
                    PacksTest.yamls(
                        String.format("%s%s/", path, sub),
                        String.format("%s/", sub)
                    )
                );
            }
        }
        return out;
    }

}
