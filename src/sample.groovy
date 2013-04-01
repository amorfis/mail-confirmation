import com.mongodb.Mongo
import com.mongodb.MongoURI

set 'port', Integer.parseInt(System.getProperty("ratpack.port"))

get("/") {
  def ua = headers['user-agent']
  "Your user-agent: ${ua}"
}
    
get("/foo/:name") {
  "Hello, ${urlparams.name}"
}

get("/send") {
  "Enter password for sending mailing:"
  "<form action=\"/send\">"
  "<input type=\"password\" name=\"password\" />"
  "<input type=\"submit\" />"
  "<form>"
}

post("/send") {
  String password = params.get("password")
  Mongo mongo = new Mongo(new MongoURI("mongodb://devcrowd:" + password + "@alex.mongohq.com:10052/app13411678"))
  db = mongo.getDB("app13411678");
  db.getCollection("participants")
}