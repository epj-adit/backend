package ch.hsr.adit.domain.model.filter;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.adit.domain.model.AdvertisementState;
import lombok.Data;

@Data
public class AdvertisementFilter {

  private String title;
  private String description;
  private Long userId;
  private List<AdvertisementState> advertisementStates = new ArrayList<>();
  private List<Long> categoryIds = new ArrayList<>();
  private List<Long> tagIds = new ArrayList<>();

  public boolean isEmpty() {
    return title == null && description == null && userId == null && advertisementStates.isEmpty()
        && tagIds.isEmpty() && categoryIds.isEmpty();
  }
}
