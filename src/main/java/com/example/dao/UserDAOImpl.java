package com.example.dao;

import com.example.config.HibernateUtil;
import com.example.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public void create(User user) {

        Transaction tx = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()){

            tx = session.beginTransaction();

            session.persist(user);

            tx.commit();

        }catch(Exception e){

            if(tx != null) tx.rollback();

            e.printStackTrace();
        }

    }

    @Override
    public User getById(Long id) {

        try(Session session = HibernateUtil.getSessionFactory().openSession()){

            return session.get(User.class, id);

        }

    }

    @Override
    public List<User> getAll() {

        try(Session session = HibernateUtil.getSessionFactory().openSession()){

            return session.createQuery("from User", User.class).list();

        }

    }

    @Override
    public void update(User user) {

        Transaction tx = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()){

            tx = session.beginTransaction();

            session.merge(user);

            tx.commit();

        }catch(Exception e){

            if(tx != null) tx.rollback();

            e.printStackTrace();
        }

    }

    @Override
    public void delete(Long id) {

        Transaction tx = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()){

            tx = session.beginTransaction();

            User user = session.get(User.class, id);

            if(user != null)
                session.remove(user);

            tx.commit();

        }catch(Exception e){

            if(tx != null) tx.rollback();

            e.printStackTrace();
        }

    }

}
