package start;

import java.util.ArrayList;

public class FajlSistem {

    // Lista svih fajlova/foldera u fajlsistemu:
    public static ArrayList<Fajl> sviFajlovi = new ArrayList<>();

    // Ukupan broj fajlova u fajlsistemu:
    public static int brojFajlova = 0;

    //Novi rasporedjivac procesa:
    public static RasporedjivacProcesa rp = new RasporedjivacProcesa();

    public Asembler as = new Asembler();

    // Trenutna lokacija korisnika u fajlsistemu:
    private Fajl lokacija;

    // Putanja koja se ispisuje u shell-u:
    private String putanja = "";

    // Novi fs sa root folderom:
    public FajlSistem() {

        Fajl root = new Fajl("root");
        root.setFolder(true);
        this.lokacija = root;
        sviFajlovi.add(root);
        brojFajlova++;
        this.putanja = "$ ";

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

    // Ako je smjer "1", dodaje se folder na putanju, inace se brise:
    public void setPutanja(String lokacija, int smjer) {
        if (smjer == 1) {
            this.putanja = this.putanja.concat("/" + lokacija);
        } else {
            this.putanja = this.putanja.replace("/" + lokacija, "");
        }
    }


}
