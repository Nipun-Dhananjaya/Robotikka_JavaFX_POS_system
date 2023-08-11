package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.ProductBo;
import com.devstack.pos.bo.custom.ProductDetailBo;
import com.devstack.pos.dto.ProductDetailDto;
import com.devstack.pos.dto.ProductDto;
import com.devstack.pos.enums.BoTypes;
import com.devstack.pos.tm.ProductDetailTm;
import com.devstack.pos.tm.ProductTm;
import com.google.zxing.WriterException;
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
import java.util.Optional;

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
    public TableView<ProductDetailTm> tblProductDetails;
    public TableColumn colPDProductCode;
    public TableColumn colPDProductQty;
    public TableColumn colPDProductSellingPrice;
    public TableColumn colPDProductBuyingPrice;
    public TableColumn colPDProductDeliverAvailability;
    public TableColumn colPDProductShowPrice;
    public TableColumn colPDProductDelete;
    private String searchText="";
    ObservableList<ProductDetailTm> obList=FXCollections.observableArrayList();
    ObservableList<ProductTm> tms= FXCollections.observableArrayList();
    ProductBo bo= BoFactory.getInstance().getBo(BoTypes.PRODUCT);
    ProductDetailBo detailBo= BoFactory.getInstance().getBo(BoTypes.PRODUCT_DETAIL);

    public void initialize() throws SQLException, ClassNotFoundException {
        colProductCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colProductDsc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colShowMore.setCellValueFactory(new PropertyValueFactory<>("showMore"));
        colDeleteBtn.setCellValueFactory(new PropertyValueFactory<>("delete"));

        colPDProductCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colPDProductQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colPDProductSellingPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        colPDProductBuyingPrice.setCellValueFactory(new PropertyValueFactory<>("buyingPrice"));
        colPDProductDeliverAvailability.setCellValueFactory(new PropertyValueFactory<>("discountAvailability"));
        colPDProductShowPrice.setCellValueFactory(new PropertyValueFactory<>("showPrice"));
        colPDProductDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));
        loadAllProducts("");
        loadProductCode();
        txtProductCode.setEditable(false);
        tblProduct.getSelectionModel().selectedItemProperty()
                .addListener((observableValue, oldValue, newValue) -> {
                    if(newValue!=null){
                        try {
                            setData(newValue);
                        } catch (SQLException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
        tblProductDetails.getSelectionModel().selectedItemProperty()
                .addListener((observableValue, oldValue, newValue) -> {
                    try {
                        if(newValue!=null){
                            loadExternalUi(true,newValue);
                        }
                    } catch (IOException | SQLException |ClassNotFoundException | WriterException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void setData(ProductTm newValue) throws SQLException, ClassNotFoundException {
        txtBadgeProductCode.setText(String.valueOf(newValue.getCode()));
        txtBadgeProductDsc.setText(newValue.getDescription());
        btnNewBadge.setDisable(false);
        loadBatch(newValue.getCode());
    }

    private void loadAllProducts(String searchText) throws SQLException, ClassNotFoundException {
        for (ProductDto dto:bo.findAllProducts()) {
            Button showMore=new Button("Show More");
            Button delete=new Button("Delete");
            ProductTm tm=new ProductTm(dto.getCode(), dto.getDescription(), showMore,delete);
            setProuctTblRemoveBtnOnAction(delete);
            tms.add(tm);
        }
        tblProduct.setItems(tms);
    }

    private void setProuctTblRemoveBtnOnAction(Button delete) {
        delete.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (result.orElse(no) == yes) {

                int index = tblProduct.getSelectionModel().getSelectedIndex();
                tms.remove(index);

                tblProduct.refresh();
            }

        });
    }

    private void setDetailsTblRemoveBtnOnAction(Button btn) {
        btn.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (result.orElse(no) == yes) {

                int index = tblProductDetails.getSelectionModel().getSelectedIndex();
                obList.remove(index);

                tblProductDetails.refresh();
            }

        });
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

    public void btnNewBadgeOnAction(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException, WriterException {
        loadExternalUi(false,null);
    }
    private void loadExternalUi(boolean state,ProductDetailTm tm) throws IOException, SQLException, ClassNotFoundException, WriterException {
        if ((!txtBadgeProductCode.getText().isEmpty())){
            Stage stage=new Stage();
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/view/NewBatchForm.fxml"));
            Parent load = fxmlLoader.load();
            NewBatchFormController controller = fxmlLoader.getController();
            controller.setDetails(Integer.parseInt(txtBadgeProductCode.getText()),
                    txtBadgeProductDsc.getText(),stage,state,tm);
            stage.setScene(new Scene(load));
            stage.centerOnScreen();
            stage.show();
        }else {
            new Alert(Alert.AlertType.WARNING,"Please Select a Valid One!");
        }
    }
    private void setUi(String url) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("/view/"+url+".fxml")))
        );
        stage.centerOnScreen();
    }

    private void loadBatch(int code) throws SQLException, ClassNotFoundException {
        for (ProductDetailDto p:detailBo.findAllProductDetails(code)) {
            Button btn=new Button("Delete");
            ProductDetailTm tm=new ProductDetailTm(
                    p.getCode(),p.getQtyOnHand(),p.getSellingPrice(),p.getBuyingPrice(), p.isDiscountAvailability(),
                    p.getShowPrice(),btn
            );
            setDetailsTblRemoveBtnOnAction(btn);
            obList.add(tm);
        }
        System.out.println(obList.size()+" :size");
        tblProductDetails.setItems(obList);
    }
}
