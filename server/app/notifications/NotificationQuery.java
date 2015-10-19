package notifications;

import java.util.ArrayList;
import java.util.List;

public class NotificationQuery {

    public InQuery installationId;

    public NotificationQuery(String installationId) {
        List<String> query = new ArrayList<>();
        query.add(installationId);
        this.installationId = new InQuery(query);
    }

}
