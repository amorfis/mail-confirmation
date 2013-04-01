import com.google.code.morphia.Datastore
import com.google.code.morphia.DatastoreImpl
import com.google.code.morphia.Morphia
import com.mongodb.Mongo
import com.mongodb.MongoURI

set 'port', Integer.parseInt(System.getProperty("ratpack.port"))

get("/send") {
  render "enterPassword.html"
}

post("/send") {
  getDatastore()
  "Sent"
}

get("/testfill") {
  ds = getDatastore([password:"devcrowd"])
  ds.save(new pl.devcrowd.Participant("testmail", "testlink", false))

  "Filled in"
}

private Datastore getDatastore(params) {
  mongoAddress = System.getProperty("mongoAddress")
  databaseName = System.getProperty("databaseName")
  username = System.getProperty("username")

  password = params.get("password")

  mongo = new Mongo(new MongoURI(mongoAddress))
  new Morphia().createDatastore(mongo, databaseName, username, password.toCharArray())
}