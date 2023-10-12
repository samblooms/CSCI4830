package Util;

import java.util.ArrayList;
import java.util.List;

import Model.Item;
import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class UtilInventoryManagement {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration().configure();
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }
        return sessionFactory;
    }

    public static List<Item> listItems(String sort, String order, String minQuantity, String maxQuantity, String search) {
        List<Item> resultList = new ArrayList<>();
        Session session = null;
        Transaction tx = null;

        try {
            session = getSessionFactory().openSession();
            tx = session.beginTransaction();

            // Building the SQL query
            StringBuilder sql = new StringBuilder("SELECT * FROM items");

            List<String> conditions = new ArrayList<>();
            if (search != null && !search.trim().isEmpty()) {
                conditions.add(" (name like :search or description like :search) ");
            }
            if (minQuantity != null && !minQuantity.trim().isEmpty()) {
                conditions.add(" quantity >= :minQuantity ");
            }
            if (maxQuantity != null && !maxQuantity.trim().isEmpty()) {
                conditions.add(" quantity <= :maxQuantity ");
            }
            if (!conditions.isEmpty()) {
                sql.append(" WHERE ").append(String.join(" AND ", conditions));
            }
            if (sort != null && !sort.trim().isEmpty() && order != null && !order.trim().isEmpty()) {
                sql.append(" ORDER BY ").append(sort).append(" ").append(order);
            }

            // Creating the SQL query
            SQLQuery query = session.createSQLQuery(sql.toString());
            query.addEntity(Item.class);

            // Setting parameters
            if (search != null && !search.trim().isEmpty()) {
                query.setParameter("search", "%" + search + "%");
            }
            if (minQuantity != null && !minQuantity.trim().isEmpty()) {
                query.setParameter("minQuantity", Integer.valueOf(minQuantity));
            }
            if (maxQuantity != null && !maxQuantity.trim().isEmpty()) {
                query.setParameter("maxQuantity", Integer.valueOf(maxQuantity));
            }

            // Getting the results
            resultList = query.list();

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return resultList;
    }



    public static List<Item> listItems() {
        return listItems(null, null, null, null, null);
    }

    public static void createItem(String name, String description, int quantity, double price) {
        Transaction tx = null;
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(new Item(name, description, quantity, price));
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public static void deleteItems(String[] itemIdsToDelete) {
        Transaction tx = null;
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            tx = session.beginTransaction();
            for (String itemIdStr : itemIdsToDelete) {
                try {
                    int itemId = Integer.parseInt(itemIdStr);
                    Item item = (Item) session.get(Item.class, itemId);
                    session.delete(item);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public static void updateItemAttribute(int itemId, String attribute, String value) {
        Transaction tx = null;
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            tx = session.beginTransaction();
            Item item = (Item) session.get(Item.class, itemId);

            switch (attribute) {
                case "name":
                    item.setName(value);
                    break;
                case "description":
                    item.setDescription(value);
                    break;
                case "quantity":
                    item.setQuantity(Integer.parseInt(value));
                    break;
                case "price":
                    item.setPrice(Double.parseDouble(value));
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected attribute: " + attribute);
            }

            session.update(item);
            tx.commit();
        } catch (HibernateException | IllegalArgumentException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
