package hiiragi283.ragium.data.server

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumBasicRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumChemicalRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumCoolRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumEnchantingRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumFluidRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumHeatRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumMaterialRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumMatterRecipeProvider
import hiiragi283.ragium.data.server.recipe.RagiumPlantingRecipeBuilder
import hiiragi283.ragium.data.server.recipe.RagiumUtilitiesRecipeProvider
import java.util.function.Consumer

class RagiumRecipeProvider(context: HTDataGenContext) : HTRecipeProvider(context) {
    override fun collectProviders(consumer: Consumer<HTSubRecipeProvider>) {
        consumer.accept(RagiumBasicRecipeProvider)
        consumer.accept(RagiumFluidRecipeProvider)
        consumer.accept(RagiumChemicalRecipeProvider)
        consumer.accept(RagiumCoolRecipeProvider)
        consumer.accept(RagiumEnchantingRecipeProvider)
        consumer.accept(RagiumHeatRecipeProvider)
        consumer.accept(RagiumMatterRecipeProvider)
        consumer.accept(RagiumPlantingRecipeBuilder)

        consumer.accept(RagiumMaterialRecipeProvider)
        consumer.accept(RagiumUtilitiesRecipeProvider)
    }
}
