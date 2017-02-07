package lmb.lmbv4.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by izik on 12/14/2016.
 */

public class User implements Parcelable
{
    private String objectId;
    private String loginEmail;
    private String facebookEmail;
    private String name;
    private ArrayList<Boat> userBoats = new ArrayList<>();
   //TODO:: private String phoneNumber;
    private String userFacebookID;

    public User (String objectId,String email, String name, ArrayList<Boat> userBoats, String facebookEmail, String userFacebookID)
    {
        this.loginEmail = email;
        this.name = name;
        this.objectId = objectId;
        this.userBoats = userBoats;
        this.facebookEmail = facebookEmail;
        this.userFacebookID = userFacebookID;
    }

    protected User(Parcel in) {
        objectId = in.readString();
        loginEmail = in.readString();
        facebookEmail = in.readString();
        name = in.readString();
        userBoats = in.createTypedArrayList(Boat.CREATOR);
        userFacebookID = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(objectId);
        parcel.writeString(loginEmail);
        parcel.writeString(facebookEmail);
        parcel.writeString(name);
        parcel.writeTypedList(userBoats);
        parcel.writeString(userFacebookID);
    }

    //region getters

    public String getObjectId() {
        return objectId;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public String getFacebookEmail() {
        return facebookEmail;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Boat> getUserBoats() {
        return userBoats;
    }

    public String getUserFacebookID() {
        return userFacebookID;
    }




    //endregion
}
