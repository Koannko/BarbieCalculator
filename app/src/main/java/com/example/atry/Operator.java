package com.example.atry;

public class Operator {

    public static String getOperatorSymbol(String lastOperator) {
        switch (lastOperator) {
            case "-":
                return "−";
            case "+":
                return "+";
            case "*":
                return "×";
            case "/":
                return "÷";
            default:
                return lastOperator;
        }
    }
}
