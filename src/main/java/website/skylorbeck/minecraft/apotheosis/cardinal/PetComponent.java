package website.skylorbeck.minecraft.apotheosis.cardinal;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public class PetComponent implements AutoSyncedComponent {
    private final WolfEntity wolfEntity;
    private int timeLeft = 0;
    private UUID ownerUUID;

    public PetComponent(WolfEntity provider) {
        this.wolfEntity = provider;

    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if (tag.contains("owner"))
        this.ownerUUID = tag.getUuid("owner");
        if (tag.contains("timeleft"))
            this.timeLeft = tag.getInt("timeleft");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        if (this.ownerUUID != null) {
            tag.putUuid("owner", this.ownerUUID);
        }
        tag.putInt("timeleft", this.timeLeft);
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }
}
