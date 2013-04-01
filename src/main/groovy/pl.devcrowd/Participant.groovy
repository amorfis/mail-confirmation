package pl.devcrowd

import com.google.code.morphia.annotations.Entity
import com.google.code.morphia.annotations.Id
import com.google.code.morphia.annotations.Index
import org.apache.commons.lang3.RandomStringUtils

@Entity(value = "participants", noClassnameStored = true)
@Index(value = "email", unique = true, sparse = true)
class Participant {

  @Id
  String email;

  String linkHash = RandomStringUtils.random(32, true, true)

  boolean mailSent = false;

  Participant(String email, String linkHash, boolean mailSent) {
    this.email = email
    this.linkHash = linkHash
    this.mailSent = mailSent
  }

  String getEmail() {
    return email
  }

  String getLinkHash() {
    return linkHash
  }

  boolean getMailSent() {
    return mailSent
  }
}
