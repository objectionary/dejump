package org.eolang.jump;

import com.jcabi.log.Logger;
import com.jcabi.xml.XML;
import com.yegor256.xsline.*;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.Map;

/**
 * Test case for single XSL-transformation pack.
 */
public final class CheckTF {
    /**
     * The scenario in YAML.
     */
    private final String script;
    CheckTF(final String scr) {
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
                train = train.with(new StEndless(new StClasspath(xsl)));
            }
        }
        train = train.with(new StClasspath("/org/eolang/jump/strip-xmir.xsl"));
        final XML xml = new Xsline(train).pass(_xml);
        final XML xmlToCheck = new Xsline(train).pass(_xmlToCheck);
        Logger.debug(this, "Output XML:\n%s", xml);
        return xmlToCheck.equals(xml);
    }
}