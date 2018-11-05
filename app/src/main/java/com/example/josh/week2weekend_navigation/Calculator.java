package com.example.josh.week2weekend_navigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.josh.week2weekend_navigation.R;

import org.w3c.dom.Text;

public class Calculator extends AppCompatActivity implements View.OnClickListener {

    //Initialization
    String leftNumber = "", rightNumber = "", outputString = "";
    Context context;
    private DrawerLayout mDrawerLayout;
    Boolean parenth = false, decimal = false;
    TextView tvOutput;
    Button btnClear, btnBackSpace, btnParenthesis, btnDivision, btnSeven, btnEight, btnNine, btnMultiply,
            btnFour, btnFive, btnSix, btnSubtract, btnOne, btnTwo, btnThree, btnAdd, btnPlusMinus,
            btnZero, btnDecimal, btnEquals, btnLn, btnSqrt, btnX2, btnPi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        context = getApplicationContext();
        tvOutput = findViewById(R.id.tvOutput);
        connectButtons();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Test");
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);



        // Setting up Navigation window
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()){
                            case R.id.Calculator:
                                Intent intent = new Intent(context,Calculator.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                break;

                        }


                        return true;
                    }
                });
    }

    //Used to append a negative value before a number
    public void plusOrMinus() {
        CharSequence outputText = tvOutput.getText();
        if (outputText.length() == 0) {
            tvOutput.append("-");
            return;
        }
        char lastChar = tvOutput.getText().charAt(outputText.length() - 1);
        if (lastChar != '-') {
            tvOutput.append("-");
        }

    }

    //Used to test whether the function being called can be run on the data provided
    //Tests to ensure the current text is a double.
    public void isNumber(char c) {
        double output;
        try {
            output = Double.parseDouble(tvOutput.getText().toString());
            if (c == 'l')
                output = Math.log(output);
            else if (c == 's')
                output = Math.sqrt(output);
            else if (c == 'x')
                output = output * output;

            tvOutput.setText(Double.toString(output));

        } catch (Exception e) {

        }
    }


    //Ensures that two operation signs cannot be used at the same time
    public boolean checkLastChar() {
        CharSequence outputText = tvOutput.getText();

        if (outputText.length() == 0) {
            return false;
        }
        char lastChar = tvOutput.getText().charAt(outputText.length() - 1);
        if (lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '÷')
            return false;
        else
            return true;
    }

    //Main Work Method of the Program
    //Recurses through parenthisis first then operates from left to right using helper function compute
    public String equals(String input) {
        int i = 0;
        while (i < input.length()) {

            if (input.indexOf("(") != -1) {
                int temp = input.indexOf(")");
                if (temp == -1) {
                    System.out.println("Must close paren");
                    return input;
                } else {
                    input = input.substring(0, input.indexOf("(")) + equals(input.substring(input.indexOf("(") + 1, temp))
                            + input.substring(temp + 1);
                }
            }
            if (input.charAt(i) == '+') {
                input = compute(input.substring(0, i), input.substring(i + 1, input.length()), '+');
                i = 1;
            } else if (input.charAt(i) == '-' && i != 0) {
                input = compute(input.substring(0, i), input.substring(i + 1, input.length()), '-');
                i = 1;
            } else if (input.charAt(i) == '÷') {
                input = compute(input.substring(0, i), input.substring(i + 1, input.length()), '÷');
                i = 1;
            } else if (input.charAt(i) == '*') {
                input = compute(input.substring(0, i), input.substring(i + 1, input.length()), '*');
                i = 1;
            }
            i++;
        }

        return input;
    }


    //Helper function that does the computation by splitting the two halves and computing their values
    public String compute(String left, String right, char operation) {
        double output = 0;
        String temp = right;
        int leftOver = 0;
        for (int i = 0; i < right.length(); i++) {
            if (right.charAt(i) == '+' || right.charAt(i) == '-' || right.charAt(i) == '*' || right.charAt(i) == '÷' || i == right.length() - 1) {
                if (right.charAt(i) == '-' && i == 0)
                    continue;
                leftOver = i;
                if (i != right.length() - 1) {
                    right = right.substring(0, i);
                }

                switch (operation) {
                    case '+':
                        output = Double.valueOf(left) + Double.valueOf(right);
                        break;
                    case '-':
                        output = Double.valueOf(left) - Double.valueOf(right);
                        break;
                    case '*':
                        output = Double.valueOf(left) * Double.valueOf(right);
                        break;
                    case '÷':
                        output = Double.valueOf(left) / Double.valueOf(right);
                        break;
                }
            }

        }
        if (leftOver == temp.length() - 1)
            return Double.toString(output);
        else
            return Double.toString(output) + temp.substring(leftOver, temp.length());

    }

    //Used to process the clicks of the various buttons, numbers are parsed and appended and math symbols are appended if helper function checkLastChar() returns true.
    @Override
    public void onClick(View v) {
        char lastChar = 'a';
        Button temp = findViewById(v.getId());
        String clicked = (String) temp.getText();
        try {
            Integer.parseInt(clicked);
            tvOutput.append(clicked);
        } catch (Exception e) {

            switch (v.getId()) {
                case R.id.btnClear:
                    tvOutput.setText("");
                    parenth = false;
                    break;
                case R.id.btnBackSpace:
                    CharSequence tempSub = tvOutput.getText();
                    if (tempSub.length() != 0) {
                        if (tempSub.charAt(tempSub.length() - 1) == ')') {
                            parenth = true;
                        } else if (tempSub.charAt(tempSub.length() - 1) == '(') {
                            parenth = false;
                        } else if (tempSub.charAt(tempSub.length() - 1) == '.') {
                            decimal = false;
                        }

                        tvOutput.setText(tempSub.subSequence(0, tempSub.length() - 1));
                    }
                    break;
                case R.id.btnParenthesis:
                    if (!parenth) {
                        if (!checkLastChar())
                            tvOutput.append("(");
                        else
                            tvOutput.append("*(");
                        parenth = true;
                    } else if (parenth && checkLastChar()) {
                        tvOutput.append(")");
                        parenth = false;
                    }
                    break;
                case R.id.btnDivision:
                    if (checkLastChar())
                        tvOutput.append("÷");
                    decimal = false;
                    break;
                case R.id.btnMultiply:
                    if (checkLastChar())
                        tvOutput.append("*");
                    decimal = false;
                    break;
                case R.id.btnSubtract:
                    if (checkLastChar())
                        tvOutput.append("-");
                    decimal = false;
                    break;
                case R.id.btnAdd:
                    if (checkLastChar())
                        tvOutput.append("+");
                    decimal = false;
                    break;
                case R.id.btnPlusMinus:
                    plusOrMinus();
                    break;
                case R.id.btnDecimal:
                    tvOutput.append(".");
                    decimal = true;
                    break;
                case R.id.btnEquals:
                    String output = tvOutput.getText().toString();
                    tvOutput.setText(equals(output));
                    break;

            }


        }

    }

    //Initializes the buttons and connects them to the interface
    public void connectButtons() {

        btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);
        btnBackSpace = findViewById(R.id.btnBackSpace);
        btnBackSpace.setOnClickListener(this);
        btnParenthesis = findViewById(R.id.btnParenthesis);
        btnParenthesis.setOnClickListener(this);
        btnDivision = findViewById(R.id.btnDivision);
        btnDivision.setOnClickListener(this);
        btnMultiply = findViewById(R.id.btnMultiply);
        btnMultiply.setOnClickListener(this);
        btnSubtract = findViewById(R.id.btnSubtract);
        btnSubtract.setOnClickListener(this);
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        btnPlusMinus = findViewById(R.id.btnPlusMinus);
        btnPlusMinus.setOnClickListener(this);
        btnDecimal = findViewById(R.id.btnDecimal);
        btnDecimal.setOnClickListener(this);
        btnEquals = findViewById(R.id.btnEquals);
        btnEquals.setOnClickListener(this);


        btnZero = findViewById(R.id.btnZero);
        btnZero.setOnClickListener(this);
        btnOne = findViewById(R.id.btnOne);
        btnOne.setOnClickListener(this);
        btnTwo = findViewById(R.id.btnTwo);
        btnTwo.setOnClickListener(this);
        btnThree = findViewById(R.id.btnThree);
        btnThree.setOnClickListener(this);
        btnFour = findViewById(R.id.btnFour);
        btnFour.setOnClickListener(this);
        btnFive = findViewById(R.id.btnFive);
        btnFive.setOnClickListener(this);
        btnSix = findViewById(R.id.btnSix);
        btnSix.setOnClickListener(this);
        btnSeven = findViewById(R.id.btnSeven);
        btnSeven.setOnClickListener(this);
        btnEight = findViewById(R.id.btnEight);
        btnEight.setOnClickListener(this);
        btnNine = findViewById(R.id.btnNine);
        btnNine.setOnClickListener(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String textSize;
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_txtUp:
                textSize = textEdit(true,false);
                setTextSize(textSize);
                return true;

            case R.id.action_txtDown:
                textSize = textEdit(false,false);
                setTextSize(textSize);
                return true;
            case R.id.action_clear:
                textSize = textEdit(false,true);
                setTextSize(textSize);
                return true;


        }
        return super.onOptionsItemSelected(item);

    }

    public String textEdit(boolean way,boolean reset) {

        SharedPreferences sharedPreferences = getSharedPreferences("saved_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!sharedPreferences.contains("TEXT_SIZE")) {
            editor.putString("TEXT_SIZE", "25dp");
        }
        if (reset){
            editor.putString("TEXT_SIZE","25dp");
            editor.apply();
            return "25dp";
        }
        String textSize = sharedPreferences.getString("TEXT_SIZE", "25dp");
        int numSize = Integer.valueOf(textSize.substring(0, 2));
        if (numSize < 30 && way) {
            numSize++;
            editor.putString("TEXT_SIZE", numSize + "sp");
            editor.apply();
            return numSize + "sp";
        } else if (numSize > 20 && !way) {
            numSize--;
            editor.putString("TEXT_SIZE", numSize + "sp");
            editor.apply();
            return numSize + "sp";
        } else
            return textSize;
    }

    public void setTextSize(String size){
        float textSize = Float.valueOf(size.substring(0,2));
        tvOutput.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
    }

}