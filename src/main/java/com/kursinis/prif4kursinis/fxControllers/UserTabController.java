package com.kursinis.prif4kursinis.fxControllers;

import com.kursinis.prif4kursinis.StartGui;
import com.kursinis.prif4kursinis.fxControllers.tableParameters.ManagerTableParameters;
import com.kursinis.prif4kursinis.fxControllers.tableviewparameters.CustomerTableParameters;
import com.kursinis.prif4kursinis.hibernateControllers.CustomHib;
import com.kursinis.prif4kursinis.model.Customer;
import com.kursinis.prif4kursinis.model.Manager;
import com.kursinis.prif4kursinis.model.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class UserTabController implements Initializable {
    @FXML
    public TableView<CustomerTableParameters> customerTable;
    @FXML
    public TableView<ManagerTableParameters> managerTable;
    @FXML
    public TableColumn<CustomerTableParameters, Integer> idTableCol;
    @FXML
    public TableColumn<CustomerTableParameters, String> loginTableCol;
    @FXML
    public TableColumn<CustomerTableParameters, String> passwordTableCol;
    @FXML
    public TableColumn<CustomerTableParameters, String> addressTableCol;
    @FXML
    public TableColumn<CustomerTableParameters, Void> dummyCol;
    @FXML
    public TableColumn<ManagerTableParameters, Integer> managerIdCol;
    @FXML
    public TableColumn<ManagerTableParameters, String> managerLoginCol;
    @FXML
    public TableColumn<ManagerTableParameters, String> managerPasswordCol;
    public TableColumn<ManagerTableParameters, String> managerEmployeeIdCol;
    public TableColumn<ManagerTableParameters, String> managerMedCertificateCol;
    public TableColumn<ManagerTableParameters, LocalDate> managerEmploymentDateCol;
    public TableColumn<ManagerTableParameters, Boolean> managerIsAdminCol;
    public TableColumn<ManagerTableParameters, Void> managerDummyCol;
    public Button registerNewUser;

    private ObservableList<CustomerTableParameters> customerData = FXCollections.observableArrayList();
    private ObservableList<ManagerTableParameters> managerData = FXCollections.observableArrayList();

    private CustomHib customHib;
    private MainShopController mainShopController;
    private EntityManagerFactory entityManagerFactory;
    private User currentUser;

    public void setMainShopController(MainShopController mainShopController) {
        this.mainShopController = mainShopController;
    }

    public void setData(CustomHib customHib, User currentUser) {
        this.customHib = customHib;
        this.currentUser = currentUser;
        loadUserTables();

        if (currentUser instanceof Manager) {
            Manager manager = (Manager) currentUser;
            if (!manager.isAdmin()) {
                managerTable.setDisable(true);
                registerNewUser.setDisable(true);
            }
        } else if (currentUser instanceof Customer) {
            mainShopController.hideManagerTabs();
        } else {
            JavaFxCustomUtils.generateAlert(Alert.AlertType.WARNING, "Unexpected User Type", "Unexpected User Type", "An unexpected user type was encountered.");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        entityManagerFactory = Persistence.createEntityManagerFactory("coursework-shop");

        initializeCustomerTable();
        initializeManagerTable();
    }

    private void initializeCustomerTable() {
        customerTable.setEditable(true);

        idTableCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        loginTableCol.setCellValueFactory(new PropertyValueFactory<>("login"));

        passwordTableCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordTableCol.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setPassword(event.getNewValue());
            Customer customer = customHib.getEntityById(Customer.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            customer.setPassword(event.getNewValue());
            customHib.update(customer);
        });

        addressTableCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        Callback<TableColumn<CustomerTableParameters, Void>, TableCell<CustomerTableParameters, Void>> callback = param -> {
            final TableCell<CustomerTableParameters, Void> cell = new TableCell<>() {
                private final Button deleteButton = new Button("Delete");

                {
                    deleteButton.setOnAction(event -> {
                        CustomerTableParameters row = getTableView().getItems().get(getIndex());
                        customHib.delete(Customer.class, row.getId());
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            };
            return cell;
        };

        dummyCol.setCellFactory(callback);
    }

    private void initializeManagerTable() {
        managerTable.setEditable(true);

        managerIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        managerLoginCol.setCellValueFactory(new PropertyValueFactory<>("login"));

        managerPasswordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        managerPasswordCol.setCellFactory(TextFieldTableCell.forTableColumn());
        managerPasswordCol.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setPassword(event.getNewValue());
            Manager manager = customHib.getEntityById(Manager.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            manager.setPassword(event.getNewValue());
            customHib.update(manager);
        });

        managerEmployeeIdCol.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        managerMedCertificateCol.setCellValueFactory(new PropertyValueFactory<>("medCertificate"));
        managerEmploymentDateCol.setCellValueFactory(new PropertyValueFactory<>("employmentDate"));
        managerIsAdminCol.setCellValueFactory(new PropertyValueFactory<>("isAdmin"));

        Callback<TableColumn<ManagerTableParameters, Void>, TableCell<ManagerTableParameters, Void>> callback = param -> {
            final TableCell<ManagerTableParameters, Void> cell = new TableCell<>() {
                private final Button deleteButton = new Button("Delete");

                {
                    deleteButton.setOnAction(event -> {
                        ManagerTableParameters row = getTableView().getItems().get(getIndex());
                        customHib.delete(Manager.class, row.getId());
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            };
            return cell;
        };

        managerDummyCol.setCellFactory(callback);
    }

    private void loadUserTables() {
        customerTable.getItems().clear();
        managerTable.getItems().clear();

        List<Customer> customerList = customHib.getAllRecords(Customer.class);
        for (Customer c : customerList) {
            CustomerTableParameters customerTableParameters = new CustomerTableParameters();
            customerTableParameters.setId(c.getId());
            customerTableParameters.setLogin(c.getLogin());
            customerTableParameters.setPassword(c.getPassword());
            customerTableParameters.setAddress(c.getAddress());
            customerData.add(customerTableParameters);
        }

        List<Manager> managerList = customHib.getAllRecords(Manager.class);
        for (Manager m : managerList) {
            ManagerTableParameters managerTableParameters = new ManagerTableParameters();
            managerTableParameters.setId(m.getId());
            managerTableParameters.setLogin(m.getLogin());
            managerTableParameters.setPassword(m.getPassword());
            managerTableParameters.setEmployeeId(m.getEmployeeId());
            managerTableParameters.setMedCertificate(m.getMedCertificate());
            managerTableParameters.setEmploymentDate(m.getEmploymentDate());
            managerTableParameters.setAdmin(m.isAdmin());
            managerData.add(managerTableParameters);
        }

        customerTable.setItems(customerData);
        managerTable.setItems(managerData);
    }

    public void registerNewUser() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("registration.fxml"));
        Parent parent = fxmlLoader.load();
        RegistrationController registrationController = fxmlLoader.getController();
        registrationController.setReturnTo("mainShopController");
        registrationController.setData(entityManagerFactory, customHib, currentUser);
        Scene scene = new Scene(parent);
        Stage stage = (Stage) registerNewUser.getScene().getWindow();
        stage.setTitle("Shop");
        stage.setScene(scene);
        stage.show();
    }
}
