package ulamspiral;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Slider;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class UlamSpiral extends Application {
    static final int mainWidth = 1920;
    static final int mainHeight = 1000;
    private Canvas canvas;
    private final int primes[] = readPrimes();
    private final int maxStart = primes[primes.length - 1];

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane root = new GridPane();
        root.setPadding(new Insets(4));
        root.setVgap(2);
        primaryStage.setTitle("Ulam Spiral");
        Scene scene = new Scene(root, mainWidth, mainHeight);
        canvas = new Canvas(mainWidth, mainHeight - 60);
        root.add(canvas, 0, 0);
        root.add(makeControls(), 0, 1);
        primaryStage.setScene(scene);
        drawSpiral(1);
        primaryStage.show();
    }

    private void drawSpiral(int firstNum) {
        GraphicsContext field = canvas.getGraphicsContext2D();
        field.clearRect(0, 0, mainWidth, mainHeight);
        int nextPrime = 0;
        // Find first prime number equal or greater firstNum
        int res = Arrays.binarySearch(primes, firstNum);
        if (res >= 0)
            nextPrime = res;
        else
            nextPrime = res * (-1) - 1;

        Point center = new Point(mainWidth/2, mainHeight/2);
        Point position = new Point(center.x, center.y);
        int last = mainWidth * mainWidth + firstNum; // Last number
        int num = firstNum; // Current number
        int edge = 0; // Length of spiral edge for current step
        while (num <= last) {
            edge++;
            for (int i = 0; i < edge; i++) {
                if (num == primes[nextPrime]) {
                    field.fillRect(position.x, position.y, 1, 1);
                    if (nextPrime + 1 < primes.length)
                        nextPrime++;
                }
                num++;
                position.x++;
            }
            for (int i = 0; i < edge; i++) {
                if (num == primes[nextPrime]) {
                    field.fillRect(position.x, position.y, 1, 1);
                    if (nextPrime + 1 < primes.length)
                        nextPrime++;
                }
                num++;
                position.y--;
            }
            edge++;
            for (int i = 0; i < edge; i++) {
                if (num == primes[nextPrime]) {
                    field.fillRect(position.x, position.y, 1, 1);
                    if (nextPrime + 1 < primes.length)
                        nextPrime++;
                }
                num++;
                position.x--;
            }
            for (int i = 0; i < edge; i++) {
                if (num == primes[nextPrime]) {
                    field.fillRect(position.x, position.y, 1, 1);
                    if (nextPrime + 1 < primes.length)
                        nextPrime++;
                }
                num++;
                position.y++;
            }
        }
    }

    private GridPane makeControls() {
        GridPane controls = new GridPane();
        controls.setVgap(2);
        controls.setHgap(4);
        controls.setPadding(new Insets(0, 200, 0,200));
        controls.setPrefHeight(40);
        Label currentStart = new Label("First number: 1");
        controls.add(currentStart, 1, 0);
        Slider startChanger = new Slider(1, 400, 1);
        startChanger.setBlockIncrement(1);
        startChanger.valueProperty().addListener(observable -> {
            int newVal = (int) startChanger.getValue();
            currentStart.setText("First number: " + Integer.toString(newVal));
            drawSpiral(newVal);
        });
        Spinner<Integer> min = new Spinner<>(1, maxStart - 1, 1);
        min.setEditable(true);
        Spinner<Integer> max = new Spinner<>(2, maxStart, 400);
        max.setEditable(true);
        min.valueProperty().addListener(observable -> {
            int curMax = (int) startChanger.getMax();
            if (min.getValue() < curMax)
                startChanger.setMin(min.getValue());
            else
                min.getValueFactory().setValue(curMax - 1);

        });
        max.valueProperty().addListener(observable ->  {
            int curMin = (int) startChanger.getMin();
            if (max.getValue() > curMin)
                startChanger.setMax(max.getValue());
            else
                max.getValueFactory().setValue(curMin + 1);
        });

        controls.addRow(1, min, startChanger, max);
        controls.setHgrow(startChanger, Priority.ALWAYS);
        return controls;
    }

    private int[] readPrimes() {
        try {
            Scanner scanner = new Scanner(new File("res/primes"));
            LinkedList<Integer> listToRead = new LinkedList<>();
            while (scanner.hasNext()) {
                listToRead.add(scanner.nextInt());
            }
            return listToRead.stream().mapToInt(i -> i).toArray();
        } catch (FileNotFoundException e) {
            System.err.println("Primes base not found! (res/primes)");
            System.exit(1);
        }
        return null;
    }
}