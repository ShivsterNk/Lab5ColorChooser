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
    private double alpha = 0;

    public void initialize() {

        //Bind sliders and textfields bidirectionally with converters
        bindBidirectional(redSlider, redTextField, 0, 255);
        bindBidirectional(greenSlider, greenTextField, 0, 255);
        bindBidirectional(blueSlider, blueTextField, 0, 255);

        alphaSlider.setMin(0);
        alphaSlider.setMax(1);
        alphaSlider.setBlockIncrement(0.01);
        alphaSlider.setMajorTickUnit(0.1);
        alphaSlider.setMinorTickCount(9);
        alphaSlider.setSnapToTicks(false);

        // Sync alpha slider -> textfield
        alphaSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            alphaTextField.setText(String.format("%.2f", newVal.doubleValue()));
        });

        // Sync alpha textfield -> slider only on focus lost (when user finishes typing)
        alphaTextField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused) {
                try {
                    double val = Double.parseDouble(alphaTextField.getText());
                    if (val < 0) val = 0;
                    if (val > 1) val = 1;
                    alphaSlider.setValue(val);
                } catch (NumberFormatException e) {
                    // Invalid input: reset to current slider value
                    alphaTextField.setText(String.format("%.2f", alphaSlider.getValue()));
                }
            }
        });

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
            textField.setText(String.format("%.2f", slider.getValue()));
        }

        slider.valueProperty().addListener((ObservableValue, oldValue, newValue) -> {
            if (max == 255) {
                textField.setText(String.valueOf(newValue.intValue()));
            }else {
                textField.setText(String.format("%.2f", newValue.doubleValue()));
            }
        });

        //TextField -> Slider
        // For RGB sliders only: update slider on TextField change
        if (max == 255) {
            textField.textProperty().addListener((obs, oldText, newText) -> {
                try {
                    int val = Integer.parseInt(newText);
                    if (val < min) val = (int) min;
                    if (val > max) val = (int) max;
                    if (slider.getValue() != val) slider.setValue(val);
                } catch (NumberFormatException ignored) {
                    // Invalid input ignored
                }
            });
        }
    }

    public void updateColor() {
        int red = (int) redSlider.getValue();
        int green = (int) greenSlider.getValue();
        int blue = (int) blueSlider.getValue();
        double alpha = alphaSlider.getValue();

        colorRectangle.setFill(Color.rgb(red,green,blue,alpha));
    }
}