package start;

import java.util.ArrayList;

public class Fajl {

    // Ako je "true" onda je folder, u suprotnom je fajl:
    boolean folder;
    private int velicinaB;
    private String naziv;
    private Fajl roditelj = null;

    // Lista djece foldera:
    private ArrayList<Fajl> djeca = new ArrayList<>();

    public Fajl(String naziv) {
        this.setNaziv(naziv);
    }

    public void noviFajl(String nazivFajla, boolean isFolder, int velicinaB) {
        for (Fajl f : getDjeca()) {
            if (f.getNaziv().equals(nazivFajla)) {
                System.out.println("Fajl/Folder vec postoji!");
                return;
            }
        }
        Fajl noviFajl = new Fajl(nazivFajla);
        noviFajl.setVelicinaB(velicinaB);
        noviFajl.setFolder(isFolder);
        noviFajl.setRoditelj(this);
        this.dodajDijete(noviFajl);
        FajlSistem.sviFajlovi.add(noviFajl);
        FajlSistem.brojFajlova++;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Fajl getRoditelj() {
        return roditelj;
    }

    public void setRoditelj(Fajl roditelj) {
        this.roditelj = roditelj;
    }

    public ArrayList<Fajl> getDjeca() {
        return djeca;
    }

    public void dodajDijete(Fajl dijete) {
        this.djeca.add(dijete);
    }

    public void ispisiDjecu() {
        for (Fajl f : getDjeca()) {
            if (f.getFolder()) {
                System.out.println("/" + f.getNaziv());
            } else {
                System.out.println(f.getNaziv());
            }
        }
    }

    public boolean getFolder() {
        return folder;
    }

    public void setFolder(boolean vrijednost) {
        folder = vrijednost;
    }

    public int getVelicinaB() {
        return velicinaB;
    }

    public void setVelicinaB(int velicinaB) {
        this.velicinaB = velicinaB;
    }


}
