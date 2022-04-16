package start;

import java.util.ArrayList;

public class FajlSistem {

    public static ArrayList<Fajl> sviFajlovi = new ArrayList<>();
    public static int brojFajlova = 0;
    private Fajl lokacija;
    private String putanja = "";
    //Novi rasporedjivac procesa:
    public static RasporedjivacProcesa rp = new RasporedjivacProcesa();

    public Asembler as = new Asembler();

    // Novi fs sa root folderom:
    public FajlSistem(boolean drugiPut) {

        // Ako se prvi put ucitava fajl sistem, drugiPut je false:
        if (!drugiPut) {
            Fajl root = new Fajl("root");
            root.setFolder(true);
            this.lokacija = root;
            sviFajlovi.add(root);
            brojFajlova++;
            this.putanja = "$ ";
        }
    }

    public Fajl getLokacija() {
        return lokacija;
    }

    public void setLokacija(Fajl nova_lokacija) {
        this.lokacija = nova_lokacija;
    }

    public String getPutanja() {
        return putanja;
    }

    public void setPutanja(String lokacija, int smjer) {
        if (smjer == 1) {
            this.putanja = this.putanja.concat("/" + lokacija);
        } else {
            this.putanja = this.putanja.replace("/" + lokacija, "");
        }
    }

    public void setPutanjaUcitavanje() {
        this.putanja = "$ ";
    }


}
