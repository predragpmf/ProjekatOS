package start;

public class Stranica {

    private Proces proces;
    private int brojStranice;

    public Stranica(Proces pr, int br){
        this.proces = pr;
        this.brojStranice = br;
    }

    public void setBrojStranice(int brojStranice) {
        this.brojStranice = brojStranice;
    }

    public int getBrojStranice() {
        return brojStranice;
    }

    public Proces getProces() {
        return proces;
    }
}
