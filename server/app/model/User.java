package model;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import model.external.SearchForAdoptionFilters;
import play.modules.mongodb.jackson.MongoDB;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class User {

    @Id
    @ObjectId
    public String id;

    public String notificationId;

    public String userName;

    public String name;

    public String lastName;

    public String email;

    public Password password;

    public String facebookId;

    public String phone;

    public Address address;

    public List<SearchForAdoptionFilters> savedSearchFilters;

    public List<Notification> notifications;

    public int acceptedPublicationReports;


    private static JacksonDBCollection<User, String> collection = MongoDB.getCollection("users", User.class, String.class);


    public User() { }

    public User(String notificationId, String userName, String name, String lastName, String email,
                Password password, String phone, Address address) {
        this.notificationId = notificationId;
        this.userName = userName;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
    }

    public User(String notificationId, String name, String lastName, String email, String facebookId,
                String phone, Address address) {
        this.notificationId = notificationId;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.facebookId = facebookId;
        this.phone = phone;
        this.address = address;
        this.password = new Password();
    }

    public void setId(String id) {
        this.id = id;
    }

    private void addNewSearchRequest(SearchForAdoptionFilters filters) {
        if (this.savedSearchFilters == null)
            this.savedSearchFilters = new ArrayList<>();
        this.savedSearchFilters.add(filters);
    }

    private void removeSearchRequests(List<Integer> indexes) {
        List<SearchForAdoptionFilters> filtersToRemove = new ArrayList<>();
        for (Integer index : indexes)
            filtersToRemove.add(this.savedSearchFilters.get(index));
        this.savedSearchFilters.removeAll(filtersToRemove);
    }

    private void addNewNotification(Notification notification) {
        if (this.notifications == null)
            this.notifications = new ArrayList<>();
        this.notifications.add(notification);
    }

    private void incrementAcceptedPublicationReports() {
        this.acceptedPublicationReports++;
    }


    public static User getById(String id) {
        return User.collection.findOneById(id);
    }

    public static User getByFacebookId(String facebookId) {
        return User.collection.findOne(new BasicDBObject("facebookId", facebookId));
    }

    public static User getByUserName(String userName) {
        return User.collection.findOne(new BasicDBObject("userName", userName));
    }

    public static String create(User user) {
        return User.collection.save(user).getSavedId();
    }

    public static Boolean exists(String userName, String email) {
        BasicDBList value = new BasicDBList();
        value.add(new BasicDBObject("userName", userName));
        value.add(new BasicDBObject("email", email));
        return User.collection.find(new BasicDBObject("$or", value)).toArray().size() > 0;
    }

    public static Boolean existsWithFacebook(String facebookId) {
        return User.collection.find(new BasicDBObject("facebookId", facebookId)).toArray().size() > 0;
    }

    public static void saveSearchRequest(SearchForAdoptionFilters filters) {
        User user = getById(filters.userId);
        user.addNewSearchRequest(filters);
        User.collection.updateById(filters.userId, user);
    }

    public static void removeFiltersFromSavedSearchFilters(String userId, List<Integer> indexes) {
        User user = getById(userId);
        user.removeSearchRequests(indexes);
        User.collection.updateById(userId, user);
    }

    public static List<User> getUsersWithSavedSearchFilters() {
        BasicDBObjectBuilder query = BasicDBObjectBuilder.start();
        query.push("savedSearchFilters").add("$ne", null).pop();
        return User.collection.find(query.get()).toArray();
    }

    public static void saveNotification(String userId, Notification notification) {
        User user = getById(userId);
        user.addNewNotification(notification);
        User.collection.updateById(userId, user);
    }

    public static void incrementAcceptedPublicationReports(String userId) {
        User user = getById(userId);
        user.incrementAcceptedPublicationReports();
        User.collection.updateById(userId, user);
    }

    public static void delete(String id) {
        User user = User.collection.findOneById(id);
        if (user != null)
            User.collection.remove(user);
    }

}
