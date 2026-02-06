package hiiragi283.ragium.data.server

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.common.data.recipe.HTMaterialRecipeProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.data.server.recipe.RagiumAlloyingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumAssemblingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumChemicalRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumCrushingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumCuttingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumEnchantingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumHeatRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumMaterialRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumOrganicRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumPlantingRecipeBuilder
import hiiragi283.ragium.data.server.recipe.RagiumPressingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumUtilitiesRecipeProvider
import java.util.function.Consumer

class RagiumRecipeProvider(context: HTDataGenContext) : HTRecipeProvider(context) {
    override fun collectProviders(consumer: Consumer<HTSubRecipeProvider>) {
        consumer.accept(HTMaterialRecipeProvider(RagiumAPI.MOD_ID))

        consumer.accept(RagiumAlloyingRecipeProvider)
        consumer.accept(RagiumAssemblingRecipeProvider)
        consumer.accept(RagiumChemicalRecipeProvider)
        consumer.accept(RagiumCrushingRecipeProvider)
        consumer.accept(RagiumCuttingRecipeProvider)
        consumer.accept(RagiumEnchantingRecipeProvider)
        consumer.accept(RagiumHeatRecipeProvider)
        consumer.accept(RagiumOrganicRecipeProvider)
        consumer.accept(RagiumPlantingRecipeBuilder)
        consumer.accept(RagiumPressingRecipeProvider)

        consumer.accept(RagiumMaterialRecipeProvider)
        consumer.accept(RagiumUtilitiesRecipeProvider)
    }
}
