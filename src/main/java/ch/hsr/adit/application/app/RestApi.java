package ch.hsr.adit.application.app;

public class RestApi {
  
  public static class App {
    public static final String WILDCARD = "*";
    public static final String AUTHENTICATE = "/authenticate";
  }

  public static class User {
    public static final String USERS_FILTERED = "/users/";
    public static final String USER_FILTERED = "/user/";
    public static final String USER_BY_ID = "/user/:id";
    public static final String REGISTER = "/register";
  }
  
  public static class UserLog {
    public static final String USER_LOG = "/userLog";
    public static final String USER_LOGS_FILTERED = "/userLogs/";
  }
  
  public static class Role {
    public static final String ROLE = "/role";
    public static final String ROLE_BY_ID = "/role/:id";
  }
  
  public static class Message {
    public static final String MESSAGE = "/message";
    public static final String MESSAGE_BY_ID = "/message/:id";
    public static final String MESSAGES_FILTERED = "/messages/";
  }
  
  public static class Advertisement {
    public static final String ADVERTISEMENT = "/advertisement";
    public static final String ADVERTISEMENT_BY_ID = "/advertisement/:id";
    public static final String ADVERTISEMENTS_FILTERED = "/advertisements/";
  }
  
  public static class Category {
    public static final String CATEGORY = "/category";
    public static final String CATEGORY_BY_ID = "/category/:id";
    public static final String CATEGORIES_FILTERED = "/categories/";
  }
  
  public static class Subscription {
    public static final String SUBSCRIPTION = "/subscription";
    public static final String SUBSCRIPTION_BY_ID = "/subscription/:id";
    public static final String SUBSCRIPTIONS_FILTERED = "/subscriptions/";
  }
  
  public static class Tag {
    public static final String TAG = "/tag";
    public static final String TAG_BY_ID = "/tag/:id";
    public static final String TAGS = "/tags/";
    public static final String TAGS_FILTERED = "/tags/";
  }
  
  public static class Media {
    public static final String MEDIA = "/media";
    public static final String MEDIA_BY_ID = "/medias/:id";
    public static final String MEDIA_UPLOAD = "/media/upload";
  }
  
  public static class Permission {
    public static final String PERMISSION = "/permission";
    public static final String PERMISSION_BY_ID = "/permission/:id";
    public static final String PERMISSIONS_FILTERED = "/permissions/";
    
  }
}
