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
    private TextField textfieldNewBand, textfieldNewAlbumsName, textfieldNewAlbumPath;
    @FXML
    private CheckBox newAuthorChb;
    @FXML
    private Button nextBandBtn, prevBandBtn, nextAlbumBtn, prevAlbumBtn, nextSongBtn, prevSongBtn, playBtn, pauseBtn, stopBtn, quitBtn, saveBtn, browseAlbumBtn;
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
        // Music service initialize
        playerService.initialize();
        labelActualAuthor.setText(playerService.getOnAirData().giveFirstElement("author"));
        labelActualAlbum.setText(playerService.getOnAirData().giveFirstElement("album"));
        labelActualSong.setText(playerService.getOnAirData().giveFirstElement("song"));
        choiceboxAddAuthor.setItems(registerService.getRegisteredAuthors());
        textfieldNewBand.setDisable(true);
        newAuthorChb.setSelected(false);
        initializeUserInterface();
        //timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println("I would called every 2 seconds!");
//            }
//        },0,2000);
    }

    private void initializeUserInterface() {

    }

    @FXML
    public void buttonHandler(ActionEvent e) {
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
        } else if (e.getSource() == playBtn) {
            String author = labelActualAuthor.getText();
            String album = labelActualAlbum.getText();
            String song = labelActualSong.getText();
            System.out.println("Playing: " + author + ", " + album + ", " +song);
            playerService.refreshOnAirData(labelActualAuthor.getText(), labelActualAlbum.getText(), labelActualSong.getText());
            playerService.continousPlay();
        } else if (e.getSource() == pauseBtn) {
            playerService.pauseSongOnAir();
        } else if (e.getSource() == stopBtn) {
            playerService.stopSongOnAir();
        } else if (e.getSource() == quitBtn) {
            registerService.exit();
        } else if (e.getSource() == saveBtn) {
            String responseAfterSave;

            if (newAuthorChb.isSelected()) {
                responseAfterSave = registerService.saveNewAuthor(textfieldNewBand.getText(), textfieldNewAlbumsName.getText(), textfieldNewAlbumPath.getText());
            } else {
                responseAfterSave = registerService.saveNewAlbumForAuthor(choiceboxAddAuthor.getValue().toString(), textfieldNewAlbumsName.getText(), textfieldNewAlbumPath.getText());
            }
            labelSaveResponse.setText(responseAfterSave);
        } else if (e.getSource() == browseAlbumBtn) {
            //DirectoryChooser dir = registerService.getDirectory();
            File files = registerService.getDirectory().showDialog(null);
            textfieldNewAlbumPath.setText(files.getAbsolutePath() + "\\");
        } else if (e.getSource() == newAuthorChb) {
            if (newAuthorChb.isSelected()) {
                textfieldNewBand.setDisable(false);
                choiceboxAddAuthor.setDisable(true);
            } else {
                textfieldNewBand.setDisable(true);
                choiceboxAddAuthor.setDisable(false);
            }
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
}
