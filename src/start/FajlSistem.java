package start;

import java.util.ArrayList;

public class FajlSistem {

    public static ArrayList<Fajl> sviFajlovi = new ArrayList<>();
    private ArrayList<Fajl> rootDjeca = new ArrayList<>();
    private Fajl lokacija;

    public FajlSistem(){
        Fajl root = new Fajl("root");
        this.lokacija = root;
        sviFajlovi.add(root);
    }

    public Fajl getLokacija(){
        return lokacija;
    }

    public void setLokacija(Fajl lokacija){
        this.lokacija = lokacija;
    }

}
