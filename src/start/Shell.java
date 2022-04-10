package start;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Shell {

    private static final Scanner scan = new Scanner(System.in);
    private static String naredba;

    public static void start() {
        FajlSistem fs = new FajlSistem();
        do {
            System.out.print(fs.getPutanja() + "> ");
            naredba = scan.nextLine();
            if (naredba.isEmpty()) {
                continue;
            }
            if (naredba.equals("exit")) {
                break;
            }
            if (naredba.equals("about")) {
                System.out.println("Projekat iz Operativnih sistema/ORS3.");
                continue;
            }
            if(naredba.matches("^mkdir\s.*$")) {
                Pattern pattern = Pattern.compile("(?<=^mkdir\s).*$");
                Matcher matcher = pattern.matcher(naredba);
                matcher.find();
                String nazivFajla = matcher.group(0);
                fs.getLokacija().noviFajl(nazivFajla);
                continue;
            }
            if (naredba.equals("ls")) {
                fs.getLokacija().ispisiDjecu();
                continue;
            }
            if(naredba.matches("^cd\s.*$")) {
                Pattern pattern = Pattern.compile("(?<=^cd\s).*$");
                Matcher matcher = pattern.matcher(naredba);
                matcher.find();
                String nazivFajla = matcher.group(0);
                boolean pronadjen = false;
                if (nazivFajla.equals("..") && fs.getLokacija().getRoditelj() != null){
                    fs.setPutanja(fs.getLokacija().getNaziv(), -1);
                    fs.setLokacija(fs.getLokacija().getRoditelj());
                    pronadjen = true;
                } else {
                    for (Fajl f : fs.getLokacija().getDjeca()) {
                        if (f.getNaziv().equals(nazivFajla)) {
                            fs.setLokacija(f);
                            fs.setPutanja(f.getNaziv(), 1);
                            pronadjen = true;
                        }
                    }
                }
                if(!pronadjen) {
                    System.out.println("Direktorijum ne postoji!");
                }
                continue;
            }
            if(naredba.matches("^rename\s.*$")){
                Pattern pattern = Pattern.compile("(?<=^rename\s).*$");
                Matcher matcher = pattern.matcher(naredba);
                matcher.find();
                String nazivFajla = matcher.group(0);
                String stariNaziv = nazivFajla.split(" ")[0];
                String noviNaziv = nazivFajla.split(" ")[1];
                boolean pronadjen = false;
                for (Fajl f : fs.getLokacija().getDjeca()) {
                    if (f.getNaziv().equals(stariNaziv)) {
                        f.setNaziv(noviNaziv);
                        pronadjen = true;
                    }
                }
                if(!pronadjen){
                    System.out.println("Naziv ne postoji!");
                }
                continue;
            }
            if(naredba.matches("^rmdir\s.*$")){
                Pattern pattern = Pattern.compile("(?<=^rmdir\s).*$");
                Matcher matcher = pattern.matcher(naredba);
                matcher.find();
                String nazivFajla = matcher.group(0);
                fs.getLokacija().getDjeca().removeIf(f -> f.getNaziv().equals(nazivFajla));
                FajlSistem.sviFajlovi.removeIf(f -> f.getNaziv().equals(nazivFajla));
                continue;
            }
            System.out.println("Naredba ne postoji!");
        } while (true);

        System.out.println("Zatvaranje...");

    }

}
