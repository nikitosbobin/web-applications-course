package server;

import models.*;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class DbContext {
    private static Session session;

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
                session.update(entity);
            }
            transaction.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getRandomCity() {
        Random random = new Random();
        String[] cities = new String[5];
        cities[0] = "Екатеринбург";
        cities[1] = "Москва";
        cities[2] = "Санкт петербург";
        cities[3] = "Омск";
        cities[4] = "Нижний новгород";
        return cities[random.nextInt(5)];
    }

    public static String getRandomStreet() {
        Random random = new Random();
        String[] streets = new String[5];
        streets[0] = "Ленина";
        streets[1] = "Лесная";
        streets[2] = "Советская";
        streets[3] = "Октябрьская";
        streets[4] = "Лермонтова";
        return streets[random.nextInt(5)];
    }

    public static String getRandomSurname() {
        Random random = new Random();
        String[] surnames = new String[10];
        surnames[0] = "Попов";
        surnames[1] = "Фёдоров";
        surnames[2] = "Иванов";
        surnames[3] = "Егоров";
        surnames[5] = "Марков";
        surnames[4] = "Беляев";
        surnames[6] = "Осипов";
        surnames[7] = "Титов";
        surnames[8] = "Власов";
        surnames[9] = "Ушаков";
        return surnames[random.nextInt(9)];
    }

    public static String getRandomLastName() {
        Random random = new Random();
        String[] lastNames = new String[10];
        lastNames[0] = "Антипович";
        lastNames[1] = "Викентьевич";
        lastNames[2] = "Давидович";
        lastNames[3] = "Ильич";
        lastNames[5] = "Феликсович";
        lastNames[4] = "Юлианович";
        lastNames[6] = "Чеславович";
        lastNames[7] = "Трифонович";
        lastNames[8] = "Иларионович";
        lastNames[9] = "Ермилович";
        return lastNames[random.nextInt(9)];
    }

    public static String getRandomName() {
        Random random = new Random();
        String[] names = new String[10];
        names[0] = "Анатолий";
        names[1] = "Лаврентий";
        names[2] = "Марат";
        names[3] = "Потап";
        names[4] = "Серафим";
        names[5] = "Тимур";
        names[6] = "Юрий";
        names[7] = "Шерлок";
        names[8] = "Радик";
        names[9] = "Никита";
        return names[random.nextInt(9)];
    }

    public static Date getRandomDate() {
        Random random = new Random();
        int year = random.nextInt(6) + 2011;
        int month = random.nextInt(12);
        int day = random.nextInt(28) + 1;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day, random.nextInt(23), random.nextInt(59), 0);
        return cal.getTime();
    }

    public static Detail getRandomDetail(int warehousesCount, int factoriesCount, int designersGroupsCount) {
        Detail detail = new Detail();
        Random random = new Random();
        detail.setCost(random.nextInt(1200) + 300);
        detail.setCreation_date(getRandomDate());
        detail.setFactories_id(random.nextInt(factoriesCount ) + 1);
        detail.setWarehouses_id(random.nextInt(warehousesCount ) + 1);
        detail.setDesigners_groups_id(random.nextInt(designersGroupsCount) + 1);
        return detail;
    }

    public static Warehouse getRandomWarehouse() {
        Random random = new Random();
        Warehouse warehouse = new Warehouse();
        warehouse.setCity(DbContext.getRandomCity());
        warehouse.setAdress(DbContext.getRandomStreet() + " " + (random.nextInt(85) + 10));
        return warehouse;
    }

    public static Factory getRandomFactory() {
        Random random = new Random();
        Factory factory = new Factory();
        factory.setCity(DbContext.getRandomCity());
        factory.setAdress(DbContext.getRandomStreet() + " " + (random.nextInt(85) + 10));
        return factory;
    }

    public static Designer getRandomDesigner(int designersGroupsCount) {
        Designer designer = new Designer();
        Random random = new Random();
        designer.setName(getRandomName());
        designer.setSecond_name(getRandomLastName());
        designer.setSurname(getRandomSurname());
        designer.setDesigners_groups_id(random.nextInt(designersGroupsCount) + 1);
        return designer;
    }

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
}
