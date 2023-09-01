package com.example.flappybird;

import javafx.animation.*;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.util.Random;

import static java.lang.Math.abs;

public class Obstacles extends Thread{
    Rectangle[] upperObstaclesRoot;
    Rectangle[] upperObstaclesHead;
    Rectangle[] lowerObstaclesHead;
    Rectangle[] lowerObstaclesRoot;
    ImageView birdImageView;
    AnchorPane mainAnchorPane;
    Pane leftPane;
    Thread birdThread;
    Label bestScoreLabel;
    Label scoreLabel;
    Label bestScoreAmountLabel;
    Button playButton;
    Pane settingsPane;
    Pane creditsPane;
    public static int gameWereStopped =0;
    public static int newScore=0;
    public static int bestScore=0;
    private int obstaclesSpawned=0;
    private int pillarPassedID=0;
    private double prevBirdTranslateY =0;
    Image bricksImage = new Image("bricks.jpg");

    public Obstacles(Rectangle[] upperObstaclesRoot, Rectangle[] upperObstaclesHead, Rectangle[] lowerObstacles1,
                     Rectangle[] lowerObstaclesRoot, ImageView birdImageView, AnchorPane mainAnchorPane, Pane leftPane,
                     Label bestScoreLabel, Label scoreLabel, Label bestScoreAmountLabel, Button playButton, Pane settingsPane, Pane creditsPane){
        this.upperObstaclesRoot = upperObstaclesRoot;
        this.upperObstaclesHead = upperObstaclesHead;
        this.lowerObstaclesHead =lowerObstacles1;
        this.lowerObstaclesRoot = lowerObstaclesRoot;
        this.birdImageView=birdImageView;
        this.mainAnchorPane=mainAnchorPane;
        this.leftPane=leftPane;
        this.bestScoreAmountLabel=bestScoreAmountLabel;
        this.scoreLabel=scoreLabel;
        this.bestScoreLabel=bestScoreLabel;
        this.playButton=playButton;
        this.settingsPane=settingsPane;
        this.creditsPane=creditsPane;
    }

