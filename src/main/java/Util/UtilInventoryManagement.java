package Util;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import Model.Item;


import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UtilInventoryManagement {

	private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            // loads configuration and mappings
            Configuration configuration = new Configuration().configure();
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();

            // builds a session factory from the service registry
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }

        return sessionFactory;
    }

    public static List<Item> listItems(String sort, String order, String minQuantity, String maxQuantity, String search) {
        List<Item> resultList = new ArrayList<>();
        Session session = getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            
            // Using SQL query instead of HQL
            StringBuilder sql = new StringBuilder("SELECT * FROM items");

            Map<String, Object> parameters = new HashMap<>();

            List<String> conditions = new ArrayList<>();
            if (search != null && !search.trim().isEmpty()) {
                conditions.add(" (name like :search or description like :search) ");
                parameters.put("search", "%" + search + "%");
            }
            if (minQuantity != null && !minQuantity.trim().isEmpty()) {
                conditions.add(" quantity >= :minQuantity ");
                parameters.put("minQuantity", Integer.valueOf(minQuantity));
            }
            if (maxQuantity != null && !maxQuantity.trim().isEmpty()) {
                conditions.add(" quantity <= :maxQuantity ");
                parameters.put("maxQuantity", Integer.valueOf(maxQuantity));
            }
            if (!conditions.isEmpty()) {
                sql.append(" WHERE ").append(String.join(" AND ", conditions));
            }
            if (sort != null && !sort.trim().isEmpty() && order != null && !order.trim().isEmpty()) {
                sql.append(" ORDER BY ").append(sort).append(" ").append(order);
            }

            // Native SQL query
            SQLQuery query = session.createSQLQuery(sql.toString());
            query.addEntity(Item.class);
            
            // Set parameters
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }

            resultList = query.list();

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return resultList;
    }





    public static class InventoryException extends RuntimeException {
        public InventoryException(String message) {
            super(message);
        }
    }

    public static void createItem(String name, String description, int quantity, double price) throws InventoryException {
        Session session = getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.save(new Item(name, description, quantity, price));
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            throw new InventoryException("Unable to add item to the inventory. Please try again.");
        } finally {
            session.close();
        }
    }

	   
}
