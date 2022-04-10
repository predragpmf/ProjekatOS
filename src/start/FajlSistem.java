package start;

import java.util.ArrayList;

public class FajlSistem {

    public static ArrayList<Fajl> sviFajlovi = new ArrayList<>();
    private Fajl lokacija;
    private String putanja;

    public FajlSistem(){
        Fajl root = new Fajl("root");
        this.lokacija = root;
        sviFajlovi.add(root);
        this.putanja = "$ ";
    }

    public Fajl getLokacija(){
        return lokacija;
    }

    public void setLokacija(Fajl lokacija){
        this.lokacija = lokacija;
    }

    public String getPutanja(){
        return putanja;
    }

    public void setPutanja(String lokacija, int smjer){
        if (smjer == 1) {
            this.putanja = this.putanja.concat("/" + lokacija);
        } else {
            this.putanja = this.putanja.replace("/" + lokacija, "");
        }
    }



}
