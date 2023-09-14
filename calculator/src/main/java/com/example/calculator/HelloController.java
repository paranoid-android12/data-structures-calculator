package com.example.calculator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HelloController {
    @FXML
    private Text mainNumber, result;
    @FXML
    //Window buttons
    private ImageView minimizeButton, closeButton;

    //0-9 Buttons
    private Pane numButton1, numButton2, numButton3, numButton4, numButton5, numButton6, numButton7, numButton8, numButton9, numButton0, numDelete, numErase, numInvert, decimalButton;
    //Operation Buttons
    private Pane plusButton, subButton, prodButton, divButton, equalButton, rootButton;

    //Row 0-2 blue buttons (CJAY)
    private Pane floorButton, ceilingButton, integerButton, floordivButton, modulusButton, factorialButton;
    @FXML
    void opacityImage(MouseEvent event){
        Object source2 = event.getSource();
        Node node = (Node) source2;
        String id = node.getId();

        if(id.equals("closeButton")){
            closeButton.setOpacity(1);
        }
        else{
            minimizeButton.setOpacity(1);
        }
    }
    @FXML
    void opacityImageRemove(MouseEvent event){
        Object source2 = event.getSource();
        Node node = (Node) source2;
        String id = node.getId();

        if(id.equals("closeButton")){
            closeButton.setOpacity(0.8);
        }
        else{
            minimizeButton.setOpacity(0.8);
        }
    }
    

    //---EQUATION EVALUATOR---

    //Main python caller
    String equationProcessor(){
        String num = mainNumber.getText();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "evaluator.py", num);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return output.toString();
            } else {
                throw new IOException("Python script execution failed");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Invalid Equation";
        }
    }
    @FXML
    void equalEval(MouseEvent event){
        System.out.println(equationProcessor());
        result.setText("= " + equationProcessor());
        return;
    }

    //---EQUATION EVALUATOR---


    @FXML
    //Event for button 0 - 9 (Viacrusis)
    void clickedNum(MouseEvent event) {
        //Get the button that called the function    <------   USE THIS TO GET SOURCE OF EVENT CALLER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        Object source2 = event.getSource();
        Node node = (Node) source2;
        String id = node.getId();

        //Parse mainNumber to String
        String num = mainNumber.getText();


        //Edge cases
        if(num.equals("0") && !id.equals("decimalButton")){
            mainNumber.setText(id.substring(id.length() - 1));
            return;
        }
        else if (num.length() > 10) {return;}


        switch(id){
            case "numButton0":
                mainNumber.setText(num + "0");
                break;
            case "numButton1":
                mainNumber.setText(num + "1");
                break;
            case "numButton2":
                mainNumber.setText(num + "2");
                break;
            case "numButton3":
                mainNumber.setText(num + "3");
                break;
            case "numButton4":
                mainNumber.setText(num + "4");
                break;
            case "numButton5":
                mainNumber.setText(num + "5");
                break;
            case "numButton6":
                mainNumber.setText(num + "6");
                break;
            case "numButton7":
                mainNumber.setText(num + "7");
                break;
            case "numButton8":
                mainNumber.setText(num + "8");
                break;
            case "numButton9":
                mainNumber.setText(num + "9");
                break;
            case "decimalButton":
                //This disgusting code essentially checks if the current number in the equation has a decimal point.
                char lastChar = num.charAt(num.length() - 1);
                //Edge cases
                if(lastChar == '.'){return;} //If last char has decimal
                else if(!Character.isDigit(lastChar)){ //If last char is not a number, allow decimal
                    mainNumber.setText(num + '.');
                    return;
                }

                //In an event that it is a digit, extract the current number typed.
                int rightIdx = num.length() - 1;
                int leftIdx = 0;
                for(int i = num.length() - 1; i >= 0; i--){
                    char currChar = num.charAt(i);

                    if(!Character.isDigit(currChar) && (currChar != '.')){
                        leftIdx = i + 1;
                    }
                }
                String currentNum = num.substring(leftIdx, rightIdx);
                if(!currentNum.contains(".")){
                    mainNumber.setText(num + '.');
                }
        }


    }

    //Simple event for closing and minimizing windows (Viacrusis)
    @FXML
    void windowButton(MouseEvent event){
        Object source2 = event.getSource();
        Node node = (Node) source2;
        String id = node.getId();
        Stage stage = (Stage) minimizeButton.getScene().getWindow();

        switch(id){
            case "closeButton":
                stage.close();
                break;
            case"minimizeButton":
                stage.setIconified(true);
                break;
        }
        // do what you have to do
    }

    @FXML
        //Event for button DEL, AC, +/- (Viacrusis)
    void delAcInvert(MouseEvent event){
        //Get event caller, and parse mainNumber to String.
        Object source2 = event.getSource();
        Node node = (Node) source2;
        String id = node.getId();
        String num = mainNumber.getText();
        System.out.println("I was called");
        if(num.equals("0")){return;}

        switch(id){
            case("numDelete"):
                if(num.length() < 2){mainNumber.setText("0");}
                else{
                    num = num.substring(0, num.length() - 1);
                    mainNumber.setText(num);
                }
                break;

            case("numErase"):
                mainNumber.setText("0");
                break;

            case("numInvert"):
                char first = num.charAt(0);
                if(first == '-'){
                    num = num.substring(1, num.length());
                    mainNumber.setText(num);
                }
                else{mainNumber.setText("-" + num);}

        }
    }

    //Event for basic operands + - * / . (Viacrusis)
    @FXML
    void basicOperations(MouseEvent event){
        //Get event caller, and parse mainNumber to String.
        Object source2 = event.getSource();
        Node node = (Node) source2;
        String id = node.getId();
        String num = mainNumber.getText();

        char lastChar = num.charAt(num.length() - 1);
        
        if(lastChar == '.'){
            mainNumber.setText(num + "0");
            num = mainNumber.getText();
        } //Auto places zero if last number ends with a decimal
        if(lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/'){return;} //Prevents operation duplicates

        switch(id){
            case "plusButton":
                mainNumber.setText(num + "+");
                break;
            case "subButton":
                mainNumber.setText(num + "-");
                break;
            case "prodButton":
                mainNumber.setText(num + "*");
                break;
            case "divButton":
                mainNumber.setText(num + "/");
                break;
        }
    }

    //Event for row 0-2 FLR,CEIL,INT,//,%,N! (Landero)
    @FXML
    void rowTwoBlues(MouseEvent event){
        //Get event caller, and parse mainNumber to String.
        Object source2 = event.getSource();
        Node node = (Node) source2;

        String id = node.getId(); // <----- ITO YUNG STRING VERSION NG BUTTON NA PININDOT PARA MACALL TONG FUNCTION. USED FOR SWITCH CASES.
        String num = mainNumber.getText(); // <--- ITO YUNG MAIN DISPLAY CURRENTLY, PARSED NA SA STRING.

        switch (id) {
            case ("floorButton"):
                mainNumber.setText("math.floor(" + num + ")");
                equalEval(event);
                break;
                case ("ceilingButton"):
                mainNumber.setText("math.ceil("+ num + ")");
                equalEval(event);
                break;
            case ("floordivButton"):
                mainNumber.setText(num + "//");
                break;
            case ("modulusButton"):
                mainNumber.setText(num + "%");
                break;
            case ("factorialButton"): 
                mainNumber.setText(num + "!");
                break;
        }    }
}