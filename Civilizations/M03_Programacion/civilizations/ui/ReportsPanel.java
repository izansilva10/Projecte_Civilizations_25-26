package civilizations.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import civilizations.Battle;
import civilizations.Civilization;

import java.util.ArrayList;

public class ReportsPanel {
    private ArrayList<Battle> battleHistory;
    private VBox contentBox;
    private Label winsLabel, lossesLabel, battleCounterLabel;
    private Label foodValueLabel, woodValueLabel, ironValueLabel, manaValueLabel;

    public ReportsPanel(ArrayList<Battle> battleHistory) {
        this.battleHistory = battleHistory;
    }

    public VBox getPanel() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(20));

        HBox resourcesBar = createResourceBar();

        VBox mainContent = new VBox(25);
        mainContent.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("📜 CENTRO DE REPORTES DE GUERRA");
        title.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 24px; -fx-font-weight: bold;");
        title.setAlignment(Pos.CENTER);
        title.setMaxWidth(Double.MAX_VALUE);

        HBox scoreboard = createScoreboard();

        VBox contentContainer = new VBox(15);
        contentContainer.setPadding(new Insets(10));
        contentContainer.setStyle("-fx-background-color: rgba(15, 23, 32, 0.6); -fx-border-color: #4ea5d9; -fx-border-radius: 12; -fx-background-radius: 12;");

        contentBox = new VBox(15);
        contentBox.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setPrefHeight(400);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        contentContainer.getChildren().add(scrollPane);

        mainContent.getChildren().addAll(title, scoreboard, contentContainer);
        panel.getChildren().addAll(resourcesBar, mainContent);
        return panel;
    }

    private HBox createResourceBar() {
        Civilization civilization = null;
        return ResourceBar.create(civilization, foodValueLabel, woodValueLabel, ironValueLabel, manaValueLabel);
    }

    private HBox createScoreboard() {
        HBox scoreboard = new HBox(30);
        scoreboard.setAlignment(Pos.CENTER);
        scoreboard.getStyleClass().add("scoreboard");
        scoreboard.setMaxWidth(700);

        VBox winsBox = new VBox(5);
        winsBox.setAlignment(Pos.CENTER);
        Label winsTitle = new Label("VICTORIAS");
        winsTitle.getStyleClass().add("team-name");
        winsLabel = new Label("0");
        winsLabel.getStyleClass().addAll("score", "victory");
        winsBox.getChildren().addAll(winsTitle, winsLabel);

        Label vs = new Label("⚔️ VS");
        vs.getStyleClass().add("vs");

        VBox lossesBox = new VBox(5);
        lossesBox.setAlignment(Pos.CENTER);
        Label lossesTitle = new Label("DERROTAS");
        lossesTitle.getStyleClass().add("team-name");
        lossesLabel = new Label("0");
        lossesLabel.getStyleClass().addAll("score", "defeat");
        lossesBox.getChildren().addAll(lossesTitle, lossesLabel);

        VBox counterBox = new VBox(2);
        counterBox.setAlignment(Pos.CENTER);
        Label counterTitle = new Label("BATALLAS LIBRADAS");
        counterTitle.setStyle("-fx-text-fill: #b2bec3; -fx-font-size: 12px;");
        battleCounterLabel = new Label("0");
        battleCounterLabel.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 20px; -fx-font-weight: bold;");
        counterBox.getChildren().addAll(battleCounterLabel, counterTitle);

        Region spacer1 = new Region();
        Region spacer2 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        scoreboard.getChildren().addAll(winsBox, spacer1, vs, spacer2, lossesBox, counterBox);
        return scoreboard;
    }

    public void updateUI() {
        int wins = 0;
        int losses = 0;
        int totalBattles = 0;

        if (battleHistory != null) {
            totalBattles = battleHistory.size();
            for (Battle b : battleHistory) {
                if (b.civilizationWon()) wins++;
                else losses++;
            }
        }

        winsLabel.setText(String.valueOf(wins));
        lossesLabel.setText(String.valueOf(losses));
        battleCounterLabel.setText(String.valueOf(totalBattles));

        if (battleHistory == null || battleHistory.isEmpty()) {
            contentBox.getChildren().clear();
            Label emptyLabel = new Label("No hay batallas registradas en el historial.");
            emptyLabel.setStyle("-fx-text-fill: #b2bec3; -fx-font-size: 16px; -fx-font-style: italic;");
            contentBox.getChildren().add(emptyLabel);
            return;
        }

        Battle lastBattle = battleHistory.get(battleHistory.size() - 1);
        int battleNum = battleHistory.size();
        displayBattleData(lastBattle, battleNum);
    }

    private void displayBattleData(Battle battle, int battleNum) {
        contentBox.getChildren().clear();

        Label header = new Label("⚔️ INFORME DE BATALLA #" + battleNum);
        header.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 0 0 10 0; -fx-border-color: #4ea5d9; -fx-border-width: 0 0 1 0;");
        contentBox.getChildren().add(header);

        String report = battle.getBattleReport(battleNum);
        String[] lines = report.split("\n");

        ArrayList<String> civUnitsList = new ArrayList<>();
        ArrayList<String> eneUnitsList = new ArrayList<>();
        String civCost = "", eneCost = "", civLoss = "", eneLoss = "", waste = "", result = "";
        boolean inStats = false;

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.contains("BATTLE STATISTICS")) {
                inStats = true;
                continue;
            }
            if (inStats && line.contains("*****")) break;

            if (inStats && !line.contains("Army planet")) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 5) {
                    civUnitsList.add(parts[0] + ": " + parts[1] + " (bajas: " + parts[2] + ")");
                    eneUnitsList.add(parts[3] + ": " + parts[4] + " (bajas: " + (parts.length > 5 ? parts[5] : "0") + ")");
                } else if (parts.length == 3) {
                    civUnitsList.add(parts[0] + ": " + parts[1] + " (bajas: " + parts[2] + ")");
                }
                continue;
            }

            if (line.contains("Cost Army Civilization:")) {
                civCost = line.replace("Cost Army Civilization:", "").trim();
            } else if (line.contains("Cost Army Enemy:")) {
                eneCost = line.replace("Cost Army Enemy:", "").trim();
            } else if (line.contains("Losses Army Civilization:")) {
                civLoss = line.replace("Losses Army Civilization:", "").trim();
            } else if (line.contains("Losses Army Enemy:")) {
                eneLoss = line.replace("Losses Army Enemy:", "").trim();
            } else if (line.contains("Waste Generated:")) {
                waste = line.replace("Waste Generated:", "").trim();
            } else if (line.contains("Battle Winned by")) {
                result = line.trim();
            }
        }

        // --- ESTADÍSTICAS DE UNIDADES ---
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(30);
        statsGrid.setVgap(8);
        statsGrid.setPadding(new Insets(10));
        statsGrid.setStyle("-fx-background-color: rgba(15, 23, 32, 0.8); -fx-border-color: #4ea5d9; -fx-border-radius: 8; -fx-background-radius: 8;");

        Label civHeader = new Label("CIVILIZACIÓN");
        civHeader.setStyle("-fx-text-fill: #4ea5d9; -fx-font-size: 16px; -fx-font-weight: bold;");
        Label eneHeader = new Label("ENEMIGO");
        eneHeader.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 16px; -fx-font-weight: bold;");

        statsGrid.add(civHeader, 0, 0);
        statsGrid.add(eneHeader, 1, 0);

        VBox civUnitsBox = new VBox(5);
        for (String unit : civUnitsList) {
            Label label = new Label(unit);
            label.setStyle("-fx-text-fill: #b2bec3; -fx-font-size: 14px;");
            civUnitsBox.getChildren().add(label);
        }
        VBox eneUnitsBox = new VBox(5);
        for (String unit : eneUnitsList) {
            Label label = new Label(unit);
            label.setStyle("-fx-text-fill: #b2bec3; -fx-font-size: 14px;");
            eneUnitsBox.getChildren().add(label);
        }

        statsGrid.add(civUnitsBox, 0, 1);
        statsGrid.add(eneUnitsBox, 1, 1);
        contentBox.getChildren().add(statsGrid);

        // --- COSTES Y PÉRDIDAS ---
        HBox costsBox = new HBox(20);
        costsBox.setAlignment(Pos.CENTER);
        costsBox.getChildren().addAll(
            createInfoBox("COSTES INICIALES", civCost, "#4ea5d9"),
            createInfoBox("COSTES INICIALES", eneCost, "#e74c3c")
        );
        contentBox.getChildren().add(costsBox);

        HBox lossesBox = new HBox(20);
        lossesBox.setAlignment(Pos.CENTER);
        lossesBox.getChildren().addAll(
            createInfoBox("PÉRDIDAS", civLoss, "#4ea5d9"),
            createInfoBox("PÉRDIDAS", eneLoss, "#e74c3c")
        );
        contentBox.getChildren().add(lossesBox);

        // --- RESIDUOS Y RESULTADO ---
        VBox wasteBox = createInfoBox("RECURSOS GANADOS", waste, "#d4ac0d");
        wasteBox.setAlignment(Pos.CENTER);
        contentBox.getChildren().add(wasteBox);

        VBox resultBox = createInfoBox("RESULTADO DE LA BATALLA", result, "#f39c12");
        resultBox.setAlignment(Pos.CENTER);
        contentBox.getChildren().add(resultBox);

        // --- LOG DE DESARROLLO ---
        VBox logBox = new VBox(8);
        logBox.setPadding(new Insets(10));
        logBox.setAlignment(Pos.TOP_LEFT);
        logBox.getStyleClass().add("log-area");

        Label logLabel = new Label("📄 DESARROLLO DE LA BATALLA");
        logLabel.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 16px; -fx-font-weight: bold;");

        String development = battle.getBattleDevelopment();
        Label logContent = new Label(development);
        logContent.setStyle("-fx-text-fill: #b2bec3; -fx-font-family: 'Courier New', monospace; -fx-font-size: 13px;");
        logContent.setWrapText(true);

        logBox.getChildren().addAll(logLabel, logContent);
        contentBox.getChildren().add(logBox);
    }

    private VBox createInfoBox(String title, String content, String color) {
        VBox box = new VBox(5);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: " + color + "15; -fx-border-color: " + color + "; -fx-border-radius: 6; -fx-border-width: 1;");
        box.setAlignment(Pos.CENTER);
        box.setMaxWidth(400);
        box.setMinWidth(250);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold; -fx-font-size: 14px;");

        Label contentLabel = new Label(content);
        contentLabel.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 13px;");
        contentLabel.setWrapText(true);
        contentLabel.setAlignment(Pos.CENTER);

        box.getChildren().addAll(titleLabel, contentLabel);
        return box;
    }

    public void updateResources(int food, int wood, int iron, int mana) {
        foodValueLabel.setText(String.format("%,d", food));
        woodValueLabel.setText(String.format("%,d", wood));
        ironValueLabel.setText(String.format("%,d", iron));
        manaValueLabel.setText(String.format("%,d", mana));
    }
}