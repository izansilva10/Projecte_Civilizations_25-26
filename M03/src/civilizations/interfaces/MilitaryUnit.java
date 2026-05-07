package civilizations.interfaces;

// Interfície que implementaran totes les unitats militars //
public interface MilitaryUnit 
{
    // Retorna el poder d'atac actual //
    int attack();
    
    // Rep un dany, reduint l'armadura //
    void takeDamage(int receivedDamage);

    // Retorna l'armadura actual //
    int getActualArmor();

    // Restableix l'armadura al valor inicial //
    void resetArmor();

    // Estableix l'experiència de la unitat //
    void setExperience(int n);

    // Retorna l'experiència actual //
    int getExperience();

    // Retorna el cost en menjar per crear la unitat //
    int getFoodCost();

    // Retorna el cost en fusta per crear la unitat //
    int getWoodCost();

    // Retorna el cost en ferro per crear la unitat //
    int getIronCost();

    // Retorna el cost en manà per crear la unitat //
    int getManaCost();

    // Retorna la probabilitat de generar residus en ser eliminada //
    int getChanceGeneratinWaste();

    // Retorna la probabilitat de tornar a atacar //
    int getChanceAttackAgain();
}