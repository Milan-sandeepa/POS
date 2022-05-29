package dao;

import dao.custom.CustomerDAO;
import dao.custom.ItemDAO;
import dao.custom.impl.*;

import java.io.Serializable;

public class DAOFactory {
    private static DAOFactory daoFactory;

    private DAOFactory(){

    }

    public static DAOFactory getDaoFactory(){
         if (daoFactory == null){
             daoFactory = new DAOFactory();
             return daoFactory;
         }
         return daoFactory;
    }

    public enum DAOTypes{
        CUSTOMER, ITEM, ORDER, ORDERDETAILS, QUERYDAO
    }

    public SuperDAO getDAO(DAOTypes types){
        switch (types){
            case CUSTOMER:
                return new CustomerDAOImpl();
            case ITEM:
                return new ItemDAOImpl();
            case ORDER:
                return new OderDAOImpl();
            case ORDERDETAILS:
                return  new OderDetailsDAOImpl();
            case QUERYDAO:
                return new QueryDAOImpl();
            default:
                return null;
        }
    }
}
