package civilizations;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;

public class Main {
    static Civilization civilization;
    static Scanner scanner;
    static ArrayList<Battle> battleHistory = new ArrayList<>();
    static ArrayList<MilitaryUnit> currentEnemyArmy = new ArrayList<>();
    static Timer resourceTimer;
    static Timer enemyTimer;

    public static void main(String[] args) {
        civilization = new Civilization();
        scanner = new Scanner(System.in);

        // Tareas programadas
        resourceTimer = new Timer();
        resourceTimer.scheduleAtFixedRate(new ResourceGenerator(civilization), 0, 60000);

        enemyTimer = new Timer();
        enemyTimer.schedule(new EnemyArmyCreator(civilization, battleHistory, currentEnemyArmy), 180000, 180000);

        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== MENÚ ===");
            System.out.println("1. Ver estado de la civilización");
            System.out.println("2. Construir edificios");
            System.out.println("3. Mejorar tecnologías");
            System.out.println("4. Crear unidades");
            System.out.println("5. Ver amenaza enemiga");
            System.out.println("6. Ver historial de batallas");
            System.out.println("7. Ayuda");
            System.out.println("8. Salir");
            System.out.print("Opción: ");
            int op = scanner.nextInt();
            scanner.nextLine();

            switch (op) {
                case 1: civilization.printStats(); break;
                case 2: buildMenu(); break;
                case 3: upgradeMenu(); break;
                case 4: createUnitsMenu(); break;
                case 5: viewThreat(); break;
                case 6: viewBattleHistory(); break;
                case 7: mostrarAyuda(); break;
                case 8: exit = true; break;
                default: System.out.println("Opción no válida");
            }
        }

