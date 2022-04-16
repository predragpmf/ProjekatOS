package start;

import java.util.ArrayList;

public class RasporedjivacProcesa {
    public static ArrayList<Proces> sviProcesi = new ArrayList<>();
    public static int brojPokrenutihProcesa = 0;
    public static Memorija mem = new Memorija();
    Tred tred = new Tred();

    public void noviProces(Fajl fajl){
        brojPokrenutihProcesa++;
        double vrijemeProcesa = (double)fajl.getVelicinaB() / 100;
        int brojStranica = fajl.getVelicinaB() / mem.getVelicinaOkviraB();
        Proces pr = new Proces(fajl.getNaziv(), brojPokrenutihProcesa, vrijemeProcesa, brojStranica);
        for (int i = 0; i < pr.getBrojStranica(); i++) {
            pr.tabelaStranica.add(i, 0);
        }
        sviProcesi.add(pr);
        ucitajProces(pr);
    }

    private void ucitajProces(Proces pr){
        int pozicija;
        for (int i = 0; i < pr.getBrojStranica(); i++) {
            Stranica str = new Stranica(pr, i);
            pozicija = mem.ucitaj(str);
            if (pozicija == -1){
                // Napravi loop neki
            }
            pr.tabelaStranica.add(i, pozicija);
        }
        // Ako tred radi nemoj pokretati novi, itd:
        if (!tred.isAlive()) {
            tred = new Tred();
            tred.start();
        }
    }


}
