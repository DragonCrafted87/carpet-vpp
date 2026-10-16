package dragoncrafted87.vpp.datagen;

import dragoncrafted87.vpp.MinecraftVPP;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.concurrent.CompletableFuture;

public class VPPRecipeProvider extends FabricRecipeProvider {
    public VPPRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public String getName() {
        return "VPP Recipes";
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries,
            BootstrapContext<Recipe<?>> recipeContext, BootstrapContext<Advancement> advancementContext) {
        return new RecipeProvider(recipeContext, advancementContext) {
            @Override
            public void buildRecipes() {
                var itemLookup = registries.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM);

                ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, MinecraftVPP.POUCH)
                        .pattern("SLS")
                        .pattern("LbL")
                        .pattern("SLS")
                        .define('S', Items.STRING)
                        .define('L', Items.LEATHER)
                        .define('b', Items.STONE_BUTTON)
                        .unlockedBy("has_leather", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER))
                        .unlockedBy("has_string", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STRING))
                        .unlockedBy("has_stone_button", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STONE_BUTTON))
                        .save(output);

                ShapelessRecipeBuilder.shapeless(itemLookup, RecipeCategory.MISC, MinecraftVPP.SATCHEL_STRAP)
                        .requires(Items.LEATHER)
                        .requires(Items.STRING)
                        .requires(Items.LEATHER)
                        .unlockedBy("has_leather", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER))
                        .unlockedBy("has_string", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STRING))
                        .save(output);

                ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, MinecraftVPP.SATCHEL)
                        .pattern("TLT")
                        .pattern("LbL")
                        .pattern("SLS")
                        .define('T', MinecraftVPP.SATCHEL_STRAP)
                        .define('L', Items.LEATHER)
                        .define('S', Items.STRING)
                        .define('b', Items.STONE_BUTTON)
                        .unlockedBy("has_satchel_strap", InventoryChangeTrigger.TriggerInstance.hasItems(MinecraftVPP.SATCHEL_STRAP))
                        .unlockedBy("has_leather", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER))
                        .unlockedBy("has_string", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STRING))
                        .unlockedBy("has_stone_button", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STONE_BUTTON))
                        .save(output);

                SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(Items.LEATHER),
                        Ingredient.of(MinecraftVPP.POUCH),
                        Ingredient.of(Items.DIAMOND),
                        RecipeCategory.MISC,
                        MinecraftVPP.UPGRADED_POUCH)
                        .unlocks("has_leather", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER))
                        .unlocks("has_pouch", InventoryChangeTrigger.TriggerInstance.hasItems(MinecraftVPP.POUCH))
                        .unlocks("has_diamond", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND))
                        .save(output, "upgraded_pouch");

                SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(Items.LEATHER),
                        Ingredient.of(MinecraftVPP.SATCHEL),
                        Ingredient.of(Items.DIAMOND),
                        RecipeCategory.MISC,
                        MinecraftVPP.UPGRADED_SATCHEL)
                        .unlocks("has_leather", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER))
                        .unlocks("has_satchel", InventoryChangeTrigger.TriggerInstance.hasItems(MinecraftVPP.SATCHEL))
                        .unlocks("has_diamond", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND))
                        .save(output, "upgraded_satchel");
            }
        };
    }
}
