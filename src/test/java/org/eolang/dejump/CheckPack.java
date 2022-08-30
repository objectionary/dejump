package org.eolang.dejump;

import com.jcabi.xml.XML;
import com.yegor256.xsline.Shift;
import com.yegor256.xsline.StClasspath;
import com.yegor256.xsline.StEndless;
import com.yegor256.xsline.TrDefault;
import com.yegor256.xsline.Train;
import com.yegor256.xsline.Xsline;
import org.eolang.parser.XMIR;
import org.yaml.snakeyaml.Yaml;
import java.io.IOException;
import java.util.Map;

/**
 * Test case for single XSL-transformation pack.
 */
public final class CheckPack {

    /**
     * The scenario in YAML.
     */
    private final String script;

    CheckPack(final String scr) {
        this.script = scr;
    }

    /**
     * Make a run check equivality of taken EO sources.
     */
    @SuppressWarnings("unchecked")
    public boolean failures() throws IOException {
        final Yaml yaml = new Yaml();
        final Map<String, Object> map = yaml.load(this.script);
        final XML _xml = RemoveGOTO.getParsedXML(map.get("before").toString());
        final XML _xmlToCheck = RemoveGOTO.getParsedXML(map.get("after").toString());
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
        train = train.with(new StEndless(new StClasspath("/org/eolang/dejump/rmv-meaningless.xsl")));
        final Train<Shift> strip = new TrDefault<Shift>()
            .with(new StEndless(new StClasspath("/org/eolang/dejump/strip-xmir.xsl")));
        final XML xml = new Xsline(strip).pass(
            RemoveGOTO.getParsedXML(
                new XMIR(
                    new Xsline(train).pass(_xml)
                ).toEO()
            )
        );
        final XML xmlToCheck = new Xsline(strip).pass(_xmlToCheck);
        // How to debug via Logger.debug() ? Unless it, uncomment these lines for debug
        /*System.out.println(xml);
        System.out.println("=========================");
        System.out.println(xmlToCheck);*/
        return xmlToCheck.equals(xml);
    }

}