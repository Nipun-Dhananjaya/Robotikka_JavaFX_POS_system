package com.devstack.pos.bo.custom.impl;

import com.devstack.pos.bo.custom.ProductDetailBo;
import com.devstack.pos.dao.DaoFactory;
import com.devstack.pos.dao.custom.ProductDetailDao;
import com.devstack.pos.dto.ProductDetailDto;
import com.devstack.pos.entity.ProductDetail;
import com.devstack.pos.enums.DaoTypes;

import java.sql.SQLException;

public class ProductDetailBoImpl implements ProductDetailBo {
    ProductDetailDao dao= DaoFactory.getInstance().getDao(DaoTypes.PRODUCT_DETAIL);
    @Override
    public boolean saveProductDetail(ProductDetailDto productDetailDto) throws SQLException, ClassNotFoundException {
        return dao.save(
                new ProductDetail(productDetailDto.getCode(),productDetailDto.getBarcode(),
                        productDetailDto.getQtyOnHand(),productDetailDto.getSellingPrice(),
                        productDetailDto.isDiscountAvailability(),productDetailDto.getShowPrice(),
                        productDetailDto.getProductCode(),productDetailDto.getBuyingPrice())
        );
    }
}
