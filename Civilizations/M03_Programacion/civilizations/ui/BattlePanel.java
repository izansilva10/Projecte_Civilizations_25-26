package civilizations.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import civilizations.Civilization;
import java.util.ArrayList;
import civilizations.MilitaryUnit;

public class BattlePanel {
    private Civilization civilization;
    private ArrayList<MilitaryUnit> currentEnemyArmy;
    private Runnable startBattleCallback;
    private TextArea threatArea;
    private Button startBattleBtn;

    public BattlePanel(Civilization civilization, ArrayList<MilitaryUnit> currentEnemyArmy, Runnable startBattleCallback) {
        this.civilization = civilization;
        this.currentEnemyArmy = currentEnemyArmy;
        this.startBattleCallback = startBattleCallback;
    }

    public VBox getPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new javafx.geometry.Insets(15));

        threatArea = new TextArea();
        threatArea.setEditable(false);
        threatArea.setPrefRowCount(6);
        threatArea.setStyle("-fx-font-family: monospace;");

        startBattleBtn = new Button("Iniciar batalla ahora");
        startBattleBtn.setOnAction(e -> startBattleCallback.run());

        panel.getChildren().addAll(new Label("⚠️ Amenaza enemiga actual:"), threatArea, startBattleBtn);
        return panel;
    }

    public void updateUI() {
        if (currentEnemyArmy.isEmpty()) {
            threatArea.setText("No hay amenaza en este momento.\nEspera a que llegue un ejército enemigo (cada 3 minutos).");
            startBattleBtn.setDisable(true);
        } else {
            int[] counts = new int[4];
            for (civilizations.MilitaryUnit u : currentEnemyArmy) {
                if (u instanceof civilizations.Swordsman) counts[0]++;
                else if (u instanceof civilizations.Spearman) counts[1]++;
                else if (u instanceof civilizations.Crossbow) counts[2]++;
                else if (u instanceof civilizations.Cannon) counts[3]++;
            }
            threatArea.setText(String.format("Swordsman: %d\nSpearman: %d\nCrossbow: %d\nCannon: %d",
                    counts[0], counts[1], counts[2], counts[3]));
            startBattleBtn.setDisable(false);
        }
    }
}