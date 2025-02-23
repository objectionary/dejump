/*
 * SPDX-FileCopyrightText: Copyright (c) 2022 Mikhail Lipanin
 * SPDX-License-Identifier: MIT
 */
package org.eolang.dejump;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import org.eolang.parser.XMIR;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Main test.
 *
 * @since 0.0.1
 * @todo #52:30min Rewrite for loop in fullRun method
 *  as OOP-style of applying functions and comparing
 *  results in MatcherAssert
 */
public final class MainTest {

    @Test
    public void fullRun(@TempDir final Path dir) throws IOException {
        final List<String> before = Arrays.asList(
            "[] > app",
            "  goto > @",
            "    [g]",
            "      seq > @",
            "        g.forward 228"
        );
        final Path src = dir.resolve("test.eo");
        Files.write(src, before);
        MatcherAssert.assertThat(
            Files.exists(src),
            Matchers.is(true)
        );
        MatcherAssert.assertThat(
            Files.readAllLines(src),
            Matchers.is(before)
        );
        final Path res = dir.resolve("generated/test_transformed.eo");
        new RemoveGoto(src.toString(), true).exec();
        final List<String> first = Files.readAllLines(res);
        final List<String> second = Arrays.asList(
            new XMIR(
                RemoveGoto.applyTrain(
                    RemoveGoto.getParsedXml(String.join("\n", before))
                )
            ).toEO().split("\n")
        );
        for (int idx = 0; idx < first.size(); ++idx) {
            if (first.get(idx).contains("flag_")) {
                first.set(
                    idx,
                    String.format(
                        "%sflag",
                        first.get(idx).substring(0, first.get(idx).indexOf("flag_"))
                    )
                );
                second.set(
                    idx,
                    String.format(
                        "%sflag",
                        second.get(idx).substring(0, second.get(idx).indexOf("flag_"))
                    )
                );
            }
            if (first.get(idx).contains("g_")) {
                first.set(
                    idx,
                    String.format(
                        "%srem",
                        first.get(idx).substring(0, first.get(idx).indexOf("g_"))
                    )
                );
                second.set(
                    idx,
                    String.format(
                        "%srem",
                        second.get(idx).substring(0, second.get(idx).indexOf("g_"))
                    )
                );
            }
        }
        MatcherAssert.assertThat(
            first,
            Matchers.is(second)
        );
    }

}
