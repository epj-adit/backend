package ch.hsr.adit.util;

import ch.hsr.adit.domain.model.Permission;


public class PermissionUtil {
  
  private PermissionUtil() {
    throw new IllegalAccessError("Utility class");
  }
  
  public static final Permission BASIC_PERMISSION = new Permission(1L, "basic_permission");
  public static final Permission SUPERVISOR_PERMISSION =
      new Permission(2L, "supervisor_permission");
  public static final Permission ADMINISTRATOR_PERMISSION =
      new Permission(3L, "administrator_permission");
  public static final Permission EDIT_CATEGORIES = new Permission(4L, "edit_categories");
  public static final Permission REVIEW_ADVERTISEMENTS =
      new Permission(5L, "review_advertisements");
  public static final Permission EDIT_ROLE = new Permission(6L, "edit_role");
  public static final Permission EDIT_ISACTIVE = new Permission(7L, "edit_isActive");

}
