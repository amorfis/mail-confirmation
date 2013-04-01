package pl.devcrowd

import com.google.code.morphia.Datastore
import com.google.code.morphia.Morphia
import com.mongodb.Mongo
import com.mongodb.MongoURI
import org.apache.commons.fileupload.FileItem
import org.apache.commons.fileupload.FileItemFactory
import org.apache.commons.fileupload.disk.DiskFileItemFactory
import org.apache.commons.fileupload.servlet.ServletFileUpload
import org.apache.commons.lang3.RandomStringUtils


class ParticipantsAppender {

  def appendParticipants(request) {
    (password, emails) = getPasswordAndEmails(request)
    def invalidEmails = []

    emails.each {
      if (email ==~ EMAIL_REGEX) {
        appendParticipant(getDatastore(password), email)
      } else {
        invalidEmails << email
      }
    }

    invalidEmails
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
    def participant = new Participant(email, RandomStringUtils.random(32, true, true), false)
    datastore.save(participant)
  }

}
