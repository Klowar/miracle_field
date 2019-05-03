package miracle.field.client.gui.scenes;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Ellipse;
import javafx.util.Duration;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class RouletteScene extends Scene implements EventHandler<ActionEvent> {
    private Pane pane;
    private Roulette roulette;
    private Button turningButton;
    private Timeline timeline;

    public RouletteScene(Parent root, int quantityOfSectors)  {
        super(root);
        pane = new Pane();
        roulette = createRoulette(quantityOfSectors);
        turningButton = new Button("Крутить!");
        turningButton.setOnAction(this);
        addAllComponentsToScene((StackPane) root, roulette.sectors);
    }
    private Roulette createRoulette(int quantityOfSectors) {
        Arc[] sectors = new Arc[quantityOfSectors];
        int radius = 200;
        double startAngle = 0;
        double length =  360.0 / quantityOfSectors;
        for (int i = 0; i < quantityOfSectors; i++) {
            Arc arc = new Arc();
            arc.setCenterX(0);
            arc.setCenterY(0);
            arc.setRadiusX(radius);
            arc.setRadiusY(radius);
            arc.setStartAngle(startAngle);
            arc.setLength(length);
            arc.setType(ArcType.ROUND);
            arc.setLayoutX(radius);
            arc.setLayoutY(radius);
            sectors[i] = arc;
            startAngle += length;
        }
        for (int i = 0; i < quantityOfSectors; i = i + 2) {
            sectors[i].setFill(Color.BLACK);
        }
        for (int i = 1; i < quantityOfSectors; i = i + 2) {
            sectors[i].setFill(Color.WHITE);
            sectors[i].setStroke(Color.BLACK);
        }
        return new Roulette(sectors);
    }
    private void addAllComponentsToScene(StackPane root, Arc[] sectors) {
        for(int i = 0; i < sectors.length; i++) {
            pane.getChildren().add(sectors[i]);
        }
        pane.getChildren().add(new Ellipse(200, 200, 10,10));
        root.getChildren().addAll(pane, turningButton);
    }

    @Data
    @AllArgsConstructor
    private class Roulette extends Node {
        private Arc[] sectors;
    }
//  Start animation
    @Override
    public void handle(ActionEvent actionEvent) {
        double milliseconds = 2 + Math.random() * 9;
        timeline = new Timeline(new KeyFrame(Duration.millis(milliseconds), e -> spinRoulette()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    public void spinRoulette() {
        Arc[] sectors = roulette.sectors;
        for (int i = 0; i < sectors.length; i++) {
            double newStartAngle = sectors[i].getStartAngle() + 1;
            if(newStartAngle >= 360 ){
                newStartAngle -=  360;
            }
            sectors[i].setStartAngle(newStartAngle);
            System.out.println(newStartAngle);
        }
    }
    //  Stop animation
    public void stopSpin(){
        timeline.stop();
    }
}
