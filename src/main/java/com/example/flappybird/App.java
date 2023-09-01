package com.example.flappybird;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class App implements Initializable
{
    @FXML
    private Label appNameLabel;
    @FXML
    public AnchorPane mainAnchorPane;
    @FXML
    private Pane leftPane;
    @FXML
    private ImageView birdImageView;
    @FXML
    private Button playButton;
    @FXML
    private Label bestScoreLabel;
    @FXML
    private Label scoreAmountLabel;
    @FXML
    private Label scoreLabel;
    @FXML
    private ImageView backgroundImageView;
    @FXML
    private ImageView settingsImageView;
    @FXML
    private Button settingsButton;
    @FXML
    private ImageView creditsImageView;
    @FXML
    private Button creditsButton;
    @FXML
    private ImageView resetImageView;
    @FXML
    private Button resetButton;
    @FXML
    private Pane settingsPane;
    @FXML
    private Label settingsPaneLabel;
    @FXML
    private Slider difficultySlider;
    @FXML
    private Slider musicVolumeSlider;
    @FXML
    private Slider soundVolumeSlider;
    @FXML
    private Label diffLabel;
    @FXML
    private Label settingsLabel;
    @FXML
    private Label creditsLabel;
    @FXML
    private Label resetLabel;
    @FXML
    private Label bestScoreSavingLabel;
    @FXML
    private Label musicLabel;
    @FXML
    private Label soundLabel;
    @FXML
    private Label confirmLabel;
    @FXML
    private Button yesButton;
    @FXML
    private Button noButton;
    @FXML
    private Button closeSettingsButton;
    @FXML
    private CheckBox saveScoreCheckBox;
    @FXML
    private Button closeCreditsButton;
    @FXML
    private Pane creditsPane;
    @FXML
    private Label creditsPaneLabel;
    @FXML
    private Label madeByLabel;
    @FXML
    private Label contactViaLabel;
    @FXML
    private Label frontendLabel;
    @FXML
    private Label backendLabel;

    public static boolean saveBestScore=false;
    public static int numberOfObstacles=4;
    Thread obstaclesThread;
    Thread birdThread;
    Rectangle[] upperObstaclesRoot =new Rectangle[numberOfObstacles];
    Rectangle[] upperObstaclesHead =new Rectangle[numberOfObstacles];
    Rectangle[] lowerObstaclesHead =new Rectangle[numberOfObstacles];
    Rectangle[] lowerObstaclesRoot =new Rectangle[numberOfObstacles];
    public static int playPressed =0;
    public static int gameStarted =0;
    public static int birdAutoMove=0;
    public static MediaPlayer menuAudioPlayer;
    public static MediaPlayer gameAudioPlayer;
    public static MediaPlayer failAudioPlayer;
    public static MediaPlayer jumpAudioPlayer;
    public static double musicVolume=0.0;
    public static double soundVolume =0.1;
    public static double obstacleSpeed =2.0/5.0+1.5;
    private double currentUpperObstaclePosition =0;
    private boolean playButtonEffectActive =false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        setupButtonsEvents();
        setupAudio();
        setupUI();
        setupObstacles();

        obstaclesThread = new Obstacles(upperObstaclesRoot, upperObstaclesHead, lowerObstaclesHead, lowerObstaclesRoot,birdImageView,
                mainAnchorPane,leftPane,bestScoreLabel,scoreLabel, scoreAmountLabel,playButton,settingsPane,creditsPane);
        obstaclesThread.setDaemon(true);
        obstaclesThread.start();
        backgroundImageView.toBack();

        birdThread = new Bird(upperObstaclesRoot, upperObstaclesHead, lowerObstaclesHead, lowerObstaclesRoot,birdImageView,mainAnchorPane);
        birdThread.setDaemon(true);
        birdThread.start();

        birdAutoMove=1;

        obstaclesThreadHandler();
    }

    private void setupObstacles()
    {
        Image bricks = new Image("bricks.jpg");
        for(int i=0;i<numberOfObstacles;i++)
        {
            Rectangle upperObstacle1=new Rectangle();
            Rectangle upperObstacle2=new Rectangle();
            Rectangle lowerObstacle1=new Rectangle();
            Rectangle lowerObstacle2=new Rectangle();

            upperObstaclesRoot[i]=upperObstacle1;
            upperObstaclesHead[i]=upperObstacle2;
            lowerObstaclesHead[i]=lowerObstacle1;
            lowerObstaclesRoot[i]=lowerObstacle2;

            mainAnchorPane.getChildren().add(upperObstaclesRoot[i]);
            mainAnchorPane.getChildren().add(upperObstaclesHead[i]);
            mainAnchorPane.getChildren().add(lowerObstaclesHead[i]);
            mainAnchorPane.getChildren().add(lowerObstaclesRoot[i]);

            upperObstaclesRoot[i].setWidth(32);
            upperObstaclesHead[i].setWidth(39);
            lowerObstaclesHead[i].setWidth(39);
            lowerObstaclesRoot[i].setWidth(32);

            Random random=new Random();
            int randHeight=random.nextInt(200 - 50 + 1) + 50;
            upperObstaclesRoot[i].setHeight(randHeight);
            upperObstaclesHead[i].setHeight(23);
            lowerObstaclesHead[i].setHeight(23);
            lowerObstaclesRoot[i].setHeight(260-randHeight);

            upperObstaclesRoot[i].setLayoutX(50+i*200);
            upperObstaclesHead[i].setLayoutX(47+i*200);
            lowerObstaclesHead[i].setLayoutX(47+i*200);
            lowerObstaclesRoot[i].setLayoutX(50+i*200);

            upperObstaclesRoot[i].setLayoutY(0);
            upperObstaclesHead[i].setLayoutY(randHeight);
            lowerObstaclesHead[i].setLayoutY(randHeight+23+95);
            lowerObstaclesRoot[i].setLayoutY(randHeight+23+95+23);

            upperObstaclesRoot[i].setFill(new ImagePattern(bricks));
            upperObstaclesHead[i].setFill(new ImagePattern(bricks));
            lowerObstaclesHead[i].setFill(new ImagePattern(bricks));
            lowerObstaclesRoot[i].setFill(new ImagePattern(bricks));

            upperObstaclesRoot[i].setStroke(Color.BLACK);
            upperObstaclesHead[i].setStroke(Color.BLACK);
            lowerObstaclesHead[i].setStroke(Color.BLACK);
            lowerObstaclesRoot[i].setStroke(Color.BLACK);

            upperObstaclesRoot[i].toBack();
            upperObstaclesHead[i].toBack();
            lowerObstaclesHead[i].toBack();
            lowerObstaclesRoot[i].toBack();

            upperObstaclesRoot[i].setCache(true);
            upperObstaclesRoot[i].setCacheHint(CacheHint.SPEED);

            upperObstaclesHead[i].setCache(true);
            upperObstaclesHead[i].setCacheHint(CacheHint.SPEED);

            lowerObstaclesHead[i].setCache(true);
            lowerObstaclesHead[i].setCacheHint(CacheHint.SPEED);

            lowerObstaclesRoot[i].setCache(true);
            lowerObstaclesRoot[i].setCacheHint(CacheHint.SPEED);
        }

        birdImageView.setCache(true);
        birdImageView.setCacheHint(CacheHint.SPEED);
    }

    private void setupUI()
    {
        difficultySlider.setValue((App.obstacleSpeed-1.3)*5);
        musicVolumeSlider.setValue(musicVolume);
        soundVolumeSlider.setValue(soundVolume);
        saveScoreCheckBox.setSelected(saveBestScore);

        confirmLabel.setOpacity(0);
        yesButton.setOpacity(0);
        noButton.setOpacity(0);

        settingsPane.setLayoutX(0);
        settingsPane.setVisible(true);
        creditsPane.setLayoutX(0);
        creditsPane.setVisible(true);

        appNameLabel.setStyle("-fx-font-family: Gameplay; -fx-font-size:25");
        diffLabel.setStyle("-fx-font-family: Gameplay; -fx-font-size:11");
        settingsLabel.setStyle("-fx-font-family: Gameplay; -fx-font-size:11");
        creditsLabel.setStyle("-fx-font-family: Gameplay; -fx-font-size:11");
        resetLabel.setStyle("-fx-font-family: Gameplay; -fx-font-size:11");
        confirmLabel.setStyle("-fx-font-family: Gameplay; -fx-font-size:11");
        yesButton.setStyle("-fx-font-family: Gameplay; -fx-font-size:9; -fx-effect:  dropshadow(three-pass-box, rgba(0,0,0,0.8), 7, 0, 0, 0)");
        noButton.setStyle("-fx-font-family: Gameplay; -fx-font-size:9; -fx-effect:  dropshadow(three-pass-box, rgba(0,0,0,0.8), 7, 0, 0, 0)");
        playButton.setStyle("-fx-font-family: Gameplay; -fx-font-size:14; -fx-background-color:  #faf6ed; " +
                "-fx-effect:  dropshadow(three-pass-box, rgba(0,0,0,0.8), 15, 0, 0, 0)");
        bestScoreLabel.setStyle("-fx-font-family: Gameplay; -fx-font-size:14");
        scoreAmountLabel.setStyle("-fx-font-family: Gameplay; -fx-font-size:14");
        scoreLabel.setStyle("-fx-font-family: Gameplay; -fx-font-size:14");

        creditsPaneLabel.setStyle("-fx-font-family: Gameplay; -fx-font-size:15");
        madeByLabel.setStyle("-fx-font-family: Gameplay; -fx-font-size:13");
        contactViaLabel.setStyle("-fx-font-family: Gameplay; -fx-font-size:11");
        frontendLabel.setStyle("-fx-font-family: Gameplay; -fx-font-size:10");
        backendLabel.setStyle("-fx-font-family: Gameplay; -fx-font-size:10");

        settingsPaneLabel.setStyle("-fx-font-family: Gameplay; -fx-font-size:15");
        bestScoreSavingLabel.setStyle("-fx-font-family: Gameplay; -fx-font-size:11");
        musicLabel.setStyle("-fx-font-family: Gameplay; -fx-font-size:11");
        soundLabel.setStyle("-fx-font-family: Gameplay; -fx-font-size:11");

        DropShadow shadow = new DropShadow( BlurType.THREE_PASS_BOX, Color.color(0,0,0,0.5), 15, 0, 4, 2);
        bestScoreLabel.setEffect(shadow);
        scoreLabel.setEffect(shadow);
        scoreAmountLabel.setEffect(shadow);
        scoreAmountLabel.setText(Integer.toString(Obstacles.bestScore));
    }

    private void obstaclesThreadHandler()
    {
        if(currentUpperObstaclePosition ==upperObstaclesRoot[0].getLayoutX())
        {
            obstaclesThread.interrupt();
            obstaclesThread.start();
            System.out.println("Thread \"obstaclesThread\" was refreshed due to unexpected freeze.");
        }
        Timeline threadCheckerTimeline = new Timeline(new KeyFrame(Duration.seconds(1)));
        threadCheckerTimeline.setOnFinished(event->obstaclesThreadHandler());
        threadCheckerTimeline.play();
        currentUpperObstaclePosition =upperObstaclesRoot[0].getLayoutX();

    }

    @FXML
    private void onPlayClick()
    {
        if(!resetButton.isVisible())
        {
            confirmFadeOut();
        }

        birdImageView.getScene().setCursor(Cursor.HAND);
        settingsPane.setVisible(false);
        creditsPane.setVisible(false);
        birdThread.interrupt();

        failAudioPlayer.stop();
        menuAudioPlayer.stop();
        gameAudioPlayer.play();
        Obstacles.newScore=0;
        Obstacles.gameWereStopped =0;
        birdAutoMove=0;

        birdImageView.setRotate(0);
        playButton.setDisable(true);
        TranslateTransition leftPaneHideTranslate = new TranslateTransition(new Duration(800));
        leftPaneHideTranslate.setToX(-350);
        leftPaneHideTranslate.setNode(leftPane);
        leftPaneHideTranslate.play();

        TranslateTransition bestScoreSlideTranslate = new TranslateTransition(new Duration(800));
        bestScoreSlideTranslate.setToX(-200);
        bestScoreSlideTranslate.setNode(bestScoreLabel);
        bestScoreSlideTranslate.setOnFinished(event->bestScoreHide());
        bestScoreSlideTranslate.play();

        TranslateTransition scoreSlideTranslate = new TranslateTransition(new Duration(800));
        scoreSlideTranslate.setToX(-200);
        scoreSlideTranslate.setNode(scoreAmountLabel);
        scoreSlideTranslate.play();

        for(int i=0;i<4;i++)
        {
            fadeObstaclesOut(upperObstaclesRoot[i]);
            fadeObstaclesOut(upperObstaclesHead[i]);
            fadeObstaclesOut(lowerObstaclesHead[i]);
            fadeObstaclesOut(lowerObstaclesRoot[i]);
        }

        TranslateTransition birdCenterTranslate = new TranslateTransition(new Duration(800));
        birdCenterTranslate.setToX(-200);
        birdCenterTranslate.setToY(0);
        birdCenterTranslate.setNode(birdImageView);
        birdCenterTranslate.play();

        TranslateTransition birdJumpTranslate = new TranslateTransition(new Duration(300));
        birdJumpTranslate.setByY(-40);
        birdJumpTranslate.setNode(birdImageView);
        birdJumpTranslate.setOnFinished(event->birdGravity());

        RotateTransition birdJumpRotate = new RotateTransition();
        birdJumpRotate.setAxis(Rotate.Z_AXIS);
        birdJumpRotate.setToAngle(-30);
        birdJumpRotate.setDuration(Duration.millis(300));
        birdJumpRotate.setNode(birdImageView);

        mainAnchorPane.setOnMousePressed(event->{
            if(gameStarted==0&&birdImageView.getTranslateY()==0)
            {
                gameStarted=1;
                birdJumpRotate.setFromAngle(birdImageView.getRotate());
                birdJumpTranslate.play();
                birdJumpRotate.play();
            }
            else if(Obstacles.gameWereStopped ==0&&gameStarted==1)
            {
                jumpAudioPlayer.stop();
                jumpAudioPlayer.play();
                birdJumpRotate.stop();
                birdJumpTranslate.stop();
                birdJumpRotate.setFromAngle(birdImageView.getRotate());
                birdJumpTranslate.play();
                birdJumpRotate.play();

            }
        });
    }

    private void fadeObstaclesOut(Node node)
    {
        FadeTransition obstaclesFadeOutTransition = new FadeTransition(Duration.millis(1000), node);
        obstaclesFadeOutTransition.setFromValue(1);
        obstaclesFadeOutTransition.setToValue(0);
        obstaclesFadeOutTransition.setOnFinished(event->hideObstacles(node));
        obstaclesFadeOutTransition.play();
    }

    private void hideObstacles(Node node)
    {
        node.setVisible(false);
        playPressed=1;
    }

    private void birdGravity()
    {
        TranslateTransition gravityTranslate = new TranslateTransition(new Duration(2000));
        gravityTranslate.setByY(500);
        gravityTranslate.setNode(birdImageView);

        RotateTransition gravityRotate = new RotateTransition();
        gravityRotate.setAxis(Rotate.Z_AXIS);
        gravityRotate.setFromAngle(-30);
        gravityRotate.setToAngle(30);
        gravityRotate.setDuration(Duration.millis(500));
        gravityRotate.setNode(birdImageView);

        gravityTranslate.play();
        gravityRotate.play();
    }

    private void bestScoreHide()
    {
        TranslateTransition bestScoreHideTranslate = new TranslateTransition(new Duration(500));
        bestScoreHideTranslate.setToY(-83);
        bestScoreHideTranslate.setNode(bestScoreLabel);
        bestScoreHideTranslate.setOnFinished(event->scoreShow());
        bestScoreHideTranslate.play();
    }

    private void scoreShow()
    {
        TranslateTransition scoreShowTranslate = new TranslateTransition(new Duration(500));
        scoreShowTranslate.setToY(83);
        scoreShowTranslate.setNode(scoreLabel);
        scoreShowTranslate.setOnFinished(e->scoreAmountLabel.setText("0"));
        scoreShowTranslate.play();
    }

    private void setupAudio()
    {
        Media media;
        try {
            media = new Media(Objects.requireNonNull(getClass().getResource("/soundtrack.mp3")).toURI().toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        menuAudioPlayer = new MediaPlayer(media);
        menuAudioPlayer.setVolume(musicVolume);
        menuAudioPlayer.setOnEndOfMedia(() -> menuAudioPlayer.seek(Duration.ZERO));
        menuAudioPlayer.play();

        Media media2;
        try {
            media2 = new Media(Objects.requireNonNull(getClass().getResource("/gameSoundtrack.mp3")).toURI().toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        gameAudioPlayer = new MediaPlayer(media2);
        gameAudioPlayer.setVolume(musicVolume);
        gameAudioPlayer.setOnEndOfMedia(() -> gameAudioPlayer.seek(Duration.ZERO));

        Media media3;
        try {
            media3 = new Media(Objects.requireNonNull(getClass().getResource("/gameLost.mp3")).toURI().toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        failAudioPlayer = new MediaPlayer(media3);
        failAudioPlayer.setVolume(soundVolume);
        App.failAudioPlayer.setOnEndOfMedia(() -> App.menuAudioPlayer.play());

        Media media4;
        try {
            media4 = new Media(Objects.requireNonNull(getClass().getResource("/jump.mp3")).toURI().toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        jumpAudioPlayer = new MediaPlayer(media4);
        jumpAudioPlayer.setVolume(soundVolume);
    }

    private void setupButtonsEvents()
    {
        settingsImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            paneSlideHandler(settingsPane);
            mouseEvent.consume();
        });

        settingsImageView.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
            settingsButton.setPrefWidth(32);
            settingsButton.setPrefHeight(32);
            settingsButton.setLayoutX(231);
            settingsButton.setLayoutY(181);
            settingsImageView.setFitWidth(24);
            settingsImageView.setFitHeight(24);
            settingsImageView.setLayoutX(235);
            settingsImageView.setLayoutY(183);
            mouseEvent.consume();
        });

        settingsImageView.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {
            settingsButton.setPrefWidth(28);
            settingsButton.setPrefHeight(28);
            settingsButton.setLayoutX(233);
            settingsButton.setLayoutY(183);
            settingsImageView.setFitWidth(22);
            settingsImageView.setFitHeight(22);
            settingsImageView.setLayoutX(236);
            settingsImageView.setLayoutY(185);
            mouseEvent.consume();
        });

        creditsImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            paneSlideHandler(creditsPane);
            mouseEvent.consume();
        });

        creditsImageView.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
            creditsButton.setPrefWidth(32);
            creditsButton.setPrefHeight(32);
            creditsButton.setLayoutX(231);
            creditsButton.setLayoutY(221);
            creditsImageView.setFitWidth(24);
            creditsImageView.setFitHeight(24);
            creditsImageView.setLayoutX(235);
            creditsImageView.setLayoutY(223);
            mouseEvent.consume();
        });

        creditsImageView.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {
            creditsButton.setPrefWidth(28);
            creditsButton.setPrefHeight(28);
            creditsButton.setLayoutX(233);
            creditsButton.setLayoutY(223);
            creditsImageView.setFitWidth(22);
            creditsImageView.setFitHeight(22);
            creditsImageView.setLayoutX(236);
            creditsImageView.setLayoutY(224);
            mouseEvent.consume();
        });

        resetImageView.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
            resetButton.setPrefWidth(32);
            resetButton.setPrefHeight(32);
            resetButton.setLayoutX(231);
            resetButton.setLayoutY(261);
            resetImageView.setFitWidth(24);
            resetImageView.setFitHeight(24);
            resetImageView.setLayoutX(235);
            resetImageView.setLayoutY(264);
            mouseEvent.consume();
        });

        resetImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            resetFadeOut();
        });

        resetImageView.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {
            resetButton.setPrefWidth(28);
            resetButton.setPrefHeight(28);
            resetButton.setLayoutX(233);
            resetButton.setLayoutY(263);
            resetImageView.setFitWidth(22);
            resetImageView.setFitHeight(22);
            resetImageView.setLayoutX(236);
            resetImageView.setLayoutY(265);
            mouseEvent.consume();
        });

        yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent ->
        {
            Obstacles.bestScore=0;
            scoreAmountLabel.setText(Integer.toString(Obstacles.bestScore));

            confirmFadeOut();
            mouseEvent.consume();
        });

        noButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent ->
        {
            confirmFadeOut();
            mouseEvent.consume();
        });

        closeSettingsButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent ->
        {
            paneSlideHandler(settingsPane);

            mouseEvent.consume();
        });

        closeCreditsButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent ->
        {
            paneSlideHandler(creditsPane);

            mouseEvent.consume();
        });

        playButton.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent ->
        {

            if(!playButtonEffectActive)
            {
                playButtonEffectActive =true;
                if(playButton.getPrefWidth()<128)
                {
                    playButton.setPrefWidth(playButton.getWidth()+1);
                }
                playButtonEffectHandler(true);
            }
            mouseEvent.consume();
        });

        playButton.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent ->
        {
            if(!playButtonEffectActive)
            {
                playButtonEffectActive =true;
                if(playButton.getPrefWidth()>148)
                {
                    playButton.setPrefWidth(playButton.getWidth()-1);
                }
                playButtonEffectHandler(false);
            }
            mouseEvent.consume();
        });
    }

    private void resetFadeOut()
    {
        FadeTransition resetLabelFadeOut = new FadeTransition(Duration.millis(500), resetLabel);
        resetLabelFadeOut.setFromValue(1);
        resetLabelFadeOut.setToValue(0);
        resetLabelFadeOut.play();

        FadeTransition resetButtonFadeOut = new FadeTransition(Duration.millis(500), resetButton);
        resetButtonFadeOut.setFromValue(1);
        resetButtonFadeOut.setToValue(0);
        resetButtonFadeOut.play();

        FadeTransition resetImageFadeOut = new FadeTransition(Duration.millis(500), resetImageView);
        resetImageFadeOut.setFromValue(1);
        resetImageFadeOut.setToValue(0);
        resetImageFadeOut.setOnFinished(event->confirmFadeIn());
        resetImageFadeOut.play();
    }

    private void confirmFadeIn()
    {
        resetLabel.setVisible(false);
        resetButton.setVisible(false);
        resetImageView.setVisible(false);

        confirmLabel.setVisible(true);
        yesButton.setVisible(true);
        noButton.setVisible(true);

        FadeTransition confirmLabelFadeIn = new FadeTransition(Duration.millis(500), confirmLabel);
        confirmLabelFadeIn.setFromValue(0);
        confirmLabelFadeIn.setToValue(1);
        confirmLabelFadeIn.play();

        FadeTransition yesButtonFadeIn = new FadeTransition(Duration.millis(500), yesButton);
        yesButtonFadeIn.setFromValue(0);
        yesButtonFadeIn.setToValue(1);
        yesButtonFadeIn.play();

        FadeTransition noButtonFadeIn = new FadeTransition(Duration.millis(500), noButton);
        noButtonFadeIn.setFromValue(0);
        noButtonFadeIn.setToValue(1);
        noButtonFadeIn.play();
    }

    private void confirmFadeOut()
    {
        FadeTransition confirmLabelFadeIn = new FadeTransition(Duration.millis(500), confirmLabel);
        confirmLabelFadeIn.setFromValue(1);
        confirmLabelFadeIn.setToValue(0);
        confirmLabelFadeIn.play();

        FadeTransition yesButtonFadeIn = new FadeTransition(Duration.millis(500), yesButton);
        yesButtonFadeIn.setFromValue(1);
        yesButtonFadeIn.setToValue(0);
        yesButtonFadeIn.play();

        FadeTransition noButtonFadeIn = new FadeTransition(Duration.millis(500), noButton);
        noButtonFadeIn.setFromValue(1);
        noButtonFadeIn.setToValue(0);
        noButtonFadeIn.setOnFinished(event->resetFadeIn());
        noButtonFadeIn.play();
    }

    private void resetFadeIn()
    {
        confirmLabel.setVisible(false);
        yesButton.setVisible(false);
        noButton.setVisible(false);

        resetLabel.setVisible(true);
        resetButton.setVisible(true);
        resetImageView.setVisible(true);

        FadeTransition resetLabelFadeIn = new FadeTransition(Duration.millis(500), resetLabel);
        resetLabelFadeIn.setFromValue(0);
        resetLabelFadeIn.setToValue(1);
        resetLabelFadeIn.play();

        FadeTransition resetButtonFadeIn = new FadeTransition(Duration.millis(500), resetButton);
        resetButtonFadeIn.setFromValue(0);
        resetButtonFadeIn.setToValue(1);
        resetButtonFadeIn.play();

        FadeTransition resetImageFadeIn = new FadeTransition(Duration.millis(500), resetImageView);
        resetImageFadeIn.setFromValue(0);
        resetImageFadeIn.setToValue(1);
        resetImageFadeIn.play();
    }

    @FXML
    private void onDifficultyChanged()
    {
        obstacleSpeed = difficultySlider.getValue()/5+1.3;
    }

    @FXML
    private void onMusicVolumeChanged()
    {
        musicVolume=musicVolumeSlider.getValue();
        menuAudioPlayer.setVolume(musicVolume);
        gameAudioPlayer.setVolume(musicVolume);
    }

    @FXML
    private void onSoundVolumeChanged()
    {
        soundVolume=soundVolumeSlider.getValue();
        failAudioPlayer.setVolume(soundVolume);
        jumpAudioPlayer.setVolume(soundVolume);
    }

    @FXML
    private void onSaveCheckBoxPressed()
    {
        saveBestScore=saveScoreCheckBox.isSelected();
    }

    @FXML
    private void paneSlideHandler(Node node)
    {

        TranslateTransition translate = new TranslateTransition(new Duration(800));
        if(node.getTranslateX()>0)
        {
            translate.setToX(-356);
            playButton.setDisable(false);

        }
        else
        {
            translate.setToX(356);
            playButton.setDisable(true);
        }

        translate.setNode(node);
        translate.play();
    }

    private void playButtonEffectHandler(boolean entered)
    {

        if(playButton.getPrefWidth()>=128&&playButton.getPrefWidth()<=148)
        {
            if(entered)
            {
                playButton.setPrefWidth(playButton.getWidth()+1);
                playButton.setLayoutX(playButton.getLayoutX()-0.5);
            }
            else
            {
                playButton.setPrefWidth(playButton.getWidth()-1);
                playButton.setLayoutX(playButton.getLayoutX()+0.5);
            }
            Timeline threadCheckerTimeline = new Timeline(new KeyFrame(Duration.millis(0.1)));
            threadCheckerTimeline.setOnFinished(event -> playButtonEffectHandler(entered));
            threadCheckerTimeline.play();
        }
        else
        {
            playButtonEffectActive =false;
        }
    }

}