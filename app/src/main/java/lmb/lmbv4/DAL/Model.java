package lmb.lmbv4.DAL;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import lmb.lmbv4.Entities.Boat;
import lmb.lmbv4.Entities.Dock;
import lmb.lmbv4.Entities.GetBoatOrder;
import lmb.lmbv4.Entities.Marine;
import lmb.lmbv4.Entities.ReturnBoatOrder;
import lmb.lmbv4.Entities.Service;
import lmb.lmbv4.Entities.User;
import lmb.lmbv4.Entities.Worker;
import lmb.lmbv4.ServerSideHandlers.ServerRequest;

/**
 * Created by izik on 12/14/2016.
 */

public class Model
{
    private static  Model instance = new Model();
    private User currentUser;
    private ArrayList<String> marineIds = new ArrayList<>();
    public static String SharedFolder = "https://192.168.1.15/Marines/";
    private static String serverIP = "http://192.168.1.15:8080";
    public static String MarineProfileImageName = "/1.jpg";
    //just for notice : serverIP = 192.168.1.15:8080

    private Model()
    {
    }

    public User getCurrentUser()
    {
        return currentUser;
    }

    //Always use this singltone, dont do new Model in your code. ever!.
    public static Model instance() {
        return instance;
    }

    //region Public Methods

    public User LoginUser(String email, String password)
    {
        JSONObject jsonUserDetails = new JSONObject();

        try {
            jsonUserDetails.put("email", email);
            jsonUserDetails.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON("http://192.168.1.15:8080/loginUser", jsonUserDetails);

        if(DidServerHadErrors(json))
        {
            return null;
        }

        //if user is found
        User userToReturn = ConvertJSONToUser(json);

        currentUser = userToReturn;
        return userToReturn;
    }

    public User RegisterUser(String email, String password)
    {
        JSONObject jsonUserDetails = new JSONObject();

        try {
            JSONArray boats = new JSONArray();

            jsonUserDetails.put("email", email);
            jsonUserDetails.put("password", password);


           /* for (Boat b : currentUser.getUserBoats()) {

                JSONObject boatsJson = ConvertBoatToJSON(b);
                boats.put(boatsJson);
            }

            jsonUserDetails.put("boats", boats.toString());*/


        } catch (JSONException e) {
            e.printStackTrace();
        }
        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON("http://192.168.1.15:8080/registerUserByEmail", jsonUserDetails);

        if(DidServerHadErrors(json))
        {
            return null;
        }

        //if user is found
        User userToReturn = ConvertJSONToUser(json);

        currentUser = userToReturn;
        return userToReturn;
    }

    public ArrayList<String> getMarineIds() {
        return marineIds;
    }

    private boolean DidServerHadErrors(JSONObject json) {
        try
        {
            String error = json.getString("error");

            //theres error!
            return true;
        }
        catch (Exception e)
        {
            // there is no error and we can return false
            return false;
        }
    }

    private User ConvertJSONToUser(JSONObject json)
    {
        String userObjectID = "";
        ArrayList<Boat> boats = new ArrayList<>();
        String email = "";
        String name = "";
        String facebookEmail ="";
        String userFacebookID = "";

        //from facebook details
        try
        {
            JSONObject userJson = (JSONObject) json.get("user");
            userObjectID = userJson.getString("_id");
            userFacebookID = userJson.getString("FacebookID");
            facebookEmail = userJson.getString("FacebookEmail");
            name = userJson.getString("Name");
            email = userJson.getString("Email");

            JSONArray jar = new JSONArray(userJson.get("BoatsIDs").toString());

            for (int i=0; i<jar.length();i++)
            {
                JSONObject jobj = jar.getJSONObject(i);
                Boat b = GetBoatByID(jobj.getString("_id"));

                if (b!= null) {
                    boats.add(b);
                }
                else
                {
                    Log.e("loadingBoatByID","there was an error while trying to load the boat's info : " + jobj.getString("_id"));
                }
            }
        }
        catch (JSONException e)
        {
        }

        return new User(userObjectID,email,name,boats, facebookEmail, userFacebookID);
    }

    public Boat GetBoatByID(String id)
    {
        JSONObject jobj = new JSONObject();

        try {
            jobj.put("_id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON(serverIP+"/getBoatByID", jobj);

        if(DidServerHadErrors(json))
        {
            return null;
        }

        try {

            JSONObject objJson = (JSONObject)json.get("boat");
            Boat b = ConvertJSONToBoat(objJson);
            return b;
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    private Boat ConvertJSONToBoat(JSONObject jobj)
    {
        String name = "";
        String objectId = "";
        double fuel=0;
        boolean isDocked = false;
        boolean isRacked = false;
        double weight = 0;
        String ownerID;
        String dockID;
        ArrayList<String> permittedUsersIDs = new ArrayList<>();
        String rackID;
        boolean isForSale;
        String lastTreatedTime;
        String lastTreatedDate;
        ArrayList<GetBoatOrder> getBoatOrders = new ArrayList<>();
        ArrayList<ReturnBoatOrder> returnBoatOrders = new ArrayList<>();
        String marineID = "";

        try
        {
            name = jobj.getString("Name");
            objectId = jobj.getString("_id");
            isDocked = Boolean.parseBoolean(jobj.getString("IsDocked"));
            isRacked = Boolean.parseBoolean(jobj.getString("IsRacked"));
            isForSale = Boolean.parseBoolean(jobj.getString("IsForSale"));
            weight = Double.parseDouble(jobj.getString("Weight"));
            ownerID = jobj.getString("OwnerID");
            rackID = jobj.getString("RackID");
            dockID = jobj.getString("DockName");
            marineID = jobj.getString("AtMarine");
            lastTreatedTime = jobj.getString("LastTreatedTime");
            lastTreatedDate = jobj.getString("LastTreatedDate");
            JSONArray jar = new JSONArray(jobj.getString("PermittedUsersIDs"));

            for (int i=0; i<jar.length();i++)
            {
                JSONObject jsonobj = jar.getJSONObject(i);
                permittedUsersIDs.add(jsonobj.getString("_id"));
            }

            jar = new JSONArray(jobj.getString("GetBoatOrders"));

            for (int i=0; i<jar.length();i++)
            {
                JSONObject jsonobj = jar.getJSONObject(i);
                String objID = jsonobj.getString("_id");
                GetBoatOrder order = GetBoatOrderByID(objID);
                if(order != null) {
                    getBoatOrders.add(order);
                }
            }

            jar = new JSONArray(jobj.getString("ReturnBoatOrders"));

            for (int i=0; i<jar.length();i++)
            {
                JSONObject jsonobj = jar.getJSONObject(i);
                String objID = jsonobj.getString("_id");
                ReturnBoatOrder order = GetReturnBoatOrderByID(objID);
                returnBoatOrders.add(order);
            }

            Boat boat = new Boat(objectId,name,fuel,isDocked,dockID,rackID,isRacked,
                            weight,ownerID,permittedUsersIDs,isForSale,lastTreatedTime,
                                                                lastTreatedDate, marineID);

            return boat;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private ReturnBoatOrder GetReturnBoatOrderByID(String objID) {

        JSONObject jobj = new JSONObject();

        try {
            jobj.put("_id", objID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON(serverIP + "/getReturnBoatOrder", jobj);

        if(DidServerHadErrors(json))
        {
            return null;
        }


        try {

            JSONObject objJson = (JSONObject)json.get("order");
            ReturnBoatOrder order = ConvertJSONToReturnBoatOrder(objJson);

            return order;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private ReturnBoatOrder ConvertJSONToReturnBoatOrder(JSONObject json) {

        String _objID = "";
        String boatID = "";
        String time = "";
        String date = "";
        ArrayList<Service> selectedServices = new ArrayList<>();
        String orderingUserID = "";

        //from facebook details
        try
        {

            _objID = json.getString("_id");
            boatID = json.getString("BoatID");
            time = json.getString("Time");
            date = json.getString("Date");
            orderingUserID = json.getString("OrderingUserID");

            JSONArray jar = new JSONArray(json.get("SelectedServices").toString());

            for (int i=0; i<jar.length();i++)
            {
                JSONObject jobj = jar.getJSONObject(i);
                Service srv = ConvertJSONToService(jobj);

                if (srv!= null) {
                    selectedServices.add(srv);
                }
                else
                {
                    Log.e("ConvertJSONToService","there was an error while trying to load the service's info : " + jobj.getString("_id"));
                }
            }

            return new ReturnBoatOrder(_objID,time,date,orderingUserID,selectedServices, boatID);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public GetBoatOrder GetBoatOrderByID(String objID)
    {
        JSONObject jobj = new JSONObject();

        try {
            jobj.put("_id", objID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON("http://192.168.1.15:8080/getBoatOrderByID", jobj);

        if(DidServerHadErrors(json))
        {
            return null;
        }

        try {

            JSONObject objJson = (JSONObject)json.get("order");
            GetBoatOrder order = ConvertJSONToGetBoatOrder(objJson);

            return order;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private GetBoatOrder ConvertJSONToGetBoatOrder(JSONObject json)
    {
        String _objID = "";
        String boatID = "";
        String time = "";
        String date = "";
        ArrayList<Service> selectedServices = new ArrayList<>();
        String orderingUserID = "";

        //from facebook details
        try
        {
            _objID = json.getString("_id");
            boatID = json.getString("BoatID");
            time = json.getString("Time");
            date = json.getString("Date");
            orderingUserID = json.getString("OrderingUserID");

            JSONArray jar = new JSONArray(json.get("SelectedServices").toString());

            for (int i=0; i<jar.length();i++)
            {
                JSONObject jobj = jar.getJSONObject(i);
                Service srv = ConvertJSONToService(jobj);

                if (srv!= null) {
                    selectedServices.add(srv);
                }
                else
                {
                    Log.e("ConvertJSONToService","there was an error while trying to load the service's info : " + jobj.getString("_id"));
                }
            }

            return new GetBoatOrder(_objID,time,date,orderingUserID,selectedServices, boatID);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private Service ConvertJSONToService(JSONObject jobj)
    {
        String name = "";
        String description="";
        String objectID="";
        double price = 0;
        String marineID;

        try
        {
            name = jobj.getString("Name");
            description = jobj.getString("Description");
            objectID = jobj.getString("_id");
            price = jobj.getDouble("Price");
            marineID = jobj.getString("MarineID");

            return new Service(name,description,objectID,price, marineID);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public User GetUserByID(String objectID)
    {
        JSONObject jobj = new JSONObject();

        try {
            jobj.put("_id", objectID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON("http://192.168.1.15:8080/getUserByID", jobj);

        if(DidServerHadErrors(json))
        {
            return null;
        }

        User user = ConvertJSONToUser(json);

        return user;
    }

    public Dock getDockByID(String objectId)
    {
        ServerRequest serverRequest = new ServerRequest();
        JSONObject jobj = new JSONObject();

        try
        {
            jobj.put("objectId", objectId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject json = serverRequest.getJSON("http://192.168.1.15:8080/getDockByID", jobj);

        if(DidServerHadErrors(json))
        {
            return null;
        }

        Dock dockedAt = ConvertJSONtoDock(json);

        return dockedAt;

    }

    private Dock ConvertJSONtoDock(JSONObject jobj)
    {
        String name ="";
        String objectId="";
        ArrayList<String> boatsIDs = new ArrayList<>();

        try
        {
            name = jobj.get("Name").toString();
            JSONArray jar = new JSONArray(jobj.getString("BoatsIDs"));

            for (int i=0; i<jar.length();i++)
            {
                boatsIDs.add(jar.getString(i));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        Dock dock = new Dock(objectId,boatsIDs,name);

        return dock;
    }

    private JSONObject ConvertBoatToJSON(Boat b)
    {
        return new JSONObject();
    }

    public boolean loginUserThroughFacebook(String id, String email, String name)
    {
        JSONObject jsonUserDetails = new JSONObject();

        try
        {
            jsonUserDetails.put("facebookID", id);
            jsonUserDetails.put("facebookEmail", email);
            jsonUserDetails.put("name", name);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON("http://192.168.1.15:8080/loginUserWithFacebook", jsonUserDetails);

        if(DidServerHadErrors(json))
        {
            Log.e("Model","loginUserWithFacebook error");
            return false;
        }

        //if user is found
        User userToReturn = ConvertJSONToUser(json);

        currentUser = userToReturn;
        return true;
    }

    public void updateUserDetails(String email, String name) {
        //TODO:: update server with user facebook details.
    }

    public  String[] getMarinesPicturesPaths()
    {
        ServerRequest serverRequest = new ServerRequest();
        JSONObject jsonUserDetails = new JSONObject();

        JSONObject json = serverRequest.getJSON(serverIP + "/getMarinesPicturesPaths", jsonUserDetails);

        if(DidServerHadErrors(json))
        {
            Log.e("Model","getMarinesPicturesPaths error");
            return new String[0];
        }

        ArrayList<String> Results = new ArrayList();
        try {
            JSONArray jar = json.getJSONArray("marines");
            for (int i = 0; i < jar.length(); i++)
            {
                String imagePath = SharedFolder + jar.getJSONObject(i).getString("_id") + MarineProfileImageName;
                Results.add(imagePath); //adding the pictures folder path

                //adding a marine to the all_marines array if its new.
                if(!marineIds.contains(jar.getJSONObject(i).getString("_id")))
                {
                    marineIds.add(jar.getJSONObject(i).getString("_id"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // converting the arraylist<string> to desired string[]
        String[] results = new String[Results.size()];
        results = Results.toArray(results);

        return results;

    }

    public Marine getMarineById(String id)
    {
        JSONObject jsonUserDetails = new JSONObject();
        try {

            jsonUserDetails.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON(serverIP + "/getMarineById", jsonUserDetails);

        if(DidServerHadErrors(json))
        {
            return null;
        }

        //if user is found
        Marine marineResult = ConvertJSONToMarine(json);

        return marineResult;
    }

    private Marine ConvertJSONToMarine(JSONObject json)
    {
        String marineObjectID = "";
        String email = "";
        String name = "";
        String facebookEmail ="";
        String marineFacebookID = "";
        ArrayList<Dock> docks = new ArrayList<>();
        ArrayList<Worker> workers = new ArrayList<>();
        ArrayList<Service> returnBoatServices = new ArrayList<>();
        ArrayList<Service> getBoatServices = new ArrayList<>();
        ArrayList<String> rackedBoatsIDs = new ArrayList<>();

        //from facebook details
        try
        {
            JSONObject marineJson = (JSONObject) json.get("marine");
            marineObjectID = marineJson.getString("_id");
            marineFacebookID = marineJson.getString("FacebookID");
            facebookEmail = marineJson.getString("FacebookEmail");
            name = marineJson.getString("Name");
            email = marineJson.getString("Email");

            JSONArray jar = new JSONArray(marineJson.get("Docks").toString());

            for (int i=0; i<jar.length();i++)
            {
                JSONObject jobj = jar.getJSONObject(i);
                Dock dock =  ConvertJSONtoDock(jobj);
                docks.add(dock);
            }

            jar = new JSONArray(marineJson.get("WorkersIDs").toString());

            for (int i=0; i<jar.length();i++)
            {
                JSONObject jobj = jar.getJSONObject(i);
                String id = jobj.getString("_id");
                Worker worker =  GetWorkerByID(id);
                workers.add(worker);
            }


            jar = new JSONArray(marineJson.get("ReturnBoatServicesIDs").toString());

            for (int i=0; i<jar.length();i++)
            {
                JSONObject jobj = jar.getJSONObject(i);
                String id = jobj.getString("_id");

                Service srv =  GetServiceByID(id);
                returnBoatServices.add(srv);
            }

            jar = new JSONArray(marineJson.get("GetBoatServicesIDs").toString());

            for (int i=0; i<jar.length();i++)
            {
                JSONObject jobj = jar.getJSONObject(i);
                String id = jobj.getString("_id");
                Service srv =  GetServiceByID(id);
                getBoatServices.add(srv);
            }

            jar = new JSONArray(marineJson.get("RackedBoatsIDs").toString());

            for (int i=0; i<jar.length();i++)
            {
                JSONObject jobj = jar.getJSONObject(i);
                String rackedBoatID =  jobj.getString("rackedBoatID");
                rackedBoatsIDs.add(rackedBoatID);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return new Marine(name ,workers ,marineObjectID, email,facebookEmail, marineFacebookID,docks,returnBoatServices, getBoatServices, rackedBoatsIDs);
    }

    public Service GetServiceByID(String id)
    {
        JSONObject jsonUserDetails = new JSONObject();
        try {

            jsonUserDetails.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON(serverIP + "/getServiceById", jsonUserDetails);

        if(DidServerHadErrors(json))
        {
            return null;
        }

        //if user is found
        JSONObject jobj = null;
        try {
            jobj = (JSONObject) json.get("service");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Service srvResult = ConvertJSONToService(jobj);

        return srvResult;
    }

    public Worker GetWorkerByID(String id)
    {
        JSONObject jsonUserDetails = new JSONObject();
        try {

            jsonUserDetails.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON(serverIP + "/getWorkerById", jsonUserDetails);

        if(DidServerHadErrors(json))
        {
            return null;
        }

        //if user is found
        Worker workerResult = ConvertJSONToWorker(json);

        return workerResult;
    }

    private Worker ConvertJSONToWorker(JSONObject jobj)
    {
        String name = "";
        String lastName = "";
        String objID = "";
        String iD = "";
        Boolean isAppointed = false;
        double salary = 0;

        try {
            jobj = (JSONObject)jobj.get("worker");
            name =  jobj.getString("Name");
            lastName =  jobj.getString("LastName");
            objID = jobj.getString("_id");
            iD = jobj.getString("ID");
            isAppointed = jobj.getBoolean("IsAppointed");
            salary = jobj.getDouble("Salary");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new Worker(name,lastName,objID,iD,isAppointed,salary);
    }

   /* public String getMarinePicturePathById(String id)
    {
        return SharedFolder + id + "/1.jpg";
    }*/

    public ArrayList<Marine> getAllMarines()
    {
        JSONObject jsonMarineDetails = new JSONObject();
        ServerRequest serverRequest = new ServerRequest();

        JSONObject json = serverRequest.getJSON(serverIP + "/getAllMarines", jsonMarineDetails);

        if(DidServerHadErrors(json))
        {
            return null;
        }

        ArrayList<Marine> Results = new ArrayList();
        try {
            JSONArray jar = json.getJSONArray("marines");
            for (int i = 0; i < jar.length(); i++) {
                Results.add(ConvertJSONToMarine(jar.getJSONObject(i))); //Getting current json object and converting to marine

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

       /* for (Marine m :  Results)
        {
            if(!allMarines.contains(m))
            {
                allMarines.add(m);
            }
        }*/

        return Results;
    }
    //endregion
}
