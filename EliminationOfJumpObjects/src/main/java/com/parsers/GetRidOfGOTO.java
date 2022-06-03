package com.parsers;

import org.eolang.parser.XMIR;

import java.util.ArrayList;

public class GetRidOfGOTO {
    final String fileName;
    String code;

    public GetRidOfGOTO(String FileName) {
        fileName = FileName;
        XMIR xmir = new XMIR(fileName);
        code = xmir.toEO();
    }
    ArrayList<StringBuffer> separate(Character sep) {
        int id = 0;
        ArrayList <StringBuffer> ret = new ArrayList<>();
        StringBuffer cur = new StringBuffer();
        for (int i = 0; i < code.length(); i++) {
            Character c = code.charAt(i);
            if (c.equals(sep)) {
                ret.add(cur);
                cur = new StringBuffer();
            }
            else cur.append(cur);
        }
        return ret;
    }
    void GotoForward(int beg, int en) {

    }
    void GotoBackward(int beg, int en) {

    }
    public String eliminate() {
        ArrayList <StringBuffer> ar = separate('\n');
        String ret = "";
        for (StringBuffer s : ar) {
            /*

             */
        }
        return ret;
    }
}
