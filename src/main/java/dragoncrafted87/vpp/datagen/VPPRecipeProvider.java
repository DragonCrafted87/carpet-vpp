package dragoncrafted87.vpp.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.data.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import dragoncrafted87.vpp.MinecraftVPP;

import java.util.concurrent.CompletableFuture;

public class VPPRecipeProvider extends FabricRecipeProvider {
    public VPPRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public String getName() {
        return "VPP Recipes";
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registries, net.minecraft.data.recipe.RecipeExporter exporter) {
        return new RecipeGenerator(registries, exporter) {
            @Override
            public void generate() {
                RegistryEntryLookup<Item> itemLookup = registries.getOrThrow(RegistryKeys.ITEM);

                ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.MISC, MinecraftVPP.POUCH)
                        .pattern("SLS")
                        .pattern("LbL")
                        .pattern("SLS")
                        .input('S', Items.STRING)
                        .input('L', Items.LEATHER)
                        .input('b', Items.STONE_BUTTON)
                        .criterion("has_leather", InventoryChangedCriterion.Conditions.items(Items.LEATHER))
                        .criterion("has_string", InventoryChangedCriterion.Conditions.items(Items.STRING))
                        .criterion("has_stone_button", InventoryChangedCriterion.Conditions.items(Items.STONE_BUTTON))
                        .offerTo(exporter);

                ShapelessRecipeJsonBuilder.create(itemLookup, RecipeCategory.MISC, MinecraftVPP.SATCHEL_STRAP)
                        .input(Items.LEATHER)
                        .input(Items.STRING)
                        .input(Items.LEATHER)
                        .criterion("has_leather", InventoryChangedCriterion.Conditions.items(Items.LEATHER))
                        .criterion("has_string", InventoryChangedCriterion.Conditions.items(Items.STRING))
                        .offerTo(exporter);

                ShapedRecipeJsonBuilder.create(itemLookup, RecipeCategory.MISC, MinecraftVPP.SATCHEL)
                        .pattern("TLT")
                        .pattern("LbL")
                        .pattern("SLS")
                        .input('T', MinecraftVPP.SATCHEL_STRAP)
                        .input('L', Items.LEATHER)
                        .input('S', Items.STRING)
                        .input('b', Items.STONE_BUTTON)
                        .criterion("has_satchel_strap", InventoryChangedCriterion.Conditions.items(MinecraftVPP.SATCHEL_STRAP))
                        .criterion("has_leather", InventoryChangedCriterion.Conditions.items(Items.LEATHER))
                        .criterion("has_string", InventoryChangedCriterion.Conditions.items(Items.STRING))
                        .criterion("has_stone_button", InventoryChangedCriterion.Conditions.items(Items.STONE_BUTTON))
                        .offerTo(exporter);

                SmithingTransformRecipeJsonBuilder.create(Ingredient.ofItems(Items.LEATHER), Ingredient.ofItems(MinecraftVPP.POUCH), Ingredient.ofItems(Items.DIAMOND), RecipeCategory.MISC, MinecraftVPP.UPGRADED_POUCH)
                        .criterion("has_leather", InventoryChangedCriterion.Conditions.items(Items.LEATHER))
                        .criterion("has_pouch", InventoryChangedCriterion.Conditions.items(MinecraftVPP.POUCH))
                        .criterion("has_diamond", InventoryChangedCriterion.Conditions.items(Items.DIAMOND))
                        .offerTo(exporter, "upgraded_pouch");

                SmithingTransformRecipeJsonBuilder.create(Ingredient.ofItems(Items.LEATHER), Ingredient.ofItems(MinecraftVPP.SATCHEL), Ingredient.ofItems(Items.DIAMOND), RecipeCategory.MISC, MinecraftVPP.UPGRADED_SATCHEL)
                        .criterion("has_leather", InventoryChangedCriterion.Conditions.items(Items.LEATHER))
                        .criterion("has_satchel", InventoryChangedCriterion.Conditions.items(MinecraftVPP.SATCHEL))
                        .criterion("has_diamond", InventoryChangedCriterion.Conditions.items(Items.DIAMOND))
                        .offerTo(exporter, "upgraded_satchel");
            }
        };
    }
}
