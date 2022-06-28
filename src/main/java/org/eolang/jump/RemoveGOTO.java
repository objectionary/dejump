package org.eolang.jump;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.jcabi.xml.XSLDocument;
import com.yegor256.xsline.StXSL;
import com.yegor256.xsline.TrDefault;
import com.yegor256.xsline.Xsline;

import java.io.IOException;

public class RemoveGOTO {
    public static void exec() throws IOException {
        XML input = new XMLDocument(Main.class.getResource("test.xml"));
        XML output = new Xsline(
                new TrDefault<>()
                        .with(new StXSL(new XSLDocument(Main.class.getResource("add-brackets.xsl"))))
        ).pass(input);
    }
}
