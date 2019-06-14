package com.parking.dao;

import com.parking.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class UserDao implements AutoCloseable {

    private EntityManager entityManager;

    public UserDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public boolean create(User user) {
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        if (entityManager.find(User.class, user.getId()) != null) {
            entityManager.getTransaction().commit();
            return true;
        }
        entityManager.getTransaction().rollback();
        return false;
    }

    public Optional<User> getById(long id) {
        entityManager.getTransaction().begin();
        Optional<User> user = Optional.of(entityManager.find(User.class, id));
        entityManager.getTransaction().commit();
        return user;
    }

    public Optional<User> getByLogin(String login) {
        try {
            return Optional.of((User) entityManager.createQuery("SELECT a FROM User a where a.login = ?1")
                    .setParameter(1, login).getSingleResult());
        } catch (NoResultException e){
            return Optional.empty();
        }
    }

    public boolean remove(long id) {
        try {
            entityManager.getTransaction().begin();
            User userRemove = entityManager.find(User.class, id);
            entityManager.remove(userRemove);
            entityManager.getTransaction().commit();
            return true;
        } catch (IllegalArgumentException e) {
            entityManager.getTransaction().rollback();
            return false;
        }
    }

    public boolean update(User newUser) {
        entityManager.getTransaction().begin();
        Optional<User> updatedCar = Optional.of(entityManager.merge(newUser));
        if (updatedCar.equals(newUser)) {
            entityManager.getTransaction().commit();
            return true;
        }
        entityManager.getTransaction().rollback();
        return false;
    }

    public List<User> getAll() {
        return entityManager.createQuery("SELECT a FROM User a ").getResultList();
    }

    @Override
    public void close() {
        entityManager.close();
    }
}
