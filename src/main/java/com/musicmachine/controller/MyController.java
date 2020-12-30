package com.musicmachine.controller;

import com.musicmachine.service.PlayerQuarterMaster;
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

    private Thread playThread;

    private PlayerService playerService;
    private RegisterService registerService;
    private PlayerQuarterMaster playerQuarterMaster;

    @Autowired
    public MyController(PlayerService playerService, RegisterService registerService, PlayerQuarterMaster playerQuarterMaster) {
        this.playerService = playerService;
        this.registerService = registerService;
        this.playerQuarterMaster = playerQuarterMaster;
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

        playThread = new Thread();
    }

    @FXML
    public void nextBand() {
        // Update -> band name
        String nextBandName = playerService.giveNextBandName();
        String nextBandFirstAlbum = playerService.getBandFirstAlbum();
        String nextAlbumFirstSongName = playerService.getFirstSongNameFromAlbum();
        labelActualAuthor.setText(nextBandName);
        labelActualSong.setText(nextAlbumFirstSongName);
        labelActualAlbum.setText(nextBandFirstAlbum);
    }
    @FXML
    public void previousBand() {
        String previousBandName = playerService.givePreviousBandName();
        String previousBandFirstAlbumName = playerService.getBandFirstAlbum();
        String previousBandFirstSongName = playerService.getFirstSongNameFromAlbum();
        labelActualAuthor.setText(previousBandName);
        labelActualAlbum.setText(previousBandFirstAlbumName);
        labelActualSong.setText(previousBandFirstSongName);

    }
    @FXML
    public void nextAlbum() {
        String nextAlbum = playerService.giveNextAlbum();
        String nextAlbumFirstSong = playerService.getFirstSongNameFromAlbum();
        labelActualAlbum.setText(nextAlbum);
        labelActualSong.setText(nextAlbumFirstSong);
    }
    @FXML
    public void previousAlbum() {
        String previousAlbum = playerService.givePreviousAlbum();
        String previousAlbumFirstSong = playerService.getFirstSongNameFromAlbum();
        labelActualAlbum.setText(previousAlbum);
        labelActualSong.setText(previousAlbumFirstSong);
    }
    @FXML
    public void nextSong() {
        String nextSong = playerService.giveNextSong();
        labelActualSong.setText(nextSong);
    }
    @FXML
    public void previousSong() {
        String previousSong = playerService.givePreviousSong();
        labelActualSong.setText(previousSong);
    }
    @FXML
    public void play() {
        String bandName = labelActualAuthor.getText();
        String albumName = labelActualAlbum.getText();
        String songName = labelActualSong.getText();

        playerQuarterMaster.play(bandName,albumName,songName);
    }
    @FXML
    public void stop() {
        playerQuarterMaster.stopSong();
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
