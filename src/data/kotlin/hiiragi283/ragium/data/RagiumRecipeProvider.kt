package hiiragi283.ragium.data

import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumBasicRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumChemicalRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumCoolRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumEnchantingRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumFluidRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumHeatRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumMaterialRecipeProvider
import hiiragi283.ragium.data.recipe.RagiumPlantingRecipeBuilder
import hiiragi283.ragium.data.recipe.RagiumUtilitiesRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

class RagiumRecipeProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) :
    HTRecipeProvider(output, registries) {
    override fun collectProviders(consumer: Consumer<HTSubRecipeProvider>) {
        consumer.accept(RagiumBasicRecipeProvider)
        consumer.accept(RagiumFluidRecipeProvider)
        consumer.accept(RagiumChemicalRecipeProvider)
        consumer.accept(RagiumCoolRecipeProvider)
        consumer.accept(RagiumEnchantingRecipeProvider)
        consumer.accept(RagiumHeatRecipeProvider)
        consumer.accept(RagiumPlantingRecipeBuilder)

        consumer.accept(RagiumMaterialRecipeProvider)
        consumer.accept(RagiumUtilitiesRecipeProvider)
    }
}
