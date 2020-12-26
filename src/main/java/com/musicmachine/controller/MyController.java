package com.musicmachine.controller;

import com.musicmachine.service.MusicService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.DirectoryChooser;
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

    @FXML
    private TableView<?> tableTrackList;

    @FXML
    private Label labelActualAuthor, labelActualAlbum;
    @FXML
    private ChoiceBox choiceboxAuthors, choiceboxAddAuthor;

    public void initialize() {
        choiceboxAddAuthor.setItems(musicService.getRegisteredAuthors());
    }

    @FXML
    public void nextAuthor(ActionEvent e) {
        //musicService.giveNextAuthor();
    }
    @FXML
    public void previousAuthor() {
        //musicService.givePreviousAuthor();
    }
    @FXML
    public void add() {
        DirectoryChooser dir = musicService.getDirectory();
    }

    public void quit() {
        musicService.exit();
    }
}