        resourceTimer.cancel();
        enemyTimer.cancel();
        scanner.close();
    }

    // Menú de construcción con costes y beneficios
    static void buildMenu() {
        System.out.println("\n=== CONSTRUIR EDIFICIOS ===");
        System.out.println("1. Granja         - Cuesta 5.000 comida, 10.000 madera, 12.000 hierro. +4.000 comida/min");
        System.out.println("2. Carpintería    - Cuesta 10.000 comida, 10.000 madera, 12.000 hierro. +2.500 madera/min");
        System.out.println("3. Herrería       - Cuesta 5.000 comida, 10.000 madera, 12.000 hierro. +750 hierro/min");
        System.out.println("4. Torre Mágica   - Cuesta 5.000 comida, 10.000 madera, 12.000 hierro. +10 maná/min (permite Magos)");
        System.out.println("5. Iglesia        - Cuesta 5.000 comida, 10.000 madera, 12.000 hierro. Permite Sacerdotes");
        System.out.println("0. Volver");
        System.out.print("Opción: ");
        int op = scanner.nextInt();
        if (op == 0) return;
        try {
            switch (op) {
                case 1: civilization.newFarm(); break;
                case 2: civilization.newCarpentry(); break;
                case 3: civilization.newSmithy(); break;
                case 4: civilization.newMagicTower(); break;
                case 5: civilization.newChurch(); break;
                default: System.out.println("Opción no válida"); return;
            }
            System.out.println("Edificio construido correctamente.");
        } catch (ResourceException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Menú de tecnologías con coste actual
    static void upgradeMenu() {
        int defCost = Variables.UPGRADE_BASE_DEFENSE_TECHNOLOGY_IRON_COST + civilization.getTechnologyDefense() * Variables.UPGRADEPLUS_DEFENSE_TECHNOLOGY_IRON_COST;
        int atkCost = Variables.UPGRADE_BASE_ATTACK_TECHNOLOGY_IRON_COST + civilization.getTechnologyAttack() * Variables.UPGRADE_PLUS_ATTACK_TECHNOLOGY_IRON_COST;
        System.out.println("\n=== MEJORAR TECNOLOGÍAS ===");
        System.out.println("1. Ataque  (nivel " + civilization.getTechnologyAttack() + " → " + (civilization.getTechnologyAttack() + 1) + ") – Cuesta " + atkCost + " hierro");
        System.out.println("2. Defensa (nivel " + civilization.getTechnologyDefense() + " → " + (civilization.getTechnologyDefense() + 1) + ") – Cuesta " + defCost + " hierro");
        System.out.println("0. Volver");
        System.out.print("Opción: ");
        int op = scanner.nextInt();
        if (op == 0) return;
        try {
            if (op == 1) civilization.upgradeTechnologyAttack();
            else if (op == 2) civilization.upgradeTechnologyDefense();
            else { System.out.println("Opción no válida"); return; }
            System.out.println("Tecnología mejorada.");
        } catch (ResourceException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Menú de unidades con costes y estadísticas
    static void createUnitsMenu() {
        System.out.println("\n=== CREAR UNIDADES ===");
        System.out.println("--- ATAQUE ---");
        System.out.println("1. Swordsman       Cuesta 8.000 comida, 3.000 madera, 50 hierro | Daño:80  Armadura:400");
        System.out.println("2. Spearman        Cuesta 5.000 comida, 6.500 madera, 50 hierro | Daño:150 Armadura:1.000");
        System.out.println("3. Crossbow        Cuesta 0 comida, 45.000 madera, 7.000 hierro | Daño:1.000 Armadura:6.000");
        System.out.println("4. Cannon          Cuesta 0 comida, 30.000 madera, 15.000 hierro | Daño:700 Armadura:8.000");
        System.out.println("--- DEFENSA ---");
        System.out.println("5. ArrowTower      Cuesta 0 comida, 2.000 madera, 0 hierro | Daño:80 Armadura:200");
        System.out.println("6. Catapult        Cuesta 0 comida, 4.000 madera, 500 hierro | Daño:250 Armadura:1.200");
        System.out.println("7. RocketLauncher  Cuesta 0 comida, 50.000 madera, 5.000 hierro | Daño:2.000 Armadura:7.000");
        System.out.println("--- ESPECIALES ---");
        System.out.println("8. Magician        Cuesta 12.000 comida, 2.000 madera, 5.000 maná | Daño:3.000 Armadura:0 (necesita Torre Mágica)");
        System.out.println("9. Priest          Cuesta 15.000 comida, 0 madera, 15.000 maná | Daño:0 Armadura:0 (necesita Iglesia)");
        System.out.println("0. Volver");
        System.out.print("Opción: ");
        int type = scanner.nextInt();
        if (type == 0) return;
        if (type < 1 || type > 9) { System.out.println("Tipo no válido"); return; }
        System.out.print("Cantidad: ");
        int n = scanner.nextInt();
        try {
            switch (type) {
                case 1: civilization.newSwordsman(n); break;
                case 2: civilization.newSpearman(n); break;
                case 3: civilization.newCrossbow(n); break;
                case 4: civilization.newCannon(n); break;
                case 5: civilization.newArrowTower(n); break;
                case 6: civilization.newCatapult(n); break;
                case 7: civilization.newRocketLauncher(n); break;
                case 8: civilization.newMagician(n); break;
                case 9: civilization.newPriest(n); break;
            }
            System.out.println("Unidades añadidas correctamente.");
        } catch (ResourceException | BuildingException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Amenaza enemiga
    static void viewThreat() {
        if (currentEnemyArmy.isEmpty()) {
            System.out.println("No hay amenaza enemiga en este momento.");
            return;
        }
        System.out.println("NEW threat COMMING");
        int[] counts = new int[4];
        for (MilitaryUnit u : currentEnemyArmy) {
            if (u instanceof Swordsman) counts[0]++;
            else if (u instanceof Spearman) counts[1]++;
            else if (u instanceof Crossbow) counts[2]++;
            else if (u instanceof Cannon) counts[3]++;
        }
        System.out.println("Swordsman " + counts[0]);
        System.out.println("Spearman " + counts[1]);
        System.out.println("Crossbow " + counts[2]);
        System.out.println("Cannon " + counts[3]);
    }

    // Historial de batallas
    static void viewBattleHistory() {
        if (battleHistory.isEmpty()) {
            System.out.println("No hay batallas registradas.");
            return;
        }
        for (int i = 0; i < battleHistory.size(); i++) {
            System.out.println(battleHistory.get(i).getBattleReport(i + 1));
            System.out.print("¿Ver desarrollo? (s/n): ");
            String resp = scanner.nextLine();
            if (resp.equalsIgnoreCase("s")) {
                System.out.println(battleHistory.get(i).getBattleDevelopment());
            }
        }
    }

    // Pantalla de ayuda
    static void mostrarAyuda() {
        System.out.println("\n=== AYUDA DE CIVILIZATIONS ===");
        System.out.println();
        System.out.println("Civilizations es un juego de estrategia por consola.");
        System.out.println("Tu objetivo es sobrevivir el mayor número de batallas contra ejércitos enemigos que atacan cada 3 minutos.");
        System.out.println();
        System.out.println("CONCEPTOS BÁSICOS:");
        System.out.println("- Recursos: Comida, Madera, Hierro y Maná se generan automáticamente cada minuto.");
        System.out.println("- Edificios: Aumentan la producción de un recurso específico. Tienen un coste fijo en recursos.");
        System.out.println("- Tecnologías: Mejoran el ataque o la defensa de las NUEVAS unidades que crees.");
        System.out.println("- Unidades: Se dividen en ataque (Swordsman, Spearman, Crossbow, Cannon),");
        System.out.println("  defensa (ArrowTower, Catapult, RocketLauncher) y especiales (Magician, Priest).");
        System.out.println("  Los Magos necesitan al menos 1 Torre Mágica. Los Sacerdotes necesitan al menos 1 Iglesia.");
        System.out.println();
        System.out.println("CONSEJOS:");
        System.out.println("1. Al inicio, espera unos minutos para acumular recursos.");
        System.out.println("2. Construye primero Granjas y Carpinterías para acelerar la producción.");
        System.out.println("3. Mejora el ataque antes de reclutar muchas tropas.");
        System.out.println("4. Equilibra tu ejército: infantería barata + unidades de alto daño.");
        System.out.println("5. Consulta el historial de batallas para aprender qué unidades son más efectivas.");
        System.out.println();
        System.out.print("Pulsa Enter para volver al menú...");
        scanner.nextLine();
    }
}