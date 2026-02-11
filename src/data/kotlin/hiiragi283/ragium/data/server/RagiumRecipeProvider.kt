package hiiragi283.ragium.data.server

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumAssemblingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumChemicalRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumCrushingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumEnchantingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumHeatRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumMaterialRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumMiscRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumOrganicRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumPlantingRecipeBuilder
import hiiragi283.ragium.data.server.recipe.RagiumSmeltingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumUtilitiesRecipeProvider
import java.util.function.Consumer

class RagiumRecipeProvider(context: HTDataGenContext) : HTRecipeProvider(context) {
    override fun collectProviders(consumer: Consumer<HTSubRecipeProvider>) {
        consumer.accept(RagiumAssemblingRecipeProvider)
        consumer.accept(RagiumChemicalRecipeProvider)
        consumer.accept(RagiumCrushingRecipeProvider)
        consumer.accept(RagiumEnchantingRecipeProvider)
        consumer.accept(RagiumHeatRecipeProvider)
        consumer.accept(RagiumMiscRecipeProvider)
        consumer.accept(RagiumOrganicRecipeProvider)
        consumer.accept(RagiumPlantingRecipeBuilder)
        consumer.accept(RagiumSmeltingRecipeProvider)

        consumer.accept(RagiumMaterialRecipeProvider)
        consumer.accept(RagiumUtilitiesRecipeProvider)
    }
}
