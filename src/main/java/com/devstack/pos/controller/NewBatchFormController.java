package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.ProductDetailBo;
import com.devstack.pos.dto.CustomerDto;
import com.devstack.pos.dto.ProductDetailDto;
import com.devstack.pos.enums.BoTypes;
import com.devstack.pos.tm.ProductDetailTm;
import com.devstack.pos.util.QrDataGenerator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import javafx.stage.Stage;
import org.apache.commons.codec.binary.*;

public class NewBatchFormController {
    public AnchorPane context;
    public ImageView imgViewBarCode;
    public TextField txtProductQty;
    public TextField txtSellingPrice;
    public TextField txtShowPrice;
    public TextField txtProductCode;
    public TextArea txtProductDescription;
    public RadioButton rBtnYes;
    public JFXButton btnSaveBatch;
    public TextField txtBuyingPrice;
    String uniqueData;
    BufferedImage bufferedImage;
    Stage stage;
    private ProductDetailBo productDetailBo= BoFactory.getInstance().getBo(BoTypes.PRODUCT_DETAIL);
    public void initialize() throws WriterException {
    }

    private void setQRCode() throws WriterException {
        uniqueData= QrDataGenerator.generate(25);
        System.out.println(uniqueData);
        QRCodeWriter qrCodeWriter=new QRCodeWriter();
        bufferedImage= MatrixToImageWriter.toBufferedImage(qrCodeWriter.encode(uniqueData, BarcodeFormat.QR_CODE,235,220));
        Image image=SwingFXUtils.toFXImage(
                bufferedImage,null
        );
        imgViewBarCode.setImage(image);
    }

    public void setDetails(int code, String description, Stage stage, boolean state, ProductDetailTm tm) throws WriterException {
        txtProductCode.setText(String.valueOf(code));
        txtProductDescription.setText(description);
        this.stage=stage;
        if (state && tm!=null){
            try {
                ProductDetailDto productDetailDto=productDetailBo.findProductDetail(String.valueOf(tm.getCode()));
                if (productDetailDto!=null){
                    txtProductQty.setText(String.valueOf(productDetailDto.getQtyOnHand()));
                    txtBuyingPrice.setText(String.valueOf(productDetailDto.getBuyingPrice()));
                    txtSellingPrice.setText(String.valueOf(productDetailDto.getSellingPrice()));
                    txtShowPrice.setText(String.valueOf(productDetailDto.getShowPrice()));
                    rBtnYes.setText(String.valueOf(productDetailDto.isDiscountAvailability()));

                    byte[] data=Base64.decodeBase64(productDetailDto.getBarcode());
                    imgViewBarCode.setImage(new Image(new ByteArrayInputStream(data)));
                }else{
                    this.stage.close();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            setQRCode();
        }
        txtProductCode.setText(String.valueOf(code));
        txtProductDescription.setText(description);
    }

    public void saveBatchOnAction(ActionEvent actionEvent) throws IOException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        javax.imageio.ImageIO.write(bufferedImage,"png",baos);
        byte[] arr= baos.toByteArray();
        System.out.println(arr.length+": length");
        ProductDetailDto productDetailDto = new ProductDetailDto(uniqueData,Base64.encodeBase64String(arr),
                Integer.parseInt(txtProductQty.getText()),Double.parseDouble(txtSellingPrice.getText())
                ,rBtnYes.isSelected()?true:false,
                Double.parseDouble(txtShowPrice.getText()),
                Integer.parseInt(txtProductCode.getText()),Double.parseDouble(txtBuyingPrice.getText()));
        try {
            if(productDetailBo.saveProductDetail(productDetailDto)){
                new Alert(Alert.AlertType.INFORMATION,"Batch Saved!").show();
                Thread.sleep(3000);
                this.stage.close();
            }else{
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }
        } catch (SQLException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
