package com.musicmachine;

import com.musicmachine.service.MusicService;
import com.musicmachine.service.WeatherService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("music-machine.fxml")
public class MyController {

    private MusicService musicService;

    
    @Autowired
    public MyController(MusicService musicService) {
        this.musicService = musicService;
    }

//    @FXML
//    private Label weatherLabel;
//
//    public void loadWeatherForecast(ActionEvent event) {
//        this.weatherLabel.setText(weatherService.getWeatherForecast());
//    }

    public void quit(ActionEvent e) {
        Platform.exit();
    }
}
