package ch.hsr.adit.application.controller;

import static ch.hsr.adit.util.JsonUtil.jsonTransformer;
import static spark.Spark.get;
import static spark.Spark.post;

import ch.hsr.adit.application.service.MessageService;
import ch.hsr.adit.domain.model.Message;

public class MessageController {

  /**
   * API Controller for /message requests.
   * 
   * @param messageService service class
   */
  public MessageController(MessageService messageService) {

    // create
    post(RestApi.Message.MESSAGE, (request, response) -> {
      Message message = messageService.transformToMessage(request);
      return messageService.createMessage(message);
    }, jsonTransformer());

    // read
    get(RestApi.Message.MESSAGE_BY_ID, (request, response) -> {
      long id = Long.parseLong(request.params(":id"));
      return messageService.get(id);
    }, jsonTransformer());

    get(RestApi.Message.MESSAGES_FILTERED, (request, response) -> {
      return messageService.getAllFiltered(request);
    }, jsonTransformer());
    
  }
}
