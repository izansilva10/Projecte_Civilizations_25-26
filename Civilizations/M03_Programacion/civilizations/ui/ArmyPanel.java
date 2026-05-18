package civilizations.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import civilizations.*;

import java.util.ArrayList;
import java.util.Optional;

public class ArmyPanel {
    private Civilization civilization;
    private Runnable updateUICallback;
    private Label[] counters;  // Array de 9 etiquetas para mostrar cantidades

    public ArmyPanel(Civilization civilization, Runnable updateUICallback) {
        this.civilization = civilization;
        this.updateUICallback = updateUICallback;
        this.counters = new Label[9];
        for (int i = 0; i < 9; i++) {
            counters[i] = new Label("0");
            counters[i].setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        }
    }

    public VBox getPanel() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(15));

        // Título
        Label title = new Label("EJÉRCITO ACTUAL");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        panel.getChildren().add(title);

        // --- Unidades de Ataque ---
        Label attackTitle = new Label("⚔️ UNIDADES DE ATAQUE");
        attackTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #27ae60;");
        GridPane attackGrid = createUnitGrid(new String[]{"Espadachín", "Lancero", "Ballesta", "Cañón"}, new int[]{0,1,2,3});

        // --- Defensas y Especiales ---
        Label defenseTitle = new Label("🛡️ DEFENSAS Y ESPECIALES");
        defenseTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #e67e22;");
        GridPane defenseGrid = createUnitGrid(new String[]{"Torre Flechas", "Catapulta", "Torre Cohete", "Mago", "Sacerdote"}, new int[]{4,5,6,7,8});

        // Botón de refresco manual (por si acaso)
        Button refreshBtn = new Button("🔄 Refrescar Contadores");
        refreshBtn.setOnAction(e -> updateUICallback.run());

        panel.getChildren().addAll(attackTitle, attackGrid, defenseTitle, defenseGrid, refreshBtn);
        return panel;
    }

    private GridPane createUnitGrid(String[] unitNames, int[] indices) {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.setPadding(new Insets(10, 0, 10, 0));
        grid.setAlignment(Pos.CENTER_LEFT);

        for (int i = 0; i < unitNames.length; i++) {
            String name = unitNames[i];
            int idx = indices[i];
            Label nameLabel = new Label(name);
            nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            Label counter = counters[idx];
            HBox counterBox = new HBox(5);
            counterBox.setAlignment(Pos.CENTER_LEFT);
            counterBox.getChildren().addAll(new Label("Cantidad:"), counter);

            Button addBtn = new Button("➕ Añadir");
            addBtn.setOnAction(e -> showAddDialog(name, idx));

            grid.add(nameLabel, 0, i);
            grid.add(counterBox, 1, i);
            grid.add(addBtn, 2, i);
        }
        return grid;
    }

    private void showAddDialog(String unitName, int unitIndex) {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Añadir " + unitName);
        dialog.setHeaderText("¿Cuántos " + unitName + " quieres añadir?");
        dialog.setContentText("Cantidad:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(value -> {
            try {
                int cantidad = Integer.parseInt(value);
                if (cantidad > 0) {
                    addUnitByIndex(unitIndex, cantidad);
                    showInfo("Se han añadido " + cantidad + " " + unitName + "(s).");
                    Platform.runLater(() -> {
                        updateUICallback.run();  // Actualiza todos los paneles y los contadores
                    });
                }
            } catch (NumberFormatException ex) {
                showAlert("Cantidad no válida.");
            } catch (ResourceException | BuildingException ex) {
                showAlert(ex.getMessage());
            }
        });
    }

    private void addUnitByIndex(int index, int amount) throws ResourceException, BuildingException {
        switch (index) {
            case 0: civilization.newSwordsman(amount); break;
            case 1: civilization.newSpearman(amount); break;
            case 2: civilization.newCrossbow(amount); break;
            case 3: civilization.newCannon(amount); break;
            case 4: civilization.newArrowTower(amount); break;
            case 5: civilization.newCatapult(amount); break;
            case 6: civilization.newRocketLauncher(amount); break;
            case 7: civilization.newMagician(amount); break;
            case 8: civilization.newPriest(amount); break;
        }
    }

    public void updateUI() {
        // Actualizar los contadores con los valores actuales de la civilización
        ArrayList<MilitaryUnit>[] armyGroups = civilization.getArmy();
        for (int i = 0; i < 9; i++) {
            int count = armyGroups[i].size();
            counters[i].setText(String.valueOf(count));
        }
    }

    private void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private void showInfo(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}