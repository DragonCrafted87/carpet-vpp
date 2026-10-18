package dragoncrafted87.vpp.gametest;

import dragoncrafted87.vpp.MinecraftVPP;

import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class VppClientGameTest implements FabricClientGameTest {
    @Override
    public void runTest(ClientGameTestContext context) {
        context.computeOnClient(
                client -> {
                    Identifier pouch =
                            Identifier.fromNamespaceAndPath(MinecraftVPP.MOD_ID, "pouch");
                    if (BuiltInRegistries.ITEM.getOptional(pouch).isEmpty()) {
                        throw new AssertionError("client registry is missing " + pouch);
                    }
                    return null;
                });
    }
}
