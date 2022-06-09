package com.parsers;

import org.eolang.parser.Syntax;
import org.eolang.parser.XMIR;

import java.util.*;

public class GetRidOfGOTO {
    /*
     * fileName -
     * cntOfFlags - to count number of flags
     * inputCode -
     * currFlag - Current flag for adding
     * delDclr - Checks if declaration of goto-object is deleted
     * initBack - Checks if main while-loop for GotoBackward is added
     * flagForBack - Gives a flag of goto-object with backward Jump
     */
    //final String fileName;
    int cntOfFlags;
    String inputCode;
    StringBuffer currFlag;
    HashMap <String, Boolean> delDclr = new HashMap<String, Boolean>();
    HashMap <String, Boolean> initBack = new HashMap<String, Boolean>();
    HashMap <String, StringBuffer> flagForBack = new HashMap<String, StringBuffer>();

    public GetRidOfGOTO(String givenCode) {
        XMIR xmir = new XMIR(givenCode);
        inputCode = xmir.toEO();
        System.out.println(inputCode);
        /*inputCode = """
goto
  [g]
    o0
      o1
        o2
        if.
          st1
          g.backward
          TRUE
        o3
      g.backward
                """;*/
        cntOfFlags = 0;
    }
    void sendException(String msg) {
        throw new RuntimeException(msg);
    }

    /*
     * Returns nesting of an Object
     */
    int getLevel(StringBuffer s) {
        int id = 0;
        while (id < s.length() && s.charAt(id) == ' ') id++;
        return id / 2;
    }

    /*
     * Sets Tab to desired level
     */
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

    /*
     * Returns the end of the scope of an object
     */
    int findObjectEnding(ArrayList <StringBuffer> ar, int beg) {
        int initLevel = getLevel(ar.get(beg));
        int pos = beg + 1;
        while (pos < ar.size() && getLevel(ar.get(pos)) > initLevel) pos++;
        return pos;
    }

    /*
     * Inverting Condition to opposite
     */
    void invertingCond(ArrayList <StringBuffer> ar, int line) {
        int begStmt = getHigherObj(ar, line) + 1;
        ar.add(begStmt, new StringBuffer("not."));
        setTab(ar, begStmt, getLevel(ar.get(begStmt + 1)));
        int curEn = findObjectEnding(ar, begStmt + 1);
        for (int i = begStmt + 1; i < curEn; i++) {
            setTab(ar, i, getLevel(ar.get(i)) + 1);
        }
    }

    /*
     * Adding additional if-statements which compares given flag equals to valOfFlag
     */
    void procOtherObjects(ArrayList <StringBuffer> ar, int pos, int en, int valOfFlag) {
        while (pos < en) {
            int currEn = findObjectEnding(ar, pos);
            int currLevel = getLevel(ar.get(pos));
            ar.add(pos, new StringBuffer("if."));
            ar.add(pos + 1, new StringBuffer("(eq. (" + currFlag + " " + valOfFlag + ")).not"));
            ar.add(currEn + 2, new StringBuffer("TRUE"));
            en += 3;
            setTab(ar, pos, currLevel);
            setTab(ar, pos + 1, currLevel + 1);
            setTab(ar, currEn + 2, currLevel + 1);
            for (int i = pos + 2; i < currEn + 2; i++) {
                setTab(ar, i, getLevel(ar.get(i)) + 1);
            }
            pos = currEn + 3;
        }
    }

    /*
     * Removes declaration of goto-object
     */
    boolean rmvDclr(ArrayList <StringBuffer> ar, int beg, int en) {
        if (beg >= 2 && ar.get(beg - 2).indexOf("goto") != -1) {
            for (int i = beg; i < en; i++) {
                setTab(ar, i, getLevel(ar.get(i)) - 2);
            }
            ar.remove(beg - 1);
            ar.remove(beg - 2);
            return true;
        }
        else return false;
    }

    /*
     * Returns an object that wraps the current
     */
    int getHigherObj(ArrayList <StringBuffer> ar, int id) {
        int lvl = getLevel(ar.get(id));
        int pos = id - 1;
        while (pos > 0 && getLevel(ar.get(pos)) >= lvl) pos--;

        return pos;
    }

    /*
     * Adds if-statement for unconditional Jump
     */
    void addIfStatement(ArrayList <StringBuffer> ar, int jump, int initLevel) {
        if (ar.get(jump - 2).indexOf("if.") == -1) {
            ar.add(jump, new StringBuffer("if."));
            ar.add(jump + 1, new StringBuffer("TRUE"));
            ar.add(jump + 3, new StringBuffer("TRUE"));
            setTab(ar, jump, initLevel);
            setTab(ar, jump + 1, initLevel + 1);
            setTab(ar, jump + 2, initLevel + 1);
            setTab(ar, jump + 3, initLevel + 1);
        }
    }

