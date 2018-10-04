package fi.academy;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Asema {
        String stationName;
        String stationShortCode;
        boolean passengerTraffic;

        public boolean isPassengerTraffic() {
            return passengerTraffic;
        }

        public String getStationShortCode() {
            return stationShortCode;
        }
        public String getStationName() {
            return stationName;
        }
        @Override
        public String toString() {
            return stationName + ", " + stationShortCode;
        }
}

