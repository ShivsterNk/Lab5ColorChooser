package com.example.lab5colorchanger;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.NumberStringConverter;


public class ColorChooserController {
    // instance variables for interacting with GUI components
    @FXML private Slider redSlider;
    @FXML private Slider greenSlider;
    @FXML private Slider blueSlider;
    @FXML private Slider alphaSlider;
    @FXML private TextField redTextField;
    @FXML private TextField greenTextField;
    @FXML private TextField blueTextField;
    @FXML private TextField alphaTextField;
    @FXML private Rectangle colorRectangle;

    // instance variables for managing
    private int red = 0;
    private int green = 0;
    private int blue = 0;
    private double alpha = 1.0;

    public void initialize() {

        //Bind sliders and textfields bidirectionally with converters
        bindBidirectional(redSlider, redTextField, 0, 255);
        bindBidirectional(greenSlider, greenTextField, 0, 255);
        bindBidirectional(blueSlider, blueTextField, 0, 255);
        bindBidirectional(alphaSlider, alphaTextField, 0, 1);

        // Add listeners to sliders to update colors
        redSlider.valueProperty().addListener((ObservableValue, oldValue, newValue) -> updateColor());
        greenSlider.valueProperty().addListener((ObservableValue, oldValue, newValue) -> updateColor());
        blueSlider.valueProperty().addListener((ObservableValue, oldValue, newValue) -> updateColor());
        alphaSlider.valueProperty().addListener((ObservableValue, oldValue, newValue) -> updateColor());

        updateColor();
    }

    private void bindBidirectional(Slider slider, TextField textField, double min, double max) {

        //Clamp slider min and max
        slider.setMin(min);
        slider.setMax(max);

        //Use Number String Converter for red, green, blue (integers)
        if (max == 255) {
            textField.setText(String.valueOf((int) slider.getValue()));
        }else {
            textField.setText(String.format("%2f", slider.getValue()));
        }

        slider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            if (max == 255) {
                textField.setText(String.valueOf(newValue.intValue()));
            }else {
                textField.setText(String.format("%.2f", newValue.doubleValue()));
            }
        });

        //TextField -> Slider
        textField.textProperty().addListener(((observableValue, oldText, newText) -> {
            try {
                double val;
                if (max == 255) {
                    val = Integer.parseInt(newText);
                } else {
                    val = Double.parseDouble(newText);
                }

                if (val < min)
                    val = min;
                if (val > max)
                    val = max;

                if (slider.getValue() != val) {
                    slider.setValue(val);
                }
            } catch (NumberFormatException e) {
                //Invalid input
            }

        }));
    }

    public void updateColor() {
        int red = (int) redSlider.getValue();
        int green = (int) greenSlider.getValue();
        int blue = (int) blueSlider.getValue();
        double alpha = alphaSlider.getValue();

        colorRectangle.setFill(Color.rgb(red,green,blue,alpha));
    }
}