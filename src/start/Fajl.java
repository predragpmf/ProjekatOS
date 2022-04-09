package start;

import java.util.ArrayList;

public class Fajl {
    private String naziv;
    private Fajl roditelj = null;
    private ArrayList<Fajl> djeca = new ArrayList<>();

    public Fajl(String naziv) {
        this.setNaziv(naziv);
    }

    public void noviFajl(String nazivFajla) {
        for (Fajl f : getDjeca()){
            if(f.getNaziv().equals(nazivFajla)){
                System.out.println("Fajl vec postoji!");
                return;
            }
        }
        Fajl noviFajl = new Fajl(nazivFajla);
        noviFajl.setRoditelj(this);
        this.dodajDijete(noviFajl);
        FajlSistem.sviFajlovi.add(noviFajl);
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv){
        this.naziv = naziv;
    }

    public Fajl getRoditelj(){
        return roditelj;
    }

    public void setRoditelj(Fajl roditelj){
        this.roditelj = roditelj;
    }

    public ArrayList<Fajl> getDjeca(){
        return djeca;
    }

    public void dodajDijete(Fajl dijete){
        this.djeca.add(dijete);
    }

    public void ispisiDjecu(){
        for (Fajl f : getDjeca()) {
            System.out.println(f.getNaziv());
        }
    }

}
