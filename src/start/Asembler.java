package start;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;


public class Asembler {

    private static int A = 0, B = 0, C = 0, D = 0, IP = 0;
    private static boolean flagZ = false;

    private final File folder = new File("src/start/podaci");
    private ArrayList<String> rijeci = new ArrayList<>();
    private Stack<Integer> stek = new Stack<>();
    private boolean izlaz = false;

    public void pokreni(String nazivFajla) {
        ucitaj(nazivFajla);
        if (izlaz) {
            return;
        }
        izvrsi();

    }

    private void izvrsi() {
        for (int i = 0; i < rijeci.size(); i++) {
            if (rijeci.get(i).equals("JMP")) {
                i = rijeci.indexOf(rijeci.get(i + 1) + ":");
            } else if (rijeci.get(i).equals("CALL")) {
                stek.push(i + 2);
                i = rijeci.indexOf(rijeci.get(i + 1) + ":");
            } else if (rijeci.get(i).equals("RET")) {
                i = stek.pop();
            } else if (rijeci.get(i).equals("HLT")) {
                break;
            } else if (rijeci.get(i).equals("MOV")) {
                String prva, druga;
                prva = rijeci.get(i + 1).substring(0, rijeci.get(i + 1).length() - 1);
                druga = rijeci.get(i + 2);
                if (isNumeric(druga)) {
                    switch (prva) {
                        case "A" -> A = Integer.parseInt(druga);
                        case "B" -> B = Integer.parseInt(druga);
                        case "C" -> C = Integer.parseInt(druga);
                        case "D" -> D = Integer.parseInt(druga);
                    }
                } else {
                    switch (prva) {
                        case "A" -> {
                            switch (druga) {
                                case "B" -> A = B;
                                case "C" -> A = C;
                                case "D" -> A = D;
                            }
                        }
                        case "B" -> {
                            switch (druga) {
                                case "A" -> B = A;
                                case "C" -> B = C;
                                case "D" -> B = D;
                            }
                        }
                        case "C" -> {
                            switch (druga) {
                                case "A" -> C = A;
                                case "B" -> C = B;
                                case "D" -> C = D;
                            }
                        }
                        case "D" -> {
                            switch (druga) {
                                case "A" -> D = A;
                                case "B" -> D = B;
                                case "C" -> D = C;
                            }
                        }
                    }
                }
            } else if (rijeci.get(i).equals("INC")) {
                switch (rijeci.get(i + 1)) {
                    case "A" -> A++;
                    case "B" -> B++;
                    case "C" -> C++;
                    case "D" -> D++;
                }
            } else if (rijeci.get(i).equals("DEC")) {
                switch (rijeci.get(i + 1)) {
                    case "A" -> A--;
                    case "B" -> B--;
                    case "C" -> C--;
                    case "D" -> D--;
                }
            } else if (rijeci.get(i).equals("CMP")) {
                String prva, druga;
                prva = rijeci.get(i + 1).substring(0, rijeci.get(i + 1).length() - 1);
                druga = rijeci.get(i + 2);
                if (isNumeric(druga)) {
                    switch (prva) {
                        case "A" -> flagZ = A == Integer.parseInt(druga);
                        case "B" -> flagZ = B == Integer.parseInt(druga);
                        case "C" -> flagZ = C == Integer.parseInt(druga);
                        case "D" -> flagZ = D == Integer.parseInt(druga);
                    }
                } else {
                    switch (prva) {
                        case "A" -> {
                            switch (druga) {
                                case "B" -> flagZ = A == B;
                                case "C" -> flagZ = A == C;
                                case "D" -> flagZ = A == D;
                            }
                        }
                        case "B" -> {
                            switch (druga) {
                                case "A" -> flagZ = B == A;
                                case "C" -> flagZ = B == C;
                                case "D" -> flagZ = B == D;
                            }
                        }
                        case "C" -> {
                            switch (druga) {
                                case "A" -> flagZ = C == A;
                                case "B" -> flagZ = C == B;
                                case "D" -> flagZ = C == D;
                            }
                        }
                        case "D" -> {
                            switch (druga) {
                                case "A" -> flagZ = D == A;
                                case "B" -> flagZ = D == B;
                                case "C" -> flagZ = D == C;
                            }
                        }
                    }
                }
            } else if (rijeci.get(i).equals("JZ")) {
                if (flagZ) {
                    i = rijeci.indexOf(rijeci.get(i + 1) + ":");
                    flagZ = false;
                }
            } else if (rijeci.get(i).equals("JNZ")) {
                if (!flagZ) {
                    i = rijeci.indexOf(rijeci.get(i + 1) + ":");
                }
            } else if (rijeci.get(i).equals("ADD")) {
                String prva, druga;
                prva = rijeci.get(i + 1).substring(0, rijeci.get(i + 1).length() - 1);
                druga = rijeci.get(i + 2);
                if (isNumeric(druga)) {
                    switch (prva) {
                        case "A" -> A += Integer.parseInt(druga);
                        case "B" -> B += Integer.parseInt(druga);
                        case "C" -> C += Integer.parseInt(druga);
                        case "D" -> D += Integer.parseInt(druga);
                    }
                } else {
                    switch (prva) {
                        case "A" -> {
                            switch (druga) {
                                case "B" -> A += B;
                                case "C" -> A += C;
                                case "D" -> A += D;
                            }
                        }
                        case "B" -> {
                            switch (druga) {
                                case "A" -> B += A;
                                case "C" -> B += C;
                                case "D" -> B += D;
                            }
                        }
                        case "C" -> {
                            switch (druga) {
                                case "A" -> C += A;
                                case "B" -> C += B;
                                case "D" -> C += D;
                            }
                        }
                        case "D" -> {
                            switch (druga) {
                                case "A" -> D += A;
                                case "B" -> D += B;
                                case "C" -> D += C;
                            }
                        }
                    }
                }
            } else if (rijeci.get(i).equals("SUB")) {
                String prva, druga;
                prva = rijeci.get(i + 1).substring(0, rijeci.get(i + 1).length() - 1);
                druga = rijeci.get(i + 2);
                if (isNumeric(druga)) {
                    switch (prva) {
                        case "A" -> A -= Integer.parseInt(druga);
                        case "B" -> B -= Integer.parseInt(druga);
                        case "C" -> C -= Integer.parseInt(druga);
                        case "D" -> D -= Integer.parseInt(druga);
                    }
                } else {
                    switch (prva) {
                        case "A" -> {
                            switch (druga) {
                                case "B" -> A -= B;
                                case "C" -> A -= C;
                                case "D" -> A -= D;
                            }
                        }
                        case "B" -> {
                            switch (druga) {
                                case "A" -> B -= A;
                                case "C" -> B -= C;
                                case "D" -> B -= D;
                            }
                        }
                        case "C" -> {
                            switch (druga) {
                                case "A" -> C -= A;
                                case "B" -> C -= B;
                                case "D" -> C -= D;
                            }
                        }
                        case "D" -> {
                            switch (druga) {
                                case "A" -> D -= A;
                                case "B" -> D -= B;
                                case "C" -> D -= C;
                            }
                        }
                    }
                }
            } else {
                continue;
            }
            IP++;
            stanje();

            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();

        }
        A = 0;
        B = 0;
        C = 0;
        D = 0;
        IP = 0;
        flagZ = false;
        stek.clear();
        rijeci.clear();
        System.out.println("Kraj.");
    }

