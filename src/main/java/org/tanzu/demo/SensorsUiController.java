package org.tanzu.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SensorsUiController {

    private final SensorsApplicationService service;

    @Value("${title}")
    private String title;

    public SensorsUiController(final SensorsApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public String fetchUI(Model model) {
        model.addAttribute("sensors", service.fetchSensorData());
        model.addAttribute("title", title);
        return "index";
    }

    @PostMapping
    public String addDummySensor(Model model) {
        service.addSensorData(NewSensorData.generate());
        return fetchUI(model);
    }
}

