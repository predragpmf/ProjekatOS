package start;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class CuvanjePodataka {

    public static void sacuvaj() throws IOException {

        // Brise sve podatke iz save i broj fajla:
        PrintWriter pw = new PrintWriter("src/start/podaci/save");
        pw.close();
        PrintWriter pw2 = new PrintWriter("src/start/podaci/broj");
        pw2.close();

        FileOutputStream f = new FileOutputStream("src/start/podaci/save");
        ObjectOutputStream o = new ObjectOutputStream(f);

        // Cuva se objekte Fajl klase u /podaci/save:
        for (Fajl fajl : FajlSistem.sviFajlovi) {
            o.writeObject(fajl);
        }

        // Cuva broj objekata klase Fajl:
        Writer wr = new FileWriter("src/start/podaci/broj");
        wr.write(Integer.toString(FajlSistem.brojFajlova));
        wr.close();
        o.close();
        f.close();
    }

    public static void ucitaj() throws IOException, ClassNotFoundException {

        Path path = Paths.get("src/start/podaci/save");

        // Provjerava da li fajl /podaci/save postoji:
        if (path.toFile().isFile()) {

            FileInputStream fi = new FileInputStream("src/start/podaci/save");
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Ucitava broj objekata iz /podaci/broj:
            FileInputStream fb = new FileInputStream("src/start/podaci/broj");
            Scanner scanner = new Scanner(fb);
            int brojObjekata = scanner.nextInt();

            // Pravi novi fajl sistem, sa argumentom (drugiPut = true) da vec postoji root folder:
            Shell.fs = new FajlSistem(true);
            for (int i = 0; i < brojObjekata; i++) {
                FajlSistem.sviFajlovi.add((Fajl) oi.readObject());
            }
            Shell.fs.setLokacija(FajlSistem.sviFajlovi.get(0));
            Shell.fs.setPutanjaUcitavanje();
            FajlSistem.brojFajlova = brojObjekata;
            oi.close();
            fi.close();
            fb.close();

            // Brise sve podatke iz save i broj fajla:
            PrintWriter pw = new PrintWriter("src/start/podaci/save");
            pw.close();
            PrintWriter pw2 = new PrintWriter("src/start/podaci/broj");
            pw2.close();

        } else {

            Shell.fs = new FajlSistem(false);
            return;

        }
    }

}
