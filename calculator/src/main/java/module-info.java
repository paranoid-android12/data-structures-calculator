module com.example.calculator {
    requires org.apache.commons.numbers.gamma;
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.calculator to javafx.fxml;
    exports com.example.calculator;
}