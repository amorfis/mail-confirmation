package pl.devcrowd

import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class MailSender {

  def sendMail() {
    String host = "mailtrap.io";
    String login = "heroku-46ea8a1ad713ebc4";
    String pass = "69731f6167f7912d";
    Properties props = System.getProperties();
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.user", login);
    props.put("mail.smtp.password", pass);
    props.put("mail.smtp.port", "2525");
    props.put("mail.smtp.auth", "true");

    Session session = Session.getDefaultInstance(props, null);
    MimeMessage message = new MimeMessage(session);
    InternetAddress to= new InternetAddress("pawelstawicki@gmail.com");
    InternetAddress from = new InternetAddress("test@todomain.com");

    message.setFrom(from);
    message.addRecipient(Message.RecipientType.TO, to);
    message.setSubject("SMTP e-mail test");
    message.setText("This is a test e-mail message.");
    Transport transport = session.getTransport("smtp");
    transport.connect(host, login, pass);
    transport.sendMessage(message, message.getAllRecipients());
    transport.close();
  }
}
