package com.parking.dao;

import com.parking.entity.Car;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class CarDao implements AutoCloseable {

    private EntityManager entityManager;

    public CarDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public boolean create(Car car) {
        entityManager.getTransaction().begin();
        entityManager.persist(car);
        if (entityManager.find(Car.class, car.getId()) != null) {
            entityManager.getTransaction().commit();
            return true;
        }
        entityManager.getTransaction().commit();
        return false;
    }

    public Optional<Car> getById(long id) {
        entityManager.getTransaction().begin();
        Optional<Car> car = Optional.of(entityManager.find(Car.class, id));
        entityManager.getTransaction().commit();
        return car;
    }

    public boolean remove(long id) {
        try {
            entityManager.getTransaction().begin();
            Car carRemove = entityManager.find(Car.class, id);
            entityManager.remove(carRemove);
            entityManager.getTransaction().commit();
            return true;
        } catch (IllegalArgumentException e) {
            entityManager.getTransaction().rollback();
            return false;
        }
    }

    public boolean update(Car newCar) {
        entityManager.getTransaction().begin();
        Optional<Car> updatedCar = Optional.of(entityManager.merge(newCar));

        if (updatedCar.get().equals(
                entityManager.find(Car.class, newCar.getId()))){
            entityManager.getTransaction().commit();
            return true;
        }
        entityManager.getTransaction().rollback();
        return false;
    }

    public List<Car> getAll() {
        return entityManager.createQuery("SELECT a FROM Car a ").getResultList();
    }

    @Override
    public void close() {
        entityManager.close();
    }
}
