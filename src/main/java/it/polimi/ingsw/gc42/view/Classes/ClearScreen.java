package it.polimi.ingsw.gc42.view.Classes;

import java.io.IOException;

public class ClearScreen {
    public static void main(String... arg) throws IOException, InterruptedException
    {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }
}
