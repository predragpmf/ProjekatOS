package start;

import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;

public class RasporedjivacProcesa {

    // Lista svih pokrenutih procesa:
    public static CopyOnWriteArrayList<Proces> sviProcesi = new CopyOnWriteArrayList<>();

    public static int brojPokrenutihProcesa = 0;

    public static Memorija mem = new Memorija();

    // Tred koji kruzi kroz listu "sviProcesi":
    public static Tred tred = new Tred();

    // Ako nema dovoljno slobodne memorije proces ide na stek "listaCekanja":
    public static Stack<Proces> listaCekanja = new Stack<>();

    public static ArrayList<Proces> blokiraniProcesi = new ArrayList<>();

    // Kreira novi proces:
    public void noviProces(Fajl fajl) {

        brojPokrenutihProcesa++;
        double vrijemeProcesa = (double) fajl.getVelicinaB() / 100;
        double broj = (double) fajl.getVelicinaB() / (double) mem.getVelicinaOkviraB();
        // Zaokruzuje broj stranica na prvi veci cijeli broj stranica:
        int brojStranica = (int) Math.ceil(broj);
        Proces pr = new Proces(fajl.getNaziv(), brojPokrenutihProcesa, vrijemeProcesa, brojStranica);
        for (int i = 0; i < pr.getBrojStranica(); i++) {
            pr.tabelaStranica.add(i, 0);
        }
        ucitajProces(pr, false);

    }

    // Ucitava novi proces u memoriju i pokrece ga:
    public void ucitajProces(Proces pr, boolean cekanje) {
        for (Proces p : sviProcesi) {
            if (p.getNaziv().equals(pr.getNaziv())) {
                System.out.println("Proces je vec aktivan!");
                return;
            }
        }
        // Ukoliko je cekanje "true", skida proces sa steka "listaCekanja":
        if (cekanje) {
            pr = listaCekanja.pop();
        }
        int pozicija;

        // Provjerava da li ima dovoljno slobodnog mjesta u memoriji za novi proces:
        if (mem.getBrojSlobodnihOkvira() >= pr.getBrojStranica()) {
            for (int i = 0; i < pr.getBrojStranica(); i++) {
                Stranica str = new Stranica(pr, i);
                pozicija = mem.ucitaj(str);
                pr.tabelaStranica.add(i, pozicija);
            }
            sviProcesi.add(pr);

            // Ako tred radi nemoj pokretati novi, itd:
            if (!tred.isAlive()) {
                tred = new Tred();
                tred.start();
            }

            // Ako nema dovoljno mjesta u memoriji, proces ide na stek "listaCekanja"
        } else {
            listaCekanja.push(pr);
        }

    }

}
