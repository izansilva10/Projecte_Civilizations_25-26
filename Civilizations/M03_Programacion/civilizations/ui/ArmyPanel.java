package civilizations.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import civilizations.*;

import java.util.ArrayList;
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

        // --- Acciones de los botones (código completo, sin referencia de método) ---
        swordsmanBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Crear unidad");
            dialog.setHeaderText("¿Cuántos Espadachines quieres crear?");
            dialog.setContentText("Cantidad:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(value -> {
                try {
                    int cantidad = Integer.parseInt(value);
                    if (cantidad > 0) {
                        civilization.newSwordsman(cantidad);
                        showInfo("Se han creado " + cantidad + " Espadachín(es).");
                        Platform.runLater(() -> {
                            updateUICallback.run();
                            armyTable.refresh();
                        });
                        System.out.println("DEBUG: Espadachines = " + civilization.getArmy()[0].size());
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Cantidad no válida.");
                } catch (ResourceException | BuildingException ex) {
                    showAlert(ex.getMessage());
                }
            });
        });

        spearmanBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Crear unidad");
            dialog.setHeaderText("¿Cuántos Lanceros quieres crear?");
            dialog.setContentText("Cantidad:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(value -> {
                try {
                    int cantidad = Integer.parseInt(value);
                    if (cantidad > 0) {
                        civilization.newSpearman(cantidad);
                        showInfo("Se han creado " + cantidad + " Lancero(s).");
                        Platform.runLater(() -> {
                            updateUICallback.run();
                            armyTable.refresh();
                        });
                        System.out.println("DEBUG: Lanceros = " + civilization.getArmy()[1].size());
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Cantidad no válida.");
                } catch (ResourceException | BuildingException ex) {
                    showAlert(ex.getMessage());
                }
            });
        });

        crossbowBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Crear unidad");
            dialog.setHeaderText("¿Cuántas Ballestas quieres crear?");
            dialog.setContentText("Cantidad:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(value -> {
                try {
                    int cantidad = Integer.parseInt(value);
                    if (cantidad > 0) {
                        civilization.newCrossbow(cantidad);
                        showInfo("Se han creado " + cantidad + " Ballesta(s).");
                        Platform.runLater(() -> {
                            updateUICallback.run();
                            armyTable.refresh();
                        });
                        System.out.println("DEBUG: Ballestas = " + civilization.getArmy()[2].size());
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Cantidad no válida.");
                } catch (ResourceException | BuildingException ex) {
                    showAlert(ex.getMessage());
                }
            });
        });

        cannonBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Crear unidad");
            dialog.setHeaderText("¿Cuántos Cañones quieres crear?");
            dialog.setContentText("Cantidad:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(value -> {
                try {
                    int cantidad = Integer.parseInt(value);
                    if (cantidad > 0) {
                        civilization.newCannon(cantidad);
                        showInfo("Se han creado " + cantidad + " Cañón(es).");
                        Platform.runLater(() -> {
                            updateUICallback.run();
                            armyTable.refresh();
                        });
                        System.out.println("DEBUG: Cañones = " + civilization.getArmy()[3].size());
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Cantidad no válida.");
                } catch (ResourceException | BuildingException ex) {
                    showAlert(ex.getMessage());
                }
            });
        });

        arrowTowerBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Crear defensa");
            dialog.setHeaderText("¿Cuántas Torres de Flechas quieres crear?");
            dialog.setContentText("Cantidad:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(value -> {
                try {
                    int cantidad = Integer.parseInt(value);
                    if (cantidad > 0) {
                        civilization.newArrowTower(cantidad);
                        showInfo("Se han creado " + cantidad + " Torre(s) de Flechas.");
                        Platform.runLater(() -> {
                            updateUICallback.run();
                            armyTable.refresh();
                        });
                        System.out.println("DEBUG: Torres Flechas = " + civilization.getArmy()[4].size());
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Cantidad no válida.");
                } catch (ResourceException | BuildingException ex) {
                    showAlert(ex.getMessage());
                }
            });
        });

        catapultBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Crear defensa");
            dialog.setHeaderText("¿Cuántas Catapultas quieres crear?");
            dialog.setContentText("Cantidad:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(value -> {
                try {
                    int cantidad = Integer.parseInt(value);
                    if (cantidad > 0) {
                        civilization.newCatapult(cantidad);
                        showInfo("Se han creado " + cantidad + " Catapulta(s).");
                        Platform.runLater(() -> {
                            updateUICallback.run();
                            armyTable.refresh();
                        });
                        System.out.println("DEBUG: Catapultas = " + civilization.getArmy()[5].size());
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Cantidad no válida.");
                } catch (ResourceException | BuildingException ex) {
                    showAlert(ex.getMessage());
                }
            });
        });

        rocketBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Crear defensa");
            dialog.setHeaderText("¿Cuántas Torres Cohete quieres crear?");
            dialog.setContentText("Cantidad:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(value -> {
                try {
                    int cantidad = Integer.parseInt(value);
                    if (cantidad > 0) {
                        civilization.newRocketLauncher(cantidad);
                        showInfo("Se han creado " + cantidad + " Torre(s) Cohete.");
                        Platform.runLater(() -> {
                            updateUICallback.run();
                            armyTable.refresh();
                        });
                        System.out.println("DEBUG: Torres Cohete = " + civilization.getArmy()[6].size());
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Cantidad no válida.");
                } catch (ResourceException | BuildingException ex) {
                    showAlert(ex.getMessage());
                }
            });
        });

        magicianBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Crear unidad especial");
            dialog.setHeaderText("¿Cuántos Magos quieres crear?");
            dialog.setContentText("Cantidad:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(value -> {
                try {
                    int cantidad = Integer.parseInt(value);
                    if (cantidad > 0) {
                        civilization.newMagician(cantidad);
                        showInfo("Se han creado " + cantidad + " Mago(s).");
                        Platform.runLater(() -> {
                            updateUICallback.run();
                            armyTable.refresh();
                        });
                        System.out.println("DEBUG: Magos = " + civilization.getArmy()[7].size());
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Cantidad no válida.");
                } catch (ResourceException | BuildingException ex) {
                    showAlert(ex.getMessage());
                }
            });
        });

        priestBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Crear unidad especial");
            dialog.setHeaderText("¿Cuántos Sacerdotes quieres crear?");
            dialog.setContentText("Cantidad:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(value -> {
                try {
                    int cantidad = Integer.parseInt(value);
                    if (cantidad > 0) {
                        civilization.newPriest(cantidad);
                        showInfo("Se han creado " + cantidad + " Sacerdote(s).");
                        Platform.runLater(() -> {
                            updateUICallback.run();
                            armyTable.refresh();
                        });
                        System.out.println("DEBUG: Sacerdotes = " + civilization.getArmy()[8].size());
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Cantidad no válida.");
                } catch (ResourceException | BuildingException ex) {
                    showAlert(ex.getMessage());
                }
            });
        });

        VBox buttonsContainer = new VBox(10);
        buttonsContainer.getChildren().addAll(attackLabel, attackBox, defenseLabel, defenseBox);
        buttonsContainer.setPadding(new Insets(10, 0, 0, 0));

        Button refreshBtn = new Button("🔄 Refrescar tabla");
        refreshBtn.setOnAction(e -> updateUICallback.run());

        panel.getChildren().addAll(new Label("Ejército actual:"), armyTable, buttonsContainer, refreshBtn);
        return panel;
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