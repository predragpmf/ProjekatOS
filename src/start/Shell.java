package start;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Shell {

    private static final Scanner scan = new Scanner(System.in);
    //Novi fs:
    public static FajlSistem fs;
    // Unesena naredba:
    private static String naredba;

    public static void start() {
        //Petlja za provjeru unosa:
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
            // Novi folder: mkdir <naziv_foldera>
            if (naredba.matches("^mkdir\s.*$")) {
                Pattern pattern = Pattern.compile("(?<=^mkdir\s).*$");
                Matcher matcher = pattern.matcher(naredba);
                matcher.find();
                String nazivFoldera = matcher.group(0);
                fs.getLokacija().noviFajl(nazivFoldera, true);
                continue;
            }
            // Ispisi sve fajlove u trenutnom folderu: ls
            if (naredba.equals("ls")) {
                fs.getLokacija().ispisiDjecu();
                continue;
            }
            // Otvori folder: cd <naziv_foldera>
            if (naredba.matches("^cd\s.*$")) {
                Pattern pattern = Pattern.compile("(?<=^cd\s).*$");
                Matcher matcher = pattern.matcher(naredba);
                matcher.find();
                String nazivFoldera = matcher.group(0);
                boolean pronadjen = false;
                if (nazivFoldera.equals("..") && fs.getLokacija().getRoditelj() != null) {
                    fs.setPutanja(fs.getLokacija().getNaziv(), -1);
                    fs.setLokacija(fs.getLokacija().getRoditelj());
                    pronadjen = true;
                } else {
                    for (Fajl f : fs.getLokacija().getDjeca()) {
                        if (f.getNaziv().equals(nazivFoldera) && f.getFolder()) {
                            fs.setLokacija(f);
                            fs.setPutanja(f.getNaziv(), 1);
                            pronadjen = true;
                        }
                    }
                }
                if (!pronadjen) {
                    System.out.println("Folder ne postoji!");
                }
                continue;
            }
            // Promjeni naziv fajla/foldera: rename <stari_naziv> <novi_naziv>
            if (naredba.matches("^rename\s.*$")) {
                Pattern pattern = Pattern.compile("(?<=^rename\s).*$");
                Matcher matcher = pattern.matcher(naredba);
                matcher.find();
                String naziv = matcher.group(0);
                String stariNaziv = naziv.split(" ")[0];
                String noviNaziv = naziv.split(" ")[1];
                boolean pronadjen = false;
                for (Fajl f : fs.getLokacija().getDjeca()) {
                    if (f.getNaziv().equals(stariNaziv)) {
                        f.setNaziv(noviNaziv);
                        pronadjen = true;
                    }
                }
                if (!pronadjen) {
                    System.out.println("Naziv ne postoji!");
                }
                continue;
            }
            // Obrisi folder/fajl rm <naziv_foldera>
            if (naredba.matches("^rm\s.*$")) {
                Pattern pattern = Pattern.compile("(?<=^rm\s).*$");
                Matcher matcher = pattern.matcher(naredba);
                matcher.find();
                String naziv = matcher.group(0);
                for(Fajl fajl : fs.getLokacija().getDjeca()){
                    if(fajl.getNaziv().equals(naziv)){
                        fajl.setRoditelj(null);
                    }
                }
                fs.getLokacija().getDjeca().removeIf(f -> f.getNaziv().equals(naziv));
                FajlSistem.sviFajlovi.removeIf(f -> f.getNaziv().equals(naziv));
                FajlSistem.brojFajlova--;
                continue;
            }
            System.out.println("Naredba ne postoji!");
        } while (true);

        System.out.println("Zatvaranje...");

    }

}
