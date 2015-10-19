package notifications;

import java.util.ArrayList;
import java.util.List;

public class NotificationQuery {

    public InQuery installationId;

    public InQuery deviceType;

    public NotificationQuery(String installationId) {
        List<String> installationIdQuery = new ArrayList<>();
        installationIdQuery.add(installationId);
        List<String> deviceTypeQuery = new ArrayList<>();
        deviceTypeQuery.add("android");
        this.installationId = new InQuery(installationIdQuery);
        this.deviceType = new InQuery(deviceTypeQuery);
    }

}
