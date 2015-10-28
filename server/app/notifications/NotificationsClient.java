package notifications;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import play.Logger;
import play.libs.Json;
import play.libs.ws.*;

@Component
public class NotificationsClient {

    private static final String APPLICATION_ID = "cLJ93SxVri3M9sCj7hGsJUnp45mA46kWMLdwdsno";
    private static final String REST_API_KEY = "99jcwgukKAkdcZb70j7YvzEBDEoGOOXQiuMdBGK6";
    private static final String PARSE_PUSH_URL = "https://api.parse.com/1/push";

    public void pushNotification(String receiverInstallationId, String notificationType, String message) {
        PushNotificationRequest request = new PushNotificationRequest(receiverInstallationId, notificationType, message);
        JsonNode jsonRequest = Json.toJson(request);
        WS.url(PARSE_PUSH_URL)
                .setContentType("application/json")
                .setHeader("X-Parse-Application-Id", APPLICATION_ID)
                .setHeader("X-Parse-REST-API-Key", REST_API_KEY)
                .post(jsonRequest);
        Logger.info("Push notification sent: " + jsonRequest);
    }

}
