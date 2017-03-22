package ch.hsr.adit.persistence;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import ch.hsr.adit.model.DbEntity;

public abstract class GenericDao<T extends DbEntity, PK extends Serializable> {

  private Class<T> type;
  protected final SessionFactory sessionFactory;

  @SuppressWarnings("unchecked")
  public GenericDao(SessionFactory sessionFactory) {
    ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
    type = (Class<T>) parameterizedType.getActualTypeArguments()[0];

    this.sessionFactory = sessionFactory;
  }

  @SuppressWarnings("unchecked")
  protected Query<T> createQuery(String hql) throws HibernateException {
    return sessionFactory.getCurrentSession().createQuery(hql);
  }

  protected void startTransaction() throws HibernateException {
    sessionFactory.getCurrentSession().beginTransaction();
  }

  protected void endTransaction() throws HibernateException {
    sessionFactory.getCurrentSession().getTransaction().commit();
  }

  public T persist(T object) throws HibernateException {
    startTransaction();
    Long id = (Long) sessionFactory.getCurrentSession().save(object);
    object.setId(id);
    endTransaction();
    return object;
  }

  public List<T> persistAll(Collection<T> objects) throws HibernateException {
    List<T> entities = new ArrayList<T>();
    objects.forEach(o -> {
      T object = persist(o);
      entities.add(object);
    });
    return entities;
  }

  public T get(Serializable id) throws HibernateException {
    startTransaction();
    T object = sessionFactory.getCurrentSession().get(type, id);
    endTransaction();
    return object;
  }

  public List<T> getAll() throws HibernateException {
    sessionFactory.getCurrentSession().beginTransaction();
    Query<T> query = createQuery("from " + type.getName());
    List<T> users = query.list();
    endTransaction();
    return users;
  }

  @SuppressWarnings("unchecked")
  public T update(T object) throws HibernateException {
    startTransaction();
    sessionFactory.getCurrentSession().update(object);
    endTransaction();
    return object;
  }

  public void updateAll(Collection<T> objects) throws HibernateException {
    objects.forEach(o -> update(o));
  }

  public void delete(T object) throws HibernateException {
    startTransaction();
    sessionFactory.getCurrentSession().delete(object);
    endTransaction();
  }

  public void delete(PK id) throws HibernateException {
    T object = get(id);
    if (object == null) {
      throw new HibernateException("Object to delete not found. Object id was " + id);
    }
    delete(object);
  }

  public void deleteAll(Collection<T> objects) throws HibernateException {
    objects.forEach(o -> delete(o));
  }

}
