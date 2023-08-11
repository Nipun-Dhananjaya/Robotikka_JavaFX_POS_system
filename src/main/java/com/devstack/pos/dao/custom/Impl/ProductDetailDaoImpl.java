package com.devstack.pos.dao.custom.Impl;

import com.devstack.pos.dao.CrudDao;
import com.devstack.pos.dao.CrudUtil;
import com.devstack.pos.dao.custom.ProductDetailDao;
import com.devstack.pos.entity.ProductDetail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailDaoImpl implements ProductDetailDao {

    @Override
    public boolean save(ProductDetail productDetail) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO product_detail VALUES(?,?,?,?,?,?,?,?)",
                productDetail.getCode(),productDetail.getBarcode(),productDetail.getQtyOnHand(),productDetail.getSellingPrice(),
                productDetail.isDiscountAvailability(),productDetail.getShowPrice(),productDetail.getProductCode(),productDetail.getBuyingPrice()
        );
    }

    @Override
    public boolean update(ProductDetail productDetail) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public ProductDetail find(String s) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public List<ProductDetail> findAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public List<ProductDetail> findAllProductDetails(int productCode) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM product_detail WHERE product_code=?", productCode);
        List<ProductDetail> list=new ArrayList<>();
        while (resultSet.next()){
            list.add(new ProductDetail(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4),
                    resultSet.getBoolean(5),
                    resultSet.getDouble(6),
                    resultSet.getInt(7),
                    resultSet.getDouble(8)
            ));
        }
        return list;
    }

    @Override
    public ProductDetail findProductDetail(String code) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=CrudUtil.execute("SELECT * FROM product_detail WHERE code=?",code);
        ProductDetail productDetail=new ProductDetail();
        while (resultSet.next()){
            productDetail.setCode(resultSet.getString(1));
            productDetail.setBarcode(resultSet.getString(2));
            productDetail.setQtyOnHand(resultSet.getInt(3));
            productDetail.setSellingPrice(resultSet.getDouble(4));
            productDetail.setDiscountAvailability(resultSet.getBoolean(5));
            productDetail.setShowPrice(resultSet.getDouble(6));
            productDetail.setProductCode(resultSet.getInt(7));
            productDetail.setBuyingPrice(resultSet.getDouble(8));
        }
        return productDetail;
    }
}
