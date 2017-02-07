package lmb.lmbv4.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ad05n on 1/29/2017.
 */
public class GetBoatOrder implements Parcelable
{
    private String boatID;
    private String objectId;
    private String time;
    private String date;
    private String orderingUserID;
    private ArrayList<Service> selectedServices;

    public GetBoatOrder(String objectId, String time, String date, String orderingUserID, ArrayList<Service> selectedServices, String boatID) {
        this.objectId = objectId;
        this.time = time;
        this.date = date;
        this.orderingUserID = orderingUserID;
        this.selectedServices = selectedServices;
        this.boatID = boatID;
   }
    protected GetBoatOrder(Parcel in) {
        boatID = in.readString();
        objectId = in.readString();
        time = in.readString();
        date = in.readString();
        orderingUserID = in.readString();
        selectedServices = in.createTypedArrayList(Service.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(boatID);
        dest.writeString(objectId);
        dest.writeString(time);
        dest.writeString(date);
        dest.writeString(orderingUserID);
        dest.writeTypedList(selectedServices);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetBoatOrder> CREATOR = new Creator<GetBoatOrder>() {
        @Override
        public GetBoatOrder createFromParcel(Parcel in) {
            return new GetBoatOrder(in);
        }

        @Override
        public GetBoatOrder[] newArray(int size) {
            return new GetBoatOrder[size];
        }
    };

    //region getters


    public String getBoatID() {
        return boatID;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getOrderingUserID() {
        return orderingUserID;
    }

    public ArrayList<Service> getSelectedServices() {
        return selectedServices;
    }




    //endregion
}
