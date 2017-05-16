package ch.hsr.adit.domain.persistence;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import ch.hsr.adit.domain.model.DbEntity;

public abstract class GenericDao<T extends DbEntity, P extends Serializable> {

  private static final Logger LOGGER = Logger.getLogger(GenericDao.class);

  private final Class<T> type;
  private final String entityName;
  protected final SessionFactory sessionFactory;

  @SuppressWarnings("unchecked")
  public GenericDao(SessionFactory sessionFactory) {
    ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
    type = (Class<T>) parameterizedType.getActualTypeArguments()[0];
    entityName = type.getSimpleName();

    this.sessionFactory = sessionFactory;
  }

  @SuppressWarnings("unchecked")
  protected Query<T> createQuery(String hql) {
    return sessionFactory.getCurrentSession().createQuery(hql);
  }

  public T persist(T object) {
    try {
      LOGGER.info("Try to persist" + entityName);
      sessionFactory.getCurrentSession().beginTransaction();
      Long id = (Long) sessionFactory.getCurrentSession().save(object);
      object.setId(id);
      sessionFactory.getCurrentSession().getTransaction().commit();
      LOGGER.info(entityName + " sucessfully persisted");
      return object;
    } catch (Exception e) {
      sessionFactory.getCurrentSession().getTransaction().rollback();
      LOGGER.error("Failed to persist " + entityName + ". Transaction rolled back.");
      throw e;
    }
  }
  
  public T get(Serializable id) {
    try {
      LOGGER.info("Try to fetch " + entityName + " with id " + id);
      sessionFactory.getCurrentSession().beginTransaction();
      T object = sessionFactory.getCurrentSession().get(type, id);
      sessionFactory.getCurrentSession().getTransaction().commit();
      if (object == null) {
        throw new ObjectNotFoundException(id, entityName + " not found");
      }
      return object;
    } catch (Exception e) {
      sessionFactory.getCurrentSession().getTransaction().rollback();
      LOGGER.error("Failed to fetch " + entityName + ". Transaction rolled back.");
      throw e;
    }
  }

  public T getByName(String name) {
    try {
      LOGGER.info("Try to fetch " + entityName + " with name " + name);
      sessionFactory.getCurrentSession().beginTransaction();
      Query<T> query = createQuery("SELECT e FROM " + entityName + " as e WHERE e.name = :name");
      query.setParameter("name", name);
      try {
        T object = query.getSingleResult();
        sessionFactory.getCurrentSession().getTransaction().commit();
        return object;
      } catch (NoResultException e) {
        LOGGER.info("No entity found with name " + name);
        LOGGER.info(e);
        sessionFactory.getCurrentSession().getTransaction().rollback();
        return null;
      }
    } catch (Exception e) {
      sessionFactory.getCurrentSession().getTransaction().rollback();
      LOGGER.error("Failed to fetch " + entityName + ". Transaction rolled back.");
      throw e;
    }
  }

  public List<T> getAll() {
    try {
      LOGGER.info("Try to fetch all " + entityName + " objects");
      sessionFactory.getCurrentSession().beginTransaction();
      Query<T> query = createQuery("FROM " + entityName);
      List<T> objects = query.list();
      sessionFactory.getCurrentSession().getTransaction().commit();
      return objects;
    } catch (Exception e) {
      LOGGER.error("Failed to fetch all " + entityName + " objects. Transaction rolled back.");
      sessionFactory.getCurrentSession().getTransaction().rollback();
      throw e;
    }
  }

  public T update(T object) {
    try {
      LOGGER.info("Try to update " + entityName);
      sessionFactory.getCurrentSession().beginTransaction();
      sessionFactory.getCurrentSession().update(object);
      sessionFactory.getCurrentSession().getTransaction().commit();
      LOGGER.info(entityName + " sucessfully updated");
      return object;
    } catch (Exception e) {
      LOGGER.error("Failed to update " + entityName + ". Transaction rolled back.");
      sessionFactory.getCurrentSession().getTransaction().rollback();
      throw e;
    }
  }

  public void updateAll(Collection<T> objects) {
    objects.forEach(o -> update(o));
  }

  public void delete(T object) {
    try {
      LOGGER.info("Try to delete " + entityName);
      sessionFactory.getCurrentSession().beginTransaction();
      sessionFactory.getCurrentSession().delete(object);
      sessionFactory.getCurrentSession().getTransaction().commit();
      LOGGER.info(entityName + " sucessfully deleted");
    } catch (Exception e) {
      LOGGER.error("Failed to delete " + entityName + ". Transaction rolled back.");
      sessionFactory.getCurrentSession().getTransaction().rollback();
      throw e;
    }
  }

  public void deleteAll(Collection<T> objects) {
    objects.forEach(o -> delete(o));
  }

}
