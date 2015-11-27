package model;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import static utils.Constants.ARG_DATE_HOUR_FORMAT;
import static utils.Constants.DATE_HOUR_FORMAT;

public class PublicationDenunciation implements Comparable<PublicationDenunciation> {

    public String denouncedUser;

    public String complainantUserId;

    public String complainantUserName;

    public String comment;

    public String date;

    public DenouncedPublication publication;


    public PublicationDenunciation(String denouncedUser, String complainantUserId, String complainantUserName,
                                   String comment, String date, DenouncedPublication publication) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_HOUR_FORMAT);
        String dateFormatted = dateTimeFormatter.parseLocalDate(date).toString(ARG_DATE_HOUR_FORMAT);
        this.denouncedUser = denouncedUser;
        this.complainantUserId = complainantUserId;
        this.complainantUserName = complainantUserName;
        this.comment = comment;
        this.date = dateFormatted;
        this.publication = publication;
    }

    @Override
    public int compareTo(PublicationDenunciation another) {
        if (this.date.compareTo(another.date) < 0) {
            return -1;
        } else {
            return 1;
        }
    }

}
