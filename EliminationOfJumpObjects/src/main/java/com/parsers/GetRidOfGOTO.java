package com.parsers;

import java.util.ArrayList;

public class GetRidOfGOTO {
    final String fileName;

    public GetRidOfGOTO(String FileName) {
        fileName = FileName;
    }
    ArrayList<StringBuffer> separate(String code, Character sep) {
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
    public String eliminate(String code) {
        ArrayList <StringBuffer> ar = separate(code, '\n');
        for (StringBuffer s : ar) {
            /*
            
             */
        }
    }
}
