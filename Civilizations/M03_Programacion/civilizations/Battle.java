package civilizations;

import java.util.ArrayList;
import java.util.Random;

public class Battle {
    // Ejércitos planos
    private ArrayList<MilitaryUnit> civilizationArmy;
    private ArrayList<MilitaryUnit> enemyArmy;
    // Desarrollo de la batalla
    private String battleDevelopment;
    // Costes iniciales de flotas: [0] civilización, [1] enemigo -> [food, wood, iron]
    private int[][] initialCostFleet;
    // Número inicial de unidades
    private int initialNumberUnitsCivilization;
    private int initialNumberUnitsEnemy;
    // Residuos generados [madera, hierro]
    private int[] wasteWoodIron;
    // Unidades caídas (drops)
    private int enemyDrops;
    private int civilizationDrops;
    // Pérdidas ponderadas: [0] civilización, [1] enemigo -> [food, wood, iron, ponderado]
    private int[][] resourcesLooses;
    // Conteo inicial por tipo: [0] civilización (9 tipos), [1] enemigo (4 tipos)
    private int[][] initialArmies;
    // Conteo actual por tipo
    private int[] actualNumberUnitsCivilization;
    private int[] actualNumberUnitsEnemy;
    private Random rand;

    public Battle(ArrayList<MilitaryUnit>[] civilizationArmyGroups, ArrayList<MilitaryUnit> enemyArmyList) {
        rand = new Random();
        battleDevelopment = "";
        wasteWoodIron = new int[2];
        resourcesLooses = new int[2][4];
        initialArmies = new int[2][9]; // enemigo solo usa índices 0-3
        actualNumberUnitsCivilization = new int[9];
        actualNumberUnitsEnemy = new int[4];

        // Civilización: grupos ya vienen separados
        civilizationArmy = new ArrayList<MilitaryUnit>();
        for (int i = 0; i < 9; i++) {
            initialArmies[0][i] = civilizationArmyGroups[i].size();
            actualNumberUnitsCivilization[i] = initialArmies[0][i];
            for (MilitaryUnit u : civilizationArmyGroups[i]) {
                civilizationArmy.add(u);
            }
        }

        // Enemigo: lista plana, contar tipos
        enemyArmy = new ArrayList<MilitaryUnit>(enemyArmyList);
        for (MilitaryUnit u : enemyArmy) {
            if (u instanceof Swordsman)      actualNumberUnitsEnemy[0]++;
            else if (u instanceof Spearman)  actualNumberUnitsEnemy[1]++;
            else if (u instanceof Crossbow)  actualNumberUnitsEnemy[2]++;
            else if (u instanceof Cannon)    actualNumberUnitsEnemy[3]++;
        }
        for (int i = 0; i < 4; i++) {
            initialArmies[1][i] = actualNumberUnitsEnemy[i];
        }

        initialNumberUnitsCivilization = civilizationArmy.size();
        initialNumberUnitsEnemy = enemyArmy.size();

        // Calcular costes iniciales de las flotas
        initialCostFleet = new int[2][3];
        initialCostFleet[0] = fleetResourceCost(civilizationArmyGroups, true);
        initialCostFleet[1] = fleetResourceCost(enemyArmyList, false);
    }

    // Calcula el coste total (food, wood, iron) de un ejército
    private int[] fleetResourceCost(Object army, boolean isCivilization) {
        int food = 0, wood = 0, iron = 0;
        if (isCivilization) {
            ArrayList<MilitaryUnit>[] groups = (ArrayList<MilitaryUnit>[]) army;
            for (int i = 0; i < 9; i++) {
                for (MilitaryUnit u : groups[i]) {
                    food += u.getFoodCost();
                    wood += u.getWoodCost();
                    iron += u.getIronCost();
                }
            }
        } else {
            ArrayList<MilitaryUnit> list = (ArrayList<MilitaryUnit>) army;
            for (MilitaryUnit u : list) {
                food += u.getFoodCost();
                wood += u.getWoodCost();
                iron += u.getIronCost();
            }
        }
        return new int[]{food, wood, iron};
    }

    public void startBattle() {
        boolean civilizationAttacks = rand.nextBoolean();

        while (true) {
            if (civilizationArmy.size() <= initialNumberUnitsCivilization * 0.2 ||
                enemyArmy.size() <= initialNumberUnitsEnemy * 0.2) {
                break;
            }

            if (civilizationAttacks) {
                performAttack(civilizationArmy, enemyArmy, true);
            } else {
                performAttack(enemyArmy, civilizationArmy, false);
            }
            civilizationAttacks = !civilizationAttacks;
        }

        calculateResults();
    }

    private void performAttack(ArrayList<MilitaryUnit> attackerArmy, ArrayList<MilitaryUnit> defenderArmy, boolean civilizationIsAttacker) {
        if (attackerArmy.isEmpty() || defenderArmy.isEmpty()) return;

        int group = civilizationIsAttacker ? getCivilizationGroupAttacker() : getEnemyGroupAttacker();
        MilitaryUnit attacker = getRandomUnitOfGroup(attackerArmy, group, civilizationIsAttacker);
        if (attacker == null) return;

        int defenderGroup = getGroupDefender(defenderArmy, !civilizationIsAttacker);
        MilitaryUnit defender = getRandomUnitOfGroup(defenderArmy, defenderGroup, !civilizationIsAttacker);
        if (defender == null) return;

        int damage = attacker.attack();
        defender.takeDamage(damage);

        String attackerType = attacker.getClass().getSimpleName();
        String defenderType = defender.getClass().getSimpleName();
        String attackerSide = civilizationIsAttacker ? "Civilization" : "Enemy";
        String defenderSide = civilizationIsAttacker ? "Enemy" : "Civilization";

        battleDevelopment += "Attacks " + attackerSide + ": " + attackerType + " attacks " + defenderType + "\n";
        battleDevelopment += attackerType + " generates the damage = " + damage + "\n";
        battleDevelopment += defenderType + " stays with armor = " + defender.getActualArmor() + "\n";

        if (defender.getActualArmor() <= 0) {
            battleDevelopment += "we eliminate " + defenderType + "\n";
            // Generación de residuos
            if (rand.nextInt(100) < defender.getChanceGeneratorInWaste()) {
                int woodWaste = (defender.getWoodCost() * Variables.PERCENTAGE_WASTE) / 100;
                int ironWaste = (defender.getIronCost() * Variables.PERCENTAGE_WASTE) / 100;
                wasteWoodIron[0] += woodWaste;
                wasteWoodIron[1] += ironWaste;
                battleDevelopment += "Waste generated: Wood " + woodWaste + " Iron " + ironWaste + "\n";
            }
            defenderArmy.remove(defender);
            updateUnitCounts(defender, defenderSide);
        }

        if (rand.nextInt(100) < attacker.getChanceAttackAgain()) {
            battleDevelopment += attackerType + " attacks again!\n";
            performAttack(attackerArmy, defenderArmy, civilizationIsAttacker);
        } else {
            battleDevelopment += "*****************CHANGE ATTACKER*****************\n";
        }
    }

    private MilitaryUnit getRandomUnitOfGroup(ArrayList<MilitaryUnit> army, int group, boolean isCivilization) {
        ArrayList<MilitaryUnit> groupList = new ArrayList<MilitaryUnit>();
        for (MilitaryUnit u : army) {
            if (isCivilization && matchesCivilizationGroup(u, group)) groupList.add(u);
            else if (!isCivilization && matchesEnemyGroup(u, group)) groupList.add(u);
        }
        if (groupList.isEmpty()) return null;
        return groupList.get(rand.nextInt(groupList.size()));
    }

    private boolean matchesCivilizationGroup(MilitaryUnit u, int group) {
        switch (group) {
            case 0: return u instanceof Swordsman;
            case 1: return u instanceof Spearman;
            case 2: return u instanceof Crossbow;
            case 3: return u instanceof Cannon;
            case 4: return u instanceof ArrowTower;
            case 5: return u instanceof Catapult;
            case 6: return u instanceof RocketLauncherTower;
            case 7: return u instanceof Magician;
            case 8: return u instanceof Priest;
            default: return false;
        }
    }

    private boolean matchesEnemyGroup(MilitaryUnit u, int group) {
        switch (group) {
            case 0: return u instanceof Swordsman;
            case 1: return u instanceof Spearman;
            case 2: return u instanceof Crossbow;
            case 3: return u instanceof Cannon;
            default: return false;
        }
    }

    private int getCivilizationGroupAttacker() {
        return selectGroup(Variables.CHANCE_ATTACK_CIVILIZATION_UNITS);
    }

    private int getEnemyGroupAttacker() {
        return selectGroup(Variables.CHANCE_ATTACK_ENEMY_UNITS);
    }

    private int selectGroup(int[] chances) {
        int total = 0;
        for (int c : chances) total += c;
        int r = rand.nextInt(total);
        int cumulative = 0;
        for (int i = 0; i < chances.length; i++) {
            cumulative += chances[i];
            if (r < cumulative) return i;
        }
        return 0;
    }

    private int getGroupDefender(ArrayList<MilitaryUnit> army, boolean isEnemy) {
        int[] counts = new int[isEnemy ? 4 : 9];
        for (MilitaryUnit u : army) {
            if (isEnemy) {
                if (u instanceof Swordsman) counts[0]++;
                else if (u instanceof Spearman) counts[1]++;
                else if (u instanceof Crossbow) counts[2]++;
                else if (u instanceof Cannon) counts[3]++;
            } else {
                if (u instanceof Swordsman) counts[0]++;
                else if (u instanceof Spearman) counts[1]++;
                else if (u instanceof Crossbow) counts[2]++;
                else if (u instanceof Cannon) counts[3]++;
                else if (u instanceof ArrowTower) counts[4]++;
                else if (u instanceof Catapult) counts[5]++;
                else if (u instanceof RocketLauncherTower) counts[6]++;
                else if (u instanceof Magician) counts[7]++;
                else if (u instanceof Priest) counts[8]++;
            }
        }
        int total = 0;
        for (int c : counts) total += c;
        if (total == 0) return 0;
        int r = rand.nextInt(total);
        int cumulative = 0;
        for (int i = 0; i < counts.length; i++) {
            cumulative += counts[i];
            if (r < cumulative) return i;
        }
        return counts.length - 1;
    }

    private void updateUnitCounts(MilitaryUnit u, String side) {
        if (side.equals("Civilization")) {
            if (u instanceof Swordsman) actualNumberUnitsCivilization[0]--;
            else if (u instanceof Spearman) actualNumberUnitsCivilization[1]--;
            else if (u instanceof Crossbow) actualNumberUnitsCivilization[2]--;
            else if (u instanceof Cannon) actualNumberUnitsCivilization[3]--;
            else if (u instanceof ArrowTower) actualNumberUnitsCivilization[4]--;
            else if (u instanceof Catapult) actualNumberUnitsCivilization[5]--;
            else if (u instanceof RocketLauncherTower) actualNumberUnitsCivilization[6]--;
            else if (u instanceof Magician) actualNumberUnitsCivilization[7]--;
            else if (u instanceof Priest) actualNumberUnitsCivilization[8]--;
        } else { // Enemy
            if (u instanceof Swordsman) actualNumberUnitsEnemy[0]--;
            else if (u instanceof Spearman) actualNumberUnitsEnemy[1]--;
            else if (u instanceof Crossbow) actualNumberUnitsEnemy[2]--;
            else if (u instanceof Cannon) actualNumberUnitsEnemy[3]--;
        }
    }

    private void calculateResults() {
        // Calcular pérdidas para cada bando
        int[] civLosses = calculateLosses(initialArmies[0], actualNumberUnitsCivilization, true);
        int[] eneLosses = calculateLosses(initialArmies[1], actualNumberUnitsEnemy, false);

        resourcesLooses[0][0] = civLosses[0];
        resourcesLooses[0][1] = civLosses[1];
        resourcesLooses[0][2] = civLosses[2];
        int weightedCiv = civLosses[2] + civLosses[1]/5 + civLosses[0]/10;
        resourcesLooses[0][3] = weightedCiv;

        resourcesLooses[1][0] = eneLosses[0];
        resourcesLooses[1][1] = eneLosses[1];
        resourcesLooses[1][2] = eneLosses[2];
        int weightedEne = eneLosses[2] + eneLosses[1]/5 + eneLosses[0]/10;
        resourcesLooses[1][3] = weightedEne;

        if (weightedCiv <= weightedEne) {
            battleDevelopment += "Battle Winned by Civilization, We Collect Rubble\n";
        } else {
            battleDevelopment += "Battle Winned by Enemy\n";
        }
    }

    private int[] calculateLosses(int[] initialCounts, int[] finalCounts, boolean isCivilization) {
        int foodLoss = 0, woodLoss = 0, ironLoss = 0;
        int[] costFood = Variables.FOOD_COST_UNITS;
        int[] costWood = Variables.WOOD_COST_UNITS;
        int[] costIron = Variables.IRON_COST_UNITS;

        int length = isCivilization ? 9 : 4;
        for (int i = 0; i < length; i++) {
            int lost = initialCounts[i] - finalCounts[i];
            if (lost > 0) {
                foodLoss += lost * costFood[i];
                woodLoss += lost * costWood[i];
                ironLoss += lost * costIron[i];
            }
        }
        return new int[]{foodLoss, woodLoss, ironLoss};
    }

    // Reportes
    public String getBattleReport(int battleNumber) {
        StringBuilder sb = new StringBuilder();
        sb.append("BATTLE NUMBER: ").append(battleNumber).append("\n");
        sb.append("BATTLE STATISTICS\n");
        sb.append("Army planet Units Drops Initial Army Enemy Units Drops\n");
        String[] namesCiv = {"Swordsman","Spearman","Crossbow","Cannon","Arrow Tower","Catapult","Rocket Launcher","Magician","Priest"};
        String[] namesEne = {"Swordsman","Spearman","Crossbow","Cannon"};
        for (int i = 0; i < 9; i++) {
            int dropsCiv = initialArmies[0][i] - actualNumberUnitsCivilization[i];
            sb.append(namesCiv[i]).append(" ").append(actualNumberUnitsCivilization[i]).append(" ").append(dropsCiv).append(" ");
            if (i < 4) {
                int dropsEne = initialArmies[1][i] - actualNumberUnitsEnemy[i];
                sb.append(namesEne[i]).append(" ").append(actualNumberUnitsEnemy[i]).append(" ").append(dropsEne);
            }
            sb.append("\n");
        }
        sb.append("**************************************************************************************\n");
        // Costes iniciales
        sb.append("Cost Army Civilization: Food ").append(initialCostFleet[0][0]).append(" Wood ").append(initialCostFleet[0][1]).append(" Iron ").append(initialCostFleet[0][2]).append("\n");
        sb.append("Cost Army Enemy: Food ").append(initialCostFleet[1][0]).append(" Wood ").append(initialCostFleet[1][1]).append(" Iron ").append(initialCostFleet[1][2]).append("\n");
        sb.append("**************************************************************************************\n");
        // Pérdidas
        sb.append("Losses Army Civilization: Food ").append(resourcesLooses[0][0]).append(" Wood ").append(resourcesLooses[0][1]).append(" Iron ").append(resourcesLooses[0][2]).append("\n");
        sb.append("Losses Army Enemy: Food ").append(resourcesLooses[1][0]).append(" Wood ").append(resourcesLooses[1][1]).append(" Iron ").append(resourcesLooses[1][2]).append("\n");
        sb.append("**************************************************************************************\n");
        sb.append("Waste Generated: Wood ").append(wasteWoodIron[0]).append(" Iron ").append(wasteWoodIron[1]).append("\n");
        // Ganador real
        if (resourcesLooses[0][3] <= resourcesLooses[1][3]) {
            sb.append("Battle Winned by Civilization, We Collect Rubble\n");
        } else {
            sb.append("Battle Winned by Enemy\n");
        }
        return sb.toString();
    }

    public String getBattleDevelopment() {
        return battleDevelopment;
    }

    public int[] getWasteWoodIron() { return wasteWoodIron; }

    public boolean civilizationWon() {
        return resourcesLooses[0][3] <= resourcesLooses[1][3];
    }

    // Resetear armaduras tras batalla (útil)
    public void resetCivilizationArmor() {
        for (MilitaryUnit u : civilizationArmy) {
            u.resetArmor();
        }
    }
}