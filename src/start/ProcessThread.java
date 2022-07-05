package start;

public class ProcessThread extends Thread {

    public void run() {
        for (int i = 0; i <= RasporedjivacProcesa.sviProcesi.size(); i++) {
            if (i == 0 && RasporedjivacProcesa.sviProcesi.size() == 0) {
                return;
            }
            if (i == RasporedjivacProcesa.sviProcesi.size()) {
                i = 0;
            }
            if (!RasporedjivacProcesa.sviProcesi.get(i).getUcitan()) {
                RasporedjivacProcesa.sviProcesi.get(i).setUcitan(true);
            }
            RasporedjivacProcesa.sviProcesi.get(i).rad();
        }
    }
}
