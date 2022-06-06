package com.parsers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        /*try (FileReader fileReader = new FileReader(args[0])) {
            StringBuffer txt = new StringBuffer();
            int c;
            while ((c = fileReader.read()) != -1) txt.append((char)c);
            String XMIRFileName = String.valueOf(txt);
            GetRidOfGOTO getRidOfGOTO = new GetRidOfGOTO(XMIRFileName);
            String ret = getRidOfGOTO.eliminate();
            System.out.println(ret);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        String XMIRFileName = "asfsaafsfs";//args[0];
        GetRidOfGOTO getRidOfGOTO = new GetRidOfGOTO(XMIRFileName);
        String ret = getRidOfGOTO.eliminate();
        System.out.println(ret);
    }
}