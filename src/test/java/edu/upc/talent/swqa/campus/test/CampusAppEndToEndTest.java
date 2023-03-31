package edu.upc.talent.swqa.campus.test;

import edu.upc.talent.swqa.campus.domain.CampusApp;
import edu.upc.talent.swqa.campus.domain.User;
import edu.upc.talent.swqa.campus.domain.UsersRepository;
import edu.upc.talent.swqa.campus.infrastructure.ConsoleEmailSender;
import edu.upc.talent.swqa.campus.infrastructure.PostgreSqlUsersRepository;
import edu.upc.talent.swqa.campus.infrastructure.UsersDb;
import edu.upc.talent.swqa.test.utils.DatabaseBackedTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public final class CampusAppEndToEndTest extends DatabaseBackedTest {

  private CampusApp app;
  private UsersRepository repo;

  @BeforeEach
  public void setUpDatabaseSchema() {
    db.update(UsersDb.groupsTableDml);
    db.update(UsersDb.usersTableDml);
    repo = new PostgreSqlUsersRepository(db);
    repo.createGroup("swqa");
    repo.createUser("John", "Doe", "john.doe@example.com", "student", "swqa");
    repo.createUser("Jane", "Doe", "jane.doe@example.com", "student", "swqa");
    repo.createUser("Mariah", "Harris", "mariah.hairam@example.com", "teacher", "swqa");
    this.app = new CampusApp(repo, new ConsoleEmailSender());
  }

  @Test
  public void testCreateUsers(){

    app.createUser("Usuari1", "Cognom1", "usuari1@gmail.com", "admin", "swqa");
    assertTrue(repo.getUsersByGroupAndRole("swqa","admin")
                .contains(new User("N/A","Usuari1", "Cognom1", "usuari1@gmail.com", "admin", "swqa")));
  }

  @Test
  public void testCreateGroup(){
    app.createGroup("bigdata");

  }

  @Test
  public void testSendEmailToGroup() {
    app.sendMailToGroup("swqa", "New campus!", "Hello everyone! We just created a new virtual campus!");
  }

  @Test
  public void testSendEmailToGroupRole() {
    app.sendMailToGroupRole("swqa", "teacher", "Hey! Teacher!", "Let them students alone!!");
  }

}
