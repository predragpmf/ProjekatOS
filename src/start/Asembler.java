// WIP
package start;

import java.io.File;

public class Asembler {

    private File folder = new File("src/start/podaci");

    public void ispis(){

        File[] listaFajlova = folder.listFiles();
        for (int i = 0; i < listaFajlova.length; i++) {
            if (listaFajlova[i].isFile()) {
                System.out.println(listaFajlova[i].getName());
            } else if (listaFajlova[i].isDirectory()) {
                System.out.println("/" + listaFajlova[i].getName());
            }
        }

    }
}
