package fi.academy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.net.URI;
import java.net.URL;
import java.util.*;

public class Main {
    static HashMap<String, String> pitkanimiLyhytnimi = new HashMap<>();
    static HashMap<String, String> lyhytnimiPitkanimi = new HashMap<>();

    public static void main(String[] args) {
        lueAsemanJSONData();

        System.out.println("Tervetuloa");

        lueJunanJSONData();
    }

    public static void lueJunanJSONData() {
        String baseurl = "https://rata.digitraffic.fi/api/v1";
        try {
            Scanner lukija = new Scanner(System.in);
            System.out.println("Anna lähtöasema (esim. Helsinki): ");
            String lahtoasema = (lukija.nextLine());
            String lahtoasemaPlus = lahtoasema + " asema";
            String lahtoasemaLyhyt;

            if (pitkanimiLyhytnimi.get(lahtoasema) == null) {
                lahtoasemaLyhyt = pitkanimiLyhytnimi.get(lahtoasemaPlus);
            } else {
                lahtoasemaLyhyt = pitkanimiLyhytnimi.get(lahtoasemaPlus);
            }

            System.out.println("Anna määränpää (esim. Lahti): ");
            String maaraasema = (lukija.nextLine());
            String maaraasemaPlus = maaraasema + " asema";
            String maaraasemaLyhyt;

            if (pitkanimiLyhytnimi.get(maaraasema) == null) {
                maaraasemaLyhyt = pitkanimiLyhytnimi.get(maaraasemaPlus);
            } else {
                maaraasemaLyhyt = pitkanimiLyhytnimi.get(maaraasemaPlus);
            }

            URL url = new URL(URI.create(String.format("%s/live-trains/station/" + lahtoasemaLyhyt + "/" + maaraasemaLyhyt, baseurl)).toASCIIString());
            ObjectMapper mapper = new ObjectMapper();
            CollectionType tarkempiListanTyyppi = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Juna.class);
            List<Juna> junat = mapper.readValue(url, tarkempiListanTyyppi);  // pelkkä List.class ei riitä tyypiksi
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
            System.out.println(sb);

        } catch (Exception ex) {
            System.out.println("Tapahtui VIRHE!");
        }
    }

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
}

