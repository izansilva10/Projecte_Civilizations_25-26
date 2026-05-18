package civilizations.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import civilizations.Civilization;
import civilizations.ResourceException;

public class ResourcesPanel {
    private Civilization civilization;
    private Runnable updateUICallback;

    // Labels que se actualizarán
    private Label foodLabel, woodLabel, ironLabel, manaLabel;
    private Label techDefLabel, techAtkLabel;
    private Label farmLabel, carpentryLabel, smithyLabel, magicTowerLabel, churchLabel;

    public ResourcesPanel(Civilization civilization, Runnable updateUICallback) {
        this.civilization = civilization;
        this.updateUICallback = updateUICallback;
    }

    public VBox getPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(15));
        panel.setAlignment(Pos.TOP_CENTER);

        // Panel de recursos
        GridPane resourcesGrid = new GridPane();
        resourcesGrid.setHgap(20);
        resourcesGrid.setVgap(10);
        resourcesGrid.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5; -fx-padding: 10;");

        foodLabel = new Label();
        woodLabel = new Label();
        ironLabel = new Label();
        manaLabel = new Label();
        techDefLabel = new Label();
        techAtkLabel = new Label();

        resourcesGrid.add(new Label("🍽️ Comida:"), 0, 0);
        resourcesGrid.add(foodLabel, 1, 0);
        resourcesGrid.add(new Label("🪵 Madera:"), 2, 0);
        resourcesGrid.add(woodLabel, 3, 0);
        resourcesGrid.add(new Label("⛏️ Hierro:"), 0, 1);
        resourcesGrid.add(ironLabel, 1, 1);
        resourcesGrid.add(new Label("✦ Maná:"), 2, 1);
        resourcesGrid.add(manaLabel, 3, 1);
        resourcesGrid.add(new Label("🛡️ Tecnología Defensa:"), 0, 2);
        resourcesGrid.add(techDefLabel, 1, 2);
        resourcesGrid.add(new Label("⚔️ Tecnología Ataque:"), 2, 2);
        resourcesGrid.add(techAtkLabel, 3, 2);

        // Panel de edificios
        GridPane buildingsGrid = new GridPane();
        buildingsGrid.setHgap(20);
        buildingsGrid.setVgap(10);
        buildingsGrid.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5; -fx-padding: 10;");
        farmLabel = new Label();
        carpentryLabel = new Label();
        smithyLabel = new Label();
        magicTowerLabel = new Label();
        churchLabel = new Label();

        buildingsGrid.add(new Label("🏚️ Granjas:"), 0, 0);
        buildingsGrid.add(farmLabel, 1, 0);
        buildingsGrid.add(new Label("🪚 Carpinterías:"), 2, 0);
        buildingsGrid.add(carpentryLabel, 3, 0);
        buildingsGrid.add(new Label("⚒️ Herrerías:"), 0, 1);
        buildingsGrid.add(smithyLabel, 1, 1);
        buildingsGrid.add(new Label("🏛️ Torres Mágicas:"), 2, 1);
        buildingsGrid.add(magicTowerLabel, 3, 1);
        buildingsGrid.add(new Label("⛪ Iglesias:"), 0, 2);
        buildingsGrid.add(churchLabel, 1, 2);

        // Botones de construcción
        GridPane buildButtons = new GridPane();
        buildButtons.setHgap(10);
        buildButtons.setVgap(10);
        Button farmBtn = new Button("Construir Granja");
        Button carpentryBtn = new Button("Construir Carpintería");
        Button smithyBtn = new Button("Construir Herrería");
        Button magicTowerBtn = new Button("Construir Torre Mágica");
        Button churchBtn = new Button("Construir Iglesia");

        farmBtn.setOnAction(e -> {
            try {
                civilization.newFarm();
                showInfo("Granja construida correctamente.");
                updateUICallback.run();
            } catch (ResourceException ex) {
                showAlert(ex.getMessage());
            }
        });
        carpentryBtn.setOnAction(e -> {
            try {
                civilization.newCarpentry();
                showInfo("Carpintería construida correctamente.");
                updateUICallback.run();
            } catch (ResourceException ex) {
                showAlert(ex.getMessage());
            }
        });
        smithyBtn.setOnAction(e -> {
            try {
                civilization.newSmithy();
                showInfo("Herrería construida correctamente.");
                updateUICallback.run();
            } catch (ResourceException ex) {
                showAlert(ex.getMessage());
            }
        });
        magicTowerBtn.setOnAction(e -> {
            try {
                civilization.newMagicTower();
                showInfo("Torre Mágica construida correctamente.");
                updateUICallback.run();
            } catch (ResourceException ex) {
                showAlert(ex.getMessage());
            }
        });
        churchBtn.setOnAction(e -> {
            try {
                civilization.newChurch();
                showInfo("Iglesia construida correctamente.");
                updateUICallback.run();
            } catch (ResourceException ex) {
                showAlert(ex.getMessage());
            }
        });

        buildButtons.add(farmBtn, 0, 0);
        buildButtons.add(carpentryBtn, 1, 0);
        buildButtons.add(smithyBtn, 2, 0);
        buildButtons.add(magicTowerBtn, 3, 0);
        buildButtons.add(churchBtn, 4, 0);

        // Botones de tecnología
        HBox techBox = new HBox(10);
        Button upgradeDefenseBtn = new Button("Mejorar Defensa");
        Button upgradeAttackBtn = new Button("Mejorar Ataque");
        upgradeDefenseBtn.setOnAction(e -> {
            try {
                civilization.upgradeTechnologyDefense();
                showInfo("Tecnología de Defensa mejorada.");
                updateUICallback.run();
            } catch (ResourceException ex) {
                showAlert(ex.getMessage());
            }
        });
        upgradeAttackBtn.setOnAction(e -> {
            try {
                civilization.upgradeTechnologyAttack();
                showInfo("Tecnología de Ataque mejorada.");
                updateUICallback.run();
            } catch (ResourceException ex) {
                showAlert(ex.getMessage());
            }
        });
        techBox.getChildren().addAll(upgradeDefenseBtn, upgradeAttackBtn);

        panel.getChildren().addAll(resourcesGrid, buildingsGrid, buildButtons, techBox);
        return panel;
    }

    public void updateUI() {
        foodLabel.setText(String.format("%,d", civilization.getFood()));
        woodLabel.setText(String.format("%,d", civilization.getWood()));
        ironLabel.setText(String.format("%,d", civilization.getIron()));
        manaLabel.setText(String.format("%,d", civilization.getMana()));
        techDefLabel.setText(String.valueOf(civilization.getTechnologyDefense()));
        techAtkLabel.setText(String.valueOf(civilization.getTechnologyAttack()));
        farmLabel.setText(String.valueOf(civilization.getFarm()));
        carpentryLabel.setText(String.valueOf(civilization.getCarpentry()));
        smithyLabel.setText(String.valueOf(civilization.getSmithy()));
        magicTowerLabel.setText(String.valueOf(civilization.getMagicTower()));
        churchLabel.setText(String.valueOf(civilization.getChurch()));
    }

    private void showAlert(String message) {
        javafx.application.Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private void showInfo(String message) {
        javafx.application.Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
