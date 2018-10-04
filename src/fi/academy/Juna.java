package fi.academy;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Juna {
        //LocalDate departureDate;  // Jackson ei oikein pidä Java 8 päivistä oletuksena
        List<TimeTableRow> timeTableRows;
        String trainType;
        String trainNumber;
        long version;
        List<Asema> assat;

        @Override
        public String toString() {
            return "Juna{" + '\'' + ", operatorShortCode='" + ", timeTableRows=" + timeTableRows + '\'' + ", trainType='" + trainType + '\'' + ", version=" + version + '}';
        }
        public String getTrainType() {
            return trainType;
        }
        public String getTrainNumber() {
            return trainNumber;
        }
        public List<Asema> getAssat() {
            return assat;
        }
        public List<TimeTableRow> getTimeTableRows() {
            return timeTableRows;
        }
        public void setTimeTableRows(List<TimeTableRow> timeTableRows) {
            this.timeTableRows = timeTableRows;
        }
}
