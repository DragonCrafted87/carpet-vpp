package dragoncrafted87.vpp.beacons;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import dragoncrafted87.vpp.MinecraftVPP;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.PersistentStateType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BeaconChunkLoaderData extends PersistentState {
    public Set<BlockPos> activeBeacons = new HashSet<>();

    private BeaconChunkLoaderData() {
    }

    private BeaconChunkLoaderData(Set<BlockPos> activeBeacons) {
        this.activeBeacons = activeBeacons;
    }

    private static final Codec<Set<BlockPos>> SET_CODEC = BlockPos.CODEC.listOf().xmap(HashSet::new, ArrayList::new);

    private static final Codec<BeaconChunkLoaderData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SET_CODEC.fieldOf("activeBeacons").forGetter(data -> data.activeBeacons))
            .apply(instance, BeaconChunkLoaderData::new));

    private static final PersistentStateType<BeaconChunkLoaderData> TYPE = new PersistentStateType<>(
            MinecraftVPP.MOD_ID,
            BeaconChunkLoaderData::new,
            CODEC,
            null);

    public static BeaconChunkLoaderData get(ServerWorld world) {
        PersistentStateManager manager = world.getPersistentStateManager();
        return manager.getOrCreate(TYPE);
    }

    public void addBeacon(BlockPos pos) {
        if (activeBeacons.add(pos)) {
            markDirty();
        }
    }

    public void removeBeacon(BlockPos pos) {
        if (activeBeacons.remove(pos)) {
            markDirty();
        }
    }
}
