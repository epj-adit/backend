package ch.hsr.adit.domain.model;

import com.google.gson.annotations.SerializedName;

public enum AdvertisementState {
  @SerializedName("0")
  TO_REVIEW, 
  @SerializedName("1")
  DECLINED, 
  @SerializedName("2")
  ACTIVE, 
  @SerializedName("3")
  EXPIRED, 
  @SerializedName("4")
  CLOSED;
}
