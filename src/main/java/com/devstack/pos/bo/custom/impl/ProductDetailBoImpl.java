package com.devstack.pos.bo.custom.impl;

import com.devstack.pos.bo.custom.ProductDetailBo;
import com.devstack.pos.dao.DaoFactory;
import com.devstack.pos.dao.custom.ProductDetailDao;
import com.devstack.pos.dto.ProductDetailDto;
import com.devstack.pos.entity.ProductDetail;
import com.devstack.pos.enums.DaoTypes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<ProductDetailDto> findAllProductDetails(int productCode) throws SQLException, ClassNotFoundException {
        List<ProductDetailDto> dtos=new ArrayList<>();
        for (ProductDetail productDetail: dao.findAllProductDetails(productCode)) {
            dtos.add(
                    new ProductDetailDto(
                            productDetail.getCode(),productDetail.getBarcode(),
                            productDetail.getQtyOnHand(),productDetail.getSellingPrice(),
                            productDetail.isDiscountAvailability(),productDetail.getShowPrice(),
                            productDetail.getProductCode(),productDetail.getBuyingPrice()
                    )
            );
        }
        return dtos;
    }

    @Override
    public ProductDetailDto findProductDetail(String code) throws SQLException, ClassNotFoundException {
        ProductDetail productDetail=dao.findProductDetail(code);
        ProductDetailDto productDetailDto=new ProductDetailDto(
                productDetail.getCode(),productDetail.getBarcode(),
                productDetail.getQtyOnHand(),productDetail.getSellingPrice(),
                productDetail.isDiscountAvailability(),productDetail.getShowPrice(),
                productDetail.getProductCode(),productDetail.getBuyingPrice()
        );

        return productDetailDto;
    }
}
