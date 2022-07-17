package org.eolang.jump;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        RemoveGOTO run = new RemoveGOTO();
        run.exec(args[0]);
    }
}