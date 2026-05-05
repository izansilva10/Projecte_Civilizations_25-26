package civilizations.game;
import java.util.ArrayList;
import civilizations.exceptions.*;
import civilizations.units.*;

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

    private int upgradeDefenseTechnologyIronCost = 2000;
    private int upgradeAttackTechnologyIronCost = 2000;

    private int upgradeDefenseTechnologyWoodCost = 200;
    private int upgradeAttackTechnologyWoodCost = 200;

    private int upgradeDefenseTechnologyFoodCost = 100;
    private int upgradeAttackTechnologyFoodCost = 100;

    public Civilization(int technologyDefense,int technologyAttack,int wood,int iron,int food,int mana,int magicTower,int church,int farm,int smithy,int carpentry,int battles)
    {
        this.technologyDefense = technologyDefense;
        this.technologyAttack = technologyAttack;
        this.upgradeDefenseTechnologyIronCost = UPGRADE_BASE_DEFENSE_TECHNOLOGY_IRON_COST;
        this.upgradeAttackTechnologyIronCost = UPGRADE_BASE_ATTACK_TECHNOLOGY_IRON_COST;

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

        for (int i = 0; i < 9; i = i +1)
        {
            army[i] = new ArrayList<MilitaryUnit>();
        }
    }

        // GETTER //
        public int getWood(){return wood;}
        public int getIron(){return iron;}
        public int getFood(){return food;}
        public int getMana(){return mana;}
        public int getTechnologyDefense(){return technologyDefense;}
        public int getTechnologyAttack(){return technologyAttack;}
        public int MagicTower(){return magicTower;}
        public int getChurch(){return church;}
        public int getFarm (){return farm;}
        public int getSmithy(){return smithy;}
        public int getCarpentry(){return carpentry;}
        public int getBattles(){return battles;}
        public ArrayList<MilitaryUnit>[] getArmy(){return army;}

        // SETTERS //
        public void setWood(int wood){this.wood = wood;}
        public void setIron(int iron){this.iron = iron;}
        public void setFood(int food){this.food = food;}
        public void setMana(int mana){this.mana = mana;}
        public void setTechnologyDefense(int technologyDefense){this.technologyDefense = technologyDefense;}
        public void setTechnologyAttack(int technologyAttack){this.technologyAttack = technologyAttack;}
        public void setMagicTower(int magicTower){this.magicTower = magicTower;}
        public void setChurch(int church){this.church = church;}
        public void setFarm(int farm){this.farm = farm;}
        public void setSmithy(int smithy){this.smithy = smithy;}
        public void setCarpentry(int carpentry){this.carpentry = carpentry;}
        public void setBattles(int battles){this.battles = battles;}
        public void setArmy(ArrayList<MilitaryUnit>[] army) {this.army = army;}

        public void newChurch() throws ResourceException 
        {
            if (this.mana >= MANA_COST_CHURCH && this.food >= FOOD_COST_CHURCH)
            {
                this.mana = this.mana - MANA_COST_CHURCH;
                this.food = this.food - FOOD_COST_CHURCH;

                this.church = this.church + 1;
                System.out.println("Iglesia construida. Total: " + this.church);
            }
            else
            {
                throw new ResourceException("No hay recursos suficientes para construir una Iglesia.");
            }
        }

        public void newMagicTower() throws ResourceException
        {
            if (this.mana >= MANA_COST_MAGICTOWER && this.wood >= WOOD_COST_MAGICTOWER)
            {
                this.mana = this.mana - MANA_COST_MAGICTOWER;
                this.wood = this.wood - WOOD_COST_MAGICTOWER;

                this.magicTower = this.magicTower + 1;
                 System.out.println("Torre Mágica construida. Total: " + this.magicTower);
            }
            else
            {
                throw new ResourceException("No hay recursos suficientes para construir una Torre Mágica.");
            }
        }

        public void newFarm() throws ResourceException
        {
            if (this.food >= FOOD_COST_FARM && this.wood >= WOOD_COST_FARM)
            {
                this.food = this.food - FOOD_COST_FARM;
                this.wood = this.wood - WOOD_COST_FARM;

                this.farm = this.farm + 1;
                System.out.println("Granja construida. Total: " + this.farm);
            }
            else
            {
                throw new ResourceException("No hay recursos suficientes para construir una Granja.");
            }
        }

        public void newCarpentry() throws ResourceException
        {
            if (this.iron >= IRON_COST_CARPENTRY && this.wood >= WOOD_COST_CARPENTRY)
            {
                this.iron = this.iron - IRON_COST_CARPENTRY;
                this.wood = this.wood - WOOD_COST_CARPENTRY;

                this.carpentry = this.carpentry + 1;
                System.out.println("Carpintería construida. Total: " + this.carpentry);
            }
            else
            {
                throw new ResourceException("No hay recursos suficientes para construir una Carpintería");
            }
        }

        public void newSmithy() throws ResourceException
        {
            if (this.iron >= IRON_COST_SMITHY && this.wood >= WOOD_COST_SMITHY)
            {
                this.iron = this.iron - IRON_COST_SMITHY;
                this.wood = this.wood - WOOD_COST_SMITHY;

                this.smithy = this.smithy + 1;
                System.out.println("Herrería construida. Total: " + this.smithy);
            }
            else
            {
                throw new ResourceException("No hay recursos suficientes para construir una Herrería");
            }
        }

        public void upgradeDefenseTechnology() throws ResourceException
        {
            if (this.iron >= upgradeDefenseTechnologyIronCost && this.wood >= upgradeDefenseTechnologyWoodCost && this.food >= upgradeDefenseTechnologyFoodCost)
            {
                this.iron = this.iron - upgradeDefenseTechnologyIronCost;
                this.wood = this.wood - upgradeDefenseTechnologyWoodCost;
                this.food = this.food - upgradeDefenseTechnologyFoodCost;

                this.technologyDefense = this.technologyDefense + 1;

                upgradeDefenseTechnologyIronCost = upgradeDefenseTechnologyIronCost + (upgradeDefenseTechnologyIronCost * 20/100);
                upgradeDefenseTechnologyWoodCost = upgradeDefenseTechnologyWoodCost + (upgradeDefenseTechnologyWoodCost * 15/100);
                upgradeDefenseTechnologyFoodCost = upgradeDefenseTechnologyFoodCost + (upgradeDefenseTechnologyFoodCost * 10/100);

                System.out.println("Tecnología de Defensa mejorada al nivel: " + this.technologyDefense);
            }
            else
            {
                throw new ResourceException("No hay recursos suficientes para mejorar la Tecnología de Defensa");
            }
        }

        public void upgradeAttackTechnology() throws ResourceException
        {
            if (this.iron >= upgradeAttackTechnologyIronCost && this.wood >= upgradeAttackTechnologyWoodCost && this.food >= upgradeAttackTechnologyFoodCost)
            {
                this.iron = this.iron - upgradeAttackTechnologyIronCost;
                this.wood = this.wood - upgradeAttackTechnologyWoodCost;
                this.food = this.food - upgradeAttackTechnologyFoodCost;

                this.technologyAttack = technologyAttack + 1;

                upgradeAttackTechnologyIronCost = upgradeAttackTechnologyIronCost + (upgradeAttackTechnologyIronCost * 20/100);
                upgradeAttackTechnologyWoodCost = upgradeAttackTechnologyWoodCost + (upgradeAttackTechnologyWoodCost * 15/100);
                upgradeAttackTechnologyFoodCost = upgradeAttackTechnologyFoodCost + (upgradeAttackTechnologyFoodCost * 10/100);

                System.out.println("Tecnología de Ataque mejorada al nivel: " + this.technologyAttack);
            }
            else
            {
                throw new ResourceException("No hay recursos suficientes para mejorar la Tecnología de Ataque");
            }
        }

        public void newSwordsman(int n) throws ResourceException
        {
            int creados = 0;
            for (int i = 0; i < n; i = i + 1)
            {
                if (this.food >= FOOD_COST_SWORDSMAN && this.iron >= IRON_COST_SWORDSMAN && this.wood >= WOOD_COST_SWORDSMAN)
                {
                    this.food = this.food - FOOD_COST_SWORDSMAN;
                    this.iron = this.iron - IRON_COST_SWORDSMAN;
                    this.wood = this.wood - WOOD_COST_SWORDSMAN;
                    
                    army[0].add(new Swordsman(this.technologyDefense,this.technologyAttack));

                    creados = creados + 1;
                }
                else
                {
                    throw new ResourceException("Recursos insuficientes. Se han añadido " + creados + "Swordsman.");
                }
            }
            System.out.println("Se han añadido " + n + "Swordsman");
        }

        public void newSpearman(int n) throws ResourceException 
        {
            int creados = 0;
            for (int i = 0; i < n; i = i + 1) 
            {
                if (this.food >= FOOD_COST_SPEARMAN && this.iron >= IRON_COST_SPEARMAN && this.wood >= WOOD_COST_SPEARMAN) 
                {
                    this.food = this.food - FOOD_COST_SPEARMAN;
                    this.iron = this.iron - IRON_COST_SPEARMAN;
                    this.wood = this.wood - WOOD_COST_SPEARMAN;
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
                if (this.iron >= IRON_COST_CROSSBOW && this.wood >= WOOD_COST_CROSSBOW) 
                {
                    this.iron = this.iron - IRON_COST_CROSSBOW;
                    this.wood = this.wood - WOOD_COST_CROSSBOW;
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
                if (this.iron >= IRON_COST_CANNON && this.wood >= WOOD_COST_CANNON) 
                {
                    this.iron = this.iron - IRON_COST_CANNON;
                    this.wood = this.wood - WOOD_COST_CANNON;
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

        public void newArrowTower(int n) throws ResourceException 
        {
            int creados = 0;
            for (int i = 0; i < n; i = i + 1) 
            {
                if (this.wood >= WOOD_COST_ARROWTOWER) 
                {
                    this.wood = this.wood -  WOOD_COST_ARROWTOWER;
                    army[4].add(new ArrowTower(this.technologyDefense, this.technologyAttack));
                    creados = creados + 1;
                } 
                else 
                {
                    throw new ResourceException("Recursos insuficientes. Se han añadido " + creados + " Arrow Tower.");
                }
            }
            System.out.println("Se han añadido " + n + " Arrow Tower.");
        }

        public void newCatapult(int n) throws ResourceException 
        {
            int creados = 0;
            for (int i = 0; i < n; i = i + 1) 
            {
                if (this.iron >= IRON_COST_CATAPULT && this.wood >= WOOD_COST_CATAPULT) 
                {
                    this.iron = this.iron - IRON_COST_CATAPULT;
                    this.wood = this.wood - WOOD_COST_CATAPULT;
                    army[5].add(new Catapult(this.technologyDefense, this.technologyAttack));
                    creados = creados + 1;
                } 
                else 
                {
                    throw new ResourceException("Recursos insuficientes. Se han añadido " + creados + " Catapult.");
                }
            }
            System.out.println("Se han añadido " + n + " Catapult.");
        }

        public void newRocketLauncher(int n) throws ResourceException 
        {
            int creados = 0;
            for (int i = 0; i < n; i = i + 1) 
            {
                if (this.iron >= IRON_COST_ROCKETLAUNCHERTOWER && this.wood >= WOOD_COST_ROCKETLAUNCHERTOWER) 
                {
                    this.iron = this.iron - IRON_COST_ROCKETLAUNCHERTOWER;
                    this.wood = this.wood - WOOD_COST_ROCKETLAUNCHERTOWER;
                    army[6].add(new RocketLauncher(this.technologyDefense, this.technologyAttack));
                    creados = creados + 1;
                } 
                else 
                {
                    throw new ResourceException("Recursos insuficientes. Se han añadido " + creados + " Rocket Launcher.");
                }
            }
            System.out.println("Se han añadido " + n + " Rocket Launcher.");
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
            System.out.println("Se han añadido " + n + " Priest.");
        }
}

