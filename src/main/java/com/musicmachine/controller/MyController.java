package com.musicmachine.controller;

import com.musicmachine.service.PlayerService;
import com.musicmachine.service.RegisterService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Timer;

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
    private TextField textfieldNewBand, textfieldNewAlbumsName, textfieldNewAlbumPath, textfieldAlbumCoverFront, textfieldAlbumCoverBack;
    @FXML
    private CheckBox newAuthorChb, albumCoverFrontCb, albumCoverBackCb;
    @FXML
    private Button nextBandBtn, prevBandBtn, nextAlbumBtn, prevAlbumBtn, nextSongBtn, prevSongBtn, playBtn, pauseBtn, stopBtn, quitBtn, saveBtn, browseAlbumBtn, browseFrontCoverBtn, browseBackCoverBtn;
    @FXML
    private ImageView coverImgView;

    private Timer timer;
    private PlayerService playerService;
    private RegisterService registerService;

    @Autowired
    public MyController(PlayerService playerService, RegisterService registerService) {
        this.playerService = playerService;
        this.registerService = registerService;
    }


    public void initialize() {
        playerService.initialize();
        initializeUserInterface();
    }

    private void initializeUserInterface() {
        System.out.println(playerService.dbNotEmpty());
        if (playerService.dbNotEmpty()) {
            labelActualAuthor.setText(playerService.getOnAirData().giveFirstElement("author"));
            labelActualAlbum.setText(playerService.getOnAirData().giveFirstElement("album"));
            labelActualSong.setText(playerService.getOnAirData().giveFirstElement("song"));
            choiceboxAddAuthor.setItems(registerService.getRegisteredAuthors());
        }
        textfieldNewBand.setDisable(true);
        newAuthorChb.setSelected(false);
        albumCoverFrontCb.setSelected(false);
        textfieldAlbumCoverFront.setDisable(true);
        albumCoverBackCb.setSelected(false);
        browseFrontCoverBtn.setDisable(true);
        textfieldAlbumCoverBack.setDisable(true);
        browseBackCoverBtn.setDisable(true);
    }

    @FXML
    public void handlePlayStopPause(ActionEvent e) {
        if (e.getSource() == playBtn) {
            playerService.refreshOnAirData(labelActualAuthor.getText(), labelActualAlbum.getText(), labelActualSong.getText());
            playerService.continousPlay();
        } else if (e.getSource() == pauseBtn) {
            playerService.pauseSongOnAir();
        } else if (e.getSource() == stopBtn) {
            playerService.stopSongOnAir();
        }
    }

    @FXML
    public void handleSongFields(ActionEvent e) {
        if (e.getSource() == nextBandBtn) {
            updateFields(playerService.nextBand(), playerService.getOnAirData().getActualBandAlbums().get(0), playerService.getOnAirData().getActualAlbumTrackList().get(0));
        } else if (e.getSource() == prevBandBtn) {
            updateFields(playerService.previousBand(), playerService.getOnAirData().getActualBandAlbums().get(0), playerService.getOnAirData().getActualAlbumTrackList().get(0));
        } else if (e.getSource() == nextAlbumBtn) {
            updateFields(playerService.giveNextAlbum(), playerService.getOnAirData().getActualAlbumTrackList().get(0));
        } else if (e.getSource() == prevAlbumBtn) {
            updateFields(playerService.givePreviousAlbum(), playerService.getOnAirData().getActualAlbumTrackList().get(0));
        } else if (e.getSource() == nextSongBtn) {
            updateFields(playerService.giveNextSong());
        } else if (e.getSource() == prevSongBtn) {
            updateFields(playerService.givePreviousSong());
        }
    }

    private void updateFields(String... arg) {
        switch (arg.length) {
            case 1:
                labelActualSong.setText(arg[0]);
                break;
            case 2:
                labelActualAlbum.setText(arg[0]);
                labelActualSong.setText(arg[1]);
                break;
            case 3:
                labelActualAuthor.setText(arg[0]);
                labelActualAlbum.setText(arg[1]);
                labelActualSong.setText(arg[2]);
                break;
        }
    }

    @FXML
    public void handleRegister(ActionEvent e) {
        if (e.getSource() == saveBtn) {
            String newAlbumName = textfieldNewAlbumsName.getText();
            String newAlbumPath = textfieldNewAlbumPath.getText();
            System.out.println("front cb is selected: " + albumCoverFrontCb.isSelected());
            String albumCoverFront = albumCoverFrontCb.isSelected() ? textfieldAlbumCoverFront.getText() : null;
            String albumCoverBack = albumCoverBackCb.isSelected() ? textfieldAlbumCoverBack.getText() : null;

            String responseAfterSave = registerService.registerNewBand(newAuthorChb.isSelected() ? textfieldNewBand.getText() : choiceboxAddAuthor.getValue().toString(),
                    newAlbumName, newAlbumPath, albumCoverFront, albumCoverBack);;

            if (newAuthorChb.isSelected()) {
//                responseAfterSave = registerService.saveNewAuthor(textfieldNewBand.getText(), textfieldNewAlbumsName.getText(), textfieldNewAlbumPath.getText());
                registerService.registerNewBand(textfieldNewBand.getText(), newAlbumName, newAlbumPath, albumCoverFront, albumCoverBack);
            } else {
                responseAfterSave = registerService.saveNewAlbumForAuthor(choiceboxAddAuthor.getValue().toString(), textfieldNewAlbumsName.getText(), textfieldNewAlbumPath.getText(), albumCoverFront, albumCoverBack);
            }
            labelSaveResponse.setText(responseAfterSave);
        } else if (e.getSource() == browseAlbumBtn) {
            textfieldNewAlbumPath.setText(registerService.getDirectory().showDialog(null).getAbsolutePath() + "\\");
        } else if (e.getSource() == browseFrontCoverBtn) {
            textfieldAlbumCoverFront.setText(registerService.getFileChooser().showOpenDialog(null).getAbsolutePath() + "\\");
        } else if (e.getSource() == browseBackCoverBtn) {
            textfieldAlbumCoverBack.setText(registerService.getFileChooser().showOpenDialog(null).getAbsolutePath() + "\\");
        } else if (e.getSource() == albumCoverFrontCb) {
            textfieldAlbumCoverFront.setDisable(false);
            browseFrontCoverBtn.setDisable(false);
        } else if (e.getSource() == albumCoverBackCb) {
            textfieldAlbumCoverBack.setDisable(false);
            browseBackCoverBtn.setDisable(false);
        }
    }

    @FXML
    public void handleRegisterCheckboxes(ActionEvent e) {
        if (e.getSource() == newAuthorChb) {
            refreshFieldsWhenNewlyRegistered();
        } else if (e.getSource() == albumCoverFrontCb) {
            refreshFieldsFrontCover();
        } else if (e.getSource() == albumCoverBackCb) {
            refreshFieldBackCover();
        }
    }

    private void refreshFieldsWhenNewlyRegistered() {
        if (newAuthorChb.isSelected()) {
            textfieldNewBand.setDisable(false);
            choiceboxAddAuthor.setDisable(true);
        } else {
            textfieldNewBand.setDisable(true);
            choiceboxAddAuthor.setDisable(false);
        }
    }

    private void refreshFieldsFrontCover() {
        if (albumCoverFrontCb.isSelected()) {
            textfieldAlbumCoverFront.setDisable(false);
            browseFrontCoverBtn.setDisable(false);
        } else {
            textfieldAlbumCoverFront.setDisable(true);
            browseFrontCoverBtn.setDisable(true);
        }
    }

    private void refreshFieldBackCover() {
        if (albumCoverBackCb.isSelected()) {
            textfieldAlbumCoverBack.setDisable(false);
            browseBackCoverBtn.setDisable(false);
        } else {
            textfieldAlbumCoverBack.setDisable(true);
            browseBackCoverBtn.setDisable(true);
        }
    }

    @FXML
    public void handleQuit() {
        playerService.stopSongOnAir();
        registerService.exit();
    }
}
