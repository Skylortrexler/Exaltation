package website.skylorbeck.minecraft.apotheosis.blocks.screens;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.origins.origin.*;
import io.github.apace100.origins.registry.ModComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import website.skylorbeck.minecraft.apotheosis.Declarar;
import website.skylorbeck.minecraft.apotheosis.cardinal.XPComponent;
import website.skylorbeck.minecraft.apotheosis.powers.BranchingClassPower;
import website.skylorbeck.minecraft.apotheosis.powers.ConsumingItemPower;

import java.util.List;
import java.util.Optional;

import static website.skylorbeck.minecraft.apotheosis.cardinal.ApotheosisComponents.APOXP;

public class AltarHandledScreen extends HandledScreen<ScreenHandler> {
    private static final Identifier TEXTURE = Declarar.getIdentifier("textures/gui/altarbg.png");
    private Origin origin;
    private final Origin[] originUpgrades = new Origin[2];
    private int AXP;
    private int AXPC;
    private int MCXP;
    private Mode mode;

    public AltarHandledScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        levelUpClicked();
    }

    private enum Mode{
        normal,
        classfork
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        this.drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        x = x + 60;
        y = y + 14;
        int t = 6839882;
        switch (mode){
            case normal -> {
                this.setZOffset(0);
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, TEXTURE);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                int u = mouseX - x;
                int v = mouseY - y;
                TranslatableText text = new TranslatableText("apotheosis.altar.levelup");
                if (MCXP>=AXPC||this.client.player.isCreative()) {
//                t = 4226832;
                    if (u >= 0 && v >= 38 && u < 107 && v < 57) {
                        this.drawTexture(matrices, x, y+38, 108, 223, 107, 19);//highlighted
                        t = 16777088;
                    } else {
                        this.drawTexture(matrices, x, y+38, 0, 223,107, 19);//has upgrade
                    }
                } else {
                    t = 8453920;
                    text = new TranslatableText("apotheosis.altar.notenoughxp");
                }
                x = x+53;
                this.textRenderer.drawTrimmed(text, (int) (x-this.textRenderer.getWidth(text)/2f),y+44, 107, t);
                t = 16777088;
                this.textRenderer.drawWithShadow(matrices,origin.getName(),x-this.textRenderer.getWidth(origin.getName())/2f,y+3, t);
                t = DyeColor.ORANGE.getSignColor();
                text =new TranslatableText("apotheosis.altar.xp");
                String xp = ": "+AXP;
                this.textRenderer.drawWithShadow(matrices,text,x-this.textRenderer.getWidth(xp)/2f-this.textRenderer.getWidth(text)/2f,y+15, t);
                this.textRenderer.drawWithShadow(matrices,xp,x+this.textRenderer.getWidth(text)/2f-this.textRenderer.getWidth(xp)/2f,y+15, t);
                t = DyeColor.LIME.getSignColor();
                text =new TranslatableText("apotheosis.altar.xpcost");
                xp = ": "+(AXPC);
                this.textRenderer.drawWithShadow(matrices,text,x-this.textRenderer.getWidth(xp)/2f-this.textRenderer.getWidth(text)/2f,y+27, t);
                this.textRenderer.drawWithShadow(matrices,xp,x+this.textRenderer.getWidth(text)/2f-this.textRenderer.getWidth(xp)/2f,y+27, t);
            }
            case classfork -> {
                for (int i = 0; i < 2; ++i) {
                    this.setZOffset(0);
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderTexture(0, TEXTURE);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    int u = mouseX - (x + (54 * i));
                    int v = mouseY - y;

                    if (origin.hasUpgrade() && originUpgrades[i] != null) {
//                t = 4226832;
                        if (u >= 0 && v >= 0 && u < 54 && v < 57) {
                            this.drawTexture(matrices, x + (54 * i), y, 108, 166, 54, 57);//highlighted
                            t = 16777088;
                        } else {
                            this.drawTexture(matrices, x + (54 * i), y, 0, 166, 54, 57);//has upgrade
                            t = 6839882;

                        }
                    } else {
                        this.drawTexture(matrices, x + (54 * i), y, 54, 166, 54, 57);//no upgrade
                        t = 8453920;
                    }
                    if (originUpgrades[i] != null) {
                        Origin up = originUpgrades[i];
                        this.textRenderer.drawTrimmed(up.getName(), (int) (x + 27 + (54 * i) - this.textRenderer.getWidth(up.getName()) / 2f), y + 3,100, t);
                        Impact impact = up.getImpact();
                        int impactValue = impact.getImpactValue();
                        int offset = impactValue * 8;
                        RenderSystem.setShader(GameRenderer::getPositionTexShader);
                        RenderSystem.setShaderTexture(0, TEXTURE);
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                        for (int j = 0; j < 3; j++) {
                            if (j < impactValue) {
                                this.drawTexture(matrices, x + 13 + (j * 10) +(54*i), y + 15, offset, 242, 8, 8);
                            } else {
                                this.drawTexture(matrices, x + 13 + (j * 10)+(54*i), y + 15, 0, 242, 8, 8);
                            }
                        }
                        ItemStack item = up.getDisplayItem().copy();
                        if (u >= 0 && v >= 0 && u < 54 && v < 57) {
                            item.addEnchantment(Enchantments.VANISHING_CURSE, 1);
                        }
                        this.itemRenderer.renderGuiItemIcon(item, x + 21 + (54 * i), y + 36);
                    } else {
                        this.textRenderer.drawWithShadow(matrices, "NULL", x + 27 + (54 * i) - this.textRenderer.getWidth("NULL") / 2f, y + 3, t);
                    }
                }
            }
        }

    }
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        switch (mode){
            case normal -> {
                if (this.isPointWithinBounds(60, 53, 107, 19, mouseX, mouseY)) {
                    PlayerEntity player =this.client.player;
                    XPComponent xpComponent= APOXP.get(player);
                    int APOXPLVL = xpComponent.getLevel();
                    List<Text> tooltip = Lists.newArrayList();
                    if ((APOXPLVL + 1) % 5 == 0 && PowerHolderComponent.hasPower(player, ConsumingItemPower.class) && !player.isCreative()) {
                        Item itemcost = APOXPLVL>=44? Items.DIAMOND:PowerHolderComponent.getPowers(player,ConsumingItemPower.class).get(0).getItem();
                        int cost = APOXPLVL>=44?APOXPLVL>=49?2:1:Math.min(Math.floorDiv(APOXPLVL+1,5),4);
                        tooltip.add(Text.of("You will need:"));
                        tooltip.add(Text.of(cost*5 + " " + itemcost.getDefaultStack().getItem().getName().getString()));
                    }
                    this.renderTooltip(matrices, tooltip, mouseX, mouseY);

                }
            }
            case classfork -> {
                for (int i = 0; i < 2; ++i) {
                    if (this.isPointWithinBounds(60 + (54 * i), 14, 54, 57, mouseX, mouseY)) {
                        if (originUpgrades[i] != null) {
                            List<Text> newPowers = Lists.newArrayList();
                            List<Text> lostPowers = Lists.newArrayList();
                            newPowers.add(Text.of("New Powers:").copy().formatted(Formatting.LIGHT_PURPLE));

                            originUpgrades[i].getPowerTypes().forEach(powerType -> {
                                if (!powerType.isHidden() && !origin.hasPowerType(powerType)){
                                    newPowers.add(powerType.getName().formatted(Formatting.GOLD));
                                    newPowers.add(Text.of("  "+powerType.getDescription().getString()));
                                }
                            });
                            int finalI = i;
                            origin.getPowerTypes().forEach((powerType -> {
                                if (!powerType.isHidden() && !originUpgrades[finalI].hasPowerType(powerType)){
                                    lostPowers.add(powerType.getName().formatted(Formatting.GOLD).formatted(Formatting.STRIKETHROUGH));
                                    LiteralText literalText = new LiteralText("  "+powerType.getDescription().getString());
                                    lostPowers.add(literalText.formatted(Formatting.STRIKETHROUGH));
                                }
                            }));
                            if (lostPowers.size()>0){
                                newPowers.add(Text.of("Lost Powers:").copy().formatted(Formatting.DARK_PURPLE));
                                newPowers.addAll(lostPowers);
                            }
                            this.renderTooltip(matrices, newPowers, mouseX, mouseY);
                            break;
                        } else {
                            this.renderTooltip(matrices, Text.of("ERROR"), mouseX, mouseY);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        switch (mode){
            case normal -> {
                double u = mouseX - (double) (x + 60);
                double v = mouseY - (double) (y + 52);
                if ((client.player.isCreative()||MCXP>=AXPC) && u >= 0.0D && v >= 0.0D && u < 107D && v < 19D && this.handler.onButtonClick(this.client.player, 3)) {
                    this.client.interactionManager.clickButton(this.handler.syncId, 3);
                    return true;
                }
            }
            case classfork -> {
                for (int i = 0; i < 2; ++i) {
                    double u = mouseX - (double) (x + 60 + (54 * i));
                    double v = mouseY - (double) (y + 14);
                    if (originUpgrades[i] != null && u >= 0.0D && v >= 0.0D && u < 54D && v < 57D && this.handler.onButtonClick(this.client.player, i)) {
                        this.client.interactionManager.clickButton(this.handler.syncId, i);
                        return true;
                    }
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public Optional<Element> hoveredElement(double mouseX, double mouseY) {
        return super.hoveredElement(mouseX, mouseY);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return super.charTyped(chr, modifiers);
    }

    @Override
    public void setInitialFocus(@Nullable Element element) {
        super.setInitialFocus(element);
    }

    @Override
    public void focusOn(@Nullable Element element) {
        super.focusOn(element);
    }

    @Override
    public boolean changeFocus(boolean lookForwards) {
        return super.changeFocus(lookForwards);
    }

    @Override
    protected void init() {
        this.client = MinecraftClient.getInstance();
        super.init();
    }

    public void levelUpClicked(){
        this.client = MinecraftClient.getInstance();
        MinecraftServer server = client.getServer();
        assert server != null;
        ServerAdvancementLoader advancementLoader = server.getAdvancementLoader();
        PlayerEntity player = server.getPlayerManager().getPlayer(client.player.getUuid());
        assert player != null;

        MCXP = player.experienceLevel;
        AXP = APOXP.get(player).getLevel();
        AXPC = APOXP.get(player).getLevelUpCost();
        OriginLayer originLayer = OriginLayers.getLayer(Declarar.getIdentifier("class"));
        origin = ModComponents.ORIGIN.get(player).getOrigin(originLayer);
//        if (AXP>=50 ||(APOXP.get(player).getAscended() && (this.AXP==15||this.AXP==25||this.AXP==45))){
//        Logger.getGlobal().log(Level.SEVERE,origin.hasUpgrade()+":"+PowerHolderComponent.hasPower(player, BranchingClassPower.class) +":"+PowerHolderComponent.getPowers(player, BranchingClassPower.class).get(0).getLevel()+":"+AXP);
        if (origin.hasUpgrade() && PowerHolderComponent.hasPower(player, BranchingClassPower.class) && AXP >= PowerHolderComponent.getPowers(player, BranchingClassPower.class).get(0).getLevel()){
            mode = Mode.classfork;
            Identifier[] advancementID = new Identifier[2];
            advancementID[0] = new Identifier(origin.getIdentifier()+"_upgrade_a");
            advancementID[1] = new Identifier(origin.getIdentifier()+"_upgrade_b");
            try {
                originUpgrades[0] =OriginRegistry.get(origin.getUpgrade(advancementLoader.get(advancementID[0])).get().getUpgradeToOrigin());
            } catch (Exception exception){
//                Logger.getGlobal().log(Level.SEVERE, String.valueOf(exception.getCause()));
                originUpgrades[0]=null;
            }
            try {
                originUpgrades[1] = OriginRegistry.get(origin.getUpgrade(advancementLoader.get(advancementID[1])).get().getUpgradeToOrigin());
            } catch (Exception exception){
//                Logger.getGlobal().log(Level.SEVERE, String.valueOf(exception.getMessage()));
                originUpgrades[1]=null;
            }
        } else {
            mode = Mode.normal;
        }
    }
}
