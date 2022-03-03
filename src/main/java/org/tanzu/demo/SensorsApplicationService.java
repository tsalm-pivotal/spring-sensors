package org.tanzu.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SensorsApplicationService {

    private final Logger log = LoggerFactory.getLogger(SensorsDataSink.class);

    private final SensorRepository sensorRepository;

    public SensorsApplicationService(final SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public List<SensorData> fetchSensorData() {
        return sensorRepository.findAll();
    }

    public SensorData fetchSensorDataById(UUID id) throws NotFoundException  {
        final Optional<SensorData> sensorData = sensorRepository.findById(id);
        if (sensorData.isEmpty()) {
            throw new NotFoundException("Sensor not found");
        }
        return sensorData.get();
    }

    public void deleteSensorData(UUID id) throws NotFoundException {
        fetchSensorDataById(id);
        sensorRepository.deleteById(id);
    }

    public SensorData addSensorData(NewSensorData newSensorData) {
        final SensorData sensorData = new SensorData(newSensorData.getTemperature(), newSensorData.getPressure());
        return sensorRepository.save(sensorData);
    }

    public SensorData updateSensorData(UUID id, NewSensorData newSensorData) throws NotFoundException  {
        final SensorData sensorData = fetchSensorDataById(id);
        sensorData.update(newSensorData.getTemperature(), newSensorData.getPressure());
        return sensorRepository.save(sensorData);
    }
}
