package ch.hsr.adit.domain.persistence;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import ch.hsr.adit.domain.model.DbEntity;
import ch.hsr.adit.exception.DatabaseError;
import ch.hsr.adit.exception.SystemException;

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
  protected Query<T> createQuery(String hql) {
    return sessionFactory.getCurrentSession().createQuery(hql);
  }

  public T persist(T object) {
    try {
      sessionFactory.getCurrentSession().beginTransaction();
      Long id = (Long) sessionFactory.getCurrentSession().save(object);
      object.setId(id);
      sessionFactory.getCurrentSession().getTransaction().commit();
      return object;
    } catch (Exception e) {
      sessionFactory.getCurrentSession().getTransaction().rollback();
      throw new SystemException(DatabaseError.USER_CONSTRAINT_VIOLATED, e);
    }
  }

  public T get(Serializable id) {
    try {
      sessionFactory.getCurrentSession().beginTransaction();
      T object = sessionFactory.getCurrentSession().get(type, id);
      sessionFactory.getCurrentSession().getTransaction().commit();
      return object;
    } catch (Exception e) {
      sessionFactory.getCurrentSession().getTransaction().rollback();
      throw new SystemException(DatabaseError.USER_CONSTRAINT_VIOLATED, e);
    }
  }

  public T getByName(String name) {
    try {
      sessionFactory.getCurrentSession().beginTransaction();
      Query<T> query = createQuery("from " + type.getSimpleName() + "where r.name = :name");
      query.setParameter("name", name);
      T object = query.getSingleResult();
      sessionFactory.getCurrentSession().getTransaction().commit();
      return object;
    } catch (Exception e) {
      sessionFactory.getCurrentSession().getTransaction().rollback();
      throw new SystemException(DatabaseError.USER_CONSTRAINT_VIOLATED, e);
    }
  }

  public List<T> getAll() {
    try {
      sessionFactory.getCurrentSession().beginTransaction();
      Query<T> query = createQuery("from " + type.getSimpleName());
      List<T> objects = query.list();
      sessionFactory.getCurrentSession().getTransaction().commit();
      return objects;
    } catch (Exception e) {
      sessionFactory.getCurrentSession().getTransaction().rollback();
      throw new SystemException(DatabaseError.USER_CONSTRAINT_VIOLATED, e);
    }
  }

  public T update(T object) {
    try {
      sessionFactory.getCurrentSession().beginTransaction();
      sessionFactory.getCurrentSession().update(object);
      sessionFactory.getCurrentSession().getTransaction().commit();
      return object;
    } catch (Exception e) {
      sessionFactory.getCurrentSession().getTransaction().rollback();
      throw new SystemException(DatabaseError.USER_CONSTRAINT_VIOLATED, e);
    }
  }

  public void updateAll(Collection<T> objects) {
    objects.forEach(o -> update(o));
  }

  public void delete(T object) {
    try {
      sessionFactory.getCurrentSession().beginTransaction();
      sessionFactory.getCurrentSession().delete(object);
      sessionFactory.getCurrentSession().getTransaction().commit();
    } catch (Exception e) {
      sessionFactory.getCurrentSession().getTransaction().rollback();
      throw new SystemException(DatabaseError.USER_CONSTRAINT_VIOLATED, e);
    }
  }

  public void deleteAll(Collection<T> objects) {
    objects.forEach(o -> delete(o));
  }

}
