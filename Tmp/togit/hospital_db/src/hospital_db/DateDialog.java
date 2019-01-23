package hospital_db;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateDialog {
    private LocalDate from;
    private LocalDate to;
    private boolean ok = false;

    public DateDialog() {}

    public Pair<LocalDate, LocalDate> showAndWait() {
        Dialog<ButtonType> dateDialog = new Dialog<>();
        dateDialog.setOnCloseRequest(e -> dateDialog.close());
        dateDialog.setTitle("Выбор даты");
        dateDialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        HBox content = new HBox();
        dateDialog.getDialogPane().setContent(content);
        DatePicker fromDP = new DatePicker();
        DatePicker toDP = new DatePicker();
        fromDP.setValue(LocalDate.now().minusMonths(1));
        toDP.setValue(LocalDate.now());
        Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item.isBefore(
                                        fromDP.getValue())
                                        ) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        };
                    }
                };
        toDP.setDayCellFactory(dayCellFactory);
        toDP.setConverter(new StringConverter<LocalDate>()
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
        fromDP.setConverter(new StringConverter<LocalDate>()
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
        content.getChildren().addAll(new Label(" С "), fromDP, new Label(" По "), toDP);
        dateDialog.setOnCloseRequest(e -> dateDialog.close());
        dateDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dateDialog.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    from = fromDP.getValue();
                    to = toDP.getValue().plusDays(1);
                    ok = true;
                });
        return new Pair<>(from, to);
    }

    public boolean isOk() {
        return ok;
    }
}
