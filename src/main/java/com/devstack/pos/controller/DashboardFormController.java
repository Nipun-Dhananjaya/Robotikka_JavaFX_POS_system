package com.devstack.pos.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardFormController {
    public JFXButton btnCustMange;
    public JFXButton btnProductMange;
    public JFXButton btnPlaceOrd;
    public JFXButton btnOrdDetails;
    public JFXButton btnIncomeReports;
    public AnchorPane context;

    public void btnCustMangeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("CustomerManageForm");
    }

    public void btnProductMangeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("ProductMainPage");
    }

    public void btnPlaceOrdOnAction(ActionEvent actionEvent) throws IOException {
        setUi("PlaceOrderForm");
    }

    public void btnOrdDetailsOnAction(ActionEvent actionEvent) {
    }

    public void btnIncomeReportsOnAction(ActionEvent actionEvent) {
    }
    private void setUi(String url) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("/view/"+url+".fxml")))
        );
        stage.centerOnScreen();
    }
}
