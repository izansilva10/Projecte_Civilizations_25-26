package civilizations.game;
import java.util.ArrayList;
import civilizations.exceptions.*;
import civilizations.units.*;
import civilizations.interfaces.Variables;

public class Civilization implements Variables
{
    public int technologyDefense;
    public int technologyAttack;

    public int wood;
    public int iron;
    public int food;
    public int mana;

    public int magicTower;
    public int church;
    public int farm;
    public int smithy;
    public int carpentry;

    public int battles;
    private ArrayList<MilitaryUnit>[] army = new ArrayList[9];

    private int upgradeDefenseTechnologyIronCost;
    private int upgradeAttackTechnologyIronCost;
    private int upgradeDefenseTechnologyWoodCost;
    private int upgradeAttackTechnologyWoodCost;
    private int upgradeDefenseTechnologyFoodCost;
    private int upgradeAttackTechnologyFoodCost;

    public Civilization(int technologyDefense,int technologyAttack,int wood,int iron,int food,int mana,int magicTower,int church,int farm,int smithy,int carpentry,int battles)
    {
        this.technologyDefense = technologyDefense;
        this.technologyAttack = technologyAttack;
        
        // Inicialización de costes base
        this.upgradeDefenseTechnologyIronCost = UPGRADE_BASE_DEFENSE_TECHNOLOGY_IRON_COST;
        this.upgradeAttackTechnologyIronCost = UPGRADE_BASE_ATTACK_TECHNOLOGY_IRON_COST;
        this.upgradeDefenseTechnologyWoodCost = UPGRADE_BASE_DEFENSE_TECHNOLOGY_WOOD_COST;
        this.upgradeAttackTechnologyWoodCost = UPGRADE_BASE_ATTACK_TECHNOLOGY_WOOD_COST;
        this.upgradeDefenseTechnologyFoodCost = UPGRADE_BASE_DEFENSE_TECHNOLOGY_FOOD_COST;
        this.upgradeAttackTechnologyFoodCost = UPGRADE_BASE_ATTACK_TECHNOLOGY_FOOD_COST;

        this.wood = wood;
        this.iron = iron;
        this.food = food;
        this.mana = mana;

        this.magicTower = magicTower;
        this.church = church;
        this.farm = farm;
        this.smithy = smithy;
        this.carpentry = carpentry;

        this.battles = battles;

        for (int i = 0; i < 9; i = i + 1) 
        {
            army[i] = new ArrayList<MilitaryUnit>();
        }
    }

    public int getWood() { return wood; }
    public void setWood(int wood) { this.wood = wood; }

    public int getIron() { return iron; }
    public void setIron(int iron) { this.iron = iron; }

    public int getFood() { return food; }
    public void setFood(int food) { this.food = food; }

    public int getMana() { return mana; }
    public void setMana(int mana) { this.mana = mana; }

    public int getTechnologyDefense() { return technologyDefense; }
    public void setTechnologyDefense(int technologyDefense) { this.technologyDefense = technologyDefense; }

    public int getTechnologyAttack() { return technologyAttack; }
    public void setTechnologyAttack(int technologyAttack) { this.technologyAttack = technologyAttack; }

    public int getMagicTower() { return magicTower; }
    public void setMagicTower(int magicTower) { this.magicTower = magicTower; }

    public int getChurch() { return church; }
    public void setChurch(int church) { this.church = church; }

    public int getFarm() { return farm; }
    public void setFarm(int farm) { this.farm = farm; }

    public int getSmithy() { return smithy; }
    public void setSmithy(int smithy) { this.smithy = smithy; }

    public int getCarpentry() { return carpentry; }
    public void setCarpentry(int carpentry) { this.carpentry = carpentry; }

    public int getBattles() { return battles; }
    public void setBattles(int battles) { this.battles = battles; }

    public ArrayList<MilitaryUnit>[] getArmy() { return army; }
    public void setArmy(ArrayList<MilitaryUnit>[] army) { this.army = army; }

    public void upgradeDefenseTechnology() throws ResourceException 
    {
        if (this.iron >= upgradeDefenseTechnologyIronCost && this.wood >= upgradeDefenseTechnologyWoodCost && this.food >= upgradeDefenseTechnologyFoodCost) 
        {
            this.iron = this.iron - upgradeDefenseTechnologyIronCost;
            this.wood = this.wood - upgradeDefenseTechnologyWoodCost;
            this.food = this.food - upgradeDefenseTechnologyFoodCost;
            this.technologyDefense = this.technologyDefense + 1;
            this.upgradeDefenseTechnologyIronCost = this.upgradeDefenseTechnologyIronCost + UPGRADE_PLUS_DEFENSE_TECHNOLOGY_IRON_COST;
        } 
        else 
        {
            throw new ResourceException("No hay recursos suficientes.");
        }
    }

    public void upgradeAttackTechnology() throws ResourceException 
    {
        if (this.iron >= upgradeAttackTechnologyIronCost && this.wood >= upgradeAttackTechnologyWoodCost && this.food >= upgradeAttackTechnologyFoodCost) 
        {
            this.iron = this.iron - upgradeAttackTechnologyIronCost;
            this.wood = this.wood - upgradeAttackTechnologyWoodCost;
            this.food = this.food - upgradeAttackTechnologyFoodCost;
            this.technologyAttack = this.technologyAttack + 1;
            this.upgradeAttackTechnologyIronCost = this.upgradeAttackTechnologyIronCost + UPGRADE_PLUS_ATTACK_TECHNOLOGY_IRON_COST;
        } 
        else 
        {
            throw new ResourceException("No hay recursos suficientes.");
        }
    }

    public void newFarm() throws ResourceException 
    {
        if (this.food >= FOOD_COST_FARM && this.wood >= WOOD_COST_FARM && this.iron >= IRON_COST_FARM) 
        {
            this.food = this.food - FOOD_COST_FARM;
            this.wood = this.wood - WOOD_COST_FARM;
            this.iron = this.iron - IRON_COST_FARM;
            this.farm = this.farm + 1;
        } 
        else 
        {
            throw new ResourceException("No hay recursos suficientes.");
        }
    }

    public void newSmithy() throws ResourceException 
    {
        if (this.food >= FOOD_COST_SMITHY && this.wood >= WOOD_COST_SMITHY && this.iron >= IRON_COST_SMITHY) 
        {
            this.food = this.food - FOOD_COST_SMITHY;
            this.wood = this.wood - WOOD_COST_SMITHY;
            this.iron = this.iron - IRON_COST_SMITHY;
            this.smithy = this.smithy + 1;
        } 
        else 
        {
            throw new ResourceException("No hay recursos suficientes.");
        }
    }

    public void newCarpentry() throws ResourceException 
    {
        if (this.food >= FOOD_COST_CARPENTRY && this.wood >= WOOD_COST_CARPENTRY && this.iron >= IRON_COST_CARPENTRY) 
        {
            this.food = this.food - FOOD_COST_CARPENTRY;
            this.wood = this.wood - WOOD_COST_CARPENTRY;
            this.iron = this.iron - IRON_COST_CARPENTRY;
            this.carpentry = this.carpentry + 1;
        } 
        else 
        {
            throw new ResourceException("No hay recursos suficientes.");
        }
    }

    public void newChurch() throws ResourceException 
    {
        if (this.food >= FOOD_COST_CHURCH && this.wood >= WOOD_COST_CHURCH && this.iron >= IRON_COST_CHURCH && this.mana >= MANA_COST_CHURCH) 
        {
            this.food = this.food - FOOD_COST_CHURCH;
            this.wood = this.wood - WOOD_COST_CHURCH;
            this.iron = this.iron - IRON_COST_CHURCH;
            this.mana = this.mana - MANA_COST_CHURCH;
            this.church = this.church + 1;
        } 
        else 
        {
            throw new ResourceException("No hay recursos suficientes.");
        }
    }

    public void newMagicTower() throws ResourceException 
    {
        if (this.food >= FOOD_COST_MAGICTOWER && this.wood >= WOOD_COST_MAGICTOWER && this.iron >= IRON_COST_MAGICTOWER && this.mana >= MANA_COST_MAGICTOWER) 
        {
            this.food = this.food - FOOD_COST_MAGICTOWER;
            this.wood = this.wood - WOOD_COST_MAGICTOWER;
            this.iron = this.iron - IRON_COST_MAGICTOWER;
            this.mana = this.mana - MANA_COST_MAGICTOWER;
            this.magicTower = this.magicTower + 1;
        } 
        else 
        {
            throw new ResourceException("No hay recursos suficientes.");
        }
    }

    public void newSwordsman(int n) throws ResourceException 
    {
        int creados = 0;
        for (int i = 0; i < n; i = i + 1) 
        {
            if (this.food >= FOOD_COST_SWORDSMAN && this.wood >= WOOD_COST_SWORDSMAN && this.iron >= IRON_COST_SWORDSMAN) 
            {
                this.food = this.food - FOOD_COST_SWORDSMAN;
                this.wood = this.wood - WOOD_COST_SWORDSMAN;
                this.iron = this.iron - IRON_COST_SWORDSMAN;
                army[0].add(new Swordsman(this.technologyDefense, this.technologyAttack));
                creados = creados + 1;
            } 
            else 
            {
                throw new ResourceException("Recursos insuficientes. Se han añadido " + creados + " Swordsman.");
            }
        }
        System.out.println("Se han añadido " + n + " Swordsman.");
    }

    public void newSpearman(int n) throws ResourceException 
    {
        int creados = 0;
        for (int i = 0; i < n; i = i + 1) 
        {
            if (this.food >= FOOD_COST_SPEARMAN && this.wood >= WOOD_COST_SPEARMAN && this.iron >= IRON_COST_SPEARMAN) 
            {
                this.food = this.food - FOOD_COST_SPEARMAN;
                this.wood = this.wood - WOOD_COST_SPEARMAN;
                this.iron = this.iron - IRON_COST_SPEARMAN;
                army[1].add(new Spearman(this.technologyDefense, this.technologyAttack));
                creados = creados + 1;
            } 
            else 
            {
                throw new ResourceException("Recursos insuficientes. Se han añadido " + creados + " Spearman.");
            }
        }
        System.out.println("Se han añadido " + n + " Spearman.");
    }

    public void newCrossbow(int n) throws ResourceException 
    {
        int creados = 0;
        for (int i = 0; i < n; i = i + 1) 
        {
            if (this.wood >= WOOD_COST_CROSSBOW && this.iron >= IRON_COST_CROSSBOW) 
            {
                this.wood = this.wood - WOOD_COST_CROSSBOW;
                this.iron = this.iron - IRON_COST_CROSSBOW;
                army[2].add(new Crossbow(this.technologyDefense, this.technologyAttack));
                creados = creados + 1;
            } 
            else 
            {
                throw new ResourceException("Recursos insuficientes. Se han añadido " + creados + " Crossbow.");
            }
        }
        System.out.println("Se han añadido " + n + " Crossbow.");
    }

    public void newCannon(int n) throws ResourceException 
    {
        int creados = 0;
        for (int i = 0; i < n; i = i + 1) 
        {
            if (this.wood >= WOOD_COST_CANNON && this.iron >= IRON_COST_CANNON) 
            {
                this.wood = this.wood - WOOD_COST_CANNON;
                this.iron = this.iron - IRON_COST_CANNON;
                army[3].add(new Cannon(this.technologyDefense, this.technologyAttack));
                creados = creados + 1;
            } 
            else 
            {
                throw new ResourceException("Recursos insuficientes. Se han añadido " + creados + " Cannon.");
            }
        }
        System.out.println("Se han añadido " + n + " Cannon.");
    }

    public void newMagician(int n) throws ResourceException, BuildingException 
    {
        if (this.magicTower < 1) 
        {
            throw new BuildingException("Necesitas una Torre Mágica.");
        }
        int creados = 0;
        for (int i = 0; i < n; i = i + 1) 
        {
            if (this.mana >= MANA_COST_MAGICIAN && this.food >= FOOD_COST_MAGICIAN && this.wood >= WOOD_COST_MAGICIAN && this.iron >= IRON_COST_MAGICIAN) 
            {
                this.mana = this.mana - MANA_COST_MAGICIAN;
                this.food = this.food - FOOD_COST_MAGICIAN;
                this.wood = this.wood - WOOD_COST_MAGICIAN;
                this.iron = this.iron - IRON_COST_MAGICIAN;
                army[7].add(new Magician(this.technologyDefense, this.technologyAttack));
                creados = creados + 1;
            } 
            else 
            {
                throw new ResourceException("Recursos insuficientes. Se han añadido " + creados + " Magician.");
            }
        }
        System.out.println("Se han añadido " + n + " Magician.");
    }

    public void newPriest(int n) throws ResourceException, BuildingException 
    {
        if (this.church < 1) 
        {
            throw new BuildingException("Necesitas una Iglesia.");
        }
        int creados = 0;
        for (int i = 0; i < n; i = i + 1) 
        {
            if (this.mana >= MANA_COST_PRIEST && this.food >= FOOD_COST_PRIEST) 
            {
                this.mana = this.mana - MANA_COST_PRIEST;
                this.food = this.food - FOOD_COST_PRIEST;
                army[8].add(new Priest(this.technologyDefense, this.technologyAttack));
                creados = creados + 1;
            } 
            else 
            {
                throw new ResourceException("Recursos insuficientes. Se han añadido " + creados + " Priest.");
            }
        }
        System.out.println("Se han añadido " + creados + " Priest.");
    }

    public void printStats() 
    {
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("                                                                CIVILIZATION STATS");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("RESOURCES\t\t\t\t\t\tBUILDINGS\t\t\t\t\t\tTECHNOLOGY");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Iron: " + this.iron + "\t\t\t\t\t\tFarms: " + this.farm + "\t\t\t\t\t\tAttack Technology: " + this.technologyAttack);
        System.out.println("Wood: " + this.wood + "\t\t\t\t\t\tSmithy: " + this.smithy + "\t\t\t\t\t\tDefense Technology: " + this.technologyDefense);
        System.out.println("Food: " + this.food + "\t\t\t\t\t\tCarpentry: " + this.carpentry);
        System.out.println("Mana: " + this.mana + "\t\t\t\t\t\tChurch: " + this.church);
        System.out.println("\t\t\t\t\t\t\tMagic Tower: " + this.magicTower);
        
        System.out.println("\n--------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("                                                                    ARMY STATS");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Swordsman: " + this.army[0].size() + "\t\tSpearman: " + this.army[1].size() + "\t\tCrossbow: " + this.army[2].size() + "\t\tCannon: " + this.army[3].size());
        System.out.println("Arrow Tower: " + this.army[4].size() + "\t\tCatapult: " + this.army[5].size() + "\t\tRocket Tower: " + this.army[6].size() + "\t\tMagician: " + this.army[7].size());
        System.out.println("Priest: " + this.army[8].size());
        
        System.out.println("\nBattles: " + this.battles);
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }
}