package org.eolang.jump;

import java.io.IOException;

public final class Main {
    public static void main(String[] args) throws IOException {
        new RemoveGOTO(args[0]).exec();
    }
}