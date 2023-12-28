package com.kursinis.prif4kursinis.fxControllers;

import com.kursinis.prif4kursinis.hibernateControllers.CustomHib;
import com.kursinis.prif4kursinis.model.ProductType;
import com.kursinis.prif4kursinis.model.Warehouse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class WarehouseTabController implements Initializable {

    @FXML
    private ListView<Warehouse> warehouseList;
    @FXML
    private TextField addressWarehouseField;
    @FXML
    private TextField titleWarehouseField;

    private CustomHib customHib;

    //public void setCustomHib(CustomHib customHib) {
        //this.customHib = customHib;
        //loadWarehouseList();
    //}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void loadWarehouseList(CustomHib customHib) {
        warehouseList.getItems().clear();
        warehouseList.getItems().addAll(customHib.getAllRecords(Warehouse.class));
    }

    public void addNewWarehouse() {
        customHib.create(new Warehouse(titleWarehouseField.getText(), addressWarehouseField.getText()));
        //loadWarehouseList();
    }

    public void updateWarehouse() {
        Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
        Warehouse warehouse = customHib.getEntityById(Warehouse.class, selectedWarehouse.getId());
        warehouse.setTitle(titleWarehouseField.getText());
        warehouse.setAddress(addressWarehouseField.getText());
        customHib.update(warehouse);
        //loadWarehouseList();
    }

    public void removeWarehouse() {
        Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
        customHib.delete(Warehouse.class, selectedWarehouse.getId());
        //loadWarehouseList();
    }

    public void loadWarehouseData() {
        Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
        titleWarehouseField.setText(selectedWarehouse.getTitle());
        addressWarehouseField.setText(selectedWarehouse.getAddress());
    }
}