package ch.hsr.adit.domain.model;

import lombok.Data;

@Data
public class Credential {
  String email;
  String plaintextPassword;
}
