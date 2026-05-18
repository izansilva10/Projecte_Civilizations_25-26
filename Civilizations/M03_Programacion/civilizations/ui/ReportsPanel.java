package civilizations.ui;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import civilizations.Battle;
import java.util.ArrayList;

public class ReportsPanel {
    private TextArea battleReportArea;
    private ArrayList<Battle> battleHistory;

    public ReportsPanel(ArrayList<Battle> battleHistory) {
        this.battleHistory = battleHistory;
    }

    public VBox getPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new javafx.geometry.Insets(15));

        battleReportArea = new TextArea();
        battleReportArea.setEditable(false);
        battleReportArea.setPrefRowCount(20);
        battleReportArea.setStyle("-fx-font-family: monospace;");

        panel.getChildren().addAll(new Label("Último reporte de batalla:"), battleReportArea);
        return panel;
    }

    public void updateUI() {
        if (battleHistory == null || battleHistory.isEmpty()) {
            battleReportArea.setText("No hay batallas registradas.");
            return;
        }
        Battle lastBattle = battleHistory.get(battleHistory.size() - 1);
        String report = lastBattle.getBattleReport(battleHistory.size());
        String development = lastBattle.getBattleDevelopment();
        battleReportArea.setText(report + "\n\n--- DESARROLLO PASO A PASO ---\n" + development);
    }
}