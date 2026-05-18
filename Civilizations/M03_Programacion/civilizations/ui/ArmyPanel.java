package civilizations.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import civilizations.*;

import java.util.Optional;

public class ArmyPanel {
    private Civilization civilization;
    private Runnable updateUICallback;
    private TableView<String> armyTable;

    public ArmyPanel(Civilization civilization, Runnable updateUICallback) {
        this.civilization = civilization;
        this.updateUICallback = updateUICallback;
    }

    public VBox getPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(15));

        armyTable = new TableView<>();
        TableColumn<String, String> unitCol = new TableColumn<>("Unidad");
        TableColumn<String, String> countCol = new TableColumn<>("Cantidad");
        armyTable.getColumns().addAll(unitCol, countCol);
        armyTable.setPrefHeight(300);

        Label attackLabel = new Label("⚔️ UNIDADES DE ATAQUE");
        attackLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        HBox attackBox = new HBox(10);
        attackBox.setAlignment(Pos.CENTER_LEFT);
        Button swordsmanBtn = new Button("Espadachín");
        Button spearmanBtn = new Button("Lancero");
        Button crossbowBtn = new Button("Ballesta");
        Button cannonBtn = new Button("Cañón");
        attackBox.getChildren().addAll(swordsmanBtn, spearmanBtn, crossbowBtn, cannonBtn);

        Label defenseLabel = new Label("🛡️ DEFENSAS Y ESPECIALES");
        defenseLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        HBox defenseBox = new HBox(10);
        defenseBox.setAlignment(Pos.CENTER_LEFT);
        Button arrowTowerBtn = new Button("Torre Flechas");
        Button catapultBtn = new Button("Catapulta");
        Button rocketBtn = new Button("Torre Cohete");
        Button magicianBtn = new Button("Mago");
        Button priestBtn = new Button("Sacerdote");
        defenseBox.getChildren().addAll(arrowTowerBtn, catapultBtn, rocketBtn, magicianBtn, priestBtn);

        // Configurar acciones
        swordsmanBtn.setOnAction(e -> createUnit(civilization::newSwordsman, "Espadachín", 0));
        spearmanBtn.setOnAction(e -> createUnit(civilization::newSpearman, "Lancero", 1));
        crossbowBtn.setOnAction(e -> createUnit(civilization::newCrossbow, "Ballesta", 2));
        cannonBtn.setOnAction(e -> createUnit(civilization::newCannon, "Cañón", 3));
        arrowTowerBtn.setOnAction(e -> createUnit(civilization::newArrowTower, "Torre de Flechas", 4));
        catapultBtn.setOnAction(e -> createUnit(civilization::newCatapult, "Catapulta", 5));
        rocketBtn.setOnAction(e -> createUnit(civilization::newRocketLauncher, "Torre Cohete", 6));
        magicianBtn.setOnAction(e -> createUnit(civilization::newMagician, "Mago", 7));
        priestBtn.setOnAction(e -> createUnit(civilization::newPriest, "Sacerdote", 8));

        VBox buttonsContainer = new VBox(10);
        buttonsContainer.getChildren().addAll(attackLabel, attackBox, defenseLabel, defenseBox);
        buttonsContainer.setPadding(new Insets(10, 0, 0, 0));

        Button refreshBtn = new Button("🔄 Refrescar tabla");
        refreshBtn.setOnAction(e -> updateUICallback.run());

        panel.getChildren().addAll(new Label("Ejército actual:"), armyTable, buttonsContainer, refreshBtn);
        return panel;
    }

    private void createUnit(java.util.function.IntConsumer unitCreator, String unitName, int unitIndex) {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Crear unidad");
        dialog.setHeaderText("¿Cuántos " + unitName + " quieres crear?");
        dialog.setContentText("Cantidad:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(value -> {
            try {
                int cantidad = Integer.parseInt(value);
                if (cantidad > 0) {
                    unitCreator.accept(cantidad);
                    showInfo("Se han creado " + cantidad + " " + unitName + "(s).");
                    Platform.runLater(() -> {
                        updateUICallback.run();
                        armyTable.refresh();
                    });
                    System.out.println("DEBUG: " + unitName + " = " + civilization.getArmy()[unitIndex].size());
                }
            } catch (NumberFormatException ex) {
                showAlert("Cantidad no válida.");
            } catch (ResourceException | BuildingException ex) {
                showAlert(ex.getMessage());
            }
        });
    }

    public void updateUI() {
        armyTable.getItems().clear();
        ArrayList<MilitaryUnit>[] armyGroups = civilization.getArmy();
        String[] unitNames = {"Espadachín", "Lancero", "Ballesta", "Cañón",
                              "Torre Flechas", "Catapulta", "Torre Cohete", "Mago", "Sacerdote"};
        for (int i = 0; i < 9; i++) {
            int count = armyGroups[i].size();
            armyTable.getItems().add(unitNames[i] + " : " + count);
        }
        armyTable.refresh();
    }

    private void showAlert(String message) {
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private void showInfo(String message) {
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}