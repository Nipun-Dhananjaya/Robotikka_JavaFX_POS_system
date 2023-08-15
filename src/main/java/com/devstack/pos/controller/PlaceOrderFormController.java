package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.CustomerBo;
import com.devstack.pos.bo.custom.ProductDetailBo;
import com.devstack.pos.dto.CustomerDto;
import com.devstack.pos.dto.ProductDetailJoinDto;
import com.devstack.pos.enums.BoTypes;
import com.devstack.pos.tm.CartTm;
import com.google.zxing.WriterException;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class PlaceOrderFormController implements Initializable {
    public AnchorPane context;
    public RadioButton rBtnManual;
    public ToggleGroup mode;
    public JFXButton btnBackToHome;
    public JFXButton btnNewCustomer;
    public JFXButton btnNewProduct;
    public JFXButton btnCompleteOrder;
    public TextField txtEmail;
    public TextField txtName;
    public TextField txtContact;
    public TextField txtSalary;
    public Hyperlink urlNewLoyalty;
    public Label lblLoyaltyType;
    public TextField txtBarcode;
    public TextField txtDescription;
    public TextField txtSellingPrice;
    public TextField txtDiscount;
    public TextField txtShowPrice;
    public TextField txtQtyOnHand;
    public TextField txtBuyingPrice;
    public TextField txtQty;
    public Label lblDiscountAvailable;
    public TableView tblOrderDetails;
    public TableColumn colBarCode;
    public TableColumn colDsc;
    public TableColumn colSellingPrice;
    public TableColumn colDiscount;
    public TableColumn colShowPrice;
    public TableColumn colQty;
    public TableColumn colTotalCost;
    public TableColumn colOperation;
    public Label lblTotal;
    CustomerBo bo= BoFactory.getInstance().getBo(BoTypes.CUSTOMER);
    private ProductDetailBo productDetailBo= BoFactory.getInstance().getBo(BoTypes.PRODUCT_DETAIL);
    ObservableList<CartTm> observableList= FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colBarCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDsc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colSellingPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colShowPrice.setCellValueFactory(new PropertyValueFactory<>("showPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotalCost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        colOperation.setCellValueFactory(new PropertyValueFactory<>("operation"));


    }
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

    public void urlNewLoyaltyOnAction(ActionEvent actionEvent) {
    }

    public void searchCustomer(ActionEvent actionEvent) {
        CustomerDto customer = null;
        try {
            customer = bo.findCustomer(txtEmail.getText());
            if (customer!=null){
                txtName.setText(customer.getName());
                txtSalary.setText(String.valueOf(customer.getSalary()));
                txtContact.setText(customer.getContact());

                fetchLoyaltyCardData(txtEmail.getText());
            }else{
                new Alert(Alert.AlertType.WARNING, "Can't find the Customer!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.WARNING, "Can't find the Customer!").show();
            throw new RuntimeException(e);
        }
    }

    private void fetchLoyaltyCardData(String text) {
        urlNewLoyalty.setText("+ New Loyalty");
        urlNewLoyalty.setVisible(true);
    }

    public void loadProductOnAction(ActionEvent actionEvent) {
        ProductDetailJoinDto p = null;
        try {
            p = productDetailBo.findProductJoinDetail(txtBarcode.getText());
            if (p!=null){
                txtDescription.setText(p.getDescription());
                txtDiscount.setText(String.valueOf(0));
                txtSellingPrice.setText(String.valueOf(p.getDto().getSellingPrice()));
                txtShowPrice.setText(String.valueOf(p.getDto().getShowPrice()));
                txtQtyOnHand.setText(String.valueOf(p.getDto().getQtyOnHand()));
                txtBuyingPrice.setText(String.valueOf(p.getDto().getBuyingPrice()));
                txtQty.requestFocus();
            }else {
                new Alert(Alert.AlertType.WARNING, "Can't find the Product!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.WARNING, "Can't find the Product!").show();
            throw new RuntimeException(e);
        }
    }

    public void addToCartOnAction(ActionEvent actionEvent) {
        int qty=Integer.parseInt(txtQty.getText());
        double sellingPrice= Double.parseDouble(txtSellingPrice.getText());
        double totalCost= qty*sellingPrice;
        CartTm selectedCartTm = isExist(txtBarcode.getText());
        if (selectedCartTm!=null){
            selectedCartTm.setQty(qty+selectedCartTm.getQty());
            selectedCartTm.setTotalCost(totalCost+selectedCartTm.getTotalCost());

            tblOrderDetails.refresh();
        }else{
            Button btn=new Button("Remove");
            setProuctTblRemoveBtnOnAction(btn);
            CartTm tm=new CartTm(
                    txtBarcode.getText(),
                    txtDescription.getText(),
                    Double.parseDouble(txtDiscount.getText()),
                    sellingPrice,
                    Double.parseDouble(txtShowPrice.getText()),
                    qty,
                    totalCost,
                    btn
            );
            observableList.add(tm);

            resetTexts();
        }

        tblOrderDetails.setItems(observableList);
        setTotal();
    }

    private void resetTexts() {
        txtBarcode.clear();
        txtDescription.clear();
        txtDiscount.clear();
        txtSellingPrice.clear();
        txtShowPrice.clear();
        txtQtyOnHand.clear();
        txtBuyingPrice.clear();
        txtQty.clear();
        lblTotal.setText("0.00/=");
        txtBarcode.requestFocus();
    }

    private void setProuctTblRemoveBtnOnAction(Button btn) {
        btn.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (result.orElse(no) == yes) {

                int index = tblOrderDetails.getSelectionModel().getSelectedIndex();
                observableList.remove(index);

                tblOrderDetails.refresh();
                setTotal();
            }

        });
    }

    private CartTm isExist(String code){
        for (CartTm tm:observableList) {
            if (tm.getCode().equals(code)){
                return tm;
            }
        }
        return null;
    }

    private void setTotal(){
        double total=0;

        for (CartTm tm: observableList) {
            total+=tm.getTotalCost();
        }
        lblTotal.setText(total+ " /= ");
    }
}