    @Override
    public void run()
    {
        AnimationTimer timer = new AnimationTimer(){

            @Override
            public void handle(long l) {
                upperObstaclesRoot[0].setLayoutX(upperObstaclesRoot[0].getLayoutX()-App.obstacleSpeed);
                upperObstaclesHead[0].setLayoutX(upperObstaclesHead[0].getLayoutX()-App.obstacleSpeed);
                lowerObstaclesHead[0].setLayoutX(lowerObstaclesHead[0].getLayoutX()-App.obstacleSpeed);
                lowerObstaclesRoot[0].setLayoutX(lowerObstaclesRoot[0].getLayoutX()-App.obstacleSpeed);
                upperObstaclesRoot[1].setLayoutX(upperObstaclesRoot[1].getLayoutX()-App.obstacleSpeed);
                upperObstaclesHead[1].setLayoutX(upperObstaclesHead[1].getLayoutX()-App.obstacleSpeed);
                lowerObstaclesHead[1].setLayoutX(lowerObstaclesHead[1].getLayoutX()-App.obstacleSpeed);
                lowerObstaclesRoot[1].setLayoutX(lowerObstaclesRoot[1].getLayoutX()-App.obstacleSpeed);
                upperObstaclesRoot[2].setLayoutX(upperObstaclesRoot[2].getLayoutX()-App.obstacleSpeed);
                upperObstaclesHead[2].setLayoutX(upperObstaclesHead[2].getLayoutX()-App.obstacleSpeed);
                lowerObstaclesHead[2].setLayoutX(lowerObstaclesHead[2].getLayoutX()-App.obstacleSpeed);
                lowerObstaclesRoot[2].setLayoutX(lowerObstaclesRoot[2].getLayoutX()-App.obstacleSpeed);
                upperObstaclesRoot[3].setLayoutX(upperObstaclesRoot[3].getLayoutX()-App.obstacleSpeed);
                upperObstaclesHead[3].setLayoutX(upperObstaclesHead[3].getLayoutX()-App.obstacleSpeed);
                lowerObstaclesHead[3].setLayoutX(lowerObstaclesHead[3].getLayoutX()-App.obstacleSpeed);
                lowerObstaclesRoot[3].setLayoutX(lowerObstaclesRoot[3].getLayoutX()-App.obstacleSpeed);
            }
        };
        try {
            obstacleChecker();
            timer.start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void obstacleChecker() throws InterruptedException {

            if(App.gameStarted ==1&& obstaclesSpawned <4)
            {
                for(int i=0;i<4;i++)
                {
                    if(upperObstaclesRoot[i].getLayoutX()<-35&& upperObstaclesRoot[i].getOpacity()==0)
                    {
                        upperObstaclesRoot[i].setOpacity(100);
                        upperObstaclesHead[i].setOpacity(100);
                        lowerObstaclesHead[i].setOpacity(100);
                        lowerObstaclesRoot[i].setOpacity(100);
                        upperObstaclesRoot[i].setVisible(true);
                        upperObstaclesHead[i].setVisible(true);
                        lowerObstaclesHead[i].setVisible(true);
                        lowerObstaclesRoot[i].setVisible(true);
                        obstaclesSpawned++;
                    }
                }
            }
            else if(App.gameStarted ==0&& obstaclesSpawned >0)
            {
                obstaclesSpawned =0;
            }
            for(int i=0;i<4;i++)
            {
                if(upperObstaclesHead[i].getLayoutX()<=-50)
                {
                    Random random=new Random();
                    int randHeight=random.nextInt(200 - 50 + 1) + 50;
                    upperObstaclesRoot[i].setHeight(randHeight);
                    upperObstaclesHead[i].setHeight(23);
                    lowerObstaclesHead[i].setHeight(23);
                    lowerObstaclesRoot[i].setHeight(260-randHeight);

                    upperObstaclesRoot[i].setLayoutY(0);
                    upperObstaclesHead[i].setLayoutY(randHeight);
                    lowerObstaclesHead[i].setLayoutY(randHeight+23+95);
                    lowerObstaclesRoot[i].setLayoutY(randHeight+23+95+23);

                    upperObstaclesRoot[i].setLayoutX(764);
                    upperObstaclesHead[i].setLayoutX(761);
                    lowerObstaclesHead[i].setLayoutX(761);
                    lowerObstaclesRoot[i].setLayoutX(764);
                }

                if(App.playPressed ==1&& gameWereStopped ==0&&App.gameStarted ==1&&abs(birdImageView.getTranslateY()- prevBirdTranslateY)<20)
                {
                    prevBirdTranslateY =birdImageView.getTranslateY();

                    if(upperObstaclesRoot[i].getLayoutX()<251&& upperObstaclesRoot[i].getLayoutX()>241&& upperObstaclesRoot[i].isVisible())
                    {
                        int lastPillarPassedID = pillarPassedID;
                        pillarPassedID =i;
                        if(pillarPassedID != lastPillarPassedID)
                        {
                            newScore++;
                            bestScoreAmountLabel.setText(Integer.toString(newScore));
                        }
                    }

                    if((birdImageView.getBoundsInParent().intersects(upperObstaclesRoot[i].getBoundsInParent())&& upperObstaclesRoot[i].isVisible())||
                            (birdImageView.getBoundsInParent().intersects(upperObstaclesHead[i].getBoundsInParent())&& upperObstaclesHead[i].isVisible())||
                            (birdImageView.getBoundsInParent().intersects(lowerObstaclesHead[i].getBoundsInParent())&& lowerObstaclesHead[i].isVisible())||
                            (birdImageView.getBoundsInParent().intersects(lowerObstaclesRoot[i].getBoundsInParent())&& lowerObstaclesRoot[i].isVisible())||
                            (birdImageView.getTranslateY()>190)||(birdImageView.getTranslateY()<-140))
                    {
                        if(newScore>bestScore)
                            bestScore=newScore;

                        Lighting lighting = new Lighting(new Light.Distant(45, 90, Color.RED));
                        ColorAdjust bright = new ColorAdjust(0, 1, 0, 0);
                        lighting.setContentInput(bright);
                        lighting.setSurfaceScale(0.0);
                        birdImageView.setEffect(lighting);


                        mainAnchorPane.getScene().setOnMousePressed(null);
                        TranslateTransition translate1 = new TranslateTransition(new Duration(500));
                        translate1.setToY(-83);
                        translate1.setNode(scoreLabel);
                        translate1.setOnFinished(event->bestScoreShow());
                        translate1.play();

                        for(int j=0;j<4;j++)
                        {
                            if(!upperObstaclesRoot[j].isVisible())
                            {
                                fadeObstaclesIn(upperObstaclesRoot[j]);
                                fadeObstaclesIn(upperObstaclesHead[j]);
                                fadeObstaclesIn(lowerObstaclesHead[j]);
                                fadeObstaclesIn(lowerObstaclesRoot[j]);
                            }
                        }

                        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2)));
                        timeline.setOnFinished(event->startBirdThread());
                        timeline.play();

                        prevBirdTranslateY =0;
                        App.gameAudioPlayer.stop();
                        App.failAudioPlayer.play();

                        App.birdAutoMove=1;
                        gameWereStopped =1;
                        App.gameStarted =0;
                        App.playPressed =0;
                    }
                }

            }

            Timeline sleepTimeline = new Timeline(new KeyFrame(Duration.millis(1)));
            sleepTimeline.setOnFinished(event-> {
                try {
                    obstacleChecker();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            sleepTimeline.play();
    }

    private void bestScoreShow()
    {
        TranslateTransition bestScoreShowTranslate = new TranslateTransition(new Duration(500));
        bestScoreShowTranslate.setToY(0);
        bestScoreShowTranslate.setNode(bestScoreLabel);
        bestScoreShowTranslate.setOnFinished(event->slideLabels());
        bestScoreShowTranslate.play();
    }

    private void fadeObstaclesIn(Node node)
    {
        node.setVisible(true);
        FadeTransition fadeObstaclesInTranslate = new FadeTransition(Duration.millis(1000), node);
        fadeObstaclesInTranslate.setFromValue(0);
        fadeObstaclesInTranslate.setToValue(1);
        fadeObstaclesInTranslate.play();
    }

    private void slideLabels()
    {
        TranslateTransition leftPaneSlideTranslate = new TranslateTransition(new Duration(800));
        leftPaneSlideTranslate.setToX(0);
        leftPaneSlideTranslate.setNode(leftPane);
        leftPaneSlideTranslate.setOnFinished(event->afterPaneSlide());
        leftPaneSlideTranslate.play();

        TranslateTransition bestScoreSlideTranslate = new TranslateTransition(new Duration(800));
        bestScoreSlideTranslate.setToX(0);
        bestScoreSlideTranslate.setNode(bestScoreLabel);
        bestScoreSlideTranslate.play();

        TranslateTransition scoreSlideTranslate = new TranslateTransition(new Duration(800));
        scoreSlideTranslate.setToX(0);
        scoreSlideTranslate.setNode(bestScoreAmountLabel);
        scoreSlideTranslate.setOnFinished(e->bestScoreAmountLabel.setText(Integer.toString(bestScore)));
        scoreSlideTranslate.play();
    }

    private void afterPaneSlide()
    {
        birdImageView.getScene().setCursor(Cursor.DEFAULT);
        for(int i=0;i<4;i++)
        {
            upperObstaclesRoot[i].setFill(new ImagePattern(bricksImage));
            upperObstaclesHead[i].setFill(new ImagePattern(bricksImage));
            lowerObstaclesHead[i].setFill(new ImagePattern(bricksImage));
            lowerObstaclesRoot[i].setFill(new ImagePattern(bricksImage));
        }

        settingsPane.setVisible(true);
        creditsPane.setVisible(true);
    }


    private void startBirdThread()
    {
        birdImageView.setEffect(null);
        playButton.setDisable(false);
        birdThread = new Bird(upperObstaclesRoot, upperObstaclesHead, lowerObstaclesHead, lowerObstaclesRoot,birdImageView,mainAnchorPane);
        birdThread.start();
    }
}