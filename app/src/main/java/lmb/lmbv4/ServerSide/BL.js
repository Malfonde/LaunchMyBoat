   var Dal = require('./DAL')
  , Apriori = require('apriori')
  , Underscore = require('underscore');

// -----------------------------------
// -------------- Add ----------------
// -----------------------------------

 exports.loginUser = function (user, res)
 {
    console.log("loginuser method got : " + user.email + " ," + user.password)

	Dal.findUserByEmailAndPass(user.email, user.password, function(err, user)
	{
	    if(err)
	    {
            console.log("Faild to check if the user already exists: " + err);
			res.send(JSON.stringify({ error:"Faild to check if the user already exists: " + err, user: user }));
		}
		else
		{
		    if(user)
		    {
		        console.log("User exists");
		        res.send(JSON.stringify({user: user }));
		    }
		    else
		    {
		         console.log("User doesn't exists");
		         res.send(JSON.stringify({ error:"User doesn't exists", user: user }));
		    }
		}
    });
 };

 exports.createNewUser = function (user,res)
 {
     console.log("createNewUser method got : " + user.email + " ," + user.password);
      var userDetails = user;

     Dal.findUserByEmail(user.email, function(err,user)
     {
        if(err)
	    {
            console.log("Faild to check if the user already exists: " + err);
			res.send(JSON.stringify({ error:"Faild to check if the user already exists: " + err, user: user }));
		}
		else
		{
		    if(user)
		    {
		        console.log("User exists");
		        res.send(JSON.stringify({user: user }));
		    }
		    else
		    {
		         console.log("Registering the user");
		         //pay attention that at this point there is no user name yet..
                 Dal.createNewUserByEmail(userDetails.email,userDetails.password,"", function(err,user)
                 {
                    if(err)
                    {
                        console.log("Failed to register new user: " + err);
                        res.send(JSON.stringify({ error:"Failed to register new user: " + err, user: null }));
                    }
                    else
                    {
                         console.log("User was created : " + user.Email);
                         res.send(JSON.stringify({user: user }));
                    }
                 });
		    }
		}
     });


 };

 exports.loginUserWithFacebook = function (user, res)
  {
     console.log("loginUserWithFacebook method got : " + user.facebookID)
     var userDetails = user;

 	Dal.findUserByFacebookID(user.facebookID, function(err, user)
 	{
 	    if(err || user == null)
 	    {
 			console.log("user doesnt exists, starting registering process");
 			Dal.createNewUserByFacebookID(userDetails.facebookID,userDetails.facebookEmail,userDetails.name, function(err,user)
 			{
 			    if(err)
               {
                     console.log("Faild to register user by facebook details: " + err);
                     res.send(JSON.stringify({ error:"Faild to register user by facebook details: " + err, user: user }));
               }
               else
               {
                    console.log("User was created");
                    res.send(JSON.stringify({user: user }));
               }
 			});
 		}
 		else
 		{
 		    if(user)
 		    {
 		        console.log("User exists");

 		        Dal.updateUserDetailsByFacebook(user.facebookID, user.facebookEmail,user.name, function(err,user)
 		        {
 		             if(err)
                       {
                     	   console.log("Faild to update user details by facebook details: " + err);
                       }
                      else
                      {
                         console.log("User details were updated");
                      }
 		        });

 		        res.send(JSON.stringify({user: user }));
 		    }
 		    else
 		    {
 		         console.log("User doesn't exists");
 		         res.send(JSON.stringify({error:"Faild to register user by facebook details: " + err, user: user }));
 		    }
 		}
     });
  };

 exports.addUser = function (user, res)
 {
    Dal.findUserByMail(user.email, function(err, user)
    {
        if(err)
        {
            console.log("Faild to check if the user already exists: " + err);
            res.send(JSON.stringify({ error:"Faild to check if the user already exists: " + err, user: user }));
        }
        else
        {
            if(user)
            {
                console.log("User exists");
                res.send(JSON.stringify({error:"User exists" , user: user }));
            }
		    else
		    {
		       Dal.addNewUser(userjson,function (err, user)
		       {
                   if (err)
                   {
                        console.log("Faild to add new user: " + err);
                        res.send(JSON.stringify({ error:"Faild to add new user: " + err, user: user }));
                   }
                   else
                   {
                       console.log("New user saved!");
                       res.send(JSON.stringify({user: user }));
                   }
               });
		    }
		}
	});

 };

 exports.getMarinesPicturesPaths = function(res)
 {
    Dal.getAllMarinesIds(function(err,ids)
    {
        if(err)
        {
             console.log("Faild to get marines ids: " + err);
             res.send(JSON.stringify({ error:"Faild to get marines ids: " + err, marines: ids }));
        }
        else
        {
              console.log("Got all Marine Ids");
              res.send(JSON.stringify({marines: ids }));
        }
    });
 };

 exports.getMarineById = function(marine, res)
 {
    Dal.getMarineById(marine.id, function(err,marine)
    {
        if(err)
        {
            console.log("Failed to get marine by id : " + err );
            res.send(JSON.stringify({ error:"Failed to get marine by id : " + err, marine: marine }));
        }
        else
        {
            console.log("Got the Marine by id");
            res.send(JSON.stringify({marine: marine}));
        }
    });
 };

 exports.getWorkerById = function(worker,res)
 {
    Dal.getWorkerById(worker.id, function(err,worker)
    {
        if(err)
        {
            console.log("Failed to get worker by id : " + err)
            res.send(JSON.stringify({ error:"Failed to get worker by id : " + err, worker: worker }));
        }
        else
        {
            console.log("Got the worker by id");
            res.send(JSON.stringify({worker:worker}));
        }
    });
 };

  exports.getServiceById = function(service,res)
  {
     Dal.getServiceById(service.id, function(err,service)
     {
         if(err)
         {
             console.log("Failed to get service by id : " + err)
             res.send(JSON.stringify({ error:"Failed to get service by id : " + err, service: service }));
         }
         else
         {
             console.log("Got the service by id");
             res.send(JSON.stringify({service:service}));
         }
     });
  };

 exports.getAllMarines = function(res)
 {
    Dal.getAllMarines(function(err,marines)
    {
        if(err)
        {
            console.log("Failed to get all marines : " + err);
            res.send(JSON.stringify({ error:"Failed to get all marines : " + err, marines: marines }));
        }
        else
        {
            console.log("Got all the Marine");
            res.send(JSON.stringify({marines: marines}));
        }
    });
 };