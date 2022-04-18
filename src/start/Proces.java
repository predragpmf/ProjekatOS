package start;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Proces {

    // Lista svih stranica procesa:
    public ArrayList<Integer> tabelaStranica = new ArrayList<>();

    // Id procesa:
    private int pid;
    private String naziv;

    // Trenutno stanje procesa:
    private boolean ucitan;

    // Preostalo vrijeme izvrsavanja procesa:
    private double vrijemeProcesaS;

    // Ukupan broj stranica procesa:
    private int brojStranica;

    public Proces(String naziv, int pid, double vrijemeProcesaS, int brojStranica) {
        this.naziv = naziv;
        this.pid = pid;
        this.vrijemeProcesaS = vrijemeProcesaS;
        this.brojStranica = brojStranica;

    }

    public boolean getUcitan() {
        return this.ucitan;
    }

    public void setUcitan(boolean ucitan) {
        this.ucitan = ucitan;
    }

    public int getBrojStranica() {
        return brojStranica;
    }

    public String getNaziv() {
        return naziv;
    }

    public int getPid() {
        return pid;
    }

    public ArrayList<Integer> getTabelaStranica() {
        return tabelaStranica;
    }

    // Simulacija izvrsavanja procesa:
    public void rad() {
        try {
            this.vrijemeProcesaS = vrijemeProcesaS - 0.1;
            if (vrijemeProcesaS <= 0) {
                RasporedjivacProcesa.sviProcesi.remove(this);
                RasporedjivacProcesa.brojPokrenutihProcesa--;
                RasporedjivacProcesa.mem.izbaci(tabelaStranica);
                if (!RasporedjivacProcesa.listaCekanja.isEmpty()) {
                    FajlSistem.rp.ucitajProces(null, true);
                }
            }
            sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
