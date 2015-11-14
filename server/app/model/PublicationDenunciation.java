package model;

public class PublicationDenunciation implements Comparable<PublicationDenunciation> {

    public String denouncedUser;

    public String complainantUserId;

    public String complainantUserName;

    public String comment;

    public String date;

    public DenouncedPublication publication;


    public PublicationDenunciation(String denouncedUser, String complainantUserId, String complainantUserName,
                                   String comment, String date, DenouncedPublication publication) {
        this.denouncedUser = denouncedUser;
        this.complainantUserId = complainantUserId;
        this.complainantUserName = complainantUserName;
        this.comment = comment;
        this.date = date;
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
