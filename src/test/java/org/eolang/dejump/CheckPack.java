/*
 * The MIT License (MIT)
 *
 * SPDX-FileCopyrightText: Copyright (c) 2022 Mikhail Lipanin
 * SPDX-License-Identifier: MIT
 */
package org.eolang.dejump;

import com.jcabi.xml.XML;
import com.yegor256.xsline.Shift;
import com.yegor256.xsline.StClasspath;
import com.yegor256.xsline.StEndless;
import com.yegor256.xsline.TrDefault;
import com.yegor256.xsline.Train;
import com.yegor256.xsline.Xsline;
import java.io.IOException;
import java.util.Map;
import org.eolang.parser.XMIR;
import org.yaml.snakeyaml.Yaml;

/**
 * Test case for single XSL-transformation pack.
 *
 * @since 0.0.1
 */
public final class CheckPack {

    /**
     * The scenario in YAML.
     */
    private final String script;

    /**
     * Ctor.
     *
     * @param scr Yaml script
     */
    CheckPack(final String scr) {
        this.script = scr;
    }

    /**
     * Make a run check equivality of taken EO sources.
     *
     * @return True if "before" + "xsls" equals "after"
     * @throws IOException When Parsing EO fails
     */
    @SuppressWarnings("unchecked")
    public boolean failures() throws IOException {
        final Yaml yaml = new Yaml();
        final Map<String, Object> map = yaml.load(this.script);
        final XML before = RemoveGoto.getParsedXml(map.get("before").toString());
        final XML after = RemoveGoto.getParsedXml(map.get("after").toString());
        final Iterable<String> xsls = (Iterable<String>) map.get("xsls");
        Train<Shift> train = new TrDefault<>();
        if (xsls != null) {
            for (final String xsl : xsls) {
                if (xsl.lastIndexOf("flags-to-memory.xsl") == -1
                    && xsl.lastIndexOf("return-value.xsl") == -1) {
                    train = train.with(new StEndless(new StClasspath(xsl)));
                } else {
                    train = train.with(new StClasspath(xsl));
                }
            }
        }
        train = train.with(
            new StEndless(new StClasspath("/org/eolang/dejump/rmv-meaningless.xsl"))
        );
        final Train<Shift> strip = new TrDefault<Shift>()
            .with(new StEndless(new StClasspath("/org/eolang/dejump/strip-xmir.xsl")));
        final XML xml = new Xsline(strip).pass(
            RemoveGoto.getParsedXml(
                new XMIR(
                    new Xsline(train).pass(before)
                ).toEO()
            )
        );
        return xml.equals(new Xsline(strip).pass(after));
    }

}
