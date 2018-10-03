package fi.academy;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.text.DateFormat;
import java.time.ZoneId;
import java.util.Locale;

import java.net.URI;
import java.net.URL;
import java.util.*;

public class Main {
    static HashMap<String, String> avainArvo = new HashMap<>();
    static HashMap<String, String> arvoAvain = new HashMap<>();

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
            String departureStation = (lukija.nextLine());
            String departureShort = avainArvo.get(departureStation);

            System.out.println("Anna määränpää (esim. Lahti): ");
            String destinationStation = (lukija.nextLine());
            String destinationShort = avainArvo.get(destinationStation);

            URL url = new URL(URI.create(String.format("%s/live-trains/station/" + departureShort + "/" + destinationShort, baseurl)).toASCIIString());
            ObjectMapper mapper = new ObjectMapper();
            CollectionType tarkempiListanTyyppi = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Juna.class);
            List<Juna> junat = mapper.readValue(url, tarkempiListanTyyppi);  // pelkkä List.class ei riitä tyypiksi
            StringBuilder sb = new StringBuilder();
            for (Juna j : junat) {
                for (TimeTableRow ttr : j.timeTableRows) {
                    if (ttr.getStationShortCode().equals(departureShort) && ttr.getType().equals("DEPARTURE")) {
                        sb.append(departureShort)
                                .append(" Juna Lähtee: ")
                                .append(ttr.getScheduledTime())
                                .append("\n");
                    }
                    if (ttr.getStationShortCode().equals(destinationShort) && ttr.getType().equals("ARRIVAL")) {
                        sb.append(destinationShort)
                                .append(" Juna saapuu: ")
                                .append(ttr.getScheduledTime())
                                .append("\n");;
                    }
                }
            }
            System.out.println(sb);
          /*
           for (int i = 0; i < junat.size(); i++) {
               System.out.println(junat.get(i).getTrainCategory() + " " + junat.get(i).getTrainNumber());
               for (int j = 0; j < junat.get(i).timeTableRows.size(); j++) {
                   String s = junat.get(i).getTimeTableRows().get(j).getStationShortCode();
                   if (s == destinationShort)
                       System.out.println("Lähtö:" +junat.get(i).getTimeTableRows().get(j).getScheduledTime());
               }


            }*/
            // Seuraavaa varten on toteutettava TimeTableRow luokka:
//           System.out.println(junat.get(0).getTimeTableRows().get(0).getScheduledTime());
//            System.out.println("\n\n");
//            System.out.println(junat.get(0));

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
                    String longName = assat.get(i).getstationName();
                    String shortName = assat.get(i).getStationShortCode();
                    avainArvo.put(longName, shortName);
                    arvoAvain.put(shortName, longName);
                }
            }
 /*           String arvo = avainArvo.get("Ahonpää");
            if (arvo == null) {
                System.out.println("Valitsemallasi asemalla ei ole matkustajaliikennettä");
            } else {
                System.out.println(arvo);
            }
            String arvoKaksi = arvoAvain.get("ÄHT");
            System.out.println(arvoKaksi);
*/
        } catch (Exception ex) {
            System.out.println("Tapahtui VIRHE");
        }
    }
}

class Juna {
    boolean cancelled;
    String commuterLineID;
    //LocalDate departureDate;  // Jackson ei oikein pidä Java 8 päivistä oletuksena
    Date departureDate;
    String operatorShortCode;
    int operatorUICCode;
    boolean runningCurrently;
    List<TimeTableRow> timeTableRows;
    Date timetableAcceptanceDate;
    String timetableType;
    String trainCategory;
    int trainNumber;
    String trainType;
    long version;
    List<Asema> assat;

    @Override
    public String toString() {
        return "Juna{" + "cancelled=" + cancelled + ", commuterLineID='" + commuterLineID + '\'' + ", departureDate=" + departureDate + ", operatorShortCode='" + operatorShortCode + '\'' + ", operatorUICCode=" + operatorUICCode + ", runningCurrently=" + runningCurrently + ", timeTableRows=" + timeTableRows + ", timetableAcceptanceDate=" + timetableAcceptanceDate + ", timetableType='" + timetableType + '\'' + ", trainCategory='" + trainCategory + '\'' + ", trainNumber=" + trainNumber + ", trainType='" + trainType + '\'' + ", version=" + version + '}';
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public List<Asema> getAssat() {
        return assat;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getCommuterLineID() {
        return commuterLineID;
    }

    public void setCommuterLineID(String commuterLineID) {
        this.commuterLineID = commuterLineID;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public String getOperatorShortCode() {
        return operatorShortCode;
    }

    public void setOperatorShortCode(String operatorShortCode) {
        this.operatorShortCode = operatorShortCode;
    }

    public int getOperatorUICCode() {
        return operatorUICCode;
    }

    public void setOperatorUICCode(int operatorUICCode) {
        this.operatorUICCode = operatorUICCode;
    }

    public boolean isRunningCurrently() {
        return runningCurrently;
    }

    public void setRunningCurrently(boolean runningCurrently) {
        this.runningCurrently = runningCurrently;
    }

    public List<TimeTableRow> getTimeTableRows() {
        return timeTableRows;
    }

    public void setTimeTableRows(List<TimeTableRow> timeTableRows) {
        this.timeTableRows = timeTableRows;
    }

    public Date getTimetableAcceptanceDate() {
        return timetableAcceptanceDate;
    }

    public void setTimetableAcceptanceDate(Date timetableAcceptanceDate) {
        this.timetableAcceptanceDate = timetableAcceptanceDate;
    }

    public String getTimetableType() {
        return timetableType;
    }

    public void setTimetableType(String timetableType) {
        this.timetableType = timetableType;
    }

    public String getTrainCategory() {
        return trainCategory;
    }

    public void setTrainCategory(String trainCategory) {
        this.trainCategory = trainCategory;
    }

    public int getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(int trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class TimeTableRow {
    Date scheduledTime;
    String stationShortCode;
    String type;

    public String getStationShortCode() {
        return stationShortCode;
    }

    public String getType() {
        return type;
    }

    public String getScheduledTime() {
        return DateFormat.getDateInstance(DateFormat.SHORT, new Locale("FI", "fi")).format(scheduledTime)  // Päivämäär
                + " "
                + scheduledTime.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();    //Tästä käytetään vain Aikaa
    }

    @Override
    public String toString() {
        return "Arrival time to " + getStationShortCode() + ": " + getScheduledTime();
    }
}
@JsonIgnoreProperties(ignoreUnknown = true)
class Asema {
        String stationName;
        String stationShortCode;
        boolean passengerTraffic;

        public boolean isPassengerTraffic() {
           return passengerTraffic;
        }

        public String getStationShortCode() {
            return stationShortCode;
        }
        public String getstationName() {
            return stationName;
        }
        @Override
        public String toString() {
            return stationName + ", " + stationShortCode;
        }
}

