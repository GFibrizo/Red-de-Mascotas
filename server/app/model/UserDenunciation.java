package model;

public class UserDenunciation implements Comparable<UserDenunciation> {

    public String id;

    public String userName;

    public String name;

    public String lastName;

    public String email;

    public String phone;

    public Address address;

    public int acceptedDenunciations;

    public Boolean active;


    public UserDenunciation(String id, String userName, String name, String lastName, String email, String phone,
                            Address address, int acceptedDenunciations, Boolean active) {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.acceptedDenunciations = acceptedDenunciations;
        this.active = active;
    }

    @Override
    public int compareTo(UserDenunciation another) {
        if (this.acceptedDenunciations < another.acceptedDenunciations) {
            return -1;
        } else {
            return 1;
        }
    }


}
