package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.UserBo;
import com.devstack.pos.bo.custom.impl.UserBoImpl;
import com.devstack.pos.dto.UserDto;
import com.devstack.pos.enums.BoTypes;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class SignUpFormController {
    public JFXTextField txtEmail;
    public JFXButton btnRegNow;
    public JFXButton btnAlreadyHaveAnAcc;
    public AnchorPane context;
    public JFXPasswordField txtPwd;
    UserBo bo= BoFactory.getInstance().getBo(BoTypes.USER);

    public void btnRegNowOnAction(ActionEvent actionEvent) {
        try {
            if(bo.saveUser(new UserDto(txtEmail.getText(),txtPwd.getText()))){
                new Alert(Alert.AlertType.INFORMATION,"User Saved!").show();
                clearFields();
            }else{
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    private void clearFields() {
        txtEmail.clear();
        txtPwd.clear();
    }

    public void btnAlreadyHaveAnAccOnAction(ActionEvent actionEvent) throws IOException {
        setUi("LoginForm");
    }
    private void setUi(String url) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("/view/"+url+".fxml")))
        );
        stage.centerOnScreen();
    }
}
