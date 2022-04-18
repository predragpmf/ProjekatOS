package start;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Shell {

    private static final Scanner scan = new Scanner(System.in);
    //Novi fs:
    public static FajlSistem fs = new FajlSistem();
    // Unesena naredba:
    private static String naredba;

    public static void start() {

        ulaz();

        fs.getLokacija().noviFajl("out", true, 0);

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
            if (naredba.equals("help")) {
                StringBuilder helpSb = new StringBuilder();
                helpSb.append("Naredbe:\n");
                helpSb.append("mkdir <naziv_foldera> -- Novi folder;\n");
                helpSb.append("mkfile <naziv_fajla> <velicina_fajla> -- Novi fajl;\n");
                helpSb.append("ls -- Ispis fajlova i foldera;\n");
                helpSb.append("cd <naziv_foldera>/<..> -- Promjeni trenutnu lokaciju;\n");
                helpSb.append("rename <naziv_fajla/foldera> -- Promjeni naziv;\n");
                helpSb.append("rm <naziv_fajla/foldera> -- Obrisi fajl/folder;\n");
                helpSb.append("run <naziv_fajla> -- Pokreni fajl;\n");
                helpSb.append("stop <naziv_fajla> -- Prekini proces;\n");
                helpSb.append("block <naziv_fajla> -- Blokiraj proces;\n");
                helpSb.append("unblock <naziv_fajla> -- Odblokiraj proces;\n");
                helpSb.append("mem -- Prikazi pokrenute procese i upotrebu memorije;\n");
                //helpSb.append("save -- Sacuvaj fajlsistem na disk;\n");
                System.out.println(helpSb);
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
            if (naredba.matches("mkfile\s.*$")) {
                Pattern pattern = Pattern.compile("(?<=^mkfile\s).*$");
                Matcher matcher = pattern.matcher(naredba);
                matcher.find();
                String naziv = matcher.group(0);
                String[] nazivIVelicina = naziv.split(" ");
                if (nazivIVelicina.length == 1) {
                    System.out.println("Greska u unosu!");
                    continue;
                }
                String nazivFajla = nazivIVelicina[0];
                int velicinaFajla = Integer.parseInt(nazivIVelicina[1]);
                fs.getLokacija().noviFajl(nazivFajla, false, velicinaFajla);
                continue;
            }
            // Pokreni fajl : run <naziv_fajla>
            if (naredba.matches("run\s.*$")) {
                Pattern pattern = Pattern.compile("(?<=^run\s).*$");
                Matcher matcher = pattern.matcher(naredba);
                matcher.find();
                String nazivFajla = matcher.group(0);
                for (Fajl f : fs.getLokacija().getDjeca()) {
                    if (f.getNaziv().equals(nazivFajla)) {
                        FajlSistem.rp.noviProces(f);
                    }
                }
                continue;
            }
            if (naredba.matches("stop\s.*$")) {
                Pattern pattern = Pattern.compile("(?<=^stop\s).*$");
                Matcher matcher = pattern.matcher(naredba);
                matcher.find();
                String nazivFajla = matcher.group(0);
                for (Proces p : RasporedjivacProcesa.sviProcesi) {
                    if (p.getNaziv().equals(nazivFajla)) {
                        RasporedjivacProcesa.sviProcesi.remove(p);
                        RasporedjivacProcesa.mem.izbaci(p.getTabelaStranica());
                    }
                }
                continue;
            }
            if (naredba.matches("block\s.*$")) {
                Pattern pattern = Pattern.compile("(?<=^block\s).*$");
                Matcher matcher = pattern.matcher(naredba);
                matcher.find();
                String nazivFajla = matcher.group(0);
                for (Proces p : RasporedjivacProcesa.sviProcesi) {
                    if (p.getNaziv().equals(nazivFajla)) {
                        RasporedjivacProcesa.blokiraniProcesi.add(p);
                        RasporedjivacProcesa.sviProcesi.remove(p);
                    }
                }
                continue;
            }
            if (naredba.matches("unblock\s.*$")) {
                Pattern pattern = Pattern.compile("(?<=^unblock\s).*$");
                Matcher matcher = pattern.matcher(naredba);
                matcher.find();
                String nazivFajla = matcher.group(0);
                for (Proces p : RasporedjivacProcesa.blokiraniProcesi) {
                    if (p.getNaziv().equals(nazivFajla)) {
                        RasporedjivacProcesa.sviProcesi.add(p);
                        RasporedjivacProcesa.tred = new Tred();
                        RasporedjivacProcesa.tred.start();
                    }
                }
                continue;
            }
            // Ispisi memoriju : mem
            if (naredba.matches("^mem$")) {
                RasporedjivacProcesa.mem.ispisiMemoriju();
                System.out.println();
                continue;
            }

            // Ispisi sve fajlove u trenutnom folderu: ls
            if (naredba.equals("ls")) {
                if (fs.getLokacija().getNaziv().equals("out")) {
                    fs.as.ispis();
                }
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
                for (Fajl fajl : fs.getLokacija().getDjeca()) {
                    if (fajl.getNaziv().equals(naziv)) {
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

    private static void ulaz() {
        StringBuilder naslovSb = new StringBuilder();
        naslovSb.append("-------------------\n");
        naslovSb.append("|---Projekat OS---|\n");
        naslovSb.append("-------------------\n");
        System.out.println(naslovSb);
        /*
        Ubaci inicijalizaciju memorije.
        */
    }

}
