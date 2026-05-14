package civilizations;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;

public class Main 
{
    static Civilization civilization;
    static Scanner scanner;
    static ArrayList<Battle> battleHistory = new ArrayList<>();
    static ArrayList<MilitaryUnit> currentEnemyArmy = new ArrayList<>();
    static Random rand = new Random();
    static Timer resourceTimer;
    static Timer enemyTimer;

    public static void main(String[] args) 
    {
        civilization = new Civilization();
        scanner = new Scanner(System.in);

        // Tareas programadas
        resourceTimer = new Timer();
        resourceTimer.scheduleAtFixedRate(new ResourceGenerator(civilization), 0, 60000);

        enemyTimer = new Timer();
        enemyTimer.schedule(new EnemyArmyCreator(civilization, battleHistory, currentEnemyArmy), 180000, 180000);

        boolean exit = false;
        while (!exit) 
        {
            System.out.println("\n=== MENÚ ===");
            System.out.println("1. Ver stats");
            System.out.println("2. Construir edificios");
            System.out.println("3. Mejorar tecnologías");
            System.out.println("4. Crear unidades");
            System.out.println("5. Ver amenaza enemiga");
            System.out.println("6. Ver historial de batallas");
            System.out.println("7. Salir");
            System.out.print("Opción: ");
            int op = scanner.nextInt();
            scanner.nextLine();

            switch (op) 
            {
                case 1: civilization.printStats(); break;
                case 2: buildMenu(); break;
                case 3: upgradeMenu(); break;
                case 4: createUnitsMenu(); break;
                case 5: viewThreat(); break;
                case 6: viewBattleHistory(); break;
                case 7: exit = true; break;
                default: System.out.println("Opción no válida");
            }
        }

        resourceTimer.cancel();
        enemyTimer.cancel();
        scanner.close();
    }

    static void buildMenu() 
    {
        System.out.println("Construir: 1.Granja 2.Carpintería 3.Herrería 4.Torre Mágica 5.Iglesia");
        int op = scanner.nextInt();
        try 
        {
            switch (op) 
            {
                case 1: civilization.newFarm(); break;
                case 2: civilization.newCarpentry(); break;
                case 3: civilization.newSmithy(); break;
                case 4: civilization.newMagicTower(); break;
                case 5: civilization.newChurch(); break;
                default: System.out.println("Opción no válida"); return;
            }
            System.out.println("Edificio construido.");
        } 
        catch (ResourceException e) 
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void upgradeMenu() 
    {
        System.out.println("Mejorar: 1.Defensa 2.Ataque");
        int op = scanner.nextInt();
        try 
        {
            if (op == 1) civilization.upgradeTechnologyDefense();
            else if (op == 2) civilization.upgradeTechnologyAttack();
            else 
                { 
                    System.out.println("Opción no válida"); return; 
                }
            System.out.println("Tecnología mejorada.");
        } 
        catch (ResourceException e) 
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void createUnitsMenu() 
    {
        System.out.println("Crear: 1.Swordsman 2.Spearman 3.Crossbow 4.Cannon 5.ArrowTower 6.Catapult 7.RocketLauncher 8.Magician 9.Priest");
        int type = scanner.nextInt();
        System.out.print("Cantidad: ");
        int n = scanner.nextInt();
        try 
        {
            switch (type) 
            {
                case 1: civilization.newSwordsman(n); 
                break;
                case 2: civilization.newSpearman(n); 
                break;
                case 3: civilization.newCrossbow(n); 
                break;
                case 4: civilization.newCannon(n); 
                break;
                case 5: civilization.newArrowTower(n); 
                break;
                case 6: civilization.newCatapult(n); 
                break;
                case 7: civilization.newRocketLauncher(n); 
                break;
                case 8: civilization.newMagician(n); 
                break;
                case 9: civilization.newPriest(n); 
                break;
                default: System.out.println("Tipo no válido");
                return;
            }
            System.out.println("Unidades añadidas.");
        } 
        catch (ResourceException | BuildingException e) 
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void viewThreat() 
    {
        if (currentEnemyArmy.isEmpty()) 
        {
            System.out.println("No hay amenaza enemiga.");
            return;
        }
        System.out.println("NEW threat COMMING");
        int[] counts = new int[4];
        for (MilitaryUnit u : currentEnemyArmy) 
        {
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

    static void viewBattleHistory() 
    {
        if (battleHistory.isEmpty()) 
        {
            System.out.println("No hay batallas registradas.");
            return;
        }
        for (int i = 0; i < battleHistory.size(); i++) 
        {
            System.out.println(battleHistory.get(i).getBattleReport(i + 1));
            System.out.print("¿Ver desarrollo? (s/n): ");
            String resp = scanner.nextLine();
            if (resp.equalsIgnoreCase("s")) 
            {
                System.out.println(battleHistory.get(i).getBattleDevelopment());
            }
        }
    }
}