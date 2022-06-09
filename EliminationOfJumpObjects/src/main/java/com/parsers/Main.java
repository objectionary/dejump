package com.parsers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static String readFromDirectory(String inp) {
        try (FileReader fileReader = new FileReader(inp)) {
            StringBuffer text = new StringBuffer();
            int c;
            while ((c = fileReader.read()) != -1) {
                text.append((char)c);
            }
            String code = String.valueOf(text);
            return code;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void writeToDirectory(String out) {
        try (FileWriter fileWriter = new FileWriter("", false)) {
            fileWriter.write(out);
            fileWriter.flush();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) throws FileNotFoundException {
        String code = readFromDirectory(args[0]);
        GetRidOfGOTO getRidOfGOTO = new GetRidOfGOTO(code);
        String ret = getRidOfGOTO.eliminate();
        System.out.println(ret);
        //writeToDirectory(ret);
    }
}