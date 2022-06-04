package com.parsers;

import org.eolang.parser.Syntax;
import org.eolang.parser.XMIR;

import java.util.*;

public class GetRidOfGOTO {
    final String fileName;
    int cntOfFlags;
    String code;
    public GetRidOfGOTO(String FileName) {
        fileName = FileName;
        //XMIR xmir = new XMIR(fileName);
        //code = xmir.toEO();
        code = """
                goto
                  [g]
                    seq
                      stdout "kek"
                      if.
                        2 < 4
                        g.forward 228
                        1337
                      stdout "lol"
                """;
        cntOfFlags = 0;
    }
    void sendException(String msg) {
        throw new RuntimeException(msg);
    }
    int getLevel(StringBuffer s) {
        int id = 0;
        while (id < s.length() && s.charAt(id) == ' ') id++;
        return id / 2;
    }
    // Set Tab to desired level
    void setTab(ArrayList <StringBuffer> ar, int id, int lvl) {
        int currLvl = getLevel(ar.get(id));
        if (currLvl < lvl) {
            StringBuffer tmp = new StringBuffer();
            for (int i = 0; i < lvl - currLvl; i++){
                tmp.append("  ");
            }
            tmp.append(ar.get(id));
            ar.set(id, tmp);
        }
        else {
            try {
                for (int i = 0; i < currLvl - lvl; i++){
                    if (!ar.get(id).substring(0, 2).equals("  ")) sendException("Error in Tabulation!");
                    StringBuffer tmp = new StringBuffer(ar.get(id).delete(0, 2));
                    ar.set(id, tmp);
                }
            }
            catch (RuntimeException e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }
    }
    // Returns the end of the scope of an object
    int findObjectEnding(ArrayList <StringBuffer> ar, int beg) {
        int initLevel = getLevel(ar.get(beg));
        int pos = beg + 1;
        while (pos < ar.size() && getLevel(ar.get(pos)) > initLevel) pos++;
        return pos;
    }
    StringBuffer invertingCond(StringBuffer cond) {
        int begStat = -1, enStat = -1;
        for (int i = 0; i < cond.length(); i++) {
            if (cond.charAt(i) != ' ') {
                if (begStat == -1) begStat = i;
                enStat = i;
            }
        }
        cond.insert(begStat, '(');
        cond.insert(enStat + 2, ").not");
        return cond;
    }
    //Splits code into an array of strings by separator
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
            else cur.append(c);
        }
        return ret;
    }
    public String toString(ArrayList <StringBuffer> ar) {
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < ar.size(); i++) {
            ret.append(ar.get(i));
            if (i < ar.size() - 1) ret.append("\n");
        }
        return ret.toString();
    }
    int GotoForward(int beg, int jump, ArrayList <StringBuffer> ar) {
        // Adding flag
        StringBuffer currFlag = new StringBuffer("flag" + String.valueOf(cntOfFlags));
        ar.add(0, new StringBuffer("memory > " + currFlag));
        ar.add(1, new StringBuffer(currFlag + ".write 0"));
        cntOfFlags++;
        beg += 2;
        jump += 2;

        int initLevel = getLevel(ar.get(beg));
        StringBuffer retValueOfJump = new StringBuffer(ar.get(jump).substring(ar.get(jump).indexOf(".forward") + 9));
        int en = findObjectEnding(ar, beg);

        // Adding if-statement if Simple forward
        if (ar.get(jump - 2).indexOf("if.") == -1) {
            ar.add(jump, new StringBuffer("if."));
            ar.add(jump + 1, new StringBuffer("TRUE"));
            ar.add(jump + 3, new StringBuffer("TRUE"));
            setTab(ar, jump, initLevel);
            setTab(ar, jump + 1, initLevel + 1);
            setTab(ar, jump + 2, initLevel + 1);
            setTab(ar, jump + 3, initLevel + 1);
            jump += 2;
            en += 3;
        }

        // Inverting Jump statement and Modifying new if-statement
        ar.set(jump - 1, invertingCond(ar.get(jump - 1)));

        Collections.swap(ar, jump, jump + 1);
        ar.set(jump + 1, new StringBuffer("seq"));
        ar.add(jump + 2, retValueOfJump);
        ar.add(jump + 3, new StringBuffer(currFlag + ".write 1"));
        setTab(ar, jump + 1, getLevel(ar.get(jump)));
        setTab(ar, jump + 2, getLevel(ar.get(jump)) + 1);
        setTab(ar, jump + 3, getLevel(ar.get(jump)) + 1);
        en += 2;

        // Adding if-statement for the rest Objects with nesting >= nesting of goto object
        int pos = jump + 4;
        while (pos < en) {
            int currEn = findObjectEnding(ar, pos);
            int currLevel = getLevel(ar.get(pos));
            ar.add(pos, new StringBuffer("if."));
            ar.add(pos + 1, new StringBuffer("(eq. (" + currFlag + " 1)).not"));
            ar.add(pos + 3, new StringBuffer("TRUE"));
            en += 3;
            setTab(ar, pos, currLevel);
            for (int i = pos + 1; i < currEn + 3; i++) {
                setTab(ar, i, currLevel + 1);
            }
            pos = currEn + 3;
        }

        return en;
    }
    void GotoBackward(int beg, int jump) {

    }
    public String eliminate() {
        ArrayList <StringBuffer> ar = separate('\n');

        for (StringBuffer now : ar){
            System.out.println(now);
        }
        System.out.println("----------------------");

        HashMap <String, Integer> mp = new HashMap<String, Integer>();
        int i = 0;
        while (i < ar.size()) {
            if (ar.get(i).indexOf("goto") != -1) {
                try {
                    i++;
                    int pos = ar.get(i).indexOf("[");
                    if (pos == -1) sendException("Initialisation of an Abstract object is wrong!");
                    int posNext = ar.get(i).indexOf("]", pos);
                    if (posNext == -1 || pos + 1 >= posNext) sendException("Initialisation of an Abstract object is wrong!");
                    String nameOfObject = ar.get(i).substring(pos + 1, posNext);
                    mp.put(nameOfObject, i + 1);
                }
                catch (RuntimeException e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }
            }
            else if (ar.get(i).indexOf(".forward") != -1) {
                try {
                    int posNext = ar.get(i).indexOf(".forward");
                    posNext--;
                    if (posNext < 0) sendException("Initialisation of Jump is wrong!");
                    int pos = posNext;
                    while (pos >= 0 && ar.get(i).charAt(pos) != ' ') pos--;
                    pos++;
                    if (pos > posNext) sendException("Initialisation of Jump is wrong!");
                    String nameOfObject = ar.get(i).substring(pos, posNext + 1);
                    if (!mp.containsKey(nameOfObject)) sendException("Initialisation of Jump is wrong!");
                    i = GotoForward(mp.get(nameOfObject), i, ar);
                }
                catch (RuntimeException e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }
            }
            else if (ar.get(i).indexOf(".backward") != -1) {
                try {
                    int posNext = ar.get(i).indexOf(".backward");
                    posNext--;
                    if (posNext < 0) sendException("Initialisation of Jump is wrong!");
                    int pos = posNext;
                    while (pos >= 0 && ar.get(i).charAt(pos) != ' ') pos--;
                    pos++;
                    if (pos >= posNext) sendException("Initialisation of Jump is wrong!");
                    String nameOfObject = ar.get(i).substring(pos, posNext + 1);
                    if (!mp.containsKey(nameOfObject)) sendException("Initialisation of Jump is wrong!");
                    GotoBackward(mp.get(nameOfObject), i);
                }
                catch (RuntimeException e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }
            }
            i++;
        }

        return toString(ar);
    }
}
