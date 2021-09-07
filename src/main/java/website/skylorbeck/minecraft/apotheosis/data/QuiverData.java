package website.skylorbeck.minecraft.apotheosis.data;

import java.util.List;

public class QuiverData {
    boolean doDamage = false;
    List<String> potions;

    public QuiverData(boolean doDamage,  List<String> potions) {
        this.doDamage = doDamage;
        this.potions = potions;
    }

    public boolean isDoDamage() {
        return doDamage;
    }

    public void setDoDamage(boolean doDamage) {
        this.doDamage = doDamage;
    }

    public  List<String> getPotions() {
        return potions;
    }

    public void setPotions( List<String> potions) {
        this.potions = potions;
    }
}