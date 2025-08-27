package dragoncrafted87.vpp.beacons;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import dragoncrafted87.vpp.MinecraftVPP;
import java.util.HashSet;
import java.util.Set;
public class BeaconChunkLoaderData extends PersistentState {
    public Set<BlockPos> activeBeacons = new HashSet<>();
    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
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
    public static BeaconChunkLoaderData createFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup lookup) {
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
        PersistentState.Type<BeaconChunkLoaderData> type = new PersistentState.Type<>(BeaconChunkLoaderData::new, BeaconChunkLoaderData::createFromNbt, null);
        return manager.getOrCreate(type, MinecraftVPP.MOD_ID);
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
