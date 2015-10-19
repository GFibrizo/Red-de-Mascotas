package notifications;

public class NotificationData {

    public String notificationType;

    public String alert;

    public NotificationData(String notificationType, String message) {
        this.notificationType = notificationType;
        this.alert = message;
    }

}
