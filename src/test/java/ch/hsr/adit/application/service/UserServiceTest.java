package ch.hsr.adit.application.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.hsr.adit.domain.model.User;
import ch.hsr.adit.domain.persistence.MessageDao;
import ch.hsr.adit.domain.persistence.UserDao;
import spark.Request;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  @Mock
  private UserDao userDao;

  @Mock
  private MessageDao messageDao;

  @Mock
  private Request request;

  @Test
  public void createUserTest() {
    User newUser = new User();
    newUser.setId(1L);
    newUser.setEmail("student@hsr.ch");

    when(userDao.persist(any(User.class))).thenReturn(newUser);

    UserService userService = new UserService(userDao, messageDao);
    User transientUser = new User();
    transientUser = userService.createUser(transientUser);
    assertEquals(1L, transientUser.getId());
  }

  @Test
  public void deleteUserByIdTest() {
    User persistentUSer = new User();
    persistentUSer.setId(1L);
    when(userDao.get(1L)).thenReturn(persistentUSer);

    UserService userService = new UserService(userDao, messageDao);
    boolean response = userService.deleteUser(1L);

    verify(userDao).delete(any(User.class));;
    assertTrue(response);
  }

}
