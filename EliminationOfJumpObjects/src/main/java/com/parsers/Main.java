package com.parsers;

public class Main {
    public static void main(String[] args) {
        String XMIRFileName = args[0];
        GetRidOfGOTO getRidOfGOTO = new GetRidOfGOTO(XMIRFileName);

    }
}