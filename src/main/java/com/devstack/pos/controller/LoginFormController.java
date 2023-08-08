package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.UserBo;
import com.devstack.pos.bo.custom.impl.UserBoImpl;
import com.devstack.pos.dto.UserDto;
import com.devstack.pos.enums.BoTypes;
import com.devstack.pos.util.PasswordManager;
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
import java.sql.*;

public class LoginFormController {
    public JFXTextField txtEmail;
    public JFXButton btnSignIn;
    public JFXButton btnCreateAnAcc;
    public AnchorPane context;
    public JFXPasswordField txtPwd;
    UserBo bo= BoFactory.getInstance().getBo(BoTypes.USER);

    public void signInOnAction(ActionEvent actionEvent) throws IOException {
        try {
            UserDto ud= bo.findUser(txtEmail.getText());
            if(ud!=null){
                if(PasswordManager.checkPassword(txtPwd.getText(),ud.getPassword())){
                    System.out.println("Completed");
                    setUi("DashboardForm");
                }
                else{
                    new Alert(Alert.AlertType.WARNING,"Check your password and try again!").show();
                }
                clearFields();
            }else{
                new Alert(Alert.AlertType.WARNING,"User Email not found!").show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    public void btnCreateAnAccOnAction(ActionEvent actionEvent) throws IOException {
        setUi("SignUpForm");
    }
    private void setUi(String url) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("/view/"+url+".fxml")))
        );
        stage.centerOnScreen();
    }
    private void clearFields() {
        txtEmail.clear();
        txtPwd.clear();
    }
}
