package server;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.util.List;


public class DbContext {
    private static Session session;

    public static boolean isOpened() {
        return session != null && session.isOpen();
    }

    public static Session openSession() {
        if (isOpened()) {
            return session;
        } else {
            while (true) {
                try {
                    Configuration configuration = new Configuration();
                    SessionFactory sessionFactory = configuration.configure().buildSessionFactory();
                    session = sessionFactory.openSession();
                    return session;
                } catch (HibernateException e) {
                    //ignore
                }
            }
        }
    }

    public static <T> T getEntityById(Class<T> entityClass, int id) {
        Session session = openSession();
        return getEntityById(session, entityClass, id);
    }

    public static <T> List<T> getEntities(Class<T> entityClass, Criterion... criterions) {
        Session session = openSession();
        return getEntities(session, entityClass, criterions);
    }

    private static <T> T getEntityById(Session session, Class<T> entityClass, int id) {
        List<T> entities = session.createCriteria(entityClass).add(Restrictions.eq("id", id)).list();
        if (entities.size() == 0){
            return null;
        }
        return entities.get(0);
    }

    private static <T> List<T> getEntities(Session session, Class<T> entityClass, Criterion... criterions) {
        Criteria criteria = session.createCriteria(entityClass);
        for (Criterion criterion : criterions) {
            criteria.add(criterion);
        }
        return criteria.list();
    }

    public static <T> boolean deleteEntity(Class<T> entityClass, int id) {
        Session session = openSession();
        try {
            T entity = getEntityById(session, entityClass, id);
            Transaction transaction = session.beginTransaction();
            session.delete(entity);
            transaction.commit();
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public static <T> boolean addOrUpdateEntity(Class<T> entityClass, int id, T entity) {
        Session session = openSession();
        try {

            T entityById = getEntityById(entityClass, id);
            Transaction transaction = session.beginTransaction();

            if (entityById == null) {
                session.save(entity);
            }else {
                session.merge(entity);
            }
            transaction.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
