package notifications;

import java.util.List;

public class InQuery {

    public List<String> $in;

    public InQuery(List<String> in) {
        this.$in = in;
    }

}
