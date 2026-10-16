package dragoncrafted87.vpp.beacons;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dragoncrafted87.vpp.MinecraftVPP;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.SavedDataStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BeaconChunkLoaderData extends SavedData {
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

    private static final SavedDataType<BeaconChunkLoaderData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(MinecraftVPP.MOD_ID, "beacons"),
            BeaconChunkLoaderData::new,
            CODEC,
            null);

    public static BeaconChunkLoaderData get(ServerLevel world) {
        SavedDataStorage manager = world.getDataStorage();
        return manager.computeIfAbsent(TYPE);
    }

    public void addBeacon(BlockPos pos) {
        if (activeBeacons.add(pos)) {
            setDirty();
        }
    }

    public void removeBeacon(BlockPos pos) {
        if (activeBeacons.remove(pos)) {
            setDirty();
        }
    }
}
