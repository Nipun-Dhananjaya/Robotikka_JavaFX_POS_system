package com.devstack.pos.dao;

import com.devstack.pos.dao.custom.Impl.CustomerDaoImpl;
import com.devstack.pos.dao.custom.Impl.ProductDaoImpl;
import com.devstack.pos.dao.custom.Impl.ProductDetailDaoImpl;
import com.devstack.pos.dao.custom.Impl.UserDaoImpl;
import com.devstack.pos.enums.DaoTypes;

public class DaoFactory {
    private static DaoFactory daoFactory;
    private DaoFactory(){}
    public static DaoFactory getInstance(){
        return (daoFactory==null)?daoFactory=new DaoFactory():daoFactory;
    }
    public <T>T getDao(DaoTypes daoTypes){
        switch (daoTypes){
            case USER:
                return (T) new UserDaoImpl();
            case CUSTOMER:
                return (T) new CustomerDaoImpl();
            case PRODUCT:
                return (T) new ProductDaoImpl();
            case PRODUCT_DETAIL:
                return (T) new ProductDetailDaoImpl();
            default:
                return null;
        }
    }
}