    /*
     * Splits code into an array of strings by separator
     */
    ArrayList<StringBuffer> separate(Character sep) {
        int id = 0;
        ArrayList <StringBuffer> ret = new ArrayList<>();
        StringBuffer cur = new StringBuffer();
        for (int i = 0; i < inputCode.length(); i++) {
            Character c = inputCode.charAt(i);
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

    int GotoForward(int beg, int jump, String nameOfObject, ArrayList <StringBuffer> ar) {
        /*
         * Making a flag
         */
        currFlag = new StringBuffer("flag" + cntOfFlags);
        ar.add(0, new StringBuffer("memory > " + currFlag));
        ar.add(1, new StringBuffer(currFlag + ".write 0"));
        cntOfFlags++;
        beg += 2;
        jump += 2;

        /*
         * initLevelOfJump - nesting of Beginning of goto-object
         * retValueOfJump - object that will return if Jump statement is TRUE
         * endOfObj - last line in goto-object (exclusive)
         */
        int initLevelOfJump = getLevel(ar.get(jump));
        StringBuffer retValueOfJump = new StringBuffer(ar.get(jump).substring(ar.get(jump).indexOf(".forward") + 9));
        int endOfObj = findObjectEnding(ar, beg);

        /*
         * Deleting declaration of goto-object
         */
        if (!delDclr.containsKey(nameOfObject)) {
            delDclr.put(nameOfObject, true);
            rmvDclr(ar, beg, endOfObj);
            beg -= 2;
            jump -= 2;
            endOfObj -= 2;
            initLevelOfJump -= 2;
        }

        /*
         * Adding additional condition to all while loops, which occurs before Jump
         */
        for (int i = beg; i < endOfObj; ) {
            if (ar.get(i).indexOf("while.") != -1) {
                int remLevel = getLevel(ar.get(i + 1));
                int curEn = findObjectEnding(ar, i + 1);
                for (int j = i + 1; j < curEn; j++) {
                    if (j == jump) initLevelOfJump++;
                    setTab(ar, j, getLevel(ar.get(j)) + 1);
                }
                ar.add(i + 1, new StringBuffer("and."));
                ar.add(curEn + 1, new StringBuffer("(eq. (" + currFlag + " 1)).not"));
                setTab(ar, i + 1, remLevel);
                setTab(ar, curEn + 1, remLevel + 1);
                jump += 2;
                endOfObj += 2;
                i = curEn + 2;
            }
            else i++;
        }

        /*
         * Modifying Jump statement
         */
        invertingCond(ar, jump);
        jump++;
        endOfObj++;
        ar.remove(jump);
        int curEn = findObjectEnding(ar, jump);
        ar.add(curEn, new StringBuffer("seq"));
        ar.add(curEn + 1, retValueOfJump);
        ar.add(curEn + 2, new StringBuffer(currFlag + ".write 1"));
        setTab(ar, curEn, initLevelOfJump);
        setTab(ar, curEn + 1, initLevelOfJump + 1);
        setTab(ar, curEn + 2, initLevelOfJump + 1);
        endOfObj += 2;

        /*
         * Adding if-statement for the rest Objects with nesting >= nesting of goto object itself
         */
        int pos = curEn + 3;
        procOtherObjects(ar, pos, endOfObj, 1);

        return beg;
    }


    int GotoBackward(int beg, int jump, String nameOfObject, ArrayList <StringBuffer> ar) {
        /*
         * Making a flag
         */
        if (!flagForBack.containsKey(nameOfObject)) {
            currFlag = new StringBuffer("flag" + cntOfFlags);
            ar.add(0, new StringBuffer("memory > " + currFlag));
            ar.add(1, new StringBuffer(currFlag + ".write 0"));
            cntOfFlags++;
            beg += 2;
            jump += 2;
            flagForBack.put(nameOfObject, currFlag);
        }
        else currFlag = flagForBack.get(nameOfObject);

        /*
         * initLevelOfJump - nesting of Beginning of goto-object
         * initLevelOfBeg - nesting of Jump itself
         * endOfObj - last line in goto-object (exclusive)
         */
        int initLevelOfJump = getLevel(ar.get(jump));
        int initLevelOfBeg = getLevel(ar.get(beg));
        int endOfObj = findObjectEnding(ar, beg);

        /*
         * Deleting declaration of goto-object
         */
        if (!delDclr.containsKey(nameOfObject)) {
            delDclr.put(nameOfObject, true);
            rmvDclr(ar, beg, endOfObj);
            beg -= 2;
            jump -= 2;
            endOfObj -= 2;
            initLevelOfBeg -= 2;
            initLevelOfJump -= 2;
        }

        /*
         * Adding main while loop of goto-object
         */
        if (!initBack.containsKey(nameOfObject)) {
            initBack.put(nameOfObject, true);
            ar.add(beg, new StringBuffer("while."));
            ar.add(beg + 1, new StringBuffer("eq. (" + currFlag + "  0)"));
            ar.add(beg + 2, new StringBuffer("seq"));
            ar.add(beg + 3, new StringBuffer(currFlag + ".write 1"));
            setTab(ar, beg, initLevelOfBeg);
            setTab(ar, beg + 1, initLevelOfBeg + 1);
            setTab(ar, beg + 2, initLevelOfBeg + 1);
            setTab(ar, beg + 3, initLevelOfBeg + 2);
            beg += 4;
            jump += 4;
            endOfObj += 4;
            initLevelOfBeg += 2;
            initLevelOfJump += 2;
            for (int i = beg; i < endOfObj; i++) {
                setTab(ar, i, getLevel(ar.get(i)) + 2);
            }
        }

        /*
         * Adding additional condition to all while loops, which occurs before Jump
         */
        for (int i = beg; i < endOfObj; ) {
            if (ar.get(i).indexOf("while.") != -1) {
                int remLevel = getLevel(ar.get(i + 1));
                int curEn = findObjectEnding(ar, i + 1);
                for (int j = i + 1; j < curEn; j++) {
                    if (j == jump) initLevelOfJump++;
                    setTab(ar, j, getLevel(ar.get(j)) + 1);
                }
                ar.add(i + 1, new StringBuffer("and."));
                ar.add(curEn + 1, new StringBuffer("(eq. (" + currFlag + " 0)).not"));
                setTab(ar, i + 1, remLevel);
                setTab(ar, curEn + 1, remLevel + 1);
                jump += 2;
                endOfObj += 2;
                i = curEn + 2;
            }
            else i++;
        }

        /*
         * Modifying Jump statement
         */
        invertingCond(ar, jump);
        jump++;
        endOfObj++;
        ar.remove(jump);
        int curEn = findObjectEnding(ar, jump);
        ar.add(curEn, new StringBuffer(currFlag + ".write 0"));
        setTab(ar, curEn, initLevelOfJump);

        /*
         * Adding if-statement for the rest Objects with nesting >= nesting of goto object itself
         */
        int pos = curEn + 1;
        procOtherObjects(ar, pos, endOfObj, 0);

        return beg;
    }

    public String eliminate() {
        ArrayList <StringBuffer> ar = separate('\n');

        /*
         * Adding if-statement if Simple forward/backward
         */
        for (int i = 0; i < ar.size(); i++) {
            if (ar.get(i).indexOf(".forward") != -1 || ar.get(i).indexOf(".backward") != -1) {
                if (i >= 2) {
                    if (ar.get(i - 2).indexOf("if.") == -1) {
                        addIfStatement(ar, i, getLevel(ar.get(i)));
                    }
                }
                else sendException("Initialisation of an Abstract object is wrong!");
            }
        }

        /*
         * mp - for each nameOfObject returns line, where it's declaration starts
         */
        HashMap <String, Integer> mp = new HashMap<String, Integer>();

        for (int i = 0; i < ar.size(); ) {
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
            else if (ar.get(i).indexOf(".forward") != -1 || ar.get(i).indexOf(".backward") != -1) {
                if (ar.get(i).indexOf(".forward") != -1) {
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
                        int ret = GotoForward(mp.get(nameOfObject), i, nameOfObject, ar);
                        mp.put(nameOfObject, ret);
                        i = ret;
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
                        if (pos > posNext) sendException("Initialisation of Jump is wrong!");
                        String nameOfObject = ar.get(i).substring(pos, posNext + 1);
                        if (!mp.containsKey(nameOfObject)) sendException("Initialisation of Jump is wrong!");
                        int ret = GotoBackward(mp.get(nameOfObject), i, nameOfObject, ar);
                        mp.put(nameOfObject, ret);
                        i = ret;
                    }
                    catch (RuntimeException e) {
                        System.out.println(Arrays.toString(e.getStackTrace()));
                    }
                }
            }
            else i++;
        }

        return toString(ar);
    }
}
