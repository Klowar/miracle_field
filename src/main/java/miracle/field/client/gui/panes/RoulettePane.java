package miracle.field.client.gui.panes;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;
import lombok.Data;

@Data
public class RoulettePane extends Pane {
    private Ellipse centerOfRoulette;
    private final int quantityOfSectors = 12;
    private Sector[] sectors;
    private Timeline timeline;
    private final String[] textStrings = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

    public RoulettePane() {
        centerOfRoulette = new Ellipse(200, 200, 10, 10);
        sectors = createRouletteSectors();
        addElements();
    }

    private Sector[] createRouletteSectors() {
        Sector[] sectors = new Sector[quantityOfSectors];
        double startAngle = 0;
        double length = 360.0 / quantityOfSectors;
        for (int i = 0; i < quantityOfSectors; i++) {
            Sector sector = new Sector(textStrings[i], length, startAngle);
            startAngle += length;
            sectors[i] = sector;
        }
        for (int i = 0; i < quantityOfSectors; i = i + 2) {
            sectors[i].color(Color.BLACK, Color.WHITE);
        }
        for (int i = 1; i < quantityOfSectors; i = i + 2) {
            sectors[i].color(Color.WHITE, Color.BLACK);

        }
        return sectors;
    }

    public void spinRoulette(int count) {
        double milliseconds = 3;
        timeline = new Timeline(new KeyFrame(Duration.millis(milliseconds), e -> spin()));
        timeline.setCycleCount(360 + count);
        timeline.play();

    }

    private void spin() {
        for (Sector sector : sectors) {
            double newStartAngle = sector.getArc().getStartAngle() + 1;
            if (newStartAngle >= 360) {
                newStartAngle -= 360;
            }
            sector.spin(newStartAngle);
        }
    }

    @Data
    private class Sector {
        private Arc arc;
        private Text text;
        private double length;
        private double startAngle;
        private final double CENTER = 0;
        private final double LAYOUT = 200;
        private static final double RADIUS = 200;

        Sector(String textString, double length, double startAngle) {
            this.length = length;
            this.startAngle = startAngle;
            text = new Text(textString);
            arc = new Arc();
            arc.setCenterX(CENTER);
            arc.setCenterY(CENTER);
            arc.setRadiusX(RADIUS);
            arc.setRadiusY(RADIUS);
            arc.setLayoutX(LAYOUT);
            arc.setLayoutY(LAYOUT);
            arc.setType(ArcType.ROUND);
            arc.setLength(length);
            arc.setStartAngle(startAngle);
            text.setBoundsType(TextBoundsType.VISUAL);
            text.setX(getXCoordinateToText());
            text.setY(getYCoordinateToText());
            text.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
        }

        double getXCoordinateToText() {
            return RADIUS + 0.75 * RADIUS * Math.cos(Math.toRadians(startAngle + length / 2));
        }

        double getYCoordinateToText() {
            return RADIUS - 0.75 * RADIUS * Math.sin(Math.toRadians(startAngle + length / 2));
        }

        void spin(double startAngle) {
            setStartAngle(startAngle);
            arc.setStartAngle(startAngle);
            text.setX(getXCoordinateToText());
            text.setY(getYCoordinateToText());
        }

        void color(Color arcColor, Color textColor) {
            arc.setFill(arcColor);
            text.setFill(textColor);
        }
    }

    private void addElements() {
        for (Sector sector : sectors) {
            getChildren().addAll(sector.getArc());
        }
        for (Sector sector : sectors) {
            getChildren().addAll(sector.getText());
        }
        getChildren().addAll(centerOfRoulette);
    }
}
