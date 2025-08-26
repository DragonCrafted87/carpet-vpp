package dragoncrafted87.vpp.beacons;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

import dragoncrafted87.vpp.MinecraftVPP;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class BeaconChunkLoaderData extends PersistentState {
    public Set<BlockPos> activeBeacons = new HashSet<>();
    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList list = new NbtList();
        for (BlockPos pos : activeBeacons) {
            NbtCompound compound = new NbtCompound();
            compound.putInt("x", pos.getX());
            compound.putInt("y", pos.getY());
            compound.putInt("z", pos.getZ());
            list.add(compound);
        }
        nbt.put("activeBeacons", list);
        return nbt;
    }
    public static BeaconChunkLoaderData createFromNbt(NbtCompound tag) {
        BeaconChunkLoaderData data = new BeaconChunkLoaderData();
        NbtList list = tag.getList("activeBeacons", 10); // 10 = NbtCompound
        for (int i = 0; i < list.size(); i++) {
            NbtCompound compound = list.getCompound(i);
            int x = compound.getInt("x");
            int y = compound.getInt("y");
            int z = compound.getInt("z");
            data.activeBeacons.add(new BlockPos(x, y, z));
        }
        return data;
    }
    public static BeaconChunkLoaderData get(ServerWorld world) {
        PersistentStateManager manager = world.getPersistentStateManager();
        Function<NbtCompound, BeaconChunkLoaderData> readFunction = BeaconChunkLoaderData::createFromNbt;
        Supplier<BeaconChunkLoaderData> supplier = BeaconChunkLoaderData::new;
        return manager.getOrCreate(readFunction, supplier, MinecraftVPP.MOD_ID);
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
