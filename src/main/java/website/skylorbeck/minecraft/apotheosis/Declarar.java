package website.skylorbeck.minecraft.apotheosis;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import website.skylorbeck.minecraft.apotheosis.blocks.AltarAbstract;
import website.skylorbeck.minecraft.apotheosis.blocks.entities.AltarEntity;
import website.skylorbeck.minecraft.apotheosis.blocks.screens.AltarScreenHandler;

public class Declarar {
    public static String MODID = "apotheosis";
    public static Identifier getIdentifier(String string){
        return new Identifier(MODID,string);
    }

    public static Block altar = new AltarAbstract(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK));
    public static BlockItem altarItem = new BlockItem(altar,new FabricItemSettings().group(ItemGroup.MISC));

    public static BlockEntityType<AltarEntity> ALTARENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            getIdentifier("altarentity"),
            FabricBlockEntityTypeBuilder.create(
                    AltarEntity::new,
                    altar
            ).build(null));
    public static ScreenHandlerType<AltarScreenHandler> ALTARSCREENHANDLER =
            ScreenHandlerRegistry.registerSimple(getIdentifier("altarscreen"),
                    ((syncId, inventory) -> new AltarScreenHandler(syncId)));
}