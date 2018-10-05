package fi.academy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.net.URI;
import java.net.URL;
import java.util.*;

public class Main {

//  esitellään hashmapit, jotta niitä voi käsitellä kaikkialta main-luokasta

    static HashMap<String, String> pitkanimiLyhytnimi = new HashMap<>();
    static HashMap<String, String> lyhytnimiPitkanimi = new HashMap<>();

    public static void main(String[] args) {

        System.out.println("___________   _______________________________________^__\n" +
                " ___   ___ |||  ___   ___   ___    ___ ___  |   __  ,----\\\n" +
                "|   | |   |||| |   | |   | |   |  |   |   | |  |  | |_____\\\n" +
                "|___| |___|||| |___| |___| |___|  | O | O | |  |  |        \\\n" +
                "           |||                    |___|___| |  |__|         )\n" +
                "___________|||______________________________|______________/\n" +
                "           |||      VR JUNAHAKU                          /--------\n" +
                "-----------'''---------------------------------------'");
        System.out.println("Tervetuloa");
        lueAsemanJSONData();
        lueJunanJSONData();
    }

/*
Alla rakennetaan kaksi hashmappia, joissa tieto asemista:
1. koko nimi - lyhyt koodi,
2. lyhyt koodi - koko nimi).
*/

    public static void lueAsemanJSONData() {
        String baseurl = "https://rata.digitraffic.fi/api/v1";

        try {
            URL url = new URL(URI.create(String.format("%s/metadata/stations", baseurl)).toASCIIString());
            ObjectMapper mapper = new ObjectMapper();
            CollectionType asemaLista = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Asema.class);
            List<Asema> assat = mapper.readValue(url, asemaLista);

            for (int i = 0; i < assat.size(); i++) {
                if (assat.get(i).isPassengerTraffic()) {
                    String pitkaNimi = assat.get(i).getStationName();
                    String lyhytNimi = assat.get(i).getStationShortCode();
                    pitkanimiLyhytnimi.put(pitkaNimi, lyhytNimi);
                    lyhytnimiPitkanimi.put(lyhytNimi, pitkaNimi);
                }
            }
        } catch (Exception ex) {
            System.out.println("Tapahtui VIRHE");
        }
    }

/*
Alla olevassa metodissa kysytään käyttäjältä lähto- ja määräasema.
VR:n avoimessa datassa käytetään sekä asemien pitkiä nimiä (kuten Helsinki asema tai Lahti) ja lyhytkoodeja (kuten HKI).
Näitä tietoja voidaan hakea hashmapeista.
*/

    public static void lueJunanJSONData() {
        String baseurl = "https://rata.digitraffic.fi/api/v1";
        try {
            Scanner lukija = new Scanner(System.in);
            System.out.println("Anna lähtöasema (esim. Helsinki): ");
            String annettuLahtoasema = (lukija.nextLine().toLowerCase());
            String lahtoasema = annettuLahtoasema.substring(0,1).toUpperCase() + annettuLahtoasema.substring(1);
            String lahtoasemaPlus = lahtoasema + " asema";
            String lahtoasemaLyhyt;

            if (pitkanimiLyhytnimi.get(lahtoasema) == null) {
                lahtoasemaLyhyt = pitkanimiLyhytnimi.get(lahtoasemaPlus);
            } else {
                lahtoasemaLyhyt = pitkanimiLyhytnimi.get(lahtoasema);
            }

            System.out.println("Anna määränpää (esim. Lahti): ");
            String annettuMaaraasema = (lukija.nextLine().toLowerCase());
            String maaraasema = annettuMaaraasema.substring(0,1).toUpperCase() + annettuMaaraasema.substring(1);
            String maaraasemaPlus = maaraasema + " asema";
            String maaraasemaLyhyt;

            if (pitkanimiLyhytnimi.get(maaraasema) == null) {
                maaraasemaLyhyt = pitkanimiLyhytnimi.get(maaraasemaPlus);
            } else {
                maaraasemaLyhyt = pitkanimiLyhytnimi.get(maaraasema);
            }

/*  Käyttäjän syötteenä saaduista lähtö- ja määräasemasta ja niistä hashmapistä haetuista vastaavista lyhytkoodeista konkatenoidaan
    url, jolla haetaan lähtö- ja määräaseman välinen matka.
    Valmistellaan printti printtaaTiedot-metodilla.
*/

            URL url = new URL(URI.create(String.format("%s/live-trains/station/" + lahtoasemaLyhyt + "/" + maaraasemaLyhyt, baseurl)).toASCIIString());
            ObjectMapper mapper = new ObjectMapper();
            CollectionType tarkempiListanTyyppi = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Juna.class);
            List<Juna> junat = mapper.readValue(url, tarkempiListanTyyppi);
            StringBuilder sb = printtaaTiedot(lahtoasema, lahtoasemaLyhyt, maaraasema, maaraasemaLyhyt, junat);
            System.out.println(sb);

        } catch (Exception ex) {
            System.out.println("Tapahtui VIRHE!");
        }
    }

// alla haetaan tieto matkasta ja luodaan Stringbuilderilla teksti, joka printataan käyttäjälle

    public static StringBuilder printtaaTiedot(String lahtoasema, String lahtoasemaLyhyt, String maaraasema, String maaraasemaLyhyt, List<Juna> junat) {
        StringBuilder sb = new StringBuilder();
        for (Juna j : junat) {
            String junanTyyppi = j.getTrainType();
            String junanNumero = j.getTrainNumber();
            sb.append("Juna " + junanTyyppi);
            sb.append(junanNumero);
            for (TimeTableRow ttr : j.timeTableRows) {
                if (ttr.getStationShortCode().equals(lahtoasemaLyhyt) && ttr.getType().equals("DEPARTURE")) {
                    sb.append(" lähtee asemalta ")
                            .append(lahtoasema)
                            .append(": ")
                            .append(ttr.getScheduledTime())
                            .append("\n");
                }
                if (ttr.getStationShortCode().equals(maaraasemaLyhyt) && ttr.getType().equals("ARRIVAL")) {
                    sb.append("Juna saapuu asemalle ")
                            .append(maaraasema)
                            .append(": ")
                            .append(ttr.getScheduledTime())
                            .append("\n \n");
                }
            }
        }
        return sb;
    }
}

