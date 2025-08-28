package dragoncrafted87.vpp.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import dragoncrafted87.vpp.MinecraftVPP;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class VPPAdvancementProvider extends FabricAdvancementProvider {
    public VPPAdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup registryLookup, Consumer<AdvancementEntry> consumer) {

        AdvancementEntry pouchUnlock = Advancement.Builder.createUntelemetered()
                .parent(new AdvancementEntry(Identifier.of("minecraft", "recipes/root"), null))
                .rewards(AdvancementRewards.Builder.recipe(Identifier.of("vpp", "pouch")))
                .criterion("has_leather", InventoryChangedCriterion.Conditions.items(Items.LEATHER))
                .criterion("has_string", InventoryChangedCriterion.Conditions.items(Items.STRING))
                .criterion("has_button", InventoryChangedCriterion.Conditions.items(Items.STONE_BUTTON))
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(Identifier.of("vpp", "pouch")))
                .requirements(AdvancementRequirements.allOf(List.of("has_leather", "has_string", "has_button", "has_the_recipe")))
                .build(consumer, "vpp:pouch");

        AdvancementEntry satchelStrapUnlock = Advancement.Builder.createUntelemetered()
                .parent(new AdvancementEntry(Identifier.of("minecraft", "recipes/root"), null))
                .rewards(AdvancementRewards.Builder.recipe(Identifier.of("vpp", "satchel_strap")))
                .criterion("has_leather", InventoryChangedCriterion.Conditions.items(Items.LEATHER))
                .criterion("has_string", InventoryChangedCriterion.Conditions.items(Items.STRING))
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(Identifier.of("vpp", "satchel_strap")))
                .requirements(AdvancementRequirements.allOf(List.of("has_leather", "has_string", "has_the_recipe")))
                .build(consumer, "vpp:satchel_strap");

        AdvancementEntry satchelUnlock = Advancement.Builder.createUntelemetered()
                .parent(new AdvancementEntry(Identifier.of("minecraft", "recipes/root"), null))
                .rewards(AdvancementRewards.Builder.recipe(Identifier.of("vpp", "satchel")))
                .criterion("has_satchel_strap", InventoryChangedCriterion.Conditions.items(MinecraftVPP.SATCHEL_STRAP))
                .criterion("has_leather", InventoryChangedCriterion.Conditions.items(Items.LEATHER))
                .criterion("has_string", InventoryChangedCriterion.Conditions.items(Items.STRING))
                .criterion("has_button", InventoryChangedCriterion.Conditions.items(Items.STONE_BUTTON))
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(Identifier.of("vpp", "satchel")))
                .requirements(AdvancementRequirements.allOf(List.of("has_satchel_strap", "has_leather", "has_string", "has_button", "has_the_recipe")))
                .build(consumer, "vpp:satchel");

        AdvancementEntry upgradedPouchUnlock = Advancement.Builder.createUntelemetered()
                .parent(new AdvancementEntry(Identifier.of("minecraft", "recipes/root"), null))
                .rewards(AdvancementRewards.Builder.recipe(Identifier.of("vpp", "upgraded_pouch")))
                .criterion("has_diamond", InventoryChangedCriterion.Conditions.items(Items.DIAMOND))
                .criterion("has_leather", InventoryChangedCriterion.Conditions.items(Items.LEATHER))
                .criterion("has_pouch", InventoryChangedCriterion.Conditions.items(MinecraftVPP.POUCH))
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(Identifier.of("vpp", "upgraded_pouch")))
                .requirements(AdvancementRequirements.allOf(List.of("has_diamond", "has_leather", "has_pouch", "has_the_recipe")))
                .build(consumer, "vpp:upgraded_pouch");

        AdvancementEntry upgradedSatchelUnlock = Advancement.Builder.createUntelemetered()
                .parent(new AdvancementEntry(Identifier.of("minecraft", "recipes/root"), null))
                .rewards(AdvancementRewards.Builder.recipe(Identifier.of("vpp", "upgraded_satchel")))
                .criterion("has_diamond", InventoryChangedCriterion.Conditions.items(Items.DIAMOND))
                .criterion("has_leather", InventoryChangedCriterion.Conditions.items(Items.LEATHER))
                .criterion("has_satchel", InventoryChangedCriterion.Conditions.items(MinecraftVPP.SATCHEL))
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(Identifier.of("vpp", "upgraded_satchel")))
                .requirements(AdvancementRequirements.allOf(List.of("has_diamond", "has_leather", "has_satchel", "has_the_recipe")))
                .build(consumer, "vpp:upgraded_satchel");
    }
}
