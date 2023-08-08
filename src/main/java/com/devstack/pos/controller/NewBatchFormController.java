package com.devstack.pos.controller;

import com.devstack.pos.util.QrDataGenerator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import javafx.embed.swing.SwingFXUtils;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.awt.image.BufferedImage;

public class NewBatchFormController {
    public AnchorPane context;
    public ImageView imgViewBarCode;
    String uniqueData;
    public void initialize() throws WriterException {
        setQRCode();
    }

    private void setQRCode() throws WriterException {
        uniqueData= QrDataGenerator.generate(25);
        System.out.println(uniqueData);
        QRCodeWriter qrCodeWriter=new QRCodeWriter();
        BufferedImage bufferedImage= MatrixToImageWriter.toBufferedImage(qrCodeWriter.encode(uniqueData, BarcodeFormat.QR_CODE,235,220));
        Image image=SwingFXUtils.toFXImage(
                bufferedImage,null
        );
        imgViewBarCode.setImage(image);
    }

    public void setProductCode(int code){

    }
}
