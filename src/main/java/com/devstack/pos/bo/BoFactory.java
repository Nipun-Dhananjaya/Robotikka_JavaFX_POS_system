package com.devstack.pos.bo;

import com.devstack.pos.bo.custom.impl.CustomerBoImpl;
import com.devstack.pos.bo.custom.impl.ProductBoImpl;
import com.devstack.pos.bo.custom.impl.UserBoImpl;
import com.devstack.pos.dao.DaoFactory;
import com.devstack.pos.dao.custom.Impl.CustomerDaoImpl;
import com.devstack.pos.dao.custom.Impl.ProductDaoImpl;
import com.devstack.pos.dao.custom.Impl.UserDaoImpl;
import com.devstack.pos.enums.BoTypes;
import com.devstack.pos.enums.DaoTypes;

public class BoFactory {
    private static BoFactory boFactory;
    private BoFactory(){}
    public static BoFactory getInstance(){
        return (boFactory==null)?boFactory=new BoFactory():boFactory;
    }
    public <T>T getBo(BoTypes boTypes){
        switch (boTypes){
            case USER:
                return (T) new UserBoImpl();
            case CUSTOMER:
                return (T) new CustomerBoImpl();
            case PRODUCT:
                return (T) new ProductBoImpl();
            default:
                return null;
        }
    }
}
