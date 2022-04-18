package start;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Proces {
    public ArrayList<Integer> tabelaStranica = new ArrayList<>();
    private int pid;
    private String naziv;
    private boolean ucitan;
    private double vrijemeProcesaS;
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

    public void setVrijemeProcesaS(int vrijemeProcesaS) {
        this.vrijemeProcesaS = vrijemeProcesaS;
    }

    public int getBrojStranica() {
        return brojStranica;
    }

    public void setBrojStranica(int brojStranica) {
        this.brojStranica = brojStranica;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public ArrayList<Integer> getTabelaStranica() {
        return tabelaStranica;
    }

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
