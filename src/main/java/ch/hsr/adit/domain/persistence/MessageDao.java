package ch.hsr.adit.domain.persistence;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import ch.hsr.adit.domain.model.Message;


public class MessageDao extends GenericDao<Message, Long> {

  private static final Logger LOGGER = Logger.getLogger(MessageDao.class);

  public MessageDao(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public List<Message> getFiltered(List<Long> userIds) {
    try {
      LOGGER.info("Try to fetch filtered messages");

      StringBuilder queryString = new StringBuilder("SELECT DISTINCT m FROM Message as m "
          + "WHERE m.userBySenderUserId.id IN (:userIds) "
          + "OR m.userByRecipientUserId.id IN (:userIds)");

      sessionFactory.getCurrentSession().beginTransaction();

      Query<Message> query = createQuery(queryString.toString());
      
      if (userIds != null && !userIds.isEmpty()) {
        query.setParameter("userIds", userIds);
      }

      List<Message> result = query.getResultList();

      sessionFactory.getCurrentSession().getTransaction().commit();
      return result;
    } catch (Exception e) {
      sessionFactory.getCurrentSession().getTransaction().rollback();
      LOGGER.error("Failed to fetch filtered messages. Transaction rolled back.");
      throw e;
    }
  }
  
  public List<Message> getByConversation(Long userId) {
    try {
      LOGGER.info("Try to fetch messages by conversations ");

      StringBuilder queryString = new StringBuilder("SELECT DISTINCT m FROM Message as m "
          + "WHERE m.userBySenderUserId.id = :recipientUserId "
          + "OR m.userByRecipientUserId.id = :senderUserId");

      sessionFactory.getCurrentSession().beginTransaction();

      Query<Message> query = createQuery(queryString.toString());

      if (userId != null) {
        query.setParameter("recipientUserId", userId);
        query.setParameter("senderUserId", userId);
      }

      List<Message> result = query.getResultList();

      sessionFactory.getCurrentSession().getTransaction().commit();
      return result;
    } catch (Exception e) {
      sessionFactory.getCurrentSession().getTransaction().rollback();
      LOGGER.error("Failed to fetch messages by conversations. Transaction rolled back.");
      throw e;
    }
  }


}
