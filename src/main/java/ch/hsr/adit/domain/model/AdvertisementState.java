package ch.hsr.adit.domain.model;

import com.google.gson.annotations.SerializedName;

public enum AdvertisementState {
  @SerializedName("0")
  DECLINED, 
  @SerializedName("1")
  ACTIVE, 
  @SerializedName("2")
  EXPIRED, 
  @SerializedName("3")
  CLOSED;
}
