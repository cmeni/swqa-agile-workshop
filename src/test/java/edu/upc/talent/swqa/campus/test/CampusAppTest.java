package edu.upc.talent.swqa.campus.test;

import edu.upc.talent.swqa.campus.domain.CampusApp;
import edu.upc.talent.swqa.campus.domain.User;
import edu.upc.talent.swqa.campus.test.utils.CampusAppState;
import edu.upc.talent.swqa.campus.test.utils.Group;
import edu.upc.talent.swqa.campus.test.utils.InMemoryEmailSender;
import edu.upc.talent.swqa.campus.test.utils.InMemoryUsersRepository;
import edu.upc.talent.swqa.campus.test.utils.SentEmail;
import edu.upc.talent.swqa.campus.test.utils.UsersRepositoryState;
import static edu.upc.talent.swqa.test.utils.Asserts.assertEquals;

import edu.upc.talent.swqa.test.utils.Asserts;
import org.junit.jupiter.api.Test;

import java.util.Set;

public final class CampusAppTest {
  private final CampusAppState defaultInitialState = new CampusAppState(
        new UsersRepositoryState(
              Set.of(
                    new User("1", "John", "Doe", "john.doe@example.com", "student", "swqa"),
                    new User("2", "Jane", "Doe", "jane.doe@example.com", "student", "swqa"),
                    new User("3", "Mariah", "Harris", "mariah.hairam@example.com", "teacher", "swqa")
              ),
              Set.of(new Group(1, "swqa"))
        ),
        Set.of()
  );

  private CampusApp setInitialState(final CampusAppState initialState) {
    return new CampusApp(
          new InMemoryUsersRepository(initialState.usersRepositoryState()),
          new InMemoryEmailSender(initialState.sentEmails())
    );
  }

  @Test
  public void testCreateGroup(){
    final var state = new CampusAppState(
          new UsersRepositoryState(Set.of(), Set.of(new Group(1, "swqa"))),
          Set.of()
    ).copy();
    final var app = setInitialState(state);
    app.createGroup("bigdata");
    final var expectedFinalState = new CampusAppState(
          new UsersRepositoryState(
                Set.of(),
                Set.of(new Group(1, "swqa"), new Group(2, "bigdata"))
          ),
          Set.of()
    );
    assertEquals(expectedFinalState, state);
  }

  @Test
  public void testCreateUser() {

    final var id = "1";
    final var name = "Usuari1";
    final var surname = "Cognom1";
    final var email = "usuari1@gmail.com";
    final var role = "admin";
    final var groupName = "swqa";

    // Creació d'un nou usuari
    final var state = new CampusAppState(
            new UsersRepositoryState(
                      Set.of()
                    , Set.of(new Group(1, groupName))),
            Set.of()
    ).copy();

    final var app = setInitialState(state);

    app.createUser(name, surname, email, role, groupName );

    final var expectedFinalState = new CampusAppState(
            new UsersRepositoryState(
                    Set.of(new User (id, name, surname, email, role, groupName)),
                    Set.of(new Group(1, groupName))
            ),
            Set.of()
    );

    assertEquals(expectedFinalState, state);
  }

  @Test
  public void testSendEmailToGroup() {
    final var state = defaultInitialState.copy();
    final var app = setInitialState(state);
    final var subject = "New campus!";
    final var body = "Hello everyone! We just created a new virtual campus!";
    app.sendMailToGroup("swqa", subject, body);
    final var expectedFinalState = new CampusAppState(
          defaultInitialState.usersRepositoryState(),
          Set.of(
                new SentEmail("john.doe@example.com", subject, body),
                new SentEmail("jane.doe@example.com", subject, body),
                new SentEmail("mariah.hairam@example.com", subject, body)
          )
    );
    assertEquals(expectedFinalState, state);
  }

  @Test
  public void testSendEmailToGroupRole() {
    final var state = defaultInitialState.copy();
    final var app = setInitialState(state);
    final var subject = "Hey! Teacher!";
    final var body = "Let them students alone!!";
    app.sendMailToGroupRole("swqa", "teacher", subject, body);
    final var expectedFinalState = new CampusAppState(
          defaultInitialState.usersRepositoryState(),
          Set.of(new SentEmail("mariah.hairam@example.com", subject, body))
    );
    assertEquals(expectedFinalState, state);
  }
}
