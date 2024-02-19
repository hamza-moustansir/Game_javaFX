package game;


import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
public class Target extends Circle {
    private TranslateTransition fallAnimation;

    public Target(double x, double y) {
        super(17);  // Set the radius of the circle
        setCenterX(x);
        setCenterY(y);
        Image image = new Image(getClass().getResourceAsStream("/images/happy.jpeg"));
        setFill(new ImagePattern(image));

        // Create a TranslateTransition for downward movement
        fallAnimation = new TranslateTransition(Duration.seconds(3), this);
        fallAnimation.setToY(600);  // Set the Y-coordinate where the target should fall to
    }

    public void fall() {
        fallAnimation.play();
    }
    public void pause() {
        fallAnimation.pause();
    }
	
}