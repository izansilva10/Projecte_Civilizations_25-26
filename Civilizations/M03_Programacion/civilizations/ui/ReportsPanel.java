package civilizations.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import civilizations.Battle;

import java.util.ArrayList;

public class ReportsPanel {
    private ArrayList<Battle> battleHistory;
    private VBox contentBox;  // Contenedor dinámico para mostrar los datos de la batalla
    private Label winsLabel, lossesLabel, battleCounterLabel;

    public ReportsPanel(ArrayList<Battle> battleHistory) {
        this.battleHistory = battleHistory;
    }

    public VBox getPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(15, 15, 15, 15));
        panel.setStyle("-fx-background: linear-gradient(from 0% 0% to 0% 100%, #3a6073, #16222a);");
        panel.setAlignment(Pos.TOP_CENTER);

        // Título principal
        Label title = new Label("📜 HISTORIAL DE BATALLAS");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
        title.setAlignment(Pos.CENTER);
        title.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(title, new Insets(0, 0, 10, 0));

        // Área de contenido desplazable
        contentBox = new VBox(15);
        contentBox.setPadding(new Insets(10));
        contentBox.setStyle("-fx-background-color: rgba(0,0,0,0.4); -fx-border-radius: 8;");
        
        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setPrefHeight(400);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Título del marcador
        Label scoreTitle = new Label("RESULTADO DE LAS BATALLAS");
        scoreTitle.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        scoreTitle.setAlignment(Pos.CENTER);
        scoreTitle.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(scoreTitle, new Insets(0, 0, 5, 0));

        // Marcador compacto (igual)
        GridPane scoreGrid = new GridPane();
        scoreGrid.setAlignment(Pos.CENTER);
        scoreGrid.setHgap(12);
        scoreGrid.setVgap(6);
        scoreGrid.setPadding(new Insets(8, 12, 8, 12));
        scoreGrid.setStyle("-fx-background-color: #1a2a3a; -fx-border-color: #5dade2; -fx-border-radius: 8; -fx-border-width: 1;");
        scoreGrid.setMaxWidth(360);

        Label playerHeader = new Label("JUGADOR");
        playerHeader.setStyle("-fx-text-fill: #e6c97e; -fx-font-size: 13px; -fx-font-weight: bold;");
        Label enemyHeader = new Label("ENEMIGO");
        enemyHeader.setStyle("-fx-text-fill: #e6c97e; -fx-font-size: 13px; -fx-font-weight: bold;");
        scoreGrid.add(playerHeader, 0, 0);
        scoreGrid.add(enemyHeader, 2, 0);

        winsLabel = new Label("0");
        winsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold; -fx-font-family: 'Courier New', monospace;");
        lossesLabel = new Label("0");
        lossesLabel.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold; -fx-font-family: 'Courier New', monospace;");
        scoreGrid.add(winsLabel, 0, 1);
        scoreGrid.add(lossesLabel, 2, 1);

        battleCounterLabel = new Label("0");
        battleCounterLabel.setStyle("-fx-text-fill: #e6c97e; -fx-font-size: 18px; -fx-font-weight: bold;");
        Label battleText = new Label("BATALLA");
        battleText.setStyle("-fx-text-fill: #e6c97e; -fx-font-size: 11px;");
        VBox centerBox = new VBox(2);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getChildren().addAll(battleCounterLabel, battleText);
        scoreGrid.add(centerBox, 1, 1);

        Label winLetter = new Label("V");
        winLetter.setStyle("-fx-text-fill: #aaffaa; -fx-font-size: 13px;");
        Label lossLetter = new Label("D");
        lossLetter.setStyle("-fx-text-fill: #ffaaaa; -fx-font-size: 13px;");
        scoreGrid.add(winLetter, 0, 2);
        scoreGrid.add(lossLetter, 2, 2);

        scoreGrid.setHalignment(playerHeader, javafx.geometry.HPos.CENTER);
        scoreGrid.setHalignment(enemyHeader, javafx.geometry.HPos.CENTER);
        scoreGrid.setHalignment(winsLabel, javafx.geometry.HPos.CENTER);
        scoreGrid.setHalignment(lossesLabel, javafx.geometry.HPos.CENTER);
        scoreGrid.setHalignment(winLetter, javafx.geometry.HPos.CENTER);
        scoreGrid.setHalignment(lossLetter, javafx.geometry.HPos.CENTER);

        panel.getChildren().addAll(title, scrollPane, scoreTitle, scoreGrid);
        return panel;
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
            Label emptyLabel = new Label("No hay batallas registradas.");
            emptyLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
            contentBox.getChildren().add(emptyLabel);
            return;
        }

        Battle lastBattle = battleHistory.get(battleHistory.size() - 1);
        int battleNum = battleHistory.size();
        displayBattleData(lastBattle, battleNum);
    }

    private void displayBattleData(Battle battle, int battleNum) {
        contentBox.getChildren().clear();

        String report = battle.getBattleReport(battleNum);
        
        // Parsear reporte
        String[] lines = report.split("\n");
        
        // Crear GridPane para las dos columnas (jugador vs enemigo)
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(20);
        statsGrid.setVgap(10);
        statsGrid.setPadding(new Insets(10));
        statsGrid.setAlignment(Pos.CENTER);
        statsGrid.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-border-radius: 8;");
        
        // Cabeceras centradas
        Label civHeader = new Label("CIVILIZACIÓN");
        civHeader.setStyle("-fx-text-fill: #5dade2; -fx-font-size: 16px; -fx-font-weight: bold;");
        Label eneHeader = new Label("ENEMIGO");
        eneHeader.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 16px; -fx-font-weight: bold;");
        statsGrid.add(civHeader, 0, 0);
        statsGrid.add(eneHeader, 1, 0);
        statsGrid.setHalignment(civHeader, javafx.geometry.HPos.CENTER);
        statsGrid.setHalignment(eneHeader, javafx.geometry.HPos.CENTER);
        
        VBox civUnitsBox = new VBox(5);
        VBox eneUnitsBox = new VBox(5);
        civUnitsBox.setAlignment(Pos.CENTER);
        eneUnitsBox.setAlignment(Pos.CENTER);
        civUnitsBox.setStyle("-fx-padding: 5;");
        eneUnitsBox.setStyle("-fx-padding: 5;");
        
        // Parsear unidades
        boolean inStats = false;
        for (String line : lines) {
            if (line.contains("BATTLE STATISTICS")) {
                inStats = true;
                continue;
            }
            if (inStats && line.contains("*****")) {
                break;
            }
            if (inStats && line.trim().length() > 0 && !line.contains("Army planet")) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length >= 5) {
                    String unitCiv = parts[0];
                    String countCiv = parts[1];
                    String dropsCiv = parts[2];
                    String unitEne = parts[3];
                    String countEne = parts[4];
                    String dropsEne = parts.length > 5 ? parts[5] : "0";
                    
                    Label civLine = new Label(unitCiv + ": " + countCiv + " (bajas: " + dropsCiv + ")");
                    civLine.setStyle("-fx-text-fill: #aaccff; -fx-font-size: 13px;");
                    Label eneLine = new Label(unitEne + ": " + countEne + " (bajas: " + dropsEne + ")");
                    eneLine.setStyle("-fx-text-fill: #ffaaaa; -fx-font-size: 13px;");
                    civUnitsBox.getChildren().add(civLine);
                    eneUnitsBox.getChildren().add(eneLine);
                } else if (parts.length == 3) {
                    // Solo civilización (defensas, especiales)
                    String unitCiv = parts[0];
                    String countCiv = parts[1];
                    String dropsCiv = parts[2];
                    Label civLine = new Label(unitCiv + ": " + countCiv + " (bajas: " + dropsCiv + ")");
                    civLine.setStyle("-fx-text-fill: #aaccff; -fx-font-size: 13px;");
                    civUnitsBox.getChildren().add(civLine);
                }
            }
        }
        
        statsGrid.add(civUnitsBox, 0, 1);
        statsGrid.add(eneUnitsBox, 1, 1);
        
        // Extraer costes, pérdidas, residuos, resultado
        String civCost = "", eneCost = "", civLoss = "", eneLoss = "", waste = "", result = "";
        for (String line : lines) {
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
        
        // Crear las tarjetas centradas
        HBox costsBox = new HBox(20);
        costsBox.setAlignment(Pos.CENTER);
        VBox civCostBox = createInfoBox("COSTES", civCost, "#5dade2");
        VBox eneCostBox = createInfoBox("COSTES", eneCost, "#e74c3c");
        costsBox.getChildren().addAll(civCostBox, eneCostBox);
        
        HBox lossesBox = new HBox(20);
        lossesBox.setAlignment(Pos.CENTER);
        VBox civLossBox = createInfoBox("PÉRDIDAS", civLoss, "#5dade2");
        VBox eneLossBox = createInfoBox("PÉRDIDAS", eneLoss, "#e74c3c");
        lossesBox.getChildren().addAll(civLossBox, eneLossBox);
        
        VBox wasteBox = createInfoBox("RECURSOS GANADOS", waste, "#f1c40f");
        wasteBox.setAlignment(Pos.CENTER);
        
        VBox resultBox = createInfoBox("RESULTADO", result, "#e67e22");
        resultBox.setAlignment(Pos.CENTER);
        
        // No se añade botón de desarrollo
        contentBox.getChildren().addAll(statsGrid, costsBox, lossesBox, wasteBox, resultBox);
    }
    
    private VBox createInfoBox(String title, String content, String color) {
        VBox box = new VBox(5);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: " + color + "20; -fx-border-color: " + color + "; -fx-border-radius: 5; -fx-border-width: 1;");
        box.setAlignment(Pos.CENTER);
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold; -fx-font-size: 14px;");
        Label contentLabel = new Label(content);
        contentLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");
        contentLabel.setWrapText(true);
        contentLabel.setAlignment(Pos.CENTER);
        box.getChildren().addAll(titleLabel, contentLabel);
        return box;
    }
}