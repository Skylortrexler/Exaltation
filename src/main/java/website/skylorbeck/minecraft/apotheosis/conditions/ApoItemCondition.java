package website.skylorbeck.minecraft.apotheosis.conditions;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.item.*;
import net.minecraft.util.registry.Registry;
import website.skylorbeck.minecraft.apotheosis.Declarar;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ApoItemCondition {
    public static void register() {
        register(new ConditionFactory<>(Declarar.getIdentifier("durability"), new SerializableData()
                .add("percent", SerializableDataTypes.FLOAT),
                (data, stack) -> {
                    int durability = 0;
                    int maxDurability = 0;
                    if (stack.getItem() instanceof ToolItem) {
                        durability = stack.getMaxDamage() - stack.getDamage();
                        maxDurability = stack.getMaxDamage();
                    }
                    return Comparison.GREATER_THAN_OR_EQUAL.compare(durability, maxDurability * (data.getFloat("percent")));
                }));
        register(new ConditionFactory<>(Declarar.getIdentifier("isshield"), new SerializableData(),
                (data, stack) -> {
                    boolean shield = false;
                    shield = stack.isOf(Items.SHIELD);
                    return shield;
                }));
    }

    private static void register(ConditionFactory<ItemStack> conditionFactory) {
        Registry.register(ApoliRegistries.ITEM_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
