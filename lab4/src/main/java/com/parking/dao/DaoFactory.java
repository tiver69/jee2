package com.parking.dao;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DaoFactory {

    private EntityManagerFactory entityManagerFactory;

    private DaoFactory() {
        entityManagerFactory= Persistence.createEntityManagerFactory
                ("NewPersistenceUnit");
    }

    private static class SingletonDaoFactory {
        private final static DaoFactory daoFactory = new DaoFactory();
    }

    public static DaoFactory getInstance(){
        return SingletonDaoFactory.daoFactory;
    }

    public CarDao getCarDao(){
        return new CarDao(
                entityManagerFactory.createEntityManager());
    }

    public UserDao getUserDao(){
        return new UserDao(
                entityManagerFactory.createEntityManager());
    }

}
