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
    String lastOperation = "="; // последняя операция
    ArrayList<String> lastOperand = new ArrayList<String>();
    private ImageView heartImage;
    public ArrayList<String> expression = new ArrayList<String>();
    int operandCount = 0;
    int operatorCount = 0;
    public String result = "";
    private ArrayList<String> key = new ArrayList<>();
    Bundle state = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // получаем все поля по id из activity_main.xml
        resultField = findViewById(R.id.resultField);
        numberField = findViewById(R.id.numberField);
        heartImage = findViewById(R.id.ic_heart);
        key.add("628");
        key.add("-");
        key.add("260");
    }
    // сохранение состояния
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    // получение ранее сохраненного состояния
    @SuppressLint("SetTextI18n")
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        resultField = findViewById(R.id.resultField);
        numberField = findViewById(R.id.numberField);
        heartImage = findViewById(R.id.ic_heart);
        lastOperation = savedInstanceState.getString("OPERATION");
        lastOperand = savedInstanceState.getStringArrayList("LAST_OPERAND");
        resultField.setText(savedInstanceState.getString("RESULT"));
        numberField.setText(savedInstanceState.getString("NUMBER"));
        expression = savedInstanceState.getStringArrayList("EXPRESSION");
        operandCount = savedInstanceState.getInt("OPERAND_COUNT");
        operatorCount = savedInstanceState.getInt("OPERATOR_COUNT");
        result = savedInstanceState.getString("RESULT_TEXT");
    }
    // обработка нажатия на числовую кнопку
    public void onNumberClick(View view){

        Button button = (Button)view;
        numberField.append(button.getText());

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
        } catch (NumberFormatException ignored) {
        }
        return false;
    }
    // обработка нажатия на кнопку операции
    public void onPlusClick(View view) {
        lastOperation = "+";
        if (!isRepeatOperator(lastOperation)) {
            onOperatorClick();
        }
    }

    public void onMinusClick(View view) {
        lastOperation = "-";
        if (!isRepeatOperator(lastOperation)) {
            onOperatorClick();
        }
    }
    public void onMultiplyClick(View view) {
        lastOperation = "*";
        if (!isRepeatOperator(lastOperation)) {
            onOperatorClick();
        }
    }
    public void onDivideClick(View view) {
        lastOperation = "/";
        if (!isRepeatOperator(lastOperation)) {
            onOperatorClick();
        }
    }
    public void onEqualClick(View view) {
        lastOperation = "=";
        evaluateExpression();
        if (Double.parseDouble(result) % 1.0 < 0.000001) {
            result = result.substring(0, result.length() - 2);
        }
        resultField.setText(result);
    }

    @SuppressLint("SetTextI18n")
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
        return !expression.isEmpty() && lastOperation.equals(expression.get(expression.size() - 1));
    }

    public void onClearClick(View view) {
        if (isSecret()) {
            state.putString("RESULT", "");
            state.putString("NUMBER", "");
            state.putString("OPERATION", lastOperation);
            state.putStringArrayList("EXPRESSION", expression);
            state.putStringArrayList("LAST_OPERAND", lastOperand);
            state.putInt("OPERAND_COUNT", operandCount);
            state.putInt("OPERATOR_COUNT", operatorCount);
            state.putString("RESULT_TEXT", result);
            onSaveInstanceState(state);
            setContentView(R.layout.secret_page);
        }
            resultField.setText("");
            numberField.setText("");
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
            String currentText = numberField.getText().toString();
            numberField.setText(currentText.subSequence(0, currentText.length() - 1));
        }
    }

    public void onHeartClick(View view) {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.heart_fly_up);

        // Применение анимации к картинке
        heartImage.startAnimation(anim);
    }

    private boolean isSecret() {
        return expression.equals(key);
    }

    public void onBackClick(View view) {
        setContentView(R.layout.activity_main);
        onRestoreInstanceState(state);
    }
}