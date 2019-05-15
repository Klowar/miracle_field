package miracle.field.client.gui.panes;

import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Ellipse;

public class AlphabetPane extends FlowPane {
    private static final char FIRST_CHAR = 'а';
    private static final int ALPHABET_SIZE = 32;

    public AlphabetPane() {
        addElements();
    }

    private void addElements() {
        int code = FIRST_CHAR;
        for(int i = 0;  i < ALPHABET_SIZE; i++) {
            Button button = new Button();
            button.setText(String.valueOf((char)code));
            getChildren().add(button);
            code++;
        }
    }
}
