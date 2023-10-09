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
// import org.apache.commons.numbers.gamma.Gamma;

import org.apache.commons.numbers.gamma.Gamma;

public class HelloController {
    @FXML
    private Text mainNumber, subNumber, variableDisplay, setButtonText, currentFunction;
    private Double currentResult = 0.0;
    private int a=0, b=0, c=0, d=0;
    private String varToSet = "", summaFunction="";
    private boolean justOperated = false;
    private boolean justEqualed = false;
    private boolean setMode = false;
    @FXML
    //Window buttons
    private ImageView minimizeButton, closeButton;

    //0-9 Buttons
    private Pane numButton1, numButton2, numButton3, numButton4, numButton5, numButton6, numButton7, numButton8, numButton9, numButton0, numDelete, numErase, numInvert, decimalButton;
    //Operation Buttons
    private Pane plusButton, subButton, prodButton, divButton, equalButton, rootButton, cubeRootButton, squareButton, multipleExpoButton, log2Button, log10Button, factorialPlusButton, factorialDivideButton;

    //Unique Operation Buttons
    private Pane aTimesB, aPlusB, aRaisedB, cTimesD, cPlusD, cRaisedD;

    //Variables
    private Pane varA, varB, varC, vard, setButton, setFunction;

    //Row 0-2 blue buttons
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

    @FXML
    void setVariables(MouseEvent event){
        //Get the button that called the function    <------   USE THIS TO GET SOURCE OF EVENT CALLER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        Object source2 = event.getSource();
        Node node = (Node) source2;
        String id = node.getId();
        String num = mainNumber.getText();
        String letter = id.substring(id.length()-1);


        if(id.equals("setButton")){
            if(setMode){setMode = false; 
                setButtonText.setText("SET = OFF");
            }
            else{setMode = true; 
                setButtonText.setText("SET = ON");
            }
        }
        else{
            if(setMode){
                switch(letter){
                    case "A": a = Integer.parseInt(num); break;
                    case "B": b = Integer.parseInt(num); break;
                    case "C": c = Integer.parseInt(num); break;
                    case "D": d = Integer.parseInt(num); break;
                }
                justOperated = false;
            }
            else{
                mainNumber.setText(letter);
                justOperated = false;
            }
        }
        variableDisplay.setText("A = " + a + "  B = " + b + "  C = " + c + "  D = " + d);
    }

    @FXML
    void setFunction(MouseEvent event){
        Object source2 = event.getSource();
        Node node = (Node) source2;
        String id = node.getId();
        String num = mainNumber.getText();
        String subNum = subNumber.getText();

        summaFunction = subNum + num;
        currentFunction.setText(summaFunction);
        
        //Set to initial values.
        subNumber.setText("");
        mainNumber.setText("");
        currentResult = 0.0;
    }  

    String equationEvaluator(String num, int type){
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "singleEval.py", num);
            if(type == 3 || type == 4){processBuilder = new ProcessBuilder("python", "doubleEval.py", num);}

            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            System.out.println(output);
            if (exitCode == 0) {
                return output.toString();
            } else {
                throw new IOException("Python script execution failed");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    Double summationProd(int type){
        int tempA = a;
        int tempB = b;

        String num = summaFunction.replace("A", "i")
                     .replace("B", tempB+"")
                     .replace("C", c+"")
                     .replace("D", d+"") + "," + tempA + "," + tempB + "," + type;
        System.out.println("ANS: " + equationEvaluator(num, type));
        return Double.parseDouble(equationEvaluator(num, type));
    }

    Double marriedSummationProd(int type){
        int tempC = c;
        int tempD = d;
        int outA = a;
        int outB = b;

        String num = summaFunction.replace("A", "i")
                .replace("B", outB+"")
                .replace("C", "x")
                .replace("D", tempD+"") + "," + outA + "," + outB + "," + tempC + "," + tempD + "," + type;
        return Double.parseDouble(equationEvaluator(num, type));
    }

    @FXML
    void summationProduct(MouseEvent event){
        Object source2 = event.getSource();
        Node node = (Node) source2;
        String id = node.getId();
        String num = mainNumber.getText();
        String subNum = subNumber.getText();

        switch(id){
            case "singleSummation":
                Double result1 = summationProd(1);
                mainNumber.setText(result1+"");
                equationProcessor();
                subNumber.setText(subNum + result1 + "+");
                break;
            case "singleProduct":
                Double result2 = summationProd(2);
                mainNumber.setText(result2+"");
                equationProcessor();
                subNumber.setText(subNum + result2 + "+");
                break;
            case "marriedSummation":
                Double result3 = marriedSummationProd(3);
                mainNumber.setText(result3+"");
                equationProcessor();
                subNumber.setText(subNum + result3 + "+");
                break;
            case "marriedProduct":
                Double result4 = marriedSummationProd(4);
                mainNumber.setText(result4+"");
                equationProcessor();
                subNumber.setText(subNum + result4 + "+");
                break;   
        }
        justOperated = true;
    }


    //Processes currentResult and value of current display in mainNum
    void equationProcessor(){
        String num = mainNumber.getText();
        String subNum = subNumber.getText();

        if(subNum.equals("")){
            currentResult = Double.parseDouble(num);
            return;
        }

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

        if(num.equals("A") || num.equals("B") || num.equals("C") || num.equals("D")){
            mainNumber.setText(digit);
            justOperated = false;
            return;
        }
        
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

    //Allows factorial for both integer and complex numbers
    double Factorize(double num){
        // return 0.0;
        return Gamma.value(num + 1);
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
        if(num.equals("A") || num.equals("B") || num.equals("C") || num.equals("D")){
            String letterTemp = num;
            //If letter is first in the calculation.
            if(subNum.equals("")){
                switch(letterTemp){
                    case "A": currentResult = Double.valueOf(a);
                    case "B": currentResult = Double.valueOf(b);
                    case "C": currentResult = Double.valueOf(c);
                    case "D": currentResult = Double.valueOf(d);
                }
            }
            else{
                mainNumber.setText(a + "");
                equationProcessor();
                mainNumber.setText(letterTemp);
            }
            
        }

        //Edge case: Proceeds to normal mode after the user had just clicked an operation after the equal button.
        else if (justEqualed == true){
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
    //THIS INCLUDES THE CURRENTLY DISPLAYED NUMBER IN mainNumber
    @FXML
    void equalOperator(MouseEvent event){
        Object source2 = event.getSource();
        Node node = (Node) source2;
        String id = node.getId();
        String num = mainNumber.getText();
        String subNum = subNumber.getText();
        
        if(num.equals("A") || num.equals("B") || num.equals("C") || num.equals("D")){
            mainNumber.setText(subNum);
        }

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
        String letter = num;
        boolean isLetter = false;

        if(num.equals("A") || num.equals("B") || num.equals("C") || num.equals("D")){
            isLetter = true;
            switch(num){
                case "A": num = a+""; break;
                case "B": num = b+""; break;
                case "C": num = c+""; break;
                case "D": num = d+""; break;
            }
        }
        
        switch (id){
            case "factorialButton":
                String tempNumFactor = num;
                mainNumber.setText(Factorize(Double.parseDouble(num)) + "");
                equationProcessor();
                subNumber.setText(subNum + tempNumFactor + "!+");
                if(isLetter){subNumber.setText(subNum + letter + "!+");}
                break;

            case "rootButton": 
                String tempNumRoot = num;
                mainNumber.setText(Math.sqrt(Double.parseDouble(num)) + "");
                equationProcessor();
                subNumber.setText(subNum + "sqrt(" + tempNumRoot + ")+");
                if(isLetter){subNumber.setText(subNum + "sqrt(" + letter + ")+");}
                break;

            case "cubeRootButton":
                String tempNumCube = num;
                mainNumber.setText(Math.cbrt(Double.parseDouble(num)) + "");
                equationProcessor();
                subNumber.setText(subNum + "cbrt(" + tempNumCube + ")+");
                if(isLetter){subNumber.setText(subNum + "cbrt(" + letter + ")+");}
                break;

            case "squareButton":
                String tempNumSquare = num;
                mainNumber.setText((Double.parseDouble(num)) * (Double.parseDouble(num)) + "");
                equationProcessor();
                subNumber.setText(subNum + tempNumSquare +  "^2+");
                if(isLetter){subNumber.setText(subNum + letter +  "^2+");}
                break;
                
            case "multipleExpoButton":
                String tempNumMultiSquare = num;
                mainNumber.setText(Math.pow(Double.parseDouble(num), Math.pow(a, b)) + "");
                equationProcessor();
                subNumber.setText(subNum + "((" + tempNumMultiSquare +  "^A)^B)+");
                if(isLetter){subNumber.setText(subNum + "((" + letter +  "^A)^B)+");}
                break;

            case "log2Button":
                String tempNumLogTwo = num;
                mainNumber.setText(((Math.log(Double.parseDouble(num))) / (Math.log(2))) + "");
                equationProcessor();
                subNumber.setText(subNum + "log2(" + tempNumLogTwo +  ")+");
                if(isLetter){subNumber.setText(subNum + "log2(" + letter +  ")+");}
                break;

            case "log10Button":
                String tempNumLogTen = num;
                mainNumber.setText(((Math.log(Double.parseDouble(num))) / (Math.log(10))) + "");
                equationProcessor();
                subNumber.setText(subNum + "log10(" + tempNumLogTen +  ")+");
                if(isLetter){subNumber.setText(subNum + "log10(" + letter +  ")+");}
                break;

            case "factorialPlusButton":
                double tempA = Factorize(a);
                double tempB = Factorize(b);
                mainNumber.setText((tempA + tempB) + "");
                equationProcessor();
                subNumber.setText(subNum + "(a!+b!)");
                break;

            case "factorialDivideButton":
                double tempDivA = Factorize(a);
                double tempDivB = Factorize(b);
                mainNumber.setText((tempDivA / tempDivB) + "");
                equationProcessor();
                subNumber.setText(subNum + "(a!/b!)");
                break;

            case "aTimesB":
                mainNumber.setText((a * b) + "");
                equationProcessor();
                subNumber.setText(subNum + "(A * B)+");
                break;
                
            case "aPlusB":
                mainNumber.setText((a + b) + "");
                equationProcessor();
                subNumber.setText(subNum + "(A + B)+");
                break;
            case "aRaisedB":
                mainNumber.setText(Math.pow(a, b) + "");
                equationProcessor();
                subNumber.setText(subNum + "(A ^ B)+");
                break;
            case "cTimesD":
                mainNumber.setText((c * d) + "");
                equationProcessor();
                subNumber.setText(subNum + "(C * D)+");
                break;
            case "cPlusD":
                mainNumber.setText((c + d) + "");
                equationProcessor();
                subNumber.setText(subNum + "(C + D)+");
                break;
            case "cRaisedD":
                mainNumber.setText(Math.pow(c, d) + "");
                equationProcessor();
                subNumber.setText(subNum + "(C ^ D)+");
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
                subNumber.setText("int(" + (num) + ")+");
                currentResult = Math.floor(Double.parseDouble(num));
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