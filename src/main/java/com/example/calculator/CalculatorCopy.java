package com.example.calculator;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import static javafx.scene.input.KeyCode.*;





public class CalculatorCopy extends Application {
    private final String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "."};
    private final String[] operations = {"+", "-", "*", "/", "sqrt", "^", "C", "="};
    HashMap<KeyCode, String> operationsDict = new HashMap<>() {{
                                                put(P, "+");
                                                put(MINUS, "-");
                                                put(EQUALS, "=");
                                                put(M, "*");
                                                put(R, "^");
                                                put(T, "sqrt");
                                                put(SLASH, "/");
    }};
    private final ArrayList<KeyCode> operationsKey = new ArrayList<>(List.of(P,M,R,T,MINUS,SLASH,EQUALS));

    @Override
    public void start(Stage stage) {

        TextField textField = new TextField();
        textField.setPrefHeight(75);
        textField.setDisable(true);
        textField.setStyle("-fx-font-size: 25px;");

        GridPane gridPane = new GridPane();
        gridPane.addRow(0, textField);

        for (int i = 0; i < 11; i++) {
            Button button = new Button(numbers[i]);
            button.setOnAction(new Handler(numbers[i], textField));
            button.setMinSize(75, 75);
            gridPane.add(button, i % 3, i / 3);
        }
        for (int i = 0; i < 8; i++) {
            Button button = new Button(operations[i]);
            button.setMinSize(75, 75);
            if (i < 4) {
                gridPane.add(button, 4, i);
                button.setOnAction(new Handler(operations[i], textField));

            } else {
                gridPane.add(button, 5, i - 4);
                button.setOnAction(new Handler(operations[i], textField));
            }
        }
        gridPane.setGridLinesVisible(true);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(textField);
        borderPane.setCenter(gridPane);
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.setMinHeight(415);
        stage.setMaxHeight(415);
        stage.setMinWidth(390);
        stage.setMaxWidth(390);
        stage.setTitle("Калькулятор");
        stage.addEventFilter(KeyEvent.KEY_PRESSED, new Key(textField));

        stage.show();

    }

    class Key implements EventHandler<KeyEvent> {
        private TextField textField;
        private static int counterOperation;
        private static double operand1;
        private static double operand2;
        private static KeyCode operator1 = null;
        private static KeyCode operator2 = null;
        private static boolean flagMinus = false;

        public Key(TextField textField) {
            this.textField = textField;
        }

        @Override
        public void handle(KeyEvent keyEvent) {

            String textField = this.textField.getText();
            Scanner inMain = new Scanner(textField);
            String regex = "[+\\-*/=^]|\\bsqrt\\b";
            inMain.useDelimiter(regex);

            if (operationsKey.contains(keyEvent.getCode()) && !this.textField.getText().isEmpty()) {
                if(this.textField.getText().toCharArray()[0] == '-'){
                    flagMinus = true;
                }
                if(operator1 == null){
                    operator1 = keyEvent.getCode();
                }

                switch (operator1) {
                    case P:
                        if (counterOperation == 0) {
                            counterOperation++;
                        } else if (counterOperation == 1) {
                            if(flagMinus){
                                operand1 = -Double.parseDouble(checkCommas(inMain.next()));
                            }else {
                                operand1 = Double.parseDouble(checkCommas(inMain.next()));
                            }
                            if (inMain.hasNext()) {
                                operand2 = Double.parseDouble(checkCommas(inMain.next()));
                            }
                            operator2 = keyEvent.getCode();

                            double resultPlus;
                            resultPlus = operand1 + operand2;
                            if (resultPlus>=0){
                                flagMinus = false;
                            }
                            assert operator2 != null;
                            if (operator2.equals(T)) {
                                operator1 = null;
                                this.textField.setText(String.valueOf(Math.sqrt(resultPlus)));
                                counterOperation = 0;
                                operator1 = null;
                                operand1 = Math.sqrt(resultPlus);
                            }
                            if (operator2.equals(EQUALS)) {
                                this.textField.setText(String.valueOf(resultPlus));
                                counterOperation = 0;
                                operator1 = null;
                                operand1 = resultPlus;
                            } else if(!operator2.equals(T)) {
                                this.textField.setText(String.valueOf(resultPlus));
                                operator1 = operator2;
                                operand1 = resultPlus;
                            }

                            operand2 = 0;
                            operator2 = null;
                        }
                        break;
                    case MINUS:
                        if (counterOperation == 0) {
                            counterOperation++;
                        } else if (counterOperation == 1) {
                            if(flagMinus){
                                operand1 = -Double.parseDouble(checkCommas(inMain.next()));
                            }else {
                                operand1 = Double.parseDouble(checkCommas(inMain.next()));
                            }
                            if (inMain.hasNext()) {
                                operand2 = Double.parseDouble(checkCommas(inMain.next()));
                            }
                            operator2 = keyEvent.getCode();
                            System.out.println(operator1);
                            System.out.println(operator2);
                            double resultMinus;
                            resultMinus = operand1 - operand2;
                            System.out.println(resultMinus);
                            if (resultMinus>=0){
                                flagMinus = false;
                            }
                            assert operator2 != null;
                            if (operator2.equals(T)) {
                                if(resultMinus<0){
                                    showErrorSqrt();
                                    this.textField.setText("");

                                }else {
                                    this.textField.setText(String.valueOf(Math.sqrt(resultMinus)));

                                }
                                operand1 = Math.sqrt(resultMinus);
                                counterOperation = 0;
                                operator1 = null;

                            }
                            if (operator2.equals(EQUALS)) {
                                this.textField.setText(String.valueOf(resultMinus));
                                counterOperation = 0;
                                operator1 = null;
                                operand1 = resultMinus;
                            } else if(!operator2.equals(T)){
                                this.textField.setText(String.valueOf(resultMinus) );
                                operator1 = operator2;
                                operand1 = resultMinus;
                            }

                            operand2 = 0;
                            operator2 = null;
                        }
                        break;
                    case SLASH:

                        if (counterOperation == 0) {
                            counterOperation++;
                        } else if (counterOperation == 1) {
                            if(flagMinus){
                                operand1 = -Double.parseDouble(checkCommas(inMain.next()));
                            }else {
                                operand1 = Double.parseDouble(checkCommas(inMain.next()));
                            }
                            if (inMain.hasNext()) {
                                operand2 = Double.parseDouble(checkCommas(inMain.next()));
                            }
                            if (operand2 == 0) {
                                showErrorDivisionZero();
                                this.textField.setText("");
                                operand1 = 0;
                                operand2 = 0;
                                counterOperation = 0;
                                operator1 = null;
                                break;
                            }
                            operator2 = keyEvent.getCode();
                            double resultDiv;
                            resultDiv = operand1 / operand2;
                            if (resultDiv>=0){
                                flagMinus = false;
                            }
                            assert operator2 != null;
                            if (operator2.equals(T)) {
                                operator1 = null;
                                this.textField.setText(String.valueOf(Math.sqrt(resultDiv)));
                                counterOperation = 0;
                                operator1 = null;
                                operand1 = Math.sqrt(resultDiv);
                            }
                            if (operator2.equals(EQUALS)) {
                                this.textField.setText(String.valueOf(resultDiv));
                                counterOperation = 0;
                                operator1 = null;
                                operand1 = resultDiv;
                            } else if(!operator2.equals(T)){
                                this.textField.setText(resultDiv + String.valueOf(operationsDict.get(operator2)));
                                operator1 = operator2;
                                operand1 = resultDiv;
                            }
                            operand2 = 0;
                            operator2 = null;
                        }
                        break;
                    case M:
                        if (counterOperation == 0) {
                            counterOperation++;
                        } else if (counterOperation == 1) {
                            if(flagMinus){
                                operand1 = -Double.parseDouble(checkCommas(inMain.next()));
                            }else {
                                operand1 = Double.parseDouble(checkCommas(inMain.next()));
                            }
                            if (inMain.hasNext()) {
                                operand2 = Double.parseDouble(checkCommas(inMain.next()));
                            }
                            operator2 = keyEvent.getCode();
                            double resultMulti;
                            resultMulti = operand1 * operand2;
                            if (resultMulti>=0){
                                flagMinus = false;
                            }
                            assert operator2 != null;
                            if (operator2.equals(T)) {
                                operator1 = null;
                                this.textField.setText(String.valueOf(Math.sqrt(resultMulti)));
                                counterOperation = 0;
                                operator1 = null;
                                operand1 = Math.sqrt(resultMulti);
                            }
                            if (operator2.equals(EQUALS)) {
                                this.textField.setText(String.valueOf(resultMulti));
                                counterOperation = 0;
                                operator1 = null;
                                operand1 = resultMulti;

                            } else if(!operator2.equals(T)){
                                this.textField.setText(resultMulti + String.valueOf(operationsDict.get(operator2)));
                                operator1 = operator2;
                                operand1 = resultMulti;
                            }


                            operand2 = 0;

                            operator2 = null;
                        }
                        break;
                    case EQUALS:
                        operand1 = Double.parseDouble(checkCommas(inMain.next()));
                        this.textField.setText(String.valueOf(operand1));
                        counterOperation = 0;
                        operator1 = null;
                        operator2 = null;
                        break;
                    case T:
                        if (counterOperation==0) {
                            if(flagMinus){
                                operand1 = -Double.parseDouble(checkCommas(inMain.next()));
                                showErrorSqrt();
                                this.textField.setText("");
                            }else {
                                operand1 = Double.parseDouble(checkCommas(inMain.next()));
                            }
                            double resultSqrt;
                            resultSqrt = Math.sqrt(operand1);
                            if(resultSqrt>0){
                                this.textField.setText(String.valueOf(resultSqrt));
                                operand1 = resultSqrt;
                            }else {
                                this.textField.setText("");
                                operand1 = 0;
                            }

                            flagMinus = false;
                            operand2 = 0;
                            operator1 = null;
                            counterOperation = 0;

                        }
                        break;

                    case R:
                        if (counterOperation == 0) {
                            counterOperation++;
                        } else if (counterOperation == 1) {
                            if(flagMinus){
                                operand1 = -Double.parseDouble(checkCommas(inMain.next()));
                            }else {
                                operand1 = Double.parseDouble(checkCommas(inMain.next()));
                            }
                            if (inMain.hasNext()) {
                                operand2 = Double.parseDouble(checkCommas(inMain.next()));
                            }
                            operator2 = keyEvent.getCode();
                            if(operand2<1){
                                showErrorSqrt();
                                this.textField.setText("");
                                flagMinus = false;
                                operand1 = 0;
                                operator1 = null;
                                counterOperation = 0;
                            }else {
                            double resultSqr;
                            resultSqr = Math.pow(operand1, operand2);
                            if (resultSqr>=0){
                                flagMinus = false;
                            }
                            assert operator2 != null;
                            if (operator2.equals(T)) {
                                operator1 = null;
                                this.textField.setText(String.valueOf(Math.sqrt(resultSqr)));
                                counterOperation = 0;
                                operator1 = null;
                                operand1 = Math.sqrt(resultSqr);
                            }
                            if (operator2.equals(EQUALS)) {
                                this.textField.setText(String.valueOf(resultSqr));
                                counterOperation = 0;
                                operator1 = null;
                                operand1 = resultSqr;
                            } else if(!operator2.equals(T)){
                                this.textField.setText(resultSqr + String.valueOf(operationsDict.get(operator2)));
                                operator1 = operator2;
                                operand1 = resultSqr;
                            }}

                            operand2 = 0;
                            operator2 = null;
                        }
                        break;
                    case null:
                        break;
                    default:
                        break;

                }
            }

                switch (keyEvent.getCode()) {
                    case DIGIT0, DIGIT1, DIGIT2, DIGIT3, DIGIT4, DIGIT5, DIGIT6, DIGIT7, DIGIT8, DIGIT9, PERIOD:
                        this.textField.setText(this.textField.getText() + keyEvent.getText());
                        break;
                    case P,M,R,MINUS,SLASH:

                        this.textField.setText(this.textField.getText() + operationsDict.get(keyEvent.getCode()));
                        break;
                    case T:
                        this.textField.setText(this.textField.getText());
                        break;
                    case EQUALS:
                        break;

                    case BACK_SPACE://Стереть
                        if (!this.textField.getText().isEmpty()) {
                            this.textField.setText(this.textField.getText().substring(0, this.textField.getText().length() - 1));
                        }else {
                            operand1 = 0;
                            operand2 = 0;
                            operator1 = null;
                            operator2 = null;
                        }
                        break;
                    case null:
                        break;
                    default:
                        System.out.println(keyEvent.getCode());
                        showErrorUnknownSymbol();
                        break;

                }
        }
    }


        class Handler implements EventHandler<ActionEvent> {
            private TextField textField;
            private String symbol;
            private static int counterOperation;
            private static double operand1 = 0;
            private static double operand2;
            private static OPERATORS operator1 = null;
            private static OPERATORS operator2 = null;
            private static String lastSymbol;
            private static boolean flagMinus = false;


            public Handler(String symbol, TextField textField) {
                this.textField = textField;
                this.symbol = symbol;

            }

            @Override
            public void handle(ActionEvent actionEvent) {

                String textField = this.textField.getText();
                lastSymbol = this.symbol;
                if (!this.symbol.equals("C")) {
                    if ("0".equals(this.textField.getText())) {
                        this.textField.setText(((Button) actionEvent.getSource()).getText());
                    } else if (!this.symbol.equals("[+\\-*/=^]|\\bsqrt\\b")) {
                        this.textField.setText(this.textField.getText() + this.symbol);
                    } else if (lastSymbol.equals("[+\\-*/=^]|\\bsqrt\\b")) {
                        this.textField.setText(textField.substring(0, textField.length() - 1) + this.symbol);
                    }
                } else {
                    this.textField.setText("0");
                    counterOperation = 0;
                }
                Scanner inMain = new Scanner(textField);
                String regex = "[+\\-*/=^]|\\bsqrt\\b";
                inMain.useDelimiter(regex);
                boolean isMatch = Pattern.matches(regex, this.symbol);
//(?<!^)-
                if(this.textField.getText().toCharArray()[0] == '-'){
                    flagMinus = true;
                }
                if ( isMatch && this.textField.getText().length()>1 ) {
                    if (counterOperation == 0) {
                            operator1 = OPERATORS.fromCode(this.symbol);
                    }
                    assert operator1 != null;
                    switch (operator1) {

                        case PLUS:
                            System.out.println("plus");
                            if (counterOperation == 0) {
                                counterOperation++;
                            } else if (counterOperation == 1) {
                                if(flagMinus){
                                    operand1 = -Double.parseDouble(checkCommas(inMain.next()));
                                }else {
                                    operand1 = Double.parseDouble(checkCommas(inMain.next()));
                                }
                                if (inMain.hasNext()) {
                                    operand2 = Double.parseDouble(checkCommas(inMain.next()));
                                }


                                operator2 = OPERATORS.fromCode(this.symbol);

                                assert operator2 != null;
                                double resultPlus;
                                resultPlus = operand1 + operand2;
                                if (resultPlus>=0){
                                    flagMinus = false;
                                }
                                if (operator2.equals(OPERATORS.SQRT)) {
                                    operator1 = null;
                                    this.textField.setText(String.valueOf(Math.sqrt(resultPlus)));
                                    counterOperation = 0;
                                }


                                if (operator2.equals(OPERATORS.EQUALS)) {
                                    this.textField.setText(String.valueOf(resultPlus));
                                    counterOperation = 0;
                                } else if(!operator2.equals(OPERATORS.SQRT)){
                                    this.textField.setText(resultPlus + operator2.getCode());
                                }
                                operand1 = resultPlus;
                                operand2 = 0;
                                operator1 = operator2;
                                operator2 = null;
                            }
                            break;


                        case MINUS:
                            System.out.println("minus");
                            if (counterOperation == 0) {
                                counterOperation++;

                            } else if (counterOperation == 1) {

                                if(flagMinus){
                                    operand1 = -Double.parseDouble(checkCommas(inMain.next()));
                                }else {
                                    operand1 = Double.parseDouble(checkCommas(inMain.next()));
                                }
                                if (inMain.hasNext()) {
                                    operand2 = Double.parseDouble(checkCommas(inMain.next()));
                                }
                                operator2 = OPERATORS.fromCode(this.symbol);
                                double resultMinus;
                                resultMinus = operand1 - operand2;
                                if (resultMinus>=0){
                                    flagMinus = false;
                                }

                                assert operator2 != null;
                                if (operator2.equals(OPERATORS.SQRT)) {

                                    if(resultMinus<0){
                                        showErrorSqrt();
                                        this.textField.setText("");
                                    }else {
                                        this.textField.setText(String.valueOf(Math.sqrt(resultMinus)));
                                    }
                                    operator1 = null;
                                    counterOperation = 0;
                                }
                                if (operator2.equals(OPERATORS.EQUALS)) {
                                    this.textField.setText(String.valueOf(resultMinus));
                                    counterOperation = 0;
                                } else if(!operator2.equals(OPERATORS.SQRT)){
                                    this.textField.setText(resultMinus + operator2.getCode());
                                }
                                operand1 = resultMinus;
                                System.out.println(operand1+" operand 1");
                                operand2 = 0;
                                operator1 = operator2;
                                operator2 = null;
                            }

                            break;

                        case EQUALS:
                            operand1 = Double.parseDouble(checkCommas(inMain.next()));
                            System.out.println(operand1);
                            this.textField.setText(String.valueOf(operand1));
                            counterOperation = 0;
                            break;

                        case DIVISION:
                            if (counterOperation == 0) {
                                counterOperation++;
                            } else if (counterOperation == 1) {
                                if(flagMinus){
                                    operand1 = -Double.parseDouble(checkCommas(inMain.next()));
                                }else {
                                    operand1 = Double.parseDouble(checkCommas(inMain.next()));
                                }
                                if (inMain.hasNext()) {
                                    operand2 = Double.parseDouble(checkCommas(inMain.next()));
                                }
                                if (operand2 == 0) {
                                    showErrorDivisionZero();
                                    this.textField.setText("");
                                    operand1 = 0;
                                    operand2 = 0;
                                    counterOperation = 0;
                                    break;
                                }
                                operator2 = OPERATORS.fromCode(this.symbol);
                                double resultDiv;
                                resultDiv = operand1 / operand2;
                                if (resultDiv>=0){
                                    flagMinus = false;
                                }
                                assert operator2 != null;

                                if (operator2.equals(OPERATORS.SQRT)) {
                                    operator1 = null;
                                    this.textField.setText(String.valueOf(Math.sqrt(resultDiv)));
                                    counterOperation = 0;
                                }

                                if (operator2.equals(OPERATORS.EQUALS)) {
                                    this.textField.setText(String.valueOf(resultDiv));
                                    counterOperation = 0;
                                } else if(!operator2.equals(OPERATORS.SQRT)){
                                    this.textField.setText(resultDiv + operator2.getCode());
                                }

                                operand1 = resultDiv;
                                operand2 = 0;
                                operator1 = operator2;
                                operator2 = null;
                            }
                            break;

                        case MULTIPLICATION:
                            if (counterOperation == 0) {
                                counterOperation++;
                            } else if (counterOperation == 1) {
                                if(flagMinus){
                                    operand1 = -Double.parseDouble(checkCommas(inMain.next()));
                                }else {
                                    operand1 = Double.parseDouble(checkCommas(inMain.next()));
                                }
                                if (inMain.hasNext()) {
                                    operand2 = Double.parseDouble(checkCommas(inMain.next()));
                                }
                                operator2 = OPERATORS.fromCode(this.symbol);
                                double resultMulti;
                                resultMulti = operand1 * operand2;
                                if (resultMulti>=0){
                                    flagMinus = false;
                                }
                                assert operator2 != null;
                                if (operator2.equals(OPERATORS.SQRT)) {
                                    operator1 = null;
                                    this.textField.setText(String.valueOf(Math.sqrt(resultMulti)));
                                    counterOperation = 0;
                                }
                                if (operator2.equals(OPERATORS.EQUALS)) {
                                    this.textField.setText(String.valueOf(resultMulti));
                                    counterOperation = 0;
                                } else if(!operator2.equals(OPERATORS.SQRT)){
                                    this.textField.setText(resultMulti + operator2.getCode());
                                }

                                operand1 = resultMulti;
                                operand2 = 0;
                                operator1 = operator2;
                                operator2 = null;
                            }
                            break;

                        case SQRT:
                            if(counterOperation==0) {

                                if(flagMinus){
                                    operand1 = -Double.parseDouble(checkCommas(inMain.next()));
                                }else {
                                    operand1 = Double.parseDouble(checkCommas(inMain.next()));
                                }

                                if (operand1<0){
                                    showErrorSqrt();
                                    operator1 = null;
                                    this.textField.setText("");
                                    flagMinus = false;
                                    break;
                                }else {
                                    double resultSqrt;
                                    resultSqrt = Math.sqrt(operand1);
                                    this.textField.setText(String.valueOf(resultSqrt));
                                    operand1 = resultSqrt;
                                    operand2 = 0;
                                    operator1 = null;

                                }

                            }
                            break;

                        case SQR:
                            if (counterOperation == 0) {
                                counterOperation++;
                            } else if (counterOperation == 1) {
                                if(flagMinus){
                                    operand1 = -Double.parseDouble(checkCommas(inMain.next()));
                                }else {
                                    operand1 = Double.parseDouble(checkCommas(inMain.next()));
                                }
                                if (inMain.hasNext()) {
                                    operand2 = Double.parseDouble(checkCommas(inMain.next()));
                                }
                                operator2 = OPERATORS.fromCode(this.symbol);
                                double resultSqr;
                                resultSqr = Math.pow(operand1, operand2);
                                assert operator2 != null;
                                if (resultSqr>=0){
                                    flagMinus = false;
                                }
                                if (operator2.equals(OPERATORS.SQRT)) {
                                    operator1 = null;
                                    this.textField.setText(String.valueOf(Math.sqrt(resultSqr)));
                                    counterOperation = 0;
                                }
                                if (operator2.equals(OPERATORS.EQUALS)) {
                                    this.textField.setText(String.valueOf(resultSqr));
                                    counterOperation = 0;
                                } else if(!operator2.equals(OPERATORS.SQRT)){
                                    this.textField.setText(resultSqr + operator2.getCode());
                                }
                                operand1 = resultSqr;
                                operand2 = 0;

                                operator1 = operator2;
                                operator2 = null;
                            }
                            break;

                        case null, default:

                            break;
                    }
                }
            }
        }
        private String checkCommas(String str) {
            System.out.println(str);
            char targetChar = '.';
            int index = 0;
            int count = 0;
            char[] strStr = str.toCharArray();
            for (int i = 0; i < strStr.length; i++) {
                if (targetChar==strStr[i]){
                    index = i;
                    break;
                }
            }
            for (int i = 0; i < str.length(); i++) {
                if (count > 1) {
                    break;
                }
                if (str.charAt(i) == targetChar) {
                    count++;
                }
            }
            if (count >= 2) {
                str = str.replaceAll("\\.","");
                str = str.substring(0, index) + targetChar + str.substring(index);
            }
            return str;
        }
        private void showErrorDivisionZero() {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка деления");
            alert.setHeaderText(null);
            alert.setContentText("Делить на 0 нельзя. Че математику не учил?");
            alert.showAndWait();
        }

        private void showErrorUnknownSymbol() {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка ввода");
            alert.setHeaderText(null);
            alert.setContentText("Неизвестный символ");
            alert.showAndWait();
        }
    private void showErrorSqrt() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка корня");
        alert.setHeaderText(null);
        alert.setContentText("Нельзя извлечь корень из отрицательного числа");
        alert.showAndWait();
    }
    }