    private void stanje() {
        System.out.println("IP = " + IP + "; A = " + A + "; B = " + B + "; C = " + C + "; D = " + D + "; Zero = " + flagZ);
        System.out.print("Stek: [ ");
        if (!stek.isEmpty()) {
            for (Integer intg : stek) {
                System.out.print(intg + "; ");
            }
        }
        System.out.println("]");
    }


    private void ucitaj(String nazivFajla) {
        File[] listaFajlova = folder.listFiles();
        boolean postoji = false;
        for (File fajl : listaFajlova) {
            if (fajl.getName().equals(nazivFajla)) {
                postoji = true;
            }
        }
        if (!postoji) {
            return;
        }
        try {
            File file = new File("src/start/podaci/" + nazivFajla);
            Scanner input = new Scanner(file);
            while (input.hasNext()) {
                String rijec = input.next();
                int test = 0;
                if (rijec.endsWith(",")) {
                    String test2 = rijec.substring(0, rijec.length() - 1);
                    if (isNumeric(test2)) {
                        test = Integer.parseInt(test2);
                    }
                }
                if (isNumeric(rijec)) {
                    test = Integer.parseInt(rijec);
                }
                if (test < 0 || test > 255) {
                    System.out.println("Broj mora biti izmedju 0 i 255!");
                    izlaz = true;
                    return;
                }

                rijeci.add(rijec);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void ispis() {
        File[] listaFajlova = folder.listFiles();
        for (File fajl : listaFajlova) {
            if (fajl.isFile()) {
                System.out.println(fajl.getName());
            } else if (fajl.isDirectory()) {
                System.out.println("/" + fajl.getName());
            }
        }
    }

    private boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int i = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
