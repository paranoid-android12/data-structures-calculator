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
    private Text mainNumber, subNumber;
    private Double currentResult = 0.0;
    private boolean justOperated = false;
    private boolean justEqualed = false;
    private String allSubNumTemp;
    @FXML
    //Window buttons
    private ImageView minimizeButton, closeButton;

    //0-9 Buttons
    private Pane numButton1, numButton2, numButton3, numButton4, numButton5, numButton6, numButton7, numButton8, numButton9, numButton0, numDelete, numErase, numInvert, decimalButton;
    //Operation Buttons
    private Pane plusButton, subButton, prodButton, divButton, equalButton, rootButton, cubeRootButton, squareButton, multipleExpoButton;

    //Row 0-2 blue buttons (CJAY)
    private Pane floorButton, ceilingButton, integerButton, floordivButton, modulusButton, factorialButton;

    //For exit and minimize button design
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




    //Processes currentResult and value of current display in mainNum
    void equationProcessor(){
        String num = mainNumber.getText();
        String subNum = subNumber.getText();
        //Lazy solution that just checks the operation by latest char of the subNum
        String operation = (subNum.charAt(subNum.length()-1)) + "";
        
        //Main processor of the previous equation.
        switch (operation){
            case "+": currentResult = currentResult + Double.parseDouble(num); break;
            case "-": currentResult = currentResult - Double.parseDouble(num); break;
            case "*": currentResult = currentResult * Double.parseDouble(num); break;
            case "/": //Case for both division and floor division
                //Floor division
                if(subNum.charAt(subNum.length()-2) == '/'){
                        Double tempResult = Math.floor(currentResult / Double.parseDouble(num));
                        currentResult = tempResult;
                }

                //Normal division
                else{
                    currentResult = currentResult / Double.parseDouble(num);
                }
                break;
            
            case "%":
                currentResult = currentResult % Double.parseDouble(num);
                break;
        }
        System.out.println("CurreRes: " + currentResult);
        System.out.println("justOperated: " + justOperated);
        System.out.println("justEqualed: " + justEqualed);
        System.out.println("");
        return;
    }



    @FXML
    //Event for button 0 - 9 (Viacrusis)
    void clickedNum(MouseEvent event) {
        //Get the button that called the function    <------   USE THIS TO GET SOURCE OF EVENT CALLER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        Object source2 = event.getSource();
        Node node = (Node) source2;
        String id = node.getId();
        //Parse mainNum/ber to String
        String num = mainNumber.getText();
        String subNum = subNumber.getText();
        String digit = id.charAt(id.length()-1) + "";

        //Decimal button process
        if(id.equals("decimalButton")){
            if(!num.contains(".")){
                mainNumber.setText(num + ".");
            }
            justOperated = false;
        }

        //If it's first number, just replace zero with current num.
        else if(num.equals("0")){ 
            mainNumber.setText(id.substring(id.length() - 1));
            justOperated = false;
        }

        //If operation was just clicked, replace the current display with a new number at first numberclick
        else if(justOperated == true){ 
            mainNumber.setText(digit);
            justOperated = false;
        }

        //Apply number as is
        else { 
            mainNumber.setText(num + digit);
        }
    }

    @FXML
    //Event for button DEL, AC, +/-
    void delAcInvert(MouseEvent event){
        //Get event caller, and parse mainNumber to String.
        Object source2 = event.getSource();
        Node node = (Node) source2;
        String id = node.getId();
        String num = mainNumber.getText();

        //Resets calculator if delete was pressed while current number is zero.
        if(num.equals("0")){
            subNumber.setText("");
            currentResult = 0.0;
            return;
        }
    
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
        justOperated = false;
        justEqualed = false;
    }

    //Processes facorial value of mainNumber
    int Factorize(int num){
        int prod = 1;

        for(int i = num; i > 0; i--){
            prod = prod * i;
        }
        return prod;
    }


    //Event for basic operands with same functionalities (operations other than the four operations are also included long as it doesn't merit a unique "mode" of some sort)
    @FXML
    void basicOperations(MouseEvent event){
        //Get event caller, and parse mainNumber to String.
        Object source2 = event.getSource();
        Node node = (Node) source2;
        String id = node.getId();
        String num = mainNumber.getText();
        String subNum = subNumber.getText();
        String symbol = "";

        //Checks the user input symbol to pass on the equationProcessor for later.
        switch(id){
            case "plusButton": symbol = "+"; break;
            case "subButton": symbol = "-"; break;
            case "prodButton": symbol = "*"; break;
            case "divButton": symbol = "/"; break;
            case "floordivButton": symbol = "//"; break;
            case "modulusButton": symbol = "%"; break;

        }

        //Edge case: Proceeds to normal mode after the user had just clicked an operation after the equal button.
        if (justEqualed == true){
            subNumber.setText(num + symbol);
            justEqualed = false;
            justOperated = true;
            return;
        }

        //justOperated mode: Allows user to change operations after the current total result has just been displayed.
        else if(justOperated == true){
            //Edge case for //
            if(subNum.charAt(subNum.length()-2) == '/'){
                subNum = subNum.substring(0, subNum.length()-2);
            }
            //Case for normal symbols
            else{
                subNum = subNum.substring(0, subNum.length()-1);
            }
            subNumber.setText(subNum + symbol);
            return;
        } 

        //Calculate mainNumber and currentResult (DEFAULT CASE)
        else if(!subNum.equals("")){
            equationProcessor();
        } 

        //If subNum is empty, just initialize currentResult based on first number input
        else{ 
            currentResult = Double.parseDouble(num);
        }

        //Updates subNumber with new result and operation.
        subNumber.setText(subNum + num + symbol);

        //Displays whole number if there's no decimal in answer
        if(currentResult % 1 == 0){mainNumber.setText(String.format("%.0f", currentResult));}
        else{mainNumber.setText(String.format("%.6f", currentResult));}

        //EXPLANATION OF THIS LINE IS ON uniqueOperations function at the bottom.
        justOperated = true;
    }

    //Equal button (simplified this to just display and i removed stacking because it's hell to program)
    @FXML
    void equalOperator(MouseEvent event){
        Object source2 = event.getSource();
        Node node = (Node) source2;
        String id = node.getId();
        String num = mainNumber.getText();
        String subNum = subNumber.getText();
        
        equationProcessor();
        subNumber.setText("");
        mainNumber.setText("" + currentResult);

        justEqualed = true;
    }

    //Functions for complex buttons with unique "modes" or properties that makes me want to ywahoo myself off the building
    //Or just make a new function for really unique buttons.
    @FXML
    void uniqueOperations(MouseEvent event){
        //Get event caller, and parse mainNumber to String.
        Object source2 = event.getSource();
        Node node = (Node) source2;
        String id = node.getId();
        String num = mainNumber.getText();
        String subNum = subNumber.getText();
        
        switch (id){
            case "factorialButton":
                subNumber.setText(allSubNumTemp);
                mainNumber.setText(Factorize(Integer.parseInt(num)) + "");
                break;

            case "rootButton": 
                subNumber.setText(subNum);
                currentResult = Double.parseDouble(num);
                mainNumber.setText(Math.sqrt(currentResult) + "");
                break;

            case "cubeRootButton":
                subNumber.setText(subNum);
                currentResult = Double.parseDouble(num);
                mainNumber.setText(Math.cbrt(currentResult) + "");
                break;

            case "squareButton":
                subNumber.setText(subNum);
                currentResult = Double.parseDouble(num);
                mainNumber.setText(Math.pow(currentResult, 2) + "");
                break;

            //Weirdly enough, these three cases essentially has the same functionalities. IDK why they included then all.
            case "floorButton":
                subNumber.setText("floor(" + (subNum) + ")+");
                currentResult = Math.floor(currentResult);
                mainNumber.setText(currentResult + "");
                break;
            case "ceilingButton":
                subNumber.setText("ceil(" + (subNum) + ")+");
                currentResult = Math.ceil(currentResult);
                mainNumber.setText(currentResult + "");
                break;
            case "integerButton":
                subNumber.setText("int(" + (subNum) + ")+");
                currentResult = Math.floor(currentResult);
                mainNumber.setText(currentResult + "");
                break;

        }

        //This is a VERY IMPORTANT line. 
        //When the user types a number and clicks a new operation, it shows the current result of the calculations so far on the mainNumber.
        //This in turn, makes justOperated true.

        //WHEN THIS IS TRUE:
        //The user's next number input replaces the answer display on the mainNumber, and returns to normal mode by turning it false.
        //This also allows the user to freely change the operation.
        //This becomes false when the user enters a number.
        justOperated = true;
        System.out.println(currentResult);
    }

}