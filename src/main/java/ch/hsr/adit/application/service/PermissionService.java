package ch.hsr.adit.application.service;

import ch.hsr.adit.domain.model.User;
import ch.hsr.adit.util.PermissionUtil;
import ch.hsr.adit.util.TokenUtil;


public class PermissionService {

  private final UserService userService;
  private final TokenUtil tokenUtil;


  public PermissionService(UserService userService) {
    this.userService = userService;
    this.tokenUtil = TokenUtil.getInstance();
  }

  public boolean checkBasicPermissions(User user, String token) {
    User dbUser = getUserFromToken(token);
    //user that has non-basic permissions should be admin or supervisor
    if (!isAdmin(dbUser) && !isSupervisor(dbUser)) {
      if (!checkIds(user, dbUser)) {
        return false;
      }
      if (!dbUser.getRole().getPermissions().contains(PermissionUtil.BASIC_PERMISSION)) {
        return false;
      }
    }
    return true;
  }

  public boolean checkEditCategoriesPermission(String token) {
    User user = getUserFromToken(token);

    if (!user.getRole().getPermissions().contains(PermissionUtil.EDIT_CATEGORIES)) {
      return false;
    }
    return true;
  }

  public boolean checkEditRolesPermission(String token) {
    User user = getUserFromToken(token);

    if (!user.getRole().getPermissions().contains(PermissionUtil.EDIT_ROLE)) {
      return false;
    }
    return true;
  }

  public boolean checkEditIsActivePermission(String token) {
    User user = getUserFromToken(token);

    if (!user.getRole().getPermissions().contains(PermissionUtil.EDIT_ISACTIVE)) {
      return false;
    }
    return true;
  }

  public boolean checkReviewAdvertisementPermission(User user, String token) {
    User dbUser = getUserFromToken(token);
    if (checkIds(user, dbUser)) {
      return true;
    }
    if (!dbUser.getRole().getPermissions().contains(PermissionUtil.REVIEW_ADVERTISEMENTS)) {
      return false;
    }
    return true;
  }

  private boolean isAdmin(User user) {
    return user.getRole().getPermissions().contains(PermissionUtil.ADMINISTRATOR_PERMISSION);
  }

  private boolean isSupervisor(User user) {
    return user.getRole().getPermissions().contains(PermissionUtil.SUPERVISOR_PERMISSION);
  }

  private User getUserFromToken(String token) {
    String email = tokenUtil.getEmailFromToken(token);
    return userService.getByEmail(email);
  }
  
  private boolean checkIds(User user, User dbUser) {
    if (user.getId() != dbUser.getId()) {
      return false;
    }
    return true;
  }
}
