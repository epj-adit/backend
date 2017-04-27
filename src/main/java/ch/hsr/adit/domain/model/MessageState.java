package ch.hsr.adit.domain.model;

import com.google.gson.annotations.SerializedName;

public enum MessageState {
  @SerializedName("0")
  READ, 
  @SerializedName("1")
  UNREAD, 
  @SerializedName("2")
  SPAM, 
  @SerializedName("3")
  DELETED;
}
