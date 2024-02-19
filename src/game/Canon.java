package game;

import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class Canon extends StackPane {
    private ImageView cannonImage;
    private boolean isMoving=true;
    public Canon(double x, double y) {
        // Load the cannon image
        Image image = new Image(getClass().getResourceAsStream("/images/icon.png"));

        // Create an ImageView for the cannon
        cannonImage = new ImageView(image);
        cannonImage.setFitWidth(140);
        cannonImage.setFitHeight(140);
       
        setTranslateX(x);
        setTranslateY(y);

        getChildren().add(cannonImage);
    }
    public void setMoving(boolean moving) {
    	this.isMoving=moving;
    }
 public boolean getMoving() {
	 return this.isMoving;
 }
    public void shoot(List<Projectile> projectiles) {
        // Create a new projectile and position it at the cannon's location
        Projectile projectile = new Projectile();
        projectile.setTranslateX(getTranslateX() + (cannonImage.getFitWidth() / 2) - (projectile.getImageRadius()));
        projectile.setTranslateY(getTranslateY());

        // Add the projectile to the list and the GamePane
        projectiles.add(projectile);
        ((GamePane) getParent()).getChildren().add(projectile);
    }


    public void moveWithMouse(double mouseX) {
    	if (isMoving)
        setTranslateX(mouseX - cannonImage.getFitWidth() / 2);
    }
}