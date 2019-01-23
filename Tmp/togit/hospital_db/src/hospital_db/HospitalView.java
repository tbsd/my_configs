package hospital_db;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class HospitalView extends VBox {
    private MenuBar menu;
    private Connection connect;
    private VBox workPanel;
    private HBox mainPanel;

    public HospitalView(Connection connect) {
        this.connect = connect;
        menu = createMenuBar();
        mainPanel = createMainPanel();
        this.getChildren().addAll(menu, mainPanel);
    }

    private void showNoConnect() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("");
        alert.setContentText("Нет соединения с базой данных.");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    private MenuBar createMenuBar(){
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(createPaymentsMenu(), createIncomeMenu(), createHelpMenu());
        return menuBar;
    }

    private  Menu createIncomeMenu() {
        Menu incomeMenu = new Menu("Доходы");
        MenuItem avgFromClientMI = new MenuItem("Средний даход за период");
        avgFromClientMI.setOnAction(e -> {
            try {
                DateDialog dateDialog = new DateDialog();
                Pair<LocalDate, LocalDate> dates = dateDialog.showAndWait();

                if (dateDialog.isOk()) {
                    Statement statement = connect.createStatement();
                    ResultSet result = statement.executeQuery("SELECT  clients.*, (SUM(services.Цена) / " +
                            ChronoUnit.DAYS.between(dates.getKey(), dates.getValue()) + " ) " +
                            " AS Средний_доход_в_день" + " FROM provided_services LEFT JOIN services" +
                            " ON services.ИН_услуги = provided_services.ИН_услуги" +
                            " LEFT JOIN clients" +
                            " ON clients.ИН_клиента = provided_services.ИН_клиента WHERE provided_services.Дата" +
                            " BETWEEN \"" + java.sql.Date.valueOf(dates.getKey()) + "\" AND \"" +
                            java.sql.Date.valueOf(dates.getValue()) + "\"" +
                            " GROUP BY clients.ИН_клиента ORDER BY Средний_доход_в_день DESC");


                    setWorkPanel(result);
                }
            } catch (Exception ex) {
                showNoConnect();
            }
        });
        MenuItem serviceChartMI = new MenuItem("Рейтинг услуг");
        serviceChartMI.setOnAction(e -> {
            try {
                Statement statement = connect.createStatement();
                ResultSet result = statement.executeQuery("SELECT  services.ИН_услуги, services.Название_услуги," +
                        " SUM(services.Цена) AS Общий_доход" +
                        " FROM provided_services LEFT JOIN services" +
                        " ON services.ИН_услуги = provided_services.ИН_услуги" +
                        " GROUP BY services.ИН_услуги ORDER BY Общий_доход DESC");
                setWorkPanel(result);
            } catch (Exception ex) {
                showNoConnect();
            }

        });

        incomeMenu.getItems().addAll(avgFromClientMI, serviceChartMI);
        return incomeMenu;
    }

    private Menu createPaymentsMenu() {
        Menu paymentsMenu = new Menu("Выплаты");
        MenuItem monthPayMI = new MenuItem("Выплаты за месяц");
        monthPayMI.setOnAction(e -> {
            try {
                Statement statement = connect.createStatement();
                String request = "SELECT  staff.*, SUM(services.Цена*money_distribution.Процент_от_оплаты / 100) AS" +
                        " Выплаты  FROM provided_services  INNER JOIN money_distribution ON" +
                        " money_distribution.ИН_услуги = provided_services.ИН_услуги INNER JOIN staff ON" +
                        " staff.ИН_сотрудника = money_distribution.ИН_сотрудника INNER JOIN services ON" +
                        " services.ИН_услуги = provided_services.ИН_услуги WHERE provided_services.Дата" +
                        " BETWEEN \"" + java.sql.Date.valueOf(LocalDate.now().minusMonths(1)) + "\" AND \"" +
                        java.sql.Date.valueOf(LocalDate.now().plusDays(1)) + "\" GROUP BY staff.ИН_сотрудника";
                ResultSet result = statement.executeQuery(request);
                setWorkPanel(result);
            } catch (Exception ex) {
                showNoConnect();
            }
        });
        MenuItem periodPayMI = new MenuItem("Выплаты за период");
        periodPayMI.setOnAction(e -> {
            try {
                DateDialog dateDialog = new DateDialog();
                Pair<LocalDate, LocalDate> dates = dateDialog.showAndWait();

                if (dateDialog.isOk()) {
                    Statement statement = connect.createStatement();
                    String request = "SELECT  staff.*, SUM(services.Цена*money_distribution.Процент_от_оплаты / 100)" +
                            " AS Выплаты  FROM provided_services  INNER JOIN money_distribution ON" +
                            " money_distribution.ИН_услуги = provided_services.ИН_услуги INNER JOIN staff ON" +
                            " staff.ИН_сотрудника = money_distribution.ИН_сотрудника INNER JOIN services ON" +
                            " services.ИН_услуги = provided_services.ИН_услуги WHERE provided_services.Дата" +
                            " BETWEEN \"" + java.sql.Date.valueOf(dates.getKey()) + "\" AND \"" +
                            java.sql.Date.valueOf(dates.getValue()) + "\" GROUP BY staff.ИН_сотрудника";
                    ResultSet result = statement.executeQuery(request);
                    setWorkPanel(result);
                }
            } catch (Exception ex) {
                showNoConnect();
            }
        });
        MenuItem payWithJobsMI = new MenuItem("Выплаты с разделением по услугам");
        payWithJobsMI.setOnAction(e -> {
            try {
                Statement statement = connect.createStatement();
                String request = "SELECT  staff.*, services.ИН_услуги, services.Название_услуги, " +
                        "SUM(services.Цена*money_distribution.Процент_от_оплаты / 100) AS Выплаты  " +
                        "FROM provided_services  INNER JOIN money_distribution ON money_distribution.ИН_услуги =" +
                        " provided_services.ИН_услуги INNER JOIN staff ON staff.ИН_сотрудника =" +
                        " money_distribution.ИН_сотрудника INNER JOIN services ON services.ИН_услуги =" +
                        " provided_services.ИН_услуги GROUP BY money_distribution.ИН_сотрудника," +
                        " money_distribution.ИН_услуги ORDER BY money_distribution.ИН_сотрудника";
                ResultSet result = statement.executeQuery(request);
                setWorkPanel(result);
            } catch (Exception ex) {
                showNoConnect();
            }
        });
        MenuItem singleWorkerMI = new MenuItem("Выплаты заданному сотруднику");
        singleWorkerMI.setOnAction(e -> {
            showPayForExactWorker();
        });
        MenuItem exitMI = new MenuItem("Выход");
        exitMI.setOnAction(e -> System.exit(0));

        paymentsMenu.getItems().addAll(monthPayMI, periodPayMI, payWithJobsMI, singleWorkerMI,
                new SeparatorMenuItem(), exitMI);
        return paymentsMenu;
    }

    private Menu createHelpMenu(){
        Menu helpMenu = new Menu("Помощь");
        MenuItem helpMI = new MenuItem("Спарвка");
        helpMI.setOnAction(e -> {
            Alert helpDialog = new Alert(Alert.AlertType.INFORMATION);
            helpDialog.setResizable(true);
            helpDialog.initModality(Modality.NONE);
            helpDialog.setHeaderText(null);
            helpDialog.setGraphic(null);
            helpDialog.setTitle("Справка");
            try {
                File readme = new File("README.html");
                WebView webView = new WebView();
                webView.getEngine().load(readme.toURI().toURL().toString());
                helpDialog.getDialogPane().setContent(webView);
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Невозможно открыть справку.");
                alert.setHeaderText("");
                alert.setOnCloseRequest(ev -> alert.close());
                alert.showAndWait();
            }
            helpDialog.show();
        });
        MenuItem aboutMI = new MenuItem("О программе");
        aboutMI.setOnAction(e -> {
            Alert aboutAlert = new Alert(Alert.AlertType.INFORMATION);
            aboutAlert.setOnCloseRequest(ev -> aboutAlert.close());
            aboutAlert.setTitle("О программе");
            aboutAlert.setHeaderText("");
            aboutAlert.setContentText("Дананя программа является СУБД для базы данных дополнительных выплат" +
                    " сотрудникам поликлиники.\n\n Автор: Антон Курбатов, ИВТ-31");
            aboutAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            aboutAlert.show();
        });

        helpMenu.getItems().addAll(helpMI, aboutMI);
        return helpMenu;
    }

    private HBox createMainPanel() {
        HBox mainPanel = new HBox();
        try {
            Statement statement = connect.createStatement();
            ResultSet result = statement.executeQuery("SELECT provided_services.*," +
                        " services.Название_услуги, clients.Фамилия, clients.Имя, clients.Отчество" +
                    " FROM provided_services INNER JOIN services" +
                        " ON services.ИН_услуги = provided_services.ИН_услуги" +
                        " INNER JOIN clients ON clients.ИН_клиента = provided_services.ИН_клиента");
            workPanel = createWorkPanel(result, "provided_services");
            mainPanel.getChildren().addAll(createTablesList(), workPanel);
        } catch (Exception ex) {}
        return  mainPanel;
    }

    private VBox createTablesList() {
        VBox tablesList = new VBox();
        tablesList.setPadding(new Insets(8));
        Hyperlink clientsLink = new Hyperlink("Клиенты");
        clientsLink.setOnAction(e -> setWorkPanel("clients"));
        clientsLink.setVisited(true);
        Hyperlink providedLink = new Hyperlink("Оказанные услуги");
        providedLink.setOnAction(e -> setWorkPanel("provided_services"));
        providedLink.setVisited(true);
        Hyperlink servicesLink = new Hyperlink("Услуги");
        servicesLink.setOnAction(e -> setWorkPanel("services"));
        servicesLink.setVisited(true);
        Hyperlink distributionLink = new Hyperlink("Распределение выплат");
        distributionLink.setOnAction(e -> setWorkPanel("money_distribution"));
        distributionLink.setVisited(true);
        Hyperlink staffLink = new Hyperlink("Персонал");
        staffLink.setOnAction(e -> setWorkPanel("staff"));
        staffLink.setVisited(true);
        tablesList.getChildren().addAll(providedLink, clientsLink, servicesLink, staffLink, distributionLink);
        return tablesList;
    }

    private void setWorkPanel(ResultSet resSet) {
        try {
            mainPanel.getChildren().remove(workPanel);
            workPanel = createWorkPanel(resSet, "");
            mainPanel.getChildren().add(workPanel);
        } catch (Exception ex) {
            showNoConnect();
        }
    }

    private void setWorkPanel(String type) {
        try {
            Statement statement = connect.createStatement();
            ResultSet result;
            if (type.equals("provided_services")) {
                result = statement.executeQuery("SELECT provided_services.*," +
                        " services.Название_услуги, clients.Фамилия, clients.Имя, clients.Отчество" +
                        " FROM provided_services INNER JOIN services" +
                        " ON services.ИН_услуги = provided_services.ИН_услуги" +
                        " INNER JOIN clients ON clients.ИН_клиента = provided_services.ИН_клиента");
            } else if (type.equals("money_distribution")) {
                result = statement.executeQuery("SELECT money_distribution.*," +
                        " services.Название_услуги, staff.Фамилия, staff.Имя, staff.Отчество, staff.Должность FROM" +
                        " money_distribution INNER JOIN services" +
                        " ON services.ИН_услуги = money_distribution.ИН_услуги" +
                        " INNER JOIN staff ON staff.ИН_сотрудника = money_distribution.ИН_сотрудника");
            } else
                result = statement.executeQuery("SELECT * FROM " + type);
            mainPanel.getChildren().remove(workPanel);
            workPanel = createWorkPanel(result, type);
            mainPanel.getChildren().add(workPanel);
        } catch (Exception ex) {
            showNoConnect();
        }
    }

    private void showPayForExactWorker() {
        TextInputDialog workerIdDialog = new TextInputDialog();
        workerIdDialog.setContentText("Введите фамилию сотрудника: ");
        workerIdDialog.setHeaderText("");
        workerIdDialog.setGraphic(null);
        workerIdDialog.showAndWait()
                .filter(response -> !response.equals(""))
                .ifPresent(response -> {
                    try {
                        Statement statement = connect.createStatement();
                        String request = "SELECT  staff.*, SUM(services.Цена*money_distribution.Процент_от_оплаты /" +
                                " 100) AS Выплаты  FROM provided_services  INNER JOIN money_distribution ON" +
                                " money_distribution.ИН_услуги = provided_services.ИН_услуги INNER JOIN staff ON" +
                                " staff.ИН_сотрудника = money_distribution.ИН_сотрудника INNER JOIN services ON" +
                                " services.ИН_услуги = provided_services.ИН_услуги WHERE staff.Фамилия = \"" +
                                response.trim() + "\" GROUP BY staff.ИН_сотрудника";
                        ResultSet result = statement.executeQuery(request);
                        setWorkPanel(result);
                    } catch (Exception ex) {
                        showNoConnect();
                    }

                });
    }

    private VBox createWorkPanel(ResultSet queryResult, String type) {
        VBox workPanel = new VBox();
        workPanel.setSpacing(2);
        HBox.setHgrow(workPanel, Priority.ALWAYS);
        workPanel.setPadding(new Insets(8));
        TableView table = createTable(queryResult);
        Button addButton = new Button("Добавить запись");
        Button removeButton = new Button("Удалить запись");
        addButton.setPrefWidth(150);
        removeButton.setPrefWidth(150);
        if (type.equals("money_distribution")) {
            addButton.setText("Изменить распределение");
            addButton.setPrefWidth(200);
            removeButton.setDisable(true);
            removeButton.setVisible(false);
        }
        if (type.equals("")) {
            addButton.setDisable(true);
            addButton.setVisible(false);
            removeButton.setDisable(true);
            removeButton.setVisible(false);
        } else {
            removeButton.setOnAction(e -> {
                ObservableList<TableColumn> columns = table.getColumns();
                ObservableList<String> selected = (ObservableList<String>) table.getSelectionModel().getSelectedItem();
                if (selected == null)
                    return;
                String deletionRequest = "DELETE FROM " + type + " WHERE " + columns.get(0).getText() + " = '" +
                        selected.get(0) + "' ";
                for (int i = 1; i < 3; ++i) {
                    deletionRequest += " AND " + columns.get(i).getText() + " = '" + selected.get(i) + "' ";
                }
                try {
                    Statement statement = connect.createStatement();
                    statement.execute(deletionRequest);
                    table.getItems().remove(selected);
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("");
                    alert.setContentText("Удаление невозможно, вероятно, данная запись связана с записями из других " +
                            "таблиц. \n\nДля удаения необходимо удалить зависимые строки.");
                    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    alert.showAndWait();
                }
            });

            addButton.setOnAction(e -> showAddDialog(type));
        }
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(2);
        buttonBox.getChildren().addAll(addButton, removeButton);
        workPanel.getChildren().addAll(table,buttonBox);

        return workPanel;
    }

    private void showAddDialog(String type) {
        switch (type) {
            case "clients":
                addClientDialog();
                break;
            case "provided_services":
                addProvidedDialog();
                break;
            case "services":
                addServiceDialog();
                break;
            case "money_distribution":
                changeDistributionDialog();
                break;
            case "staff":
                addStaffDialog();
                break;
            default:
                Alert alert = new Alert(Alert.AlertType.ERROR, "Нельзя добавить в эту таблицу.");
                alert.setHeaderText("");
                alert.showAndWait();
                break;
        }
    }


    private void changeDistributionDialog() {
        TableView<PaymentDistribution> distTable = new TableView<>();
        ObservableList<PaymentDistribution> data = FXCollections.observableArrayList();
        distTable.setItems(data);
        Dialog<ButtonType> distrDialog = new Dialog<>();
        distrDialog.setOnCloseRequest(e -> distrDialog.close());
        distrDialog.setTitle("Изменить распределение оплаты");
        distrDialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        GridPane content = new GridPane();
        distrDialog.getDialogPane().setContent(content);
        distrDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        final Button okButton = (Button) distrDialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        Button addButton = new Button(">");
        Button removeButton = new Button("X");
        Label totalPercent = new Label("0");
        ChoiceBox service = new ChoiceBox();
        try {
            Statement statement = connect.createStatement();
            ResultSet result = statement.executeQuery("SELECT DISTINCT ИН_услуги, Название_услуги FROM services");
            while(result.next()) {
                service.getItems().add(result.getString("ИН_услуги") + " " +
                        result.getString("Название_услуги"));
            }
        }catch (Exception ex) {
            showNoConnect();
        }
        service.valueProperty().addListener(e -> {
            addButton.setDisable(false);
            removeButton.setDisable(false);
            if ((totalPercent.getText().equals("100") ||
                    distTable.getItems().size() == 0) && service.getValue() != null)
                okButton.setDisable(false);
            else
                okButton.setDisable(true);
            data.clear();
            try {
                Statement statement = connect.createStatement();
                ResultSet result = statement.executeQuery("SELECT staff.ИН_сотрудника, staff.Имя, staff.Фамилия, " +
                        "staff.Отчество, money_distribution.Процент_от_оплаты FROM staff LEFT JOIN " +
                        "money_distribution ON staff.ИН_сотрудника=money_distribution.ИН_сотрудника WHERE" +
                        " money_distribution.ИН_услуги = " + service.getValue().toString().split(" ")[0]);
                while(result.next()) {
                    data.add(new PaymentDistribution(result.getInt("staff.ИН_сотрудника"),
                            result.getInt("money_distribution.Процент_от_оплаты"),
                            result.getString("staff.Фамилия") + " " + result.getString("staff.Имя") + " " +
                                    result.getString("staff.Отчество")));
                }
                int sum = 0;
                for (PaymentDistribution item: distTable.getItems()) {
                    sum += item.getPercentage();
                }
                totalPercent.setText(Integer.toString(sum));
            }catch (Exception ex) {
                showNoConnect();
            }

        });

        TableColumn<PaymentDistribution, Integer> staffId = new TableColumn<>("ИН_сотрудника");
        staffId.setCellValueFactory(new PropertyValueFactory<>("staffId"));
        TableColumn<PaymentDistribution, String> name = new TableColumn<>("Имя_сотрудника");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<PaymentDistribution, Integer> percentage = new TableColumn<>("Процент_от_оплаты, %");
        percentage.setCellValueFactory(new PropertyValueFactory<>("percentage"));
        percentage.setEditable(true);
        distTable.setEditable(true);
        percentage.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        percentage.setOnEditCommit(e -> {
            try {
                if (e.getNewValue() < 1 || e.getNewValue() > 100) {
                    Alert error = new Alert(Alert.AlertType.ERROR, "Ошибка");
                    error.setHeaderText("");
                    error.setContentText("Введите число от 1 до 100.");
                    error.showAndWait();
                    e.getTableView().refresh();
                    return;
                }
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setPercentage(e.getNewValue());
            } catch (Exception ex) {
                Alert error = new Alert(Alert.AlertType.ERROR, "Ошибка");
                error.setHeaderText("");
                error.setContentText("Введите число.");
                error.showAndWait();
                return;
            }
            int sum = 0;
            for (PaymentDistribution item: distTable.getItems()) {
                sum += item.getPercentage();
            }
            totalPercent.setText(Integer.toString(sum));
            if ((sum == 100 || distTable.getItems().size() == 0) && service.getValue() != null)
                okButton.setDisable(false);
            else
                okButton.setDisable(true);
        });
        distTable.getColumns().addAll(staffId, name, percentage);
        try {
            Statement statement = connect.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM staff");
            final TableView staffTable = createTable(result);

            addButton.setDisable(true);
            addButton.setOnAction( e -> {
                ObservableList<String> selected =
                        (ObservableList<String>) staffTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    if (!distTable.getItems().isEmpty()) {
                        for (PaymentDistribution item : distTable.getItems()) {
                            try {
                                if (item.getStaffId() == Integer.parseInt(selected.get(0)))
                                    return;
                            } catch (NullPointerException ex) {
                                break;
                            }
                        }
                    }
                    data.add(new PaymentDistribution(Integer.parseInt(selected.get(0)), 100,
                            selected.get(1) + " " + selected.get(2) + " " + selected.get(3)));
                    int sum = 0;
                    for (PaymentDistribution item: distTable.getItems()) {
                        sum += item.getPercentage();
                    }
                    totalPercent.setText(Integer.toString(sum));
                    if (sum == 100 && service.getValue() != null)
                        okButton.setDisable(false);
                    else
                        okButton.setDisable(true);
                }
            });
            removeButton.setDisable(true);
            removeButton.setOnAction(e -> {
                PaymentDistribution selected = distTable.getSelectionModel().getSelectedItem();
                if (selected != null)
                    distTable.getItems().remove(selected);
                if (distTable.getItems().size() == 0)
                    okButton.setDisable(false);
                int sum = 0;
                for (PaymentDistribution item: distTable.getItems()) {
                    sum += item.getPercentage();
                }
                totalPercent.setText(Integer.toString(sum));
                if ((sum == 100 || distTable.getItems().size() == 0) && service.getValue() != null)
                    okButton.setDisable(false);
                else
                    okButton.setDisable(true);
            });

            VBox middleBox = new VBox();
            middleBox.setSpacing(2);
            middleBox.getChildren().addAll(totalPercent, addButton, removeButton);
            middleBox.setAlignment(Pos.CENTER);
            content.addRow(0, service);
            content.addRow(1, staffTable, middleBox, distTable);
            content.setHgap(4);
            content.setVgap(2);
        } catch (Exception ex) {
            showNoConnect();
        }

        distrDialog.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    try {
                        Statement statement = connect.createStatement();
                        String deletionRequest = "DELETE FROM money_distribution WHERE ИН_услуги = " +
                                ((String) service.getValue()).split(" ")[0];
                        statement.execute(deletionRequest);
                        String addRequest = "INSERT INTO money_distribution VALUES ";
                        for (PaymentDistribution item: distTable.getItems()) {
                            addRequest = String.format("%s ('%d', '%s', '%d') , ", addRequest, item.getStaffId(),
                                    ((String) service.getValue()).split(" ")[0], item.getPercentage());
                        }
                        if(addRequest.split(" ").length != 4) {
                            addRequest = addRequest.substring(0, addRequest.length() - 3);
                            statement.execute(addRequest);
                        }
                        setWorkPanel("money_distribution");
                    } catch (Exception ex) {
                        showNoConnect();
                    }
                });


    }

    private void addProvidedDialog() {
        Dialog<ButtonType> clientDialog = new Dialog<>();
        clientDialog.setOnCloseRequest(e -> clientDialog.close());
        clientDialog.setTitle("Добавить оплату");
        GridPane content = new GridPane();
        clientDialog.getDialogPane().setContent(content);
        content.setHgap(2);
        content.setVgap(2);
        content.addRow(0, new Label("Услуги"), new Label("ИН_клента"), new Label("Дата и время"));
        ChoiceBox service = new ChoiceBox();
        clientDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        final Button okButton = (Button) clientDialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        service.valueProperty().addListener(e -> {
            if (service.getValue() != null)
                okButton.setDisable(false);
            else
                okButton.setDisable(true);
        });
        try {
            Statement statement = connect.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM services");
            while(result.next()) {
                service.getItems().add(result.getString("ИН_услуги") + " " + result.getString("Название_услуги"));
            }
        } catch (Exception ex) {
            showNoConnect();
        }
        DatePicker date = new DatePicker(LocalDate.now());
        date.setConverter(new StringConverter<LocalDate>()
        {
            private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate localDate)
            {
                if(localDate==null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString)
            {
                if(dateString==null || dateString.trim().isEmpty())
                {
                    return null;
                }
                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        });
        Spinner<Double> client_id = new Spinner<>(0, Integer.MAX_VALUE, 1, 1.0);
        client_id.focusedProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!newValue) {
                    client_id.increment(0);
                }
            } catch (Exception ex) {
                client_id.getValueFactory().setValue(0.0);
            }
        });
        client_id.setEditable(true);

        Calendar calendar = Calendar.getInstance();
        Spinner<Double> hours = new Spinner<>(0, 23, calendar.get(Calendar.HOUR_OF_DAY), 1.0);
        hours.focusedProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!newValue) {
                    hours.increment(0);
                }
            } catch (Exception ex) {
                client_id.getValueFactory().setValue(0.0);
            }
        });
        hours.setPrefWidth(60);
        hours.setEditable(true);
        Spinner<Double> minutes = new Spinner<>(0, 59, calendar.get(Calendar.MINUTE), 1.0);
        minutes.focusedProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!newValue) {
                    minutes.increment(0);
                }
            } catch (Exception ex) {
                client_id.getValueFactory().setValue(0.0);
            }
        });
        minutes.setPrefWidth(60);
        minutes.setEditable(true);
        Spinner<Double> seconds = new Spinner<>(0, 59, calendar.get(Calendar.SECOND), 1.0);
        seconds.focusedProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!newValue) {
                    seconds.increment(0);
                }
            } catch (Exception ex) {
                client_id.getValueFactory().setValue(0.0);
            }
        });
        seconds.setPrefWidth(60);
        seconds.setEditable(true);
        Spinner<Double> ms = new Spinner<>(0, 999, calendar.get(Calendar.MILLISECOND), 1.0);
        ms.focusedProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!newValue) {
                    ms.increment(0);
                }
            } catch (Exception ex) {
                client_id.getValueFactory().setValue(0.0);
            }
        });
        ms.setPrefWidth(80);
        ms.setEditable(true);
        content.addRow(1, service, client_id, date, hours, new Label(":"), minutes, new Label(":"), seconds,
                new Label("."), ms);
        clientDialog.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    try {
                        String service_id = ((String) service.getValue()).split(" ")[0];
                        Statement statement = connect.createStatement();
                        String addRequest = String.format("INSERT INTO provided_services VALUES ('%s " +
                                "%d:%d:%d.%d', '%s', '%d')", date.getValue(), hours.getValue().intValue(),
                                minutes.getValue().intValue(), seconds.getValue().intValue(), ms.getValue().intValue(),
                                service_id, client_id.getValue().intValue());
                        statement.execute(addRequest);
                        setWorkPanel("provided_services");
                    } catch (Exception ex) {
                        if (ex.toString().contains("ИН_клиента")) {
                            Alert noClient = new Alert(Alert.AlertType.CONFIRMATION);
                            noClient.setHeaderText("");
                            noClient.setContentText("Клиент с таким ИН не найден. Добавить клиента?");
                            noClient.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                            noClient.showAndWait().filter(res -> res == ButtonType.OK)
                                    .ifPresent(res -> {

                                        addClientDialog("\"" + client_id.getValue().intValue() + "\"");
                                        try {
                                            String service_id = ((String) service.getValue()).split(" ")[0];
                                            Statement statement = connect.createStatement();
                                            String addRequest = String.format("INSERT INTO provided_services " +
                                                    "VALUES ('%s %d:%d:%d.%d', '%s', '%d')", date.getValue(),
                                                    hours.getValue().intValue(), minutes.getValue().intValue(),
                                                    seconds.getValue().intValue(), ms.getValue().intValue(),
                                                    service_id, client_id.getValue().intValue());
                                            statement.execute(addRequest);
                                            setWorkPanel("provided_services");
                                        } catch (Exception except) {
                                            showNoConnect();
                                        }
                                    });
                        } else {
                            showNoConnect();
                        }
                    }
                });

    }


    private void addServiceDialog() {
        Dialog<ButtonType> clientDialog = new Dialog<>();
        clientDialog.setOnCloseRequest(e -> clientDialog.close());
        clientDialog.setTitle("Добавить услугу");
        GridPane content = new GridPane();
        clientDialog.getDialogPane().setContent(content);
        content.addRow(0, new Label("Название_услуги"), new Label("Цена"));
        clientDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        final Button okButton = (Button) clientDialog.getDialogPane().lookupButton(ButtonType.OK);
        TextField name = new TextField();
        name.setOnKeyReleased(e -> {
            if (name.getCharacters().length() == 0)
                okButton.setDisable(true);
            else
                okButton.setDisable(false);
        });
        Spinner<Double> price = new Spinner<>(0, Integer.MAX_VALUE, 0, 0.01);
        price.focusedProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!newValue) {
                    price.increment(0);
                }
            } catch (Exception ex) {
                price.getValueFactory().setValue(0.0);
            }
        });
        if (name.getCharacters().length() == 0)
            okButton.setDisable(true);
        else
            okButton.setDisable(false);
        price.setEditable(true);
        content.addRow(1, name, price);
        clientDialog.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    try {
                        Statement statement = connect.createStatement();
                        String addRequest = String.format("INSERT INTO services VALUES (null, '%s', '%s')",
                                name.getCharacters().toString().trim(), price.getValue());
                        statement.execute(addRequest);
                        setWorkPanel("services");
                    } catch (Exception ex) {
                        showNoConnect();
                    }
                });

    }

    private void addClientDialog() {
        addClientDialog("null");
    }

    private void addClientDialog(String id) {
        Dialog<ButtonType> clientDialog = new Dialog<>();
        clientDialog.setOnCloseRequest(e -> clientDialog.close());
        clientDialog.setTitle("Добавить клиента");
        GridPane content = new GridPane();
        clientDialog.getDialogPane().setContent(content);
        content.addRow(0, new Label("Фамилия"), new Label("Имя"), new Label("Отчество"));
        TextField lastName = new TextField();
        TextField name = new TextField();
        TextField patronymic = new TextField();
        clientDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        final Button okButton = (Button) clientDialog.getDialogPane().lookupButton(ButtonType.OK);
        if (lastName.getCharacters().length() == 0 || name.getCharacters().length() == 0 ||
                patronymic.getCharacters().length() == 0)
            okButton.setDisable(true);
        else
            okButton.setDisable(false);
        lastName.setOnKeyReleased(e -> {
            if (lastName.getCharacters().length() == 0 || name.getCharacters().length() == 0 ||
                    patronymic.getCharacters().length() == 0)
                okButton.setDisable(true);
            else
                okButton.setDisable(false);

        });
        name.setOnKeyReleased(e -> {
            if (lastName.getCharacters().length() == 0 || name.getCharacters().length() == 0 ||
                    patronymic.getCharacters().length() == 0)
                okButton.setDisable(true);
            else
                okButton.setDisable(false);

        });
        patronymic.setOnKeyReleased(e -> {
            if (lastName.getCharacters().length() == 0 || name.getCharacters().length() == 0 ||
                    patronymic.getCharacters().length() == 0)
                okButton.setDisable(true);
            else
                okButton.setDisable(false);

        });
        content.addRow(1, lastName, name, patronymic);
        clientDialog.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    try {
                        Statement statement = connect.createStatement();
                        String addRequest = String.format("INSERT INTO clients VALUES (%s, '%s', '%s', '%s')",
                                id, lastName.getCharacters().toString().trim(), name.getCharacters().toString().trim(),
                                patronymic.getCharacters().toString().trim());
                        statement.execute(addRequest);
                        setWorkPanel("clients");
                    } catch (Exception ex) {
                        showNoConnect();
                    }
                });

    }

    private void addStaffDialog() {
        Dialog<ButtonType> clientDialog = new Dialog<>();
        clientDialog.setOnCloseRequest(e -> clientDialog.close());
        clientDialog.setTitle("Добавить сотрудника");
        GridPane content = new GridPane();
        clientDialog.getDialogPane().setContent(content);
        content.addRow(0, new Label("Фамилия"), new Label("Имя"), new Label("Отчество"),
                new Label("Должность"));
        TextField lastName = new TextField();
        TextField name = new TextField();
        TextField patronymic = new TextField();
        TextField post = new TextField();
        clientDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        final Button okButton = (Button) clientDialog.getDialogPane().lookupButton(ButtonType.OK);
        if (lastName.getCharacters().length() == 0 || name.getCharacters().length() == 0 ||
                patronymic.getCharacters().length() == 0 || post.getCharacters().length() == 0)
            okButton.setDisable(true);
        else
            okButton.setDisable(false);
        lastName.setOnKeyReleased(e -> {
            if (lastName.getCharacters().length() == 0 || name.getCharacters().length() == 0 ||
                    patronymic.getCharacters().length() == 0 || post.getCharacters().length() == 0)
                okButton.setDisable(true);
            else
                okButton.setDisable(false);

        });
        name.setOnKeyReleased(e -> {
            if (lastName.getCharacters().length() == 0 || name.getCharacters().length() == 0 ||
                    patronymic.getCharacters().length() == 0 || post.getCharacters().length() == 0)
                okButton.setDisable(true);
            else
                okButton.setDisable(false);

        });
        patronymic.setOnKeyReleased(e -> {
            if (lastName.getCharacters().length() == 0 || name.getCharacters().length() == 0 ||
                    patronymic.getCharacters().length() == 0 || post.getCharacters().length() == 0)
                okButton.setDisable(true);
            else
                okButton.setDisable(false);

        });
        post.setOnKeyReleased(e -> {
            if (lastName.getCharacters().length() == 0 || name.getCharacters().length() == 0 ||
                    patronymic.getCharacters().length() == 0 || post.getCharacters().length() == 0)
                okButton.setDisable(true);
            else
                okButton.setDisable(false);

        });
        content.addRow(1, lastName , name, patronymic, post);
        clientDialog.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    try {
                        Statement statement = connect.createStatement();
                        String addRequest = String.format("INSERT INTO staff VALUES (null, '%s', '%s', '%s', '%s')",
                                lastName.getCharacters().toString().trim(), name.getCharacters().toString().trim(),
                                patronymic.getCharacters().toString().trim(), post.getCharacters().toString().trim());
                        statement.execute(addRequest);
                        setWorkPanel("staff");
                    } catch (Exception ex) {
                        showNoConnect();
                    }
                });

    }

    private TableView createTable(ResultSet queryResult) {
        TableView table = new TableView();
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        try {
            ResultSetMetaData meta = queryResult.getMetaData();
            int colCount = meta.getColumnCount();
            for (int i = 0; i < colCount; ++i) {
                TableColumn newCol = new TableColumn(meta.getColumnName(i + 1));
                newCol.setEditable(false);
                final int j = i;
                newCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>,
                        ObservableValue<String>>) param ->
                        new SimpleStringProperty(param.getValue().get(j).toString()));
                table.getColumns().add(newCol);
            }

            while (queryResult.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 0; i < colCount; ++i) {
                    row.add(queryResult.getString(i + 1));
                }
                data.add(row);
            }
            table.setItems(data);
        } catch (Exception e) {
            table = new TableView();
            data = FXCollections.observableArrayList();
            table.setItems(data);
            e.printStackTrace();
        }
        table.setPlaceholder(new Label("Данные отсутсвуют"));
        return table;

    }
}
