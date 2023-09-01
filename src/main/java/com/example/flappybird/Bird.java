package com.example.flappybird;

import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Bird extends Thread {
    Rectangle[] upperObstaclesRoot;
    Rectangle[] upperObstaclesHead;
    Rectangle[] lowerObstaclesHead;
    Rectangle[] lowerObstaclesRoot;
    ImageView birdImageView;
    AnchorPane mainAnchorPane;

    public Bird(Rectangle[] upperObstaclesRoot, Rectangle[] upperObstaclesHead, Rectangle[] lowerObstaclesHead,
                Rectangle[] lowerObstaclesRoot, ImageView birdImageView, AnchorPane mainAnchorPane) {
        this.upperObstaclesRoot = upperObstaclesRoot;
        this.upperObstaclesHead = upperObstaclesHead;
        this.lowerObstaclesHead = lowerObstaclesHead;
        this.lowerObstaclesRoot = lowerObstaclesRoot;
        this.birdImageView = birdImageView;
        this.mainAnchorPane = mainAnchorPane;
    }

    @Override
    public void run() {
        loop();
    }

    private void loop()
    {
        double toNextObstacle;
        birdImageView.setRotate(0);
        birdImageView.setTranslateX(0);
        int pillarPassedID=5;
        while(App.birdAutoMove==1) {
            for (int i = 0; i < 4; i++)
            {
                toNextObstacle= upperObstaclesHead[i].getLayoutX() - birdImageView.getLayoutX();
                if (toNextObstacle>=140&&toNextObstacle<143&&pillarPassedID!=i) {
                    pillarPassedID=i;
                    TranslateTransition birdAutoMoveTranslate = new TranslateTransition(new Duration(600));
                    birdAutoMoveTranslate.setToY(upperObstaclesHead[i].getLayoutY() - 110);
                    birdAutoMoveTranslate.setNode(birdImageView);
                    birdAutoMoveTranslate.play();
                }
            }
        }
    }
}

