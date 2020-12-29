package com.musicmachine.controller;

import com.musicmachine.service.RegisterService;
import com.musicmachine.service.PlayerService;
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

    @FXML
    private TableView<?> tableTrackList, tableAdd;
    @FXML
    private Label labelActualAuthor, labelActualAlbum, labelSaveResponse, labelActualSong;
    @FXML
    private ChoiceBox choiceboxAuthors, choiceboxAddAuthor;
    @FXML
    private TextField textfieldNewBand, textfieldNewAlbumsName, textfieldNewAlbumPath;
    @FXML
    private CheckBox checkboxNewAuthor;

    private PlayerService playerService;
    private RegisterService registerService;

    @Autowired
    public MyController(PlayerService playerService, RegisterService registerService) {
        this.playerService = playerService;
        this.registerService = registerService;
    }

    private String actualAuthorName;
    private String actualAlbumName;
    private String actualSongName;

    public void initialize() {
        // MUSIC-BOX PAGE
        // Music service initialize
        playerService.initialize();
        // Get first band name from database
        actualAuthorName = playerService.getFirstBandName();
        // Initialize Actual author label
        labelActualAuthor.setText(actualAuthorName);
        // Initialize Actual album label with actual author's first registered album
        actualAlbumName = playerService.getFirstAlbumFromThisAuthor();
        labelActualAlbum.setText(actualAlbumName);
        // Get first song name from album
        actualSongName = playerService.getFirstSongfFromAlbum(actualAlbumName);
        labelActualSong.setText(actualSongName);
        // ADD PAGE
        // Add page choose author choicebox initialize -> get registered authors from database
        choiceboxAddAuthor.setItems(registerService.getRegisteredAuthors());
        tableAdd.setEditable(true);
        textfieldNewBand.setDisable(true);
        checkboxNewAuthor.setSelected(false);

    }

    @FXML
    public void nextAuthor() {
        String nextAuthor = registerService.giveNextAuthor();
        System.out.println("Next author: " + nextAuthor);
//        albumList = songService.listAlbumNames(nextAuthor);
//        songList = songService.getSongsByAlbumName(nextAuthor);
        labelActualAuthor.setText(nextAuthor);
    }
    @FXML
    public void previousAuthor() {
        String previousAuthor = registerService.givePreviousAuthor();
        labelActualAuthor.setText(previousAuthor);
    }
    @FXML
    public void add() {
        DirectoryChooser dir = registerService.getDirectory();
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
            responseAfterSave = registerService.saveNewAuthor(textfieldNewBand.getText(), textfieldNewAlbumsName.getText(), textfieldNewAlbumPath.getText());
        } else {
            responseAfterSave = registerService.saveNewAlbumForAuthor(choiceboxAddAuthor.getValue().toString(), textfieldNewAlbumsName.getText(), textfieldNewAlbumPath.getText());
        }
        labelSaveResponse.setText(responseAfterSave);
    }

    public void quit() {
        registerService.exit();
    }
}
