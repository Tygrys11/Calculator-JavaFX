package com.example.calculator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private Label resultLabel;

    @FXML
    private ImageView btnClose, btnMinimum;

    @FXML
    private Pane root1;

    private boolean lastButtonWasNumber = false; //odpowiada za śledzenie informacji o tym, czy ostatnio naciśnięty przycisk był cyfrą/liczba czy nie
    //używana w logice obsługi przycisków, aby określić, jak traktować kolejne naciśnięcia, czyli czy dołączać cyfry do aktualnego wejścia czy zaczynać nowe wejście od cyfry.

    private StringBuilder currentInput = new StringBuilder(); //jest używana do przechowywania aktualnie wprowadzanych danych (liczb, operatorów, itp.)
    private String previousValue = ""; //pełni rolę przechowywania wartości liczbowej lub wyniku poprzedniej operacji w kalkulatorze
    private String currentOperator = ""; //pełni rolę przechowywania aktualnie wybranego operatora
    private double x, y; //uzywane do przechowywania dwóch liczb, na których sa przeprowadzane operacje matematyczne

    @FXML
    private ToggleButton toggleButtonOnOff;

    @FXML
    private Button btnMod, btnSin, btnCos, btnTan, btn1, btn2, btn3, btn0, btn4, btn5, btn6, btn7, btn8, btn9, btnLog, btnPlus, btnMinus, btnPierw, btnEquels, btndziel, btnKropka, btnDeleteOne, BtnDeleteAll, btnPow, BtnPlusMinus, btnmnoz;

    public Controller() {
    }

    public void init(Stage stage) {
        root1.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root1.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });

        btnMinimum.setOnMouseClicked(event -> stage.setIconified(true)); //gdy użytkownik kliknie guzik zostanie wywołana akcja zminimalizowania okna
        btnClose.setOnMouseClicked(event -> stage.close()); //gdy użytkownik kliknie guzik zostanie wywolana akcja zamknięcia okna/zatrzymanie programu
    }

    @FXML
    public void handleNumber(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        if (lastButtonWasNumber || currentOperator.isEmpty()) {
            // Jeśli poprzednią akcją był numer lub aktualny operator jest pusty,
            // dołącza cyfrę do bieżącego wejścia.
            currentInput.append(clickedButton.getText());
            resultLabel.setText(resultLabel.getText() + clickedButton.getText());
        } else {
            // Jeśli poprzednią akcją był operator,
            // zaczyna nowe wejście z aktualnie naciśniętą cyfrą.
            resultLabel.setText(clickedButton.getText());
            currentInput.setLength(0);
            currentInput.append(clickedButton.getText());
        }

        lastButtonWasNumber = true;
    }

    @FXML
    public void handleDot(ActionEvent event) {
        if (!currentInput.toString().contains(".")) {
            // Dodaje kropkę tylko jeśli jej jeszcze nie ma w bieżącym wejściu.
            currentInput.append(".");
            resultLabel.setText(resultLabel.getText() + ".");
            lastButtonWasNumber = true;
        }
    }

    @FXML
    public void handleOperator(ActionEvent event) {
        if (lastButtonWasNumber && !currentInput.toString().isEmpty()) //Sprawdza, czy poprzednią akcją było wprowadzenie liczby i czy bieżące wejście nie jest puste
             {
            performOperation();
            lastButtonWasNumber = false;
        }

        Button operatorButton = (Button) event.getSource();
        if (!resultLabel.getText().isEmpty()) {
            // Sprawdza, czy jest już liczba przed dodaniem operatora
            if (!Character.isDigit(resultLabel.getText().charAt(resultLabel.getText().length() - 1))) {
                // Jeśli nie ma liczby, zignoruje dodanie operatora
                return;
            }
        }
        currentOperator = operatorButton.getText();
        resultLabel.setText(resultLabel.getText() + " " + currentOperator + " ");
    }

    @FXML
    public void handleEquals(ActionEvent event) {
        if (!currentInput.toString().isEmpty()) {
            performOperation();
            lastButtonWasNumber = true;
            currentOperator = "";
        }
    }

    @FXML
    public void handleClear(ActionEvent event) {
        resultLabel.setText("");
        lastButtonWasNumber = false;
        currentInput.setLength(0);
        previousValue = "";
        currentOperator = "";
    }

    @FXML
    public void handleSquareRoot(ActionEvent event) {
        handleUnaryOperation("sqrt");
    }//metoda kierujaca za pomoca zdarzenia event do metody handleUnaryOperation, w ktorej wykonuje obliczenie pierwiastka drugiego stopnia, zwracajac do pola wynik

    @FXML
    public void handleSin(ActionEvent event) {
        handleUnaryOperation("sin");
    }//metoda kierujaca za pomoca zdarzenia event do metody handleUnaryOperation, w ktorej wykonuje obliczenie sinusa, zwracajac do pola wynik

    @FXML
    public void handleCos(ActionEvent event) {
        handleUnaryOperation("cos");
    } //metoda kierujaca za pomoca zdarzenia event do metody handleUnaryOperation, w ktorej wykonuje obliczenie cosinusa, zwracajac do pola wynik

    @FXML
    public void handleTan(ActionEvent event) {
        handleUnaryOperation("tan");
    }//metoda kierujaca za pomoca zdarzenia event do metody handleUnaryOperation, w ktorej wykonuje obliczenie tangensa, zwracajac do pola wynik

    @FXML
    public void handleLogarithm(ActionEvent event) {
        handleUnaryOperation("log");
    }//metoda kierujaca za pomoca zdarzenia event do metody handleUnaryOperation, w ktorej wykonuje obliczenie logarytmu o podstawie 10, zwracajac do pola wynik

    public void handlePower(ActionEvent event) {
        handleUnaryOperation("pow");
    }//metoda kierujaca za pomoca zdarzenia event do metody handleUnaryOperation, w ktorej wykonuje obliczenie pow(liczba_odczytana,2), zwracajac do pola wynik

    @FXML
    public void handleDeleteOne(ActionEvent event) {
        if (currentInput.length() > 0) {
            currentInput.deleteCharAt(currentInput.length() - 1);
            updateResultLabel();
        }
    }

    private void updateResultLabel() {
        if (currentInput.length() > 0) {
            resultLabel.setText(currentInput.toString());
        } else {
            resultLabel.setText("");
            lastButtonWasNumber = false;
        }
    }

    @FXML
    public void initialize() {
        resultLabel.setText("Off");
        disableButtons(true);
        toggleButtonOnOff.setSelected(false);
    }

    @FXML
    public void handleToggleOnOff(ActionEvent event) {
        boolean isOn = toggleButtonOnOff.isSelected();

        if (!isOn) {
            resultLabel.setText("Off");
            disableButtons(true);
        } else {
            resultLabel.setText("");
            disableButtons(false);
        }

        lastButtonWasNumber = false;
        currentInput.setLength(0);
        previousValue = "";
        currentOperator = "";
    }

    private void disableButtons(boolean disable) {
        btnMod.setDisable(disable);
        btnSin.setDisable(disable);
        btnCos.setDisable(disable);
        btnTan.setDisable(disable);
        btn0.setDisable(disable);
        btn1.setDisable(disable);
        btn2.setDisable(disable);
        btn3.setDisable(disable);
        btn4.setDisable(disable);
        btn5.setDisable(disable);
        btn6.setDisable(disable);
        btn7.setDisable(disable);
        btn8.setDisable(disable);
        btn9.setDisable(disable);
        btnPlus.setDisable(disable);
        btnMinus.setDisable(disable);
        btndziel.setDisable(disable);
        btnmnoz.setDisable(disable);
        btnKropka.setDisable(disable);
        btnLog.setDisable(disable);
        btnEquels.setDisable(disable);
        btnPierw.setDisable(disable);
        BtnPlusMinus.setDisable(disable);
        BtnDeleteAll.setDisable(disable);
        btnDeleteOne.setDisable(disable);
        btnPow.setDisable(disable);

        // ToggleButtonOnOff nie jest blokowany/odblokowywany
        toggleButtonOnOff.setDisable(false);
    }

    @FXML
    public void handlePlusMinus(ActionEvent event) {
        if (!resultLabel.getText().isEmpty()) {
            double value = Double.parseDouble(resultLabel.getText());
            value = -value;
            resultLabel.setText(String.valueOf(value));
            currentInput.setLength(0);
            currentInput.append(value);
            previousValue = String.valueOf(value);
            lastButtonWasNumber = true;
        }
    }


    private void performOperation() {
        if (!previousValue.isEmpty()) {
            performBinaryOperation(currentOperator);
        } else {
            previousValue = currentInput.toString();
        }
    }
    private void performBinaryOperation(String operator) {
        //Parsowanie liczb na zmiennoprzecinkowe
        x = Double.parseDouble(previousValue);
        y = Double.parseDouble(currentInput.toString());

        switch (operator) {
            case "+":
                x = x + y;
                break;
            case "-":
                x = x - y;
                break;
            case "x":
                x = x * y;
                break;
            case "÷":
                if (y != 0) {
                    x = x / y;
                } else {
                    resultLabel.setText("Error");
                    return;
                }
                break;
            case "%":
                x = x % y;
                break;
            default:
                break;
        }

        resultLabel.setText(String.valueOf(x));
        currentInput.setLength(0);
        currentInput.append(x);
        previousValue = String.valueOf(x);
    }

    private void handleUnaryOperation(String operation) {
        if (!currentInput.toString().isEmpty()) {
            try {
                double value = Double.parseDouble(currentInput.toString());
                double result = 0;

                switch (operation) {
                    case "sqrt":
                        result = Math.sqrt(value);
                        break;
                    case "sin":
                        result = Math.sin(Math.toRadians(value));
                        break;
                    case "cos":
                        result = Math.cos(Math.toRadians(value));
                        break;
                    case "tan":
                        result = Math.tan(Math.toRadians(value));
                        break;
                    case "log":
                        result = Math.log10(value);
                        break;
                    case "pow":
                        result = Math.pow(value,2);
                    default:
                        break;
                }

                resultLabel.setText(String.valueOf(result));
                currentInput.setLength(0);
                currentInput.append(result);
            } catch (NumberFormatException e) {
                resultLabel.setText("Error");
            }
        }
    }
}
