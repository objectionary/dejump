package com.parsers;

public class Main {
    public static void main(String[] args) {
        String XMIRFileName = "asfsaafsfs";//args[0];
        GetRidOfGOTO getRidOfGOTO = new GetRidOfGOTO(XMIRFileName);
        String ret = getRidOfGOTO.eliminate();

        System.out.println(ret);
    }
}