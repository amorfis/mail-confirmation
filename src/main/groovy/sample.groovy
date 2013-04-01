private static final EMAIL_REGEX = /[_A-Za-z0-9-]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})/

set 'port', Integer.parseInt(System.getProperty("ratpack.port"))

get("/send") {
  render "enterPassword.html"
}

post("/send") {
  getDatastore(params.get("password"))
  "Sent"
}

get("/sendmail") {
  new pl.devcrowd.MailSender().sendMail()
}

get("/appendParticipants") {
  render "submitParticipantsFile.html"
}

post("/appendParticipants") {
  invalidEmails = new pl.devcrowd.ParticipantsAppender().appendParticipants(request)

  "Added. Invalid emails: " + invalidEmails
}
