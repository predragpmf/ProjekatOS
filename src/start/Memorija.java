package start;

import java.util.ArrayList;

public class Memorija {
    private int velicinaMemorijeB = 16000;
    private int velicinaOkviraB = 500;
    private int brojOkvira = 32;
    private ArrayList<Stranica> okviri = new ArrayList<>(brojOkvira);
    private int brojZauzetihOkvira = 0;

    public Memorija(){
        for (int i = 0; i < brojOkvira; i++) {
            okviri.add(i, null);
        }
    }

    public int ucitaj(Stranica str) {
        int brojacPozicije = 0;
        for (int i = 0; i < brojOkvira; i++) {
            if (okviri.get(i) == null){
                okviri.set(i, str);
                return brojacPozicije;  // Vraca poziciju stranice.
            }
            brojacPozicije++;
        }
        return -1;  // Nema slobodnog mjesta.
    }

    public void izbaci(ArrayList<Integer> tabelaStranica) {
        for (int ts : tabelaStranica) {
            okviri.set(ts, null);
        }
    }

    public void ispisiMemoriju() {
        System.out.println("Procesi u memoriji: ");
        if (RasporedjivacProcesa.sviProcesi.isEmpty()){
            System.out.println("\tNema");
        } else {
            for (Proces p : RasporedjivacProcesa.sviProcesi){
                System.out.println("\t" + p.getPid() + ". " + p.getNaziv() + "; Aktivan:" + p.getUcitan() + "; " + p.getBrojStranica() * velicinaOkviraB + "kB");
            }
        }
        System.out.println("Pregled memorije: ");
        System.out.println("\tUkupno : " + velicinaMemorijeB + "kB");
        for (Stranica s : okviri) {
            if (s != null) {
                brojZauzetihOkvira++;
            }
        }
        System.out.println("\tSlobodno : " + (velicinaMemorijeB - (brojZauzetihOkvira * velicinaOkviraB)) + "kB");
        brojZauzetihOkvira = 0;
        for (Stranica s : okviri) {
            if (s == null) {
                System.out.print("| |");
            } else {
                System.out.print("|" + s.getProces().getPid() + "|");
            }
        }
    }

    public void setVelicinaMemorijeB(int velicinaMemorijeB) {
        this.velicinaMemorijeB = velicinaMemorijeB;
    }

    public void setBrojOkvira(int brojOkvira) {
        this.brojOkvira = brojOkvira;
    }

    public void setVelicinaOkviraB(int velicinaOkviraB) {
        this.velicinaOkviraB = velicinaOkviraB;
    }

    public int getVelicinaOkviraB() {
        return velicinaOkviraB;
    }

    public int getBrojOkvira() {
        return brojOkvira;
    }

    public int getVelicinaMemorijeB() {
        return velicinaMemorijeB;
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
