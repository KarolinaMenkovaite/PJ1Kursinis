package com.kursinis.prif4kursinis.fxControllers;

import com.kursinis.prif4kursinis.StartGui;
import com.kursinis.prif4kursinis.hibernateControllers.CustomHib;
import com.kursinis.prif4kursinis.hibernateControllers.UserHib;
import com.kursinis.prif4kursinis.model.Customer;
import com.kursinis.prif4kursinis.model.Manager;
import com.kursinis.prif4kursinis.model.User;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {

    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField repeatPasswordField;
    @FXML
    public TextField nameField;
    @FXML
    public TextField surnameField;
    @FXML
    public RadioButton customerRadioButton;
    @FXML
    public ToggleGroup userType;
    @FXML
    public RadioButton managerRadioButton;
    @FXML
    public TextField addressField;
    @FXML
    public TextField cardNoField;
    @FXML
    public DatePicker birthDateField;
    @FXML
    public TextField employeeIdField;
    @FXML
    public TextField medCertificateField;
    @FXML
    public DatePicker employmentDateField;
    @FXML
    public CheckBox isAdminCheck;

    private EntityManagerFactory entityManagerFactory;
    private UserHib userHib;
    private CustomHib customHib;
    private User currentUser;
    private String returnTo;

    public void setData(EntityManagerFactory entityManagerFactory, boolean showManagerFields) {
        this.entityManagerFactory = entityManagerFactory;
        disableFields(showManagerFields);
    }

    public void setData(EntityManagerFactory entityManagerFactory, CustomHib customHib, User currentUser) {
        this.entityManagerFactory = entityManagerFactory;
        this.customHib = customHib;
        this.currentUser = currentUser;
        toggleFields(customerRadioButton.isSelected());
    }

    public void setReturnTo(String returnTo) {
        this.returnTo = returnTo;
    }

    private void disableFields(boolean showManagerFields) {
        if (!showManagerFields) {
            employeeIdField.setVisible(false);
            medCertificateField.setVisible(false);
            employmentDateField.setVisible(false);
            isAdminCheck.setVisible(false);
            managerRadioButton.setVisible(false);
        }
    }

    private void toggleFields(boolean isCustomer) {
        if (isCustomer) {
            addressField.setDisable(false);
            cardNoField.setDisable(false);
            employeeIdField.setDisable(true);
            medCertificateField.setDisable(true);
            employmentDateField.setDisable(true);
            isAdminCheck.setDisable(true);
        } else {
            addressField.setDisable(true);
            cardNoField.setDisable(true);
            employeeIdField.setDisable(false);
            medCertificateField.setDisable(false);
            employmentDateField.setDisable(false);
            isAdminCheck.setDisable(false);
        }
    }


    public void createUser() {
        userHib = new UserHib(entityManagerFactory);
        if (customerRadioButton.isSelected()) {
            User user = new Customer(loginField.getText(), passwordField.getText(), birthDateField.getValue(), nameField.getText(), surnameField.getText(), addressField.getText(), cardNoField.getText());
            userHib.createUser(user);
        } else if (managerRadioButton.isSelected()) {
            User user = new Manager(loginField.getText(), passwordField.getText(), birthDateField.getValue(), nameField.getText(), surnameField.getText(), employeeIdField.getText(), medCertificateField.getText(), employmentDateField.getValue(), isAdminCheck.isSelected());
            userHib.createUser(user);
        }

        clearFields();
    }

    private void clearFields() {
        loginField.clear();
        passwordField.clear();
        repeatPasswordField.clear();
        nameField.clear();
        surnameField.clear();
        addressField.clear();
        cardNoField.clear();
        birthDateField.setValue(null);
        employeeIdField.clear();
        medCertificateField.clear();
        employmentDateField.setValue(null);
        isAdminCheck.setSelected(false);
        userType.selectToggle(null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userType.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                boolean isCustomerSelected = newValue == customerRadioButton;
                toggleFields(isCustomerSelected);
            }
        });
    }

    public void returnToPreviousScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            if ("loginController".equals(returnTo)) {
                loader.setLocation(StartGui.class.getResource("login.fxml"));
                Parent root = loader.load();
                stage.setScene(new Scene(root));
            } else if ("mainShopController".equals(returnTo)) {
                loader.setLocation(StartGui.class.getResource("main-shop.fxml"));
                Parent root = loader.load();
                MainShopController mainShopController = loader.getController();
                mainShopController.setData(entityManagerFactory, currentUser);
                stage.setScene(new Scene(root));
            } else {
                loader.setLocation(StartGui.class.getResource("login.fxml"));
                Parent root = loader.load();
                stage.setScene(new Scene(root));
            }

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
