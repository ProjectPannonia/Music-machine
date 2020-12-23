package com.musicmachine.controller;

import com.musicmachine.service.MusicService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
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

    @FXML
    private TableView<?> tableTrackList;

    @FXML
    private Label labelActualAuthor, labelActualAlbum;

    @FXML
    public void nextAuthor(ActionEvent e) {
        //musicService.giveNextAuthor();
    }
    @FXML
    public void previousAuthor() {
        //musicService.givePreviousAuthor();
    }

    public void quit() {
        musicService.exit();
    }
}
