package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        List<Detail> entities = DbContext.getEntities(Detail.class);
        Detail detail = DbContext.getEntityById(Detail.class, 3);
        List<DesignersGroup> id = DbContext
                .getEntities(DesignersGroup.class, Restrictions.eq("id", detail.getDesigners_groups_id()));

        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(id.get(0).getDesigners());
        int t = 0;
    }

    public static void createDb() {
        Session session = DbContext.openSession();
        Transaction transaction = session.beginTransaction();
        for (int i = 0; i < 4; ++i) {
            session.save(new DesignersGroup());
        }
        for (int i = 0; i < 6; ++i) {
            session.save(DbContext.getRandomFactory());
        }
        for (int i = 0; i < 15; ++i) {
            session.save(DbContext.getRandomWarehouse());
        }
        for (int i = 0; i < 30; ++i) {
            session.save(DbContext.getRandomDesigner(4));
        }
        for (int i = 0; i < 150; ++i) {
            session.save(DbContext.getRandomDetail(15, 6, 4));
        }
        transaction.commit();
        session.close();
    }
}
