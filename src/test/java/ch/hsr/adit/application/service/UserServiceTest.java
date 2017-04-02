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

import ch.hsr.adit.domain.model.Role;
import ch.hsr.adit.domain.model.User;
import ch.hsr.adit.domain.persistence.UserDao;
import spark.Request;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  @Mock
  private UserDao userDao;
  
  @Mock
  private RoleService roleService;

  @Mock
  private Request request;
  
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
  
  @Test
  public void deleteUserByIdTest() {
    User persistentUSer = new User();
    persistentUSer.setId(1L);
    when(userDao.get(1L)).thenReturn(persistentUSer);
    
    UserService userService = new UserService(userDao, roleService);
    boolean response = userService.deleteUser(1L);
    
    verify(userDao).delete(any(User.class));;
    assertTrue(response);
  }

  @Test
  public void transformUserAllTest() {
    // arrange
    User dbUser = new User();
    dbUser.setId(1L);
    when(userDao.get(1L)).thenReturn(dbUser);
    
    when(request.params(":id")).thenReturn("1");
    when(request.queryParams("username")).thenReturn("natuil");
    when(request.queryParams("email")).thenReturn("student@hsr.ch");
    when(request.queryParams("passwordHash")).thenReturn("hufd17zduh10xdf");
    when(request.queryParams("isPrivate")).thenReturn("true");
    when(request.queryParams("wantsNotification")).thenReturn("true");
    when(request.queryParams("isActive")).thenReturn("true");
    when(request.queryParams("jwtToken")).thenReturn("u842588u44328u5.jsad8232235325.28395789fdhdasd");
    when(request.queryParams("updated")).thenReturn("Sun Apr 02 15:55:59 UTC 2017");
    when(request.queryParams("role")).thenReturn("1");
    
    Role role = new Role();
    role.setId(1L);
    role.setName("Admin");
    when(roleService.getRole(1L)).thenReturn(role);

    // act
    UserService userService = new UserService(userDao, roleService);
    User user = userService.transformToUser(request);
    
    // assert
    assertEquals(1L, user.getId());
    assertEquals("natuil", user.getUsername());
    assertEquals("student@hsr.ch", user.getEmail());
    assertEquals("hufd17zduh10xdf", user.getPasswordHash());
    assertEquals(true, user.isIsPrivate());
    assertEquals(true, user.isWantsNotification());
    assertEquals(true, user.isIsActive());
    assertEquals("u842588u44328u5.jsad8232235325.28395789fdhdasd", user.getJwtToken());
    // TODO assertEquals(new Date(), user.getUpdated());
    assertEquals(role, user.getRole());
  }
  
}
