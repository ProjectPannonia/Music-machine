package com.musicmachine.controller;

import com.musicmachine.service.MusicService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@FxmlView("music-machine.fxml")
public class MyController {

    private MusicService musicService;

    @Autowired
    public MyController(MusicService musicService) {
        this.musicService = musicService;
    }

    @FXML
    private TableView<?> tableTrackList, tableAdd;

    @FXML
    private Label labelActualAuthor, labelActualAlbum, labelSaveResponse;

    @FXML
    private ChoiceBox choiceboxAuthors, choiceboxAddAuthor;
    @FXML
    private TextField textfieldNewBand, textfieldNewAlbumsName, textfieldNewAlbumPath;
    @FXML
    private CheckBox checkboxNewAuthor;

    public void initialize() {
        musicService.initialize();
        choiceboxAddAuthor.setItems(musicService.getRegisteredAuthors());
        tableAdd.setEditable(true);
        textfieldNewBand.setDisable(true);
        checkboxNewAuthor.setSelected(false);
        labelActualAuthor.setText(musicService.getFirstBandName());

    }

    @FXML
    public void nextAuthor() {
        labelActualAuthor.setText(musicService.giveNextAuthor());
    }
    @FXML
    public void previousAuthor() {
        labelActualAuthor.setText(musicService.givePreviousAuthor());
    }
    @FXML
    public void add() {
        DirectoryChooser dir = musicService.getDirectory();
        File files = dir.showDialog(null);

        textfieldNewAlbumPath.setText(files.getAbsolutePath() + "\\");
    }
    @FXML
    public void newAuthorChecked() {
        if(checkboxNewAuthor.isSelected()){
            textfieldNewBand.setDisable(false);
            choiceboxAddAuthor.setDisable(true);
        }else{
            textfieldNewBand.setDisable(true);
            choiceboxAddAuthor.setDisable(false);
        }
    }
    @FXML
    public void save() {
        String responseAfterSave;

        if(checkboxNewAuthor.isSelected()) {
            responseAfterSave = musicService.saveNewAuthor(textfieldNewBand.getText(), textfieldNewAlbumsName.getText(), textfieldNewAlbumPath.getText());
        } else {
            responseAfterSave = musicService.saveNewAlbumForAuthor(choiceboxAddAuthor.getValue().toString(), textfieldNewAlbumsName.getText(), textfieldNewAlbumPath.getText());
        }
        labelSaveResponse.setText(responseAfterSave);
    }

    public void quit() {
        musicService.exit();
    }
}
