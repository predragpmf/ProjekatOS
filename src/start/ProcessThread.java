package start;

import java.util.ListIterator;

public class ProcessThread extends Thread {

    public void run() {
        ListIterator<Proces> it = RasporedjivacProcesa.sviProcesi.listIterator();
        Proces pr;
        while (true) {
            if (it.hasNext()) {
                pr = it.next();
            } else {
                it = RasporedjivacProcesa.sviProcesi.listIterator();
                continue;
            }
            if (!pr.getUcitan()) {
                pr.setUcitan(true);
            }
            pr.rad();
        }
    }
}
