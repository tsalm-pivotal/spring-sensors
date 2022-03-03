package org.tanzu.demo;

public class NewSensorData {
    private double temperature;
    private double pressure;

    private NewSensorData(double temperature, double pressure) {
        this.temperature = temperature;
        this.pressure = pressure;
    }

    public static NewSensorData generate() {
        return new NewSensorData(Math.random() * 100, Math.random() * 100);
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }
}
