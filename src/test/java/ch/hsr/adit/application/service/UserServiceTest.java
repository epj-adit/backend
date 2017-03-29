package ch.hsr.adit.application.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.hsr.adit.application.service.RoleService;
import ch.hsr.adit.application.service.UserService;
import ch.hsr.adit.domain.model.User;
import ch.hsr.adit.domain.persistence.UserDao;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  @Mock
  private UserDao userDao;
  
  @Mock
  private RoleService roleService;

  
  @Test
  public void createUserTest() {
    User newUser = new User();
    newUser.setId(1L);
    newUser.setEmail("student@hsr.ch");
    
    when(userDao.persist(any(User.class))).thenReturn(newUser);
    
    UserService userService = new UserService(userDao, roleService);
    User transientUser = new User();
    transientUser = userService.createUser(transientUser);
    assertEquals(1L, transientUser.getId());

  }

}
