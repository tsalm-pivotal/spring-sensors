package org.tanzu.demo;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class SensorData {

    @Id
    private UUID id;
    private double temperature;
    private double pressure;

    public SensorData() {
    }

    public SensorData(double temperature, double pressure) {
        this(UUID.randomUUID(), temperature, pressure);
    }

    public SensorData(UUID id, double temperature, double pressure) {
        this.id = id;
        this.temperature = temperature;
        this.pressure = pressure;
    }

    public UUID getId() {
        return id;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getPressure() {
        return pressure;
    }

    public void update(double temperature, double pressure) {
        this.temperature = temperature;
        this.pressure = pressure;
    }
}
