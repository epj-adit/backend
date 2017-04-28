package ch.hsr.adit.application.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.JsonSyntaxException;

import ch.hsr.adit.domain.model.Message;
import ch.hsr.adit.domain.persistence.MessageDao;
import ch.hsr.adit.util.JsonUtil;
import spark.Request;


public class MessageService {

  private static final Logger LOGGER = Logger.getLogger(MessageService.class);
  private final MessageDao messageDao;


  public MessageService(MessageDao messageDao) {
    this.messageDao = messageDao;
  }

  public Message createMessage(Message message) {
    return (Message) messageDao.persist(message);
  }

  public Message get(Message message) {
    return get(message.getId());
  }

  public Message get(Long id) {
    Message message = messageDao.get(id);
    return message;
  }
  
  public List<Message> getAllFiltered(Request request) {
    List<Long> userIds = new ArrayList<>();
    if (request.queryParams("userId") != null) {
      String[] users = request.queryParamsValues("userId");
      for (int i = 0; i < users.length; i++) {
        userIds.add(Long.valueOf(users[i]));
      }
      return messageDao.getFiltered(userIds);
    } else {
      return messageDao.getAll();
    }
  }


  public List<Message> getAll() {
    return messageDao.getAll();
  }
  
  public Message transformToMessage(Request request) {
    try {
      Message message = JsonUtil.fromJson(request.body(), Message.class);
      LOGGER.info("Received JSON data: " + message.toString());
      return message;
    } catch (JsonSyntaxException e) {
      LOGGER.error("Cannot parse JSON: " + request.body());
      throw e;
    }
  }


}
