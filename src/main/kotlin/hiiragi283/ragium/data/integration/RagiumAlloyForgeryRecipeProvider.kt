package hiiragi283.ragium.data.integration

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class RagiumAlloyForgeryRecipeProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricRecipeProvider(output, registriesFuture) {
    override fun getName(): String = "Recipes/Alloy Forgery"

    override fun generate(exporter: RecipeExporter) {
        // generateRecipes(withConditions(exporter, ResourceConditions.allModsLoaded("alloy_forgery")))
    }

    /*private fun generateRecipes(exporter: RecipeExporter) {
        // ragi-alloy
        AlloyForgeryRecipeBuilder
            .create(RagiumContents.Ingots.RAGI_ALLOY)
            .input(ConventionalItemTags.COPPER_INGOTS, 1)
            .input(RagiumContents.Dusts.CRUDE_RAGINITE, 4)
            .criterion("has_the_input", RecipeProvider.conditionsFromItem(RagiumContents.Dusts.CRUDE_RAGINITE))
            .offerTo(exporter, RagiumAPI.id("alloy_forgery/ragi_alloy_ingot"))
        // ragi-steel
        AlloyForgeryRecipeBuilder
            .create(RagiumContents.Ingots.RAGI_STEEL)
            .input(ConventionalItemTags.IRON_INGOTS, 1)
            .input(RagiumContents.Dusts.RAGINITE, 4)
            .setMinimumForgeTier(2)
            .criterion("has_the_input", RecipeProvider.conditionsFromItem(RagiumContents.Dusts.RAGINITE))
            .offerTo(exporter, RagiumAPI.id("alloy_forgery/ragi_steel_ingot"))
        // refined ragi-steel
        AlloyForgeryRecipeBuilder
            .create(RagiumContents.Ingots.REFINED_RAGI_STEEL)
            .input(RagiumItemTags.STEEL_INGOTS, 1)
            .input(RagiumContents.Dusts.RAGI_CRYSTAL, 4)
            .input(ConventionalItemTags.QUARTZ_GEMS, 1)
            .setMinimumForgeTier(3)
            .criterion("has_the_input", RecipeProvider.conditionsFromItem(RagiumContents.Dusts.RAGINITE))
            .offerTo(exporter, RagiumAPI.id("alloy_forgery/refined_ragi_steel_ingot"))
        // silicon plate
        AlloyForgeryRecipeBuilder
            .create(RagiumContents.Plates.SILICON)
            .input(ConventionalItemTags.QUARTZ_GEMS, 2)
            .input(ItemTags.COALS, 4)
            .setMinimumForgeTier(2)
            .criterion("has_the_input", RecipeProvider.conditionsFromTag(ConventionalItemTags.QUARTZ_GEMS))
            .offerTo(exporter, RagiumAPI.id("alloy_forgery/silicon_plate"))
    }*/
}
