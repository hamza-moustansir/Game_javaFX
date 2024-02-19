package application;

import game.Canon;
import game.GamePane;

import game.Projectile;
import game.Target;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.Cursor;
import javafx.scene.shape.Rectangle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class ShootingGame extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private GamePane gamePane;
    private Canon canon;
    private List<Target> targets;
    private List<Projectile> projectiles;
    private int score;
    private Text scoreText; // Text node to display the score
    private Text winText;
    
    Button restartButton;
    private int fallenTargetsCount;
    private MediaPlayer mediaPlayer1;// Déclarer les variables pour la musique
    private Button muteButton;
    private Button pauseButton;


    @Override
    public void start(Stage primaryStage) {
    	
    	
        primaryStage.setTitle("Shooting Game");
        primaryStage.setResizable(false);
        
        
        Image icon = new Image(getClass().getResourceAsStream("/images/icon.png"), 64, 64, true, true);

        // Définissez l'icône de la fenêtre
        primaryStage.getIcons().add(icon);
        mediaPlayer1 = new MediaPlayer(new Media(Paths.get("C:\\Users\\souad\\Desktop\\music\\1.mp3").toUri().toString()));
        
        gamePane = new GamePane();

        scoreText = new Text("Score: " + score);
        scoreText.setStyle("-fx-font-size: 30; -fx-background-color: red; -fx-text-fill: white;");
       
        scoreText.setLayoutX(10); // Adjust X position
        scoreText.setLayoutY(35); // Adjust Y position
        gamePane.getChildren().add(scoreText);
        
        muteButton = new Button();
        muteButton.setPrefSize(35, 35);
        muteButton.setGraphic(createImage("/images/unmute.png"));
        muteButton.setLayoutX(740);
        muteButton.setLayoutY(15);
        muteButton.setOnAction(event -> {
        	if (mediaPlayer1.isMute()) {
                mediaPlayer1.setMute(false);
                muteButton.setGraphic(createImage("/images/unmute.png")); // Set the path to your unmute image
            } else {
                mediaPlayer1.setMute(true);
                muteButton.setGraphic(createImage("/images/mute.png")); // Set the path to your mute image
            } 
        });
        gamePane.getChildren().add(muteButton);


        pauseButton = new Button("");
        pauseButton.setPrefSize(55, 35);
        pauseButton.setLayoutX(650);
        pauseButton.setLayoutY(15);
        pauseButton.setGraphic(pauseIcon);
        pauseButton.setOnAction(event -> togglePause());
        gamePane.getChildren().add(pauseButton);
        
       

        Rectangle background = new Rectangle(800, 600); // Adjust width and height as needed
        background.setFill(Color.rgb(0, 0, 0, 0.7)); // Adjust alpha value (0.0 to 1.0) for transparency

        Button startButton = new Button("Start");
        startButton.setPrefSize(180, 100);
        startButton.setStyle("-fx-font-size: 20; -fx-background-color: orange; -fx-text-fill: white;");
        // Set cursor to hand when mouse enters the button
        startButton.setOnMouseEntered(event -> {
            startButton.setCursor(Cursor.HAND);
            // Make the button bigger and change the color on hover
            startButton.setPrefSize(180, 100); // Set new preferred width and height
            startButton.setStyle("-fx-font-size: 20; -fx-background-color: red; -fx-text-fill: white;");
        });

        // Set cursor to default when mouse exits the button
        startButton.setOnMouseExited(event -> {
            startButton.setCursor(Cursor.DEFAULT);
            // Restore the original size and color when the mouse exits
            startButton.setPrefSize(180, 100); // Set original preferred width and height
            startButton.setStyle("-fx-font-size: 20; -fx-background-color: orange; -fx-text-fill: white;");
        });

        startButton.setOnAction(event -> {
            Platform.runLater(() -> { background.setVisible(false); startButton.setVisible(false); });
            new Thread(() -> {
                Platform.runLater(() -> {
                    // Simulate a time-consuming operation (e.g., score increment) in a separate thread
                    mediaPlayer1.play();
                    canon = new Canon(400, 400);
                    gamePane.getChildren().addAll(canon);
                    new Thread(() -> {
                        try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
                        Platform.runLater(() -> spawnTargets());
                    }).start();
                });
            }).start();
        });

     
        
        winText = new Text("Congratulations! You Win!");
        winText.setStyle("-fx-font-size: 50; -fx-text-fill: white;");
        winText.setLayoutX(100);
        winText.setLayoutY(300);
        winText.setVisible(false); // Initially, set it to invisible
        winText.setFill(Color.WHITE);
       
     // Bouton de redémarrage
        restartButton = new Button("Restart");
        restartButton.setPrefSize(180, 100);
        restartButton.setStyle("-fx-font-size: 20; -fx-background-color: green; -fx-text-fill: white;");
        restartButton.setLayoutX(308);
        restartButton.setLayoutY(450);
        background.setVisible(true);

        restartButton.setVisible(false); // Initialement, défini sur invisible

        // Action du bouton de redémarrage
        restartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Masquer le rectangle et le bouton lorsque le bouton est cliqué
                fallenTargetsCount = 0;

                // Effacer les cibles et projectiles existants
                for (Target target : targets) {
                    // Masquer visuellement les cibles tombées
                    target.setVisible(false);
                    // Retirer la cible de GamePane
                    gamePane.getChildren().remove(target);
                }

                targets.clear();
                projectiles.clear();

                // Réinitialiser le score
                score = 0;

                // Masquer le message de victoire et le bouton de redémarrage
                winText.setVisible(false);
                winText.setText("Congratulations! You Win!"); // Réinitialiser le texte du message de victoire
                winText.setLayoutX(100);
                winText.setLayoutY(300);

                Platform.runLater(() -> background.setVisible(false));
                Platform.runLater(() -> restartButton.setVisible(false));

                // Créer un nouveau canon et l'ajouter à GamePane
                canon = new Canon(400, 400);
                gamePane.getChildren().add(canon);

                // Appeler spawnTargets() après que le bouton a été cliqué
                spawnTargets();
            }
        });

        // Gestionnaire d'événements pour le survol du bouton de redémarrage
        restartButton.setOnMouseEntered(event -> {
            restartButton.setCursor(Cursor.HAND);
            // Augmenter la taille du bouton et changer la couleur au survol
            restartButton.setPrefSize(180, 100); // Définir la nouvelle largeur et hauteur préférées
            restartButton.setStyle("-fx-font-size: 20; -fx-background-color: Orange; -fx-text-fill: white;");
            background.setVisible(true); // Afficher l'arrière-plan lorsque le bouton est survolé
        });

        // Gestionnaire d'événements pour la sortie de la souris du bouton de redémarrage
        restartButton.setOnMouseExited(event -> {
            restartButton.setCursor(Cursor.DEFAULT);
            // Restaurer la taille et la couleur d'origine lorsque la souris sort
            restartButton.setPrefSize(180, 100); // Définir la largeur et la hauteur préférées d'origine
            restartButton.setStyle("-fx-font-size: 20; -fx-background-color: green; -fx-text-fill: white;");
        });

        // Ajouter les éléments à GamePane
        gamePane.getChildren().addAll(background, startButton, winText, restartButton);
        startButton.setLayoutX(325); // Adjust X position
        startButton.setLayoutY(200); // Adjust Y position

        Scene scene = new Scene(gamePane, 800, 600);


        targets = new ArrayList<>();
        projectiles = new ArrayList<>();
        score = 0;
        

        // Add mouse movement handling
        scene.setOnMouseMoved(event -> {
        	
            canon.moveWithMouse(event.getX());
        });
        
        
        scene.setOnMouseClicked(event -> {
            if (event.getButton().toString().equals("PRIMARY")) {
                // Shoot projectiles when left mouse button is clicked
                canon.shoot(projectiles);
            }
        });

        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.DECORATED);

        
     // Center the Start button
        startButton.setLayoutX((gamePane.getWidth() - startButton.getPrefWidth()) / 2);
        startButton.setLayoutY((gamePane.getHeight() - startButton.getPrefHeight()) / 2);

        gameLoop().start();
        primaryStage.show();
     
        		
    }

    // Déclarer les ImageView pour les icônes de pause et de reprise
    private ImageView pauseIcon = createImage("/images/ppause.png");
    private ImageView resumeIcon = createImage("/images/play.png");


    // Variable pour suivre l'état de pause du jeu
    private boolean isPaused = false;

    // Méthode pour basculer entre la pause et la reprise
    private void togglePause() {
        if (isPaused) {
            // Si le jeu est en pause, reprendre le jeu
            isPaused = false;
            gameLoop().start(); // Reprendre la boucle du jeu
            mediaPlayer1.play(); // Reprendre la lecture de la musique
            canon.setMoving(true); // Autoriser le mouvement du canon

            // Définir l'icône du bouton de pause sur l'icône de pause
            pauseButton.setGraphic(pauseIcon);
        } else {
            // Si le jeu n'est pas en pause, mettre le jeu en pause
            isPaused = true;
            gameLoop().stop(); // Arrêter la boucle du jeu
            mediaPlayer1.pause(); // Mettre en pause la musique
            canon.setMoving(false); // Arrêter le mouvement du canon

            // Définir l'icône du bouton de pause sur l'icône de reprise
            pauseButton.setGraphic(resumeIcon);
        }
    }

    private ImageView createImage(String imagePath) {
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(25); // Set the width of the image
        imageView.setFitHeight(25); // Set the height of the image
        return imageView;
    }
   

    private AnimationTimer gameLoop() {
        return new AnimationTimer() {
            @Override
            public void handle(long now) {
            	 if (!isPaused) {
                     updateGame();
                     checkCollisions();
                     removeOutOfBounds();
                     updateScoreText();
                     gamePane.update();
                 }
            }
        };
    }

    private void updateScoreText() {
        scoreText.setText("Score: " + score);
    }
    private void updateGame() {
        // Update game logic (e.g., movement, scoring)
        for (Projectile projectile : projectiles) {
            projectile.move();
        }
    }

    private void checkCollisions() {
        Iterator<Projectile> projectileIterator = projectiles.iterator();
        while (projectileIterator.hasNext()) {
            Projectile projectile = projectileIterator.next();

            Iterator<Target> targetIterator = targets.iterator();
            while (targetIterator.hasNext()) {
                Target target = targetIterator.next();

                if (projectile.getBoundsInParent().intersects(target.getBoundsInParent())) {
                    // Collision detected
                    targetIterator.remove();
                    projectileIterator.remove();

                    // Change the form of the target
                    changeTargetForm(target);
                    projectile.setVisible(false);
                    // Increment the score in a separate thread
                    incrementScoreInBackground();
                }
            }
        }
    }

    private void changeTargetForm(Target target) {
    	target.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/images/angry.jpeg"))));
    }

    
    private void incrementScoreInBackground() {
        Task<Void> scoreIncrementTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                // Increment the score (modify this part according to your game logic)
                score += 1;

                // Update the score text on the JavaFX Application Thread
                Platform.runLater(() -> updateScoreText());
                if (score == 5) {
                    Platform.runLater(() -> {
                        winText.setVisible(true);
                        
                       
                        restartButton.setVisible(true);
                        winText.setVisible(true);
                      
                        gamePane.getChildren().remove(canon); // Optionally remove the canon
                       


                      
                    });
                }

                return null;
            }
        };
        new Thread(scoreIncrementTask).start();
    }
  


    
    private void removeOutOfBounds() {
        Iterator<Target> targetIterator = targets.iterator();
        while (targetIterator.hasNext()) {
            Target target = targetIterator.next();

            if (target.getTranslateY() >= 600) { // Vérifiez si la cible est tombée au sol
                targetIterator.remove();
                fallenTargetsCount++;

                // Vérifiez si le joueur a perdu
                if (fallenTargetsCount >= 5) {
                    // Implémentez la logique pour traiter la perte (par exemple, afficher un message)
                    Platform.runLater(() -> {
                        showLossMessage();
                        restartButton.setVisible(true);
                        gamePane.getChildren().remove(canon); // Optionnellement, supprimez le canon
                        restartButton.setStyle("-fx-font-size: 20; -fx-background-color: red; -fx-text-fill: white;");
                    });
                }

                // Décrémentez le score dans un thread séparé seulement si le score est supérieur ou égal à 1
                if (score >= 1) {
                    decrementScoreInBackground();
                }
            }
        }

        Iterator<Projectile> projectileIterator = projectiles.iterator();
        while (projectileIterator.hasNext()) {
            Projectile projectile = projectileIterator.next();

            if (projectile.getTranslateY() < 0 || projectile.getTranslateY() >= 600) {
                // Vérifiez si le projectile est sorti de l'écran vers le haut ou vers le bas
                projectileIterator.remove();
            }
        }
    }


    private void showLossMessage() {
        winText.setText("Sorry! You Lose!");
        winText.setVisible(true);
        winText.setStyle("-fx-font-size: 50; -fx-text-fill: white;");
        
        // Set the background color to white
        winText.setFill(Color.WHITE);

        winText.setLayoutX(200);
    }


    private void decrementScoreInBackground() {
        Task<Void> scoreDecrementTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Simulate a time-consuming operation (e.g., score decrement) in a separate thread

                // Decrement the score only if it's 1 or more
                if (score >= 1) {
                    score -= 1; // Adjust the amount to decrement
                }

                // Update the score text on the JavaFX Application Thread
                Platform.runLater(() -> updateScoreText());

                return null;
            }
        };
        new Thread(scoreDecrementTask).start();
    }
  



 // Générer les cibles
    private void spawnTargets() {
        Random random = new Random();
        AnimationTimer timer = new AnimationTimer() {
            private long lastSpawnTime = 0;

            @Override
            public void handle(long now) {
                // Arrêter de générer des cibles lorsque le message de victoire est visible
                if (winText.isVisible()) {
                    stop();
                    return;
                }

                // Générer une nouvelle cible chaque seconde
                if (now - lastSpawnTime >= 1_550_000_000) {
                    // Faire tomber les cibles si le canon est en mouvement
                    if (canon.getMoving()) {
                        targets.forEach(Target::fall);

                        // Générer une nouvelle cible
                        int x = random.nextInt(800);
                        Target target = new Target(x, 0);
                        targets.add(target);
                        gamePane.getChildren().add(target);
                        
                        // Faire tomber la cible
                        target.fall();
                        
                        lastSpawnTime = now;
                    } else {
                        // Mettre en pause la chute des cibles si le canon ne bouge pas
                        targets.forEach(Target::pause);
                    }
                }
            }
        };
        timer.start();
    }


    
}