// PINJA teki suomalaisen päivämäärä- ja aikamuotoilun

package fi.academy;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeTableRow {
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
            return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, new Locale("FI", "fi")).format(scheduledTime);
        }
        public Date getScheduledTimeDate() {
            return scheduledTime;
        }
//        public Date getScheduledTime() {
//            return scheduledTime;
//        }

        @Override
        public String toString() {
            return "Lähtöasema " + getStationShortCode() + ": " + getScheduledTime();
        }
}

