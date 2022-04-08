package start;

import java.util.Scanner;

public class CmdPrompt {

    private static final Scanner scan = new Scanner(System.in);
    private static String naredba;

    public static void start() {
        do {
            System.out.print("$ ");
            naredba = scan.nextLine();
            if (naredba.isEmpty()) {
                continue;
            }
            if (naredba.equals("exit")) {
                break;
            }
            if (naredba.equals("about")) {
                System.out.println("Projekat iz Operativnih sistema/ORS3.");
                continue;
            }
            System.out.println("Naredba ne postoji!");
        } while (true);

        System.out.println("Zatvaranje...");

    }

}
