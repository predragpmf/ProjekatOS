package start;

import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;

public class RasporedjivacProcesa {
    public static CopyOnWriteArrayList<Proces> sviProcesi = new CopyOnWriteArrayList<>();
    public static int brojPokrenutihProcesa = 0;
    public static Memorija mem = new Memorija();
    public static Tred tred = new Tred();
    public static Stack<Proces> listaCekanja = new Stack<>();
    public static ArrayList<Proces> blokiraniProcesi = new ArrayList<>();

    public void noviProces(Fajl fajl) {
        brojPokrenutihProcesa++;
        double vrijemeProcesa = (double) fajl.getVelicinaB() / 100;
        double broj = (double) fajl.getVelicinaB() / (double) mem.getVelicinaOkviraB();
        int brojStranica = (int) Math.ceil(broj);
        Proces pr = new Proces(fajl.getNaziv(), brojPokrenutihProcesa, vrijemeProcesa, brojStranica);
        for (int i = 0; i < pr.getBrojStranica(); i++) {
            pr.tabelaStranica.add(i, 0);
        }
        ucitajProces(pr, false);
    }

    public void ucitajProces(Proces pr, boolean cekanje) {
        for (Proces p : sviProcesi) {
            if (p.getNaziv().equals(pr.getNaziv())) {
                System.out.println("Proces je vec aktivan!");
                return;
            }
        }
        if (cekanje) {
            pr = listaCekanja.pop();
        }
        int pozicija;
        if (mem.getBrojSlobodnihOkvira() >= pr.getBrojStranica()) {
            for (int i = 0; i < pr.getBrojStranica(); i++) {
                Stranica str = new Stranica(pr, i);
                pozicija = mem.ucitaj(str);
                if (pozicija == -1) {
                    // TODO
                }
                pr.tabelaStranica.add(i, pozicija);
            }
            sviProcesi.add(pr);

            // Ako tred radi nemoj pokretati novi, itd:
            if (!tred.isAlive()) {
                tred = new Tred();
                tred.start();
            }
        } else {
            listaCekanja.push(pr);
        }

    }

}
