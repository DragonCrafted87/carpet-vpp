package dragoncrafted87.vpp.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import dragoncrafted87.vpp.MinecraftVPP;

import java.util.concurrent.CompletableFuture;

public class VPPRecipeProvider extends FabricRecipeProvider {
    public VPPRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(net.minecraft.data.server.recipe.RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, MinecraftVPP.POUCH)
                .pattern("SLS")
                .pattern("LbL")
                .pattern("SLS")
                .input('S', Items.STRING)
                .input('L', Items.LEATHER)
                .input('b', Items.STONE_BUTTON)
                .criterion(hasItem(Items.LEATHER), conditionsFromItem(Items.LEATHER))
                .criterion(hasItem(Items.STRING), conditionsFromItem(Items.STRING))
                .criterion(hasItem(Items.STONE_BUTTON), conditionsFromItem(Items.STONE_BUTTON))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, MinecraftVPP.SATCHEL_STRAP)
                .input(Items.LEATHER)
                .input(Items.STRING)
                .input(Items.LEATHER)
                .criterion(hasItem(Items.LEATHER), conditionsFromItem(Items.LEATHER))
                .criterion(hasItem(Items.STRING), conditionsFromItem(Items.STRING))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, MinecraftVPP.SATCHEL)
                .pattern("TLT")
                .pattern("LbL")
                .pattern("SLS")
                .input('T', MinecraftVPP.SATCHEL_STRAP)
                .input('L', Items.LEATHER)
                .input('S', Items.STRING)
                .input('b', Items.STONE_BUTTON)
                .criterion(hasItem(MinecraftVPP.SATCHEL_STRAP), conditionsFromItem(MinecraftVPP.SATCHEL_STRAP))
                .criterion(hasItem(Items.LEATHER), conditionsFromItem(Items.LEATHER))
                .criterion(hasItem(Items.STRING), conditionsFromItem(Items.STRING))
                .criterion(hasItem(Items.STONE_BUTTON), conditionsFromItem(Items.STONE_BUTTON))
                .offerTo(exporter);

        SmithingTransformRecipeJsonBuilder.create(Ingredient.ofItems(Items.LEATHER), Ingredient.ofItems(MinecraftVPP.POUCH), Ingredient.ofItems(Items.DIAMOND), RecipeCategory.MISC, MinecraftVPP.UPGRADED_POUCH)
                .criterion(hasItem(Items.LEATHER), conditionsFromItem(Items.LEATHER))
                .criterion(hasItem(MinecraftVPP.POUCH), conditionsFromItem(MinecraftVPP.POUCH))
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter, "upgraded_pouch");

        SmithingTransformRecipeJsonBuilder.create(Ingredient.ofItems(Items.LEATHER), Ingredient.ofItems(MinecraftVPP.SATCHEL), Ingredient.ofItems(Items.DIAMOND), RecipeCategory.MISC, MinecraftVPP.UPGRADED_SATCHEL)
                .criterion(hasItem(Items.LEATHER), conditionsFromItem(Items.LEATHER))
                .criterion(hasItem(MinecraftVPP.SATCHEL), conditionsFromItem(MinecraftVPP.SATCHEL))
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter, "upgraded_satchel");
    }
}
