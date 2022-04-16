package start;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Shell {

    private static final Scanner scan = new Scanner(System.in);
    //Novi fs:
    public static FajlSistem fs;
    // Unesena naredba:
    private static String naredba;

    public static void start() throws IOException {

        String velicinaMem;
        String brojOkviraMem;

        System.out.println("Projekat iz ORS3");
        System.out.print("Unesite velicinu memorije (podrazumjevano = 16000kB): ");
        velicinaMem = scan.nextLine().strip();
        System.out.print("Unesite broj okvira (podrazumjevano = 32): ");
        brojOkviraMem = scan.nextLine().strip();
        if (velicinaMem != "" && velicinaMem != "") {
            RasporedjivacProcesa.mem.setVelicinaMemorijeB(Integer.parseInt(velicinaMem));
            RasporedjivacProcesa.mem.setBrojOkvira(Integer.parseInt(brojOkviraMem));
            RasporedjivacProcesa.mem.setVelicinaOkviraB(Integer.parseInt(velicinaMem) / Integer.parseInt(brojOkviraMem));
        }
        System.out.println("Dobrodosli!");

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
                fs.getLokacija().noviFajl(nazivFoldera, true, 0);
                continue;
            }
            // Novi fajl : mkfile <naziv_fajla> <velicina_fajla>
            if (naredba.matches("mkfile\s.*$")){
                Pattern pattern = Pattern.compile("(?<=^mkfile\s).*$");
                Matcher matcher = pattern.matcher(naredba);
                matcher.find();
                String naziv = matcher.group(0);
                String nazivFajla = naziv.split(" ")[0];
                int velicinaFajla = Integer.parseInt(naziv.split(" ")[1]);
                fs.getLokacija().noviFajl(nazivFajla, false, velicinaFajla);
                continue;
            }
            // Pokreni fajl : run <naziv_fajla>
            if (naredba.matches("run\s.*$")){
                Pattern pattern = Pattern.compile("(?<=^run\s).*$");
                Matcher matcher = pattern.matcher(naredba);
                matcher.find();
                String nazivFajla = matcher.group(0);
                for (Fajl f : fs.getLokacija().getDjeca()) {
                    if (f.getNaziv().equals(nazivFajla)) {
                        fs.rp.noviProces(f);
                    }
                }
                continue;
            }
            // Ispisi memoriju : mem
            if (naredba.matches("^mem$")) {
                fs.rp.mem.ispisiMemoriju();
                System.out.println();
                continue;
            }

            // Mozda ne radi:
            // Sacuvaj podatke i izadji: save
            if (naredba.matches("^save$")) {
                CuvanjePodataka.sacuvaj();
                return;
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
