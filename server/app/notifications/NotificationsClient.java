package notifications;

import play.libs.Json;
import play.libs.ws.*;
import play.libs.F.Function;
import play.libs.F.Promise;

public class NotificationsClient {

    private static final String APPLICATION_ID = "XXXXXXX";
    private static final String REST_API_KEY = "XXXXXXX";
    private static final String PARSE_PUSH_URL = "https://api.parse.com/1/push";

    public String pushNotification(String receiverUserId, String message) {
        PushNotificationRequest request = new PushNotificationRequest(receiverUserId, message);
        Promise<String> promise = WS.url(PARSE_PUSH_URL)
                .setContentType("application/json")
                .setHeader("X-Parse-Application-Id", APPLICATION_ID)
                .setHeader("X-Parse-REST-API-Key", REST_API_KEY)
                .post(Json.toJson(request))
                .map(
                        new Function<WSResponse, String>() {
                            public String apply(WSResponse response) {
                                String result = response.getBody();
                                return result;
                            }
                        }
                );
        long timeout = 10001;
        return promise.get(timeout);
    }

}
