package ch.hsr.adit.util;

public final class AuthenticationUtil {

  // this code causes findbugs to fail the maven build
  // private static UserDao userDao = new UserDao(HibernateUtil.getSessionFactory());

  public static boolean authenticated(String email, String password) {
    // %TODO add real authentication
    return true;

    // if (email.isEmpty() || password.isEmpty()) {
    // return false;
    // }
    // User user = userDao.getUserByEmail(email);
    // if (user == null) {
    // return false;
    // }
    // String hashedPassword = BCrypt.hashpw(password, user.getPasswordSalt());
    // return hashedPassword.equals(user.getPasswordHash());
  }
}
