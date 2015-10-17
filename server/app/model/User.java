package model;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import play.modules.mongodb.jackson.MongoDB;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.ObjectId;

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
    }

    public void setId(String id) {
        this.id = id;
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

    public static void delete(String id) {
        User user = User.collection.findOneById(id);
        if (user != null)
            User.collection.remove(user);
    }

}
