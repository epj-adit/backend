package ch.hsr.adit.application.controller;

import static ch.hsr.adit.util.JsonUtil.jsonTransformer;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import ch.hsr.adit.application.service.SubscriptionService;
import ch.hsr.adit.domain.model.Subscription;

public class SubscriptionController {

  /**
   * API Controller for /subscription requests.
   * 
   * @param SubscriptionService class
   */
  public SubscriptionController(SubscriptionService subscriptionService) {

    // create
    post(RestApi.Subscription.SUBSCRIPTION, (request, response) -> {
      Subscription subscription = subscriptionService.transformToSubscription(request);
      return subscriptionService.createSubscription(subscription);
    }, jsonTransformer());

    // read
    get(RestApi.Subscription.SUBSCRIPTION_BY_ID, (request, response) -> {
      long id = Long.parseLong(request.params(":id"));
      return subscriptionService.get(id);
    }, jsonTransformer());

    get(RestApi.Subscription.SUBSCRIPTIONS_FILTERED, (request, response) -> {
      return subscriptionService.getAllFiltered(request);
    }, jsonTransformer());

    // update
    put(RestApi.Subscription.SUBSCRIPTION_BY_ID, (request, response) -> {
      Subscription subscription = subscriptionService.transformToSubscription(request);
      long id = Long.parseLong(request.params(":id"));
      subscription.setId(id);
      return subscriptionService.updateSubscription(subscription);
    }, jsonTransformer());

    // delete
    delete(RestApi.Subscription.SUBSCRIPTION_BY_ID, (request, response) -> {
      long id = Long.parseLong(request.params(":id"));
      return subscriptionService.deleteSubscription(id);
    }, jsonTransformer());
  }
}
