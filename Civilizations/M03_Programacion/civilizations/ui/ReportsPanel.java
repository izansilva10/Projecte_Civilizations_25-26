package civilizations.ui;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class ReportsPanel {
    private TextArea battleReportArea;

    public ReportsPanel() {
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

    public void updateUI(String report) {
        battleReportArea.setText(report);
    }

    public void appendBattleReport(String report, String development) {
        battleReportArea.setText(report + "\n\n--- DESARROLLO PASO A PASO ---\n" + development);
    }
}
