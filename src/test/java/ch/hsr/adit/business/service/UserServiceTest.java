package ch.hsr.adit.business.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.hibernate.HibernateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.hsr.adit.exception.DatabaseException;
import ch.hsr.adit.model.User;
import ch.hsr.adit.persistence.GenericDao;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  @Mock
  private GenericDao<User, Long> userDao;
  
  @Test
  public void createUserTest() {
    User newUser = new User();
    newUser.setId(1L);
    newUser.setEmail("student@hsr.ch");
    
    when(userDao.persist(any(User.class))).thenReturn(newUser);
    
    UserService userService = new UserService(userDao);
    User transientUser = new User();
    transientUser = userService.createUser(transientUser);
    
    assertEquals(new Long(1), transientUser.getId());
  }
  
  @Test
  public void createUserExceptionTest() {
    doThrow(new HibernateException("")).when(userDao).persist(any(User.class));
    
    UserService userService = new UserService(userDao);
    User transientUser = new User();
    transientUser = userService.createUser(transientUser);
    
    assertNull(transientUser);
  }
  
  @Test(expected = DatabaseException.class)
  public void updateUserExceptionTest() {
    doThrow(new HibernateException("")).when(userDao).update(any(User.class));
    
    UserService userService = new UserService(userDao);
    User updatedUser = new User();
    userService.updateUser(updatedUser);
  }
  
  @Test
  public void deleteUserExceptionTest() {
    doThrow(new HibernateException("")).when(userDao).delete(any(User.class));
    doThrow(new HibernateException("")).when(userDao).delete(100L);
    
    UserService userService = new UserService(userDao);
    User userToDelete = new User();
    
    assertFalse(userService.deleteUser(100L));
    assertFalse(userService.deleteUser(userToDelete));
  }
  
  @Test
  public void getUserExceptionTest() {
    doThrow(new HibernateException("")).when(userDao).get(100L);
    UserService userService = new UserService(userDao);
    
    assertNull(userService.get(100L));
  }
  
  @Test
  public void getAllUserExceptionTest() {
    doThrow(new HibernateException("")).when(userDao).getAll();
    UserService userService = new UserService(userDao);
    
    assertNull(userService.getAll());
  }
}
