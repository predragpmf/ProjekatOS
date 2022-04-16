package start;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        // Ucitava sve sacuvane objekte iz /podaci/save fajla:
        CuvanjePodataka.ucitaj();

        // Pokrece Shell:
        Shell.start();

        // TODO: Popravi ovo:
            // Sacuva sve objekte u /podaci/save fajl:
            //CuvanjePodataka.sacuvaj();

    }

}
