package com.example.flappybird;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.util.Locale;
import java.util.Scanner;

public class Main extends Application {
    private File dataFile;
    private File configFile;

    @Override
    public void start(Stage stage) throws IOException
    {
        dataFile = new File("data.txt");
        configFile=new File("config.txt");
        readDataAndConfig();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 400);
        scene.getStylesheets().add("fonts.css");
        stage.setTitle("FlappyBird");
        Image bird = new Image("birdIcon.png");
        stage.getIcons().add(bird);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        saveDataAndConfig(stage);
    }

    public static void main(String[] args) {
        launch();
    }

    private void readDataAndConfig() throws FileNotFoundException {
        if(dataFile.exists() && !dataFile.isDirectory()) {
            Scanner dataScanner = new Scanner(dataFile);
            while(dataScanner.hasNextInt())
            {
                Obstacles.bestScore = dataScanner.nextInt();
            }
            dataScanner.close();
        }

        if(configFile.exists() && !configFile.isDirectory()) {
            Scanner configScanner = new Scanner(configFile);
            configScanner.useLocale(Locale.UK);

            App.obstacleSpeed=configScanner.nextDouble();
            App.musicVolume=configScanner.nextDouble();
            App.soundVolume=configScanner.nextDouble();
            App.saveBestScore=configScanner.nextBoolean();

            configScanner.close();
        }
    }

    private void saveDataAndConfig(Stage stage)
    {
        stage.setOnHiding(windowEvent ->
        {
            if(App.saveBestScore)
            {
                try {
                    PrintWriter dataWriter = new PrintWriter(dataFile);
                    dataWriter.println(Obstacles.bestScore);
                    dataWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Best score saved!");
            }
            else
            {
                if(dataFile.exists() && !dataFile.isDirectory()) {
                    try {
                        Files.delete(dataFile.toPath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            try {
                PrintWriter configWriter = new PrintWriter(configFile);
                configWriter.println(App.obstacleSpeed);
                configWriter.println(App.musicVolume);
                configWriter.println(App.soundVolume);
                configWriter.println(App.saveBestScore);
                configWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Config saved!");
        });
    }
}