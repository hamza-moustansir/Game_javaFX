package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Projectile extends ImageView {
    public Projectile() {
        // Initialize projectile appearance and shooting animation
        Image projectileImage = new Image(getClass().getResourceAsStream("/images/projectile.png"));
        setImage(projectileImage);
        setFitWidth(45); // Set the width of the projectile
        setFitHeight(45); // Set the height of the projectile
        setVisible(true);
        
    }



	public void move() {
	    // Handle projectile movement (e.g., move the projectile up)
	    setY(getY() - 10); // Customize the projectile speed
	}
	public double getImageRadius() {
        // Assuming the image is a square, return half of its width (or height) as the "radius"
        return getFitWidth() / 2.0;
    }
}