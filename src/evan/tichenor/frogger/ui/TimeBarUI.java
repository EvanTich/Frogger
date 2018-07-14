package evan.tichenor.frogger.ui;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * @author Evan Tichenor
 * @version 1.0, 4/4/2018
 *
 * Taken from AlmasB's FXGL progress bar, but tidied up to my liking.
 */
public class TimeBarUI extends Parent {

    private DoubleProperty minValue = new SimpleDoubleProperty(0.0);
    private DoubleProperty currentValue = new SimpleDoubleProperty(0.0);
    private DoubleProperty maxValue = new SimpleDoubleProperty(100.0);

    private DoubleProperty width = new SimpleDoubleProperty(200.0);
    private DoubleProperty height = new SimpleDoubleProperty(10.0);

    public TimeBarUI(Paint insideColor) {
        Rectangle backgroundBar = new Rectangle();
        Rectangle innerBar = new Rectangle();

        innerBar.setTranslateX(5);
        innerBar.setTranslateY(1);
        innerBar.setFill(insideColor);

        backgroundBar.widthProperty().bind(width);
        backgroundBar.heightProperty().bind(height);
        backgroundBar.setFill(Color.gray(0, 0));

        innerBar.heightProperty().bind(height.subtract(2));

        getChildren().add(new Group(backgroundBar, innerBar));

        innerBar.widthProperty().bind(width.subtract(10).multiply(new DoubleBinding() {
            {
                super.bind(minValue, currentValue, maxValue);
            }

            @Override
            protected double computeValue() {
                return (currentValue.get() - minValue.get())
                        / (maxValue.get() - minValue.get());
            }
        }));
    }

    public void setWidth(double value) {
        if (value <= 0)
            throw new IllegalArgumentException("Width must be > 0");

        width.set(value);
    }

    public void setHeight(double value) {
        if (value <= 0)
            throw new IllegalArgumentException("Height must be > 0");

        height.set(value);
    }

    public void setMinValue(double value) {
        if (value > currentValue.get()) {
            currentValue.set(value);
        }

        if (value >= maxValue.get()) {
            maxValue.set(value + 1);
        }

        minValue.set(value);
    }

    public DoubleProperty minValueProperty() {
        return minValue;
    }

    public void setCurrentValue(double value) {
        double newValue = value;

        if (value < minValue.get()) {
            newValue = minValue.get();
        } else if (value > maxValue.get()) {
            newValue = maxValue.get();
        }

        currentValue.set(newValue);
    }

    public double getCurrentValue() {
        return currentValue.get();
    }

    public DoubleProperty currentValueProperty() {
        return currentValue;
    }

    public void setMaxValue(double value) {
        if (value <= minValue.get()) {
            minValue.set(value - 1);
        }

        if (value < currentValue.get()) {
            currentValue.set(value);
        }

        maxValue.set(value);
    }

    public DoubleProperty maxValueProperty() {
        return maxValue;
    }
}
