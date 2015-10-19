package notifications;

public class PushNotificationRequest {

    public NotificationQuery where;

    public NotificationData data;

    public PushNotificationRequest(String receiverInstallationId, String notificationType, String message) {
        this.where = new NotificationQuery(receiverInstallationId);
        this.data = new NotificationData(notificationType, message);
    }

}
