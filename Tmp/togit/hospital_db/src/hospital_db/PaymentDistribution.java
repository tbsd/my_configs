package hospital_db;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class PaymentDistribution {
    private SimpleIntegerProperty staffId;
    private SimpleStringProperty name;
    private SimpleIntegerProperty percentage;

    public PaymentDistribution(int staffId, int percentage, String name) {
        setName(name);
        setPercentage(percentage);
        setStaffId(staffId);
    }

    public SimpleIntegerProperty staffIdProperty() {
        if (staffId == null)
            staffId = new SimpleIntegerProperty();
        return staffId;
    }

    public SimpleStringProperty nameProperty() {
        if (name == null)
            name = new SimpleStringProperty();
        return name;
    }

    public SimpleIntegerProperty percentageProperty() {
        if (percentage == null)
            percentage = new SimpleIntegerProperty();
        return percentage;
    }

    public int getPercentage() {
        return percentage.get();
    }

    public int getStaffId() {
        return staffId.get();
    }

    public String getName() {
        return name.get();
    }

    public void setStaffId(int staffId) {
        staffIdProperty().set(staffId);
    }

    public void setName(String name) {
        nameProperty().set(name);
    }

    public void setPercentage(int percentage) {
        percentageProperty().set(percentage);
    }
}
