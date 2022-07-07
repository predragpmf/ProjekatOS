package start;

import java.util.ArrayList;

public class Memorija {
    public static int velicinaMemorijeB;
    public static int velicinaOkviraB;
    public static int brojOkvira;

    // Lista svih okvira memorije:
    private ArrayList<Stranica> okviri = new ArrayList<>(brojOkvira);

    // Inicijalizuje okvire memorije na "null":
    public Memorija() {
        for (int i = 0; i < brojOkvira; i++) {
            okviri.add(i, null);
        }
    }

    // Ucitava novu stranicu u memoriju:
    public int ucitaj(Stranica str) {
        int brojacPozicije = 0;
        for (int i = 0; i < brojOkvira; i++) {
            if (okviri.get(i) == null) {
                okviri.set(i, str);
                // Vraca poziciju okvira u kojeg je smjestena stranica:
                return brojacPozicije;
            }
            brojacPozicije++;
        }
        // Nema slobodnog mjesta:
        return -1;
    }

    // Brise sve ucitane stranice procesa iz memorije:
    public void izbaci(ArrayList<Integer> tabelaStranica) {
        for (int ts : tabelaStranica) {
            okviri.set(ts, null);
        }
    }

    public void ispisiMemoriju() {
        System.out.println("Procesi u memoriji: ");
        if (RasporedjivacProcesa.sviProcesi.isEmpty()) {
            System.out.println("\tNema");
        } else {
            for (Proces p : RasporedjivacProcesa.sviProcesi) {
                System.out.println("\t" + p.getPid() + ". " + p.getNaziv() + "; Aktivan:" + p.getUcitan() + "; " + p.getBrojStranica() * velicinaOkviraB + "kB");
            }
        }
        System.out.println("Pregled memorije: ");
        System.out.println("\tUkupno : " + velicinaMemorijeB + "kB");
        System.out.println("\tSlobodno : " + (getBrojSlobodnihOkvira() * velicinaOkviraB) + "kB");
        for (Stranica s : okviri) {
            if (s == null) {
                System.out.print("| |");
            } else {
                System.out.print("|" + s.getProces().getPid() + "|");
            }
        }
    }

    public int getVelicinaOkviraB() {
        return velicinaOkviraB;
    }

    public int getBrojSlobodnihOkvira() {
        int broj = 0;
        for (Stranica s : okviri) {
            if (s == null) {
                broj++;
            }
        }
        return broj;
    }


}
