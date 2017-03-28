package ch.hsr.adit.business.service;

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
    
<<<<<<< Upstream, based on branch 'develop' of https://github.com/fabianhauser/engineering-projekt-server.git
    assertEquals(1L, transientUser.getId());
=======
    //assertEquals(new Long(1), transientUser.getId());
>>>>>>> 818ebd5 KeyStore & Token refactoring done
  }

}
