package com.musicmachine.controller;

import com.musicmachine.service.PlayerQuarterMasterService;
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
    private PlayerQuarterMasterService playerQuarterMasterService;

    @Autowired
    public MyController(PlayerService playerService, RegisterService registerService, PlayerQuarterMasterService playerQuarterMasterService) {
        this.playerService = playerService;
        this.registerService = registerService;
        this.playerQuarterMasterService = playerQuarterMasterService;
    }


    public void initialize() {
        // MUSIC-BOX PAGE
        // Music service initialize
        playerService.initialize();
        labelActualAuthor.setText(playerService.getOnAirData().giveFirstElement("author"));
        labelActualAlbum.setText(playerService.getOnAirData().giveFirstElement("album"));
        labelActualSong.setText(playerService.getOnAirData().giveFirstElement("song"));
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
        labelActualAuthor.setText(playerService.nextBand());
        labelActualAlbum.setText(playerService.getOnAirData().getActualBandAlbums().get(0));
        labelActualSong.setText(playerService.getOnAirData().getActualAlbumTrackList().get(0));
    }
    @FXML
    public void previousBand() {
        labelActualAuthor.setText(playerService.previousBand());
        labelActualAlbum.setText(playerService.getOnAirData().getActualBandAlbums().get(0));
        labelActualSong.setText(playerService.getOnAirData().getActualAlbumTrackList().get(0));

    }


    @FXML
    public void nextAlbum() {
        labelActualAlbum.setText(playerService.giveNextAlbum());
        labelActualSong.setText(playerService.getOnAirData().getActualAlbumTrackList().get(0));
    }
    @FXML
    public void previousAlbum() {
        labelActualAlbum.setText(playerService.givePreviousAlbum());
        labelActualSong.setText(playerService.getOnAirData().getActualAlbumTrackList().get(0));
    }


    @FXML
    public void nextSong() {
        labelActualSong.setText(playerService.giveNextSong());
    }
    @FXML
    public void previousSong() {
        labelActualSong.setText(playerService.givePreviousSong());
    }


    @FXML
    public void play() {
        String bandName = labelActualAuthor.getText();
        String albumName = labelActualAlbum.getText();
        String songName = labelActualSong.getText();
        playerService.play(bandName,albumName,songName);
    }
    @FXML
    public void stop() {

        //playerQuarterMasterService.stopSong();
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
