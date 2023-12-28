package com.kursinis.prif4kursinis.fxControllers.tableParameters;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class ManagerTableParameters extends UserTableParameters {
    private SimpleStringProperty employeeId = new SimpleStringProperty();
    private SimpleStringProperty medCertificate = new SimpleStringProperty();
    private SimpleObjectProperty<LocalDate> employmentDate = new SimpleObjectProperty<>();
    private SimpleObjectProperty<Boolean> isAdmin = new SimpleObjectProperty<>();

    public ManagerTableParameters(SimpleIntegerProperty id, SimpleStringProperty login,
                                  SimpleStringProperty password, SimpleStringProperty employeeId,
                                  SimpleStringProperty medCertificate, SimpleObjectProperty<LocalDate> employmentDate,
                                  SimpleObjectProperty<Boolean> isAdmin) {
        super(id, login, password);
        this.employeeId = employeeId;
        this.medCertificate = medCertificate;
        this.employmentDate = employmentDate;
        this.isAdmin = isAdmin;
    }

    public ManagerTableParameters() {
    }

    public String getEmployeeId() {
        return employeeId.get();
    }

    public SimpleStringProperty employeeIdProperty() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId.set(employeeId);
    }

    public String getMedCertificate() {
        return medCertificate.get();
    }

    public SimpleStringProperty medCertificateProperty() {
        return medCertificate;
    }

    public void setMedCertificate(String medCertificate) {
        this.medCertificate.set(medCertificate);
    }

    public String getEmploymentDate() {
        LocalDate date = employmentDate.get();
        if (date != null) {
            return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } else {
            return null;
        }
    }

    public SimpleObjectProperty<LocalDate> employmentDateProperty() {
        return employmentDate;
    }

    public void setEmploymentDate(LocalDate employmentDate) {
        this.employmentDate.set(employmentDate);
    }

    public Boolean getIsAdmin() {
        return isAdmin.get();
    }

    public SimpleObjectProperty<Boolean> isAdminProperty() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin.set(admin);
    }
}
