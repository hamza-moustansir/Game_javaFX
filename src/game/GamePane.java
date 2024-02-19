package game;



import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class GamePane extends Pane {
    private Canon canon;
//    private ImageView playerCharacter;

    public GamePane() {
        // Set background image for GamePane
        Image backgroundImage = new Image(getClass().getResourceAsStream("/images/faar.jpg"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(800);
        backgroundImageView.setFitHeight(600);
        getChildren().add(backgroundImageView);

        
    }

    public void moveCanonWithMouse(double mouseX) {
        canon.moveWithMouse(mouseX);
    }

    public void update() {
        // Additional update logic, if needed
    }


}