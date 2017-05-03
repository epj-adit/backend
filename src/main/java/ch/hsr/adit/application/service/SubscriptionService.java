package ch.hsr.adit.application.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;

import com.google.gson.JsonSyntaxException;

import ch.hsr.adit.domain.model.Subscription;
import ch.hsr.adit.domain.persistence.SubscriptionDao;
import ch.hsr.adit.util.JsonUtil;
import spark.Request;


public class SubscriptionService {

  private static final Logger LOGGER = Logger.getLogger(SubscriptionService.class);
  private final SubscriptionDao subscriptionDao;

  public SubscriptionService(SubscriptionDao subscriptionDao) {
    this.subscriptionDao = subscriptionDao;
  }

  public Subscription createSubscription(Subscription subscription) {
    return (Subscription) subscriptionDao.persist(subscription);
  }

  public Subscription updateSubscription(Subscription subscription) {
    try {
      // may throws ObjectNotFoundException
      subscriptionDao.get(subscription.getId());

      subscription.setLastUpdated(new Date());
      return subscriptionDao.update(subscription);
    } catch (ObjectNotFoundException e) {
      LOGGER.warn("Subscription with id " + subscription.getId() + " not found. Nothing updated");
      throw e;
    } 
  }

  public boolean deleteSubscription(Subscription subscriptionToDelete) {
    subscriptionDao.delete(subscriptionToDelete);
    return true;
  }

  public boolean deleteSubscription(long id) {
    Subscription subscription = get(id);
    return deleteSubscription(subscription);
  }

  public Subscription get(Long id) {
    Subscription subscription = subscriptionDao.get(id);
    return subscription;
  }

  public List<Subscription> getAll() {
    return subscriptionDao.getAll();
  }

  public List<Subscription> getAllFiltered(Request request) {
    if (request.queryParams("categoryId") != null) {
      Long categoryId = Long.parseLong(request.queryParams("categoryId"));
      return subscriptionDao.getFiltered(categoryId);
    } else {
      return subscriptionDao.getAll();
    }
  }

  public Subscription transformToSubscription(Request request) {
    try {
      Subscription subscription = JsonUtil.fromJson(request.body(), Subscription.class);
      LOGGER.info("Received JSON data: " + subscription.toString());
      return subscription;
    } catch (JsonSyntaxException e) {
      LOGGER.error("Cannot parse JSON: " + request.body());
      throw e;
    }
  }

}
