import com.google.code.morphia.Datastore
import com.google.code.morphia.Morphia
import com.mongodb.Mongo
import com.mongodb.MongoURI
import org.apache.commons.fileupload.FileItem
import org.apache.commons.fileupload.FileItemFactory
import org.apache.commons.fileupload.disk.DiskFileItemFactory
import org.apache.commons.fileupload.servlet.ServletFileUpload
import org.apache.commons.lang3.RandomStringUtils

private static final EMAIL_REGEX = /[_A-Za-z0-9-]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})/

set 'port', Integer.parseInt(System.getProperty("ratpack.port"))

get("/send") {
  render "enterPassword.html"
}

post("/send") {
  getDatastore(params.get("password"))
  "Sent"
}

get("/appendParticipants") {
  render "submitParticipantsFile.html"
}

post("/appendParticipants") {
  (password, emails) = getPasswordAndEmails(request)
  def invalidEmails = []

  emails.each {
    if (email ==~ EMAIL_REGEX) {
      appendParticipant(getDatastore(password), email)
    } else {
      invalidEmails << email
    }
  }

  "Added. Invalid emails: " + invalidEmails
}

private List getPasswordAndEmails(request) {
  items = getUploadedItems(request)

  def participantsFile = File.createTempFile("participants", null)
  def password;

  for (FileItem item : items) {
    if (item.fieldName == "participantsEmails") {
      item.write(participantsFile)
    } else if (item.fieldName == "password") {
      password = item.string
    }
  }

  List<String> emails = parseEmailsFile(participantsFile)

  (password, emails)
}

private List<FileItem> getUploadedItems(request) {
  FileItemFactory factory = new DiskFileItemFactory();
  factory.setSizeThreshold(10 * 1024 * 1024)

  ServletFileUpload upload = new ServletFileUpload(factory);
  upload.parseRequest(request);
}

private Datastore getDatastore(String password) {
  mongoAddress = System.getProperty("mongoAddress")
  databaseName = System.getProperty("databaseName")
  username = System.getProperty("username")

  mongo = new Mongo(new MongoURI(mongoAddress))
  new Morphia().createDatastore(mongo, databaseName, username, password.toCharArray())
}

private List<String> parseEmailsFile(File file) {
  def emails = []
  file.eachLine { line ->
    def email = line.trim()
    if (email) {
      if (email[0] != "#") {
        emails << email
      }
    }
  }

  emails
}

def appendParticipant(Datastore datastore, String email) {
  def participant = new pl.devcrowd.Participant(email, RandomStringUtils.random(32, true, true), false)
  datastore.save(participant)
}