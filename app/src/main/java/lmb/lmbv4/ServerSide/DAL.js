var mydb = require('./myDB')
 , ObjectID = require('mongodb').ObjectID;

var connection;
mydb.openDatabase(function (err, db) {
    if (db != null) {
        connection = db;
    }
});

module.exports = {

	addNewUser: function (newUser, callback){
		var collection = connection.collection("Users");
		
		var result = collection.insert({
			Email: newUser.email
		}, function (err, data){
			
			if(err){
				console.log('new user error: ' + err);
				callback(err,data);
			}
			else{
				console.log('new user success');
                callback(null, data.ops[0]);
			}			
		});
	},

	findUserByEmailAndPass: function (email,password, callback)
	{
	    var collection = connection.collection("Users");

        var result = collection.findOne({Email: email, Password: password}, function(err,result){
            if(err){
                console.log('err: ' + err);
                callback(err,result);
            }
            else
            {
                console.log('!@#!@#!@#!@!#@!#@ found 1 user !#!@#!@#!@#!@#!@# : ' + result);
                callback(null,result);
            }
        });
	},

	findUserByEmail: function (email, callback)
	{
	    var collection = connection.collection("Users");

	     var result = collection.findOne({Email: email}, function(err,result){
                    if(err){
                        console.log('err: ' + err);
                        callback(err,result);
                    }
                    else
                    {
                        console.log('!@#!@#!@#!@!#@!#@ found 1 user !#!@#!@#!@#!@#!@# : ' + result);
                        callback(null,result);
                    }
                });
	},

	findUserByFacebookID: function (id, callback)
	{
        var collection = connection.collection("Users");

        var result = collection.findOne({FacebookID: id}, function(err,result){
            if(err){
                console.log('err: ' + err);
                callback(err,result);
            }
            else
            {
                console.log('!@#!@#!@#!@!#@!#@ found 1 user !#!@#!@#!@#!@#!@# : ' + result);
                callback(null,result);
            }
        });
	},

	createNewUserByEmail: function (email,password,name, callback)
	{
        var collection = connection.collection("Users");

        var result = collection.insert({
        			//Favorites: newUser.favorites,
        			FacebookID: "",
        			Name: name,
        			FacebookEmail: "",
        			Email:email,
        			Password:password
        		}, function (err, data){

        			if(err){
        				console.log('new user error: ' + err);
        				callback(err,data);
        			}
        			else{
        				console.log('new user success');
                        callback(null, data.ops[0]);
        			}
        		});
	},

	createNewUserByFacebookID: function (id,email,name, callback)
	{
        var collection = connection.collection("Users");

        var result = collection.insert({
        			//Favorites: newUser.favorites,
        			FacebookID: id,
        			Name: name,
        			FacebookEmail: email,
        			Email:"",
        			Password:""
        		}, function (err, data){

        			if(err){
        				console.log('new user error: ' + err);
        				callback(err,data);
        			}
        			else{
        				console.log('new user success');
                        callback(null, data.ops[0]);
        			}
        		});
	},

	updateUserDetailsByFacebook: function(id,email,name,callback)
	{
         var collection = connection.collection("Users");

          var result = collection.update(
                 {FacebookID: id },
                 {Name: name,
                  FacebookEmail: email
                 }, function (err, numberOfRecordsUpdatedJson) {
                     if (err) {
                             console.log('update User Details By FacebookID error: ' + err);
                             callback(err, objectWithStatusOperation);
                         }
                         else {
                             console.log('update User Details By FacebookID success');
                             console.log('number of records Changed : ' + numberOfRecordsUpdatedJson);
                             callback(null, numberOfRecordsUpdatedJson);
                         }
                 });
	},

	getAllMarinesIds: function(callback)
	{
	    var results;
        connection.collection("Marines").find({},{ fields: { 'Name': 0, 'FacebookID':0,'FacebookEmail':0,'Email':0,'Docks':0,'WorkersIDs':0,'ReturnBoatServicesIDs':0,
        'GetBoatServicesIDs':0, 'RackedBoatsIDs':0} })
        .toArray(function (err, ids)
        {
            if(err)
            {
                console.log("get Marines ids err : " + err);
                callback(err, results);
            }
            else
            {
                results = ids;
                console.log(results);
                callback(null, results);
            }
        });
	},

	getMarineById: function(id ,callback)
	{
         var collection = connection.collection("Marines");
         var o_id = ObjectID(id);

         var result = collection.findOne({_id:o_id}, function(err,result)
         {
            if(err){
                console.log('err: ' + err);
                callback(err,result);
            }
            else
            {
                console.log('!@#!@#!@#!@!#@!#@ found 1 marine !#!@#!@#!@#!@#!@# : ' + result);
                callback(null,result);
            }
        });
	},

	getWorkerById: function(id,callback)
	{
         var collection = connection.collection("Workers");
         var o_id = ObjectID(id);

         var result = collection.findOne({_id:o_id}, function(err,result)
         {
            if(err){
                console.log('err: ' + err);
                callback(err,result);
            }
            else
            {
                console.log('!@#!@#!@#!@!#@!#@ found 1 worker !#!@#!@#!@#!@#!@# : ' + result);
                callback(null,result);
            }
        });
	},

	getServiceById: function(id,callback)
    	{
             var collection = connection.collection("Services");
             var o_id = ObjectID(id);

             var result = collection.findOne({_id:o_id}, function(err,result)
             {
                if(err){
                    console.log('err: ' + err);
                    callback(err,result);
                }
                else
                {
                    console.log('!@#!@#!@#!@!#@!#@ found 1 service !#!@#!@#!@#!@#!@# : ' + result);
                    callback(null,result);
                }
            });
    	},

	getAllMarines: function(callback)
	{
	     var collection = connection.collection("Marines");

         var result = collection.find({}).toArray(function(err, results)
         {
            if(err){
                console.log('err: ' + results);
                callback(err,results);
            }
            else
            {
                console.log('!@#!@#!@#!@!#@!#@ found 1 marine !#!@#!@#!@#!@#!@# : ' + results);
                callback(null,results);
            }
        });
	}
};