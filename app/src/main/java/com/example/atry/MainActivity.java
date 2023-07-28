package com.example.atry;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    TextView resultField; // текстовое поле для вывода результата
    EditText numberField;   // поле для ввода числа
    TextView operationField;    // текстовое поле для вывода знака операции
    Double operand = null;  // операнд операции
    String lastOperation = "="; // последняя операция
    ArrayList<String> lastOperand = new ArrayList<String>();
    private ImageButton heartButton;
    private ImageView heartImage;
    public ArrayList<String> expression = new ArrayList<String>();
    int operandCount = 0;
    int operatorCount = 0;
    public String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // получаем все поля по id из activity_main.xml
        resultField = findViewById(R.id.resultField);
        numberField = findViewById(R.id.numberField);
        heartButton = findViewById(R.id.btn_heart);
        heartImage = findViewById(R.id.ic_heart);
    }
    // сохранение состояния
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("OPERATION", lastOperation);
        if(operand!=null)
            outState.putDouble("OPERAND", operand);
        super.onSaveInstanceState(outState);
    }
    // получение ранее сохраненного состояния
    @SuppressLint("SetTextI18n")
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastOperation = savedInstanceState.getString("OPERATION");
        operand= savedInstanceState.getDouble("OPERAND");
        System.out.println(Integer.getInteger(result));
        System.out.println(Double.valueOf(result));
        resultField.setText(result);
    }
    // обработка нажатия на числовую кнопку
    public void onNumberClick(View view){

        Button button = (Button)view;
        numberField.append(button.getText());

        if(lastOperation.equals("=") && operand!=null){
            operand = null;
        }
        lastOperand.add((String) button.getText());
        if (!expression.isEmpty() && isNumber(expression.get(expression.size() - 1))){
            expression.remove(expression.size() - 1);
            expression.add(String.join("", lastOperand));
            evaluateExpression();
        } else {
            expression.add(String.join("", lastOperand));
        }
    }
    public void evaluateExpression() {
        Stack<Double> stack = new Stack<>();
        String op = "";
        for (String token : expression) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            }
            if (stack.size() == 2) {
                double operand2 = stack.pop();
                double operand1 = stack.pop();

                switch (op) {
                    case "+":
                        stack.push(operand1 + operand2);
                        break;
                    case "-":
                        stack.push(operand1 - operand2);
                        break;
                    case "*":
                        stack.push(operand1 * operand2);
                        break;
                    case "/":
                        stack.push(operand1 / operand2);
                        break;
                }
            } else {
                op = token;
            }
            System.out.println(expression.toString());
            System.out.println(stack);
        }

        result = stack.pop().toString();
    }
    private static boolean isNumber(String str) {
        try {
            double v = Double.parseDouble(str);
            return true;
        } catch (NumberFormatException nfe) {
        }
        return false;
    }
    // обработка нажатия на кнопку операции
    public void onPlusClick(View view) {
        lastOperation = "+";
        if (lastOperand.isEmpty()) {
            expression.remove(expression.size() - 1);
        } else if (!isRepeatOperator(lastOperation)) {
            onOperatorClick();
        }
    }

    public void onMinusClick(View view) {
        lastOperation = "-";
        if (lastOperand.isEmpty()) {
            expression.remove(expression.size() - 1);
        } else if (!isRepeatOperator(lastOperation)) {
            onOperatorClick();
        }
    }
    public void onMultiplyClick(View view) {
        lastOperation = "*";
        if (lastOperand.isEmpty()) {
            expression.remove(expression.size() - 1);
        } else if (!isRepeatOperator(lastOperation)) {
            onOperatorClick();
        }
    }
    public void onDivideClick(View view) {
        lastOperation = "/";
        if (lastOperand.isEmpty()) {
            expression.remove(expression.size() - 1);
        } else if (!isRepeatOperator(lastOperation)) {
            onOperatorClick();
        }
    }
    public void onEqualClick(View view) {
        lastOperation = "=";
        evaluateExpression();
        if (Double.valueOf(result) % 1.0 < 0.000001) {
            result = result.substring(0, result.length() - 2);
        }
        resultField.setText(result);
    }

    private void onOperatorClick() {
        String currentText = numberField.getText().toString();
        numberField.setText(currentText + Operator.getOperatorSymbol(lastOperation));
        operatorCount++;
        operandCount++;
        addStringToExpression(lastOperation);
        lastOperand.clear();
    }
    private void addStringToExpression(String str) {
        expression.add(str);
    }

    private boolean isRepeatOperator(String lastOperation) {
        if (!expression.isEmpty() && lastOperation.equals(expression.get(expression.size() - 1))) {
            return true;
        }
        return false;
    }

    public void onClearClick(View view) {
        resultField = findViewById(R.id.resultField);
        numberField = findViewById(R.id.numberField);
        resultField.setText("");
        numberField.setText("");
        operand = null;
        lastOperation = "=";
        lastOperand.clear();
        operandCount = 0;
        operatorCount = 0;
        expression.clear();
    }

    public void onBackspaceClick(View view) {
        lastOperation = "=";
        int expSize = expression.size();
        if (expSize != 0) {
            int lastElemLength = expression.get(expSize - 1).length();
            String lastElem = expression.get(expSize - 1);
            if (lastElemLength == 1) {
                if (isNumber(lastElem)) {
                    operandCount--;
                } else {
                    operatorCount--;
                }
                expression.remove(expSize - 1);
            } else {
                lastElem = lastElem.substring(0, lastElemLength - 1);
                expression.remove(expSize - 1);
                expression.add(lastElem);
            }
        }
    }

    public void onHeartClick(View view) {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.heart_fly_up);

        // Применение анимации к картинке
        heartImage.startAnimation(anim);
    }
}