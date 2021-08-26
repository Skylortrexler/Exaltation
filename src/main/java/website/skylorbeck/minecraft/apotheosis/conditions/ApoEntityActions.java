package website.skylorbeck.minecraft.apotheosis.conditions;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import website.skylorbeck.minecraft.apotheosis.Declarar;

public class ApoEntityActions {
    public static void register() {
        register(new ActionFactory<>(Declarar.getIdentifier("mending"), new SerializableData(),
                (data, entity) -> {
                    if (entity instanceof PlayerEntity) {
                        PlayerInventory inventory = ((PlayerEntity) entity).getInventory();
                        for (int i = 0; i <inventory.size() ; i++) {
                            ItemStack stack = inventory.getStack(i);
                            if (stack.getDamage()>0){
                                stack.setDamage(stack.getDamage()-1);
                            }
                        }
                    }
                }));
        register(new ActionFactory<>(Declarar.getIdentifier("ultimate_mending"), new SerializableData(),
                (data, entity) -> {
                    if (entity instanceof PlayerEntity) {
                        PlayerInventory inventory = ((PlayerEntity) entity).getInventory();
                        for (int i = 0; i <inventory.size() ; i++) {
                            ItemStack stack = inventory.getStack(i);
                            if (stack.getDamage()>0){
                                stack.setDamage(0);
                            }
                        }
                    }
                }));
    }
    private static void register(ActionFactory<Entity> actionFactory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}