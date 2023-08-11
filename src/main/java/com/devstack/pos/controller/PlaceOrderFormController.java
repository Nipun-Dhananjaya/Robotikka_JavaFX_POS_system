package com.devstack.pos.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class PlaceOrderFormController {
    public AnchorPane context;
    public RadioButton rBtnManual;
    public ToggleGroup mode;
    public JFXButton btnBackToHome;
    public JFXButton btnNewCustomer;
    public JFXButton btnNewProduct;
    public JFXButton btnCompleteOrder;

    public void rBtnManualOnAction(ActionEvent actionEvent) {
    }

    public void btnBackToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm",false);
    }

    public void btnNewCustomerOnAction(ActionEvent actionEvent) throws IOException {
        setUi("CustomerManageForm",true);
    }

    public void btnNewProductOnAction(ActionEvent actionEvent) throws IOException {
        setUi("ProductMainPage",true);
    }

    public void btnCompleteOrderOnAction(ActionEvent actionEvent) {
    }
    private void setUi(String url,boolean state) throws IOException {
        Stage stage=null;
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/view/" + url + ".fxml")));
        if (state){
            stage=new Stage();
            stage.setScene(scene);
            stage.show();
        }else {
            stage = (Stage) context.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
        }
    }
}
