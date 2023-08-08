package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.ProductBo;
import com.devstack.pos.dto.ProductDto;
import com.devstack.pos.enums.BoTypes;
import com.devstack.pos.tm.ProductTm;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ProductMainPageController {

    public AnchorPane context;
    public JFXButton btnBackToHome;
    public JFXButton btnNewproduct;
    public JFXTextField txtProductCode;
    public TextArea txtArDsc;
    public JFXButton btnSaveProduct;
    public TextField txtSearch;
    public TableView<ProductTm> tblProduct;
    public TableColumn colProductCode;
    public TableColumn colProductDsc;
    public TableColumn colShowMore;
    public TableColumn colDeleteBtn;
    public JFXButton btnNewBadge;
    public TextArea txtBadgeProductDsc;
    public TextField txtBadgeProductCode;
    public TableView tblProductDetails;
    public TableColumn colPDProductCode;
    public TableColumn colPDProductQty;
    public TableColumn colPDProductSellingPrice;
    public TableColumn colPDProductBuyingPrice;
    public TableColumn colPDProductDeliverAvailability;
    public TableColumn colPDProductShowPrice;
    public TableColumn colPDProductDelete;
    private String searchText="";
    ProductBo bo= BoFactory.getInstance().getBo(BoTypes.PRODUCT);

    public void initialize() throws SQLException, ClassNotFoundException {
        colProductCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colProductDsc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colShowMore.setCellValueFactory(new PropertyValueFactory<>("showMore"));
        colDeleteBtn.setCellValueFactory(new PropertyValueFactory<>("delete"));
        loadAllProducts("");
        loadProductCode();
        txtProductCode.setEditable(false);
        tblProduct.getSelectionModel().selectedItemProperty()
                .addListener((observableValue, oldValue, newValue) -> {
                    if(newValue!=null){
                        setData(newValue);
                    }
                });
    }

    private void setData(ProductTm newValue) {
        txtProductCode.setText(String.valueOf(newValue.getCode()));
        txtArDsc.setText(newValue.getDescription());
    }

    private void loadAllProducts(String searchText) throws SQLException, ClassNotFoundException {
        ObservableList<ProductTm> tms= FXCollections.observableArrayList();
        for (ProductDto dto:bo.findAllProducts()) {
            Button showMore=new Button("Show More");
            Button delete=new Button("Delete");
            ProductTm tm=new ProductTm(dto.getCode(), dto.getDescription(), showMore,delete);
            tms.add(tm);
        }
        tblProduct.setItems(tms);
    }
    private void loadProductCode() throws SQLException, ClassNotFoundException {
        txtProductCode.setText(String.valueOf(bo.getLastProductId()));
    }

    public void btnBackToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }

    public void btnNewproductOnAction(ActionEvent actionEvent) {
    }

    public void btnSaveProductOnAction(ActionEvent actionEvent) {
        try {
            if (btnSaveProduct.getText().equals("Save Product")){
                if(bo.saveProduct(new ProductDto(Integer.parseInt(txtProductCode.getText()),txtArDsc.getText()))){
                    new Alert(Alert.AlertType.INFORMATION,"Product Saved!").show();
                    clearFields();
                    loadAllProducts(searchText);
                    loadProductCode();
                    btnSaveProduct.setText("Save Product");
                }else{
                    new Alert(Alert.AlertType.WARNING,"Try Again!").show();
                }
            }else{
                if(bo.updateProduct(new ProductDto(Integer.parseInt(txtProductCode.getText()),txtArDsc.getText()))){
                    new Alert(Alert.AlertType.INFORMATION,"Product Updated!").show();
                    clearFields();
                    loadAllProducts(searchText);
                    loadProductCode();
                    btnSaveProduct.setText("Save Product");
                }else{
                    new Alert(Alert.AlertType.WARNING,"Try Again!").show();
                }
            }
        } catch (SQLException |ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        txtProductCode.clear();
        txtArDsc.clear();
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
    }

    public void btnNewBadgeOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage=new Stage();
        Parent load=FXMLLoader.load(getClass().getResource("/view/NewBatchForm.fxml"));
        stage.setScene(new Scene(load));
        stage.centerOnScreen();
        stage.show();
    }
    private void setUi(String url) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("/view/"+url+".fxml")))
        );
        stage.centerOnScreen();
    }
}
