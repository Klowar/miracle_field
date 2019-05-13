package miracle.field.client.gui.elements;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import lombok.Data;

@Data
public class Triangle extends Polygon {
    private final double[] xPoints = { 0, 50, 100};
    private final double[] yPoints = {50, 0, 50};
    public Triangle() {
        for(int i = 0; i < 3; i++) {
            getPoints().addAll(xPoints[i], yPoints[i]);
        }
        setFill(Color.ORANGE);
    }
}
