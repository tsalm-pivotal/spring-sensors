package org.tanzu.demo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(SensorsRestController.BASE_URI)
@OpenAPIDefinition(
        tags = {@Tag(name = "Sensors", description = "API for sensor data")},
        info = @Info(title = "Sensors API", version = "1.0", description = "API for sensor data")
)
public class SensorsRestController {
    static final String BASE_URI = "/api/v1/sensors";

    private final SensorsApplicationService service;

    SensorsRestController(SensorsApplicationService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(
            summary = "Fetch data of all sensors", description = "This API returns the data of all the sensors or an " +
            "empty list if no sensor data is available", tags = {"Sensors"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType =
                    "application/json"))
    })
    public ResponseEntity<List<SensorData>> fetchSensorData() {
        return ResponseEntity.ok(service.fetchSensorData());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Fetch data for a specific sensor", description = "This API returns the data of a specific " +
            "sensor", tags = {"Sensors"},
            parameters = {
                    @Parameter(name = "id", description = "The ID of the sensor")
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType =
                    "application/json")),
            @ApiResponse(responseCode = "404", description = "No sensor for ID found")
    })
    public ResponseEntity<SensorData> fetchSensorDataById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.fetchSensorDataById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update the data for an existing sensor", description = "With this API it's possible to " +
            "update the data for an existing sensor.", tags = {"Sensors"},
            parameters = {
                    @Parameter(name = "id", description = "The ID of the sensor"),
                    @Parameter(name = "newSensorData", description = "New data for an existing sensor")
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType =
                    "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid sensor data"),
            @ApiResponse(responseCode = "404", description = "No sensor for ID found")
    })
    public ResponseEntity<SensorData> updateSensorData(@PathVariable UUID id,
                                                       @NotNull @Valid @RequestBody NewSensorData newSensorData) {
        final SensorData sensorData = service.updateSensorData(id, newSensorData);
        return ResponseEntity.ok(sensorData);
    }

    @PostMapping
    @Operation(summary = "Add the data of a new sensor", description = "With this API it's possible to " +
            "add data for a new sensor.", tags = {"Sensors"},
            parameters = {
                    @Parameter(name = "newSensorData", description = "Data for an new sensor")
            })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = "Successful operation", content = @Content(mediaType =
                    "application/json"), headers = @Header(name = "location", description = "The URL of the new sensor")
            ),
            @ApiResponse(responseCode = "400", description = "Invalid sensor data")
    })
    public ResponseEntity<SensorData> addSensorData(@NotNull @Valid @RequestBody NewSensorData newSensorData) {
        final SensorData sensorData = service.addSensorData(newSensorData);
        final URI sensorUri = URI.create(String.format("%s/%s", BASE_URI, sensorData.getId()));
        return ResponseEntity.created(sensorUri).body(sensorData);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete the data of a sensor", description = "With this API it's possible to delete the data " +
            "for a sensor", tags = {"Sensors"},
            parameters = {
                    @Parameter(name = "id", description = "The ID of the sensor"),
            })
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "No sensor for ID found")
    })
    public ResponseEntity<Void> deleteSensorData(@PathVariable UUID id) {
        service.deleteSensorData(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Void> handleException() {
        return ResponseEntity.notFound().build();
    }
}
