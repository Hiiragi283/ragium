package hiiragi283.ragium.common.datagen

import hiiragi283.core.api.data.HTDynamicResourceProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.datagen.map.RagiumDataMapProviders
import hiiragi283.ragium.common.datagen.recipe.RagiumAlloyingRecipeProvider
import hiiragi283.ragium.common.datagen.recipe.RagiumAssemblingRecipeProvider
import hiiragi283.ragium.common.datagen.recipe.RagiumChemicalRecipeProvider
import hiiragi283.ragium.common.datagen.recipe.RagiumCrushingRecipeProvider
import hiiragi283.ragium.common.datagen.recipe.RagiumHeatRecipeProvider
import hiiragi283.ragium.common.datagen.recipe.RagiumMaterialRecipeProvider
import hiiragi283.ragium.common.datagen.recipe.RagiumMiscRecipeProvider
import hiiragi283.ragium.common.datagen.recipe.RagiumOrganicRecipeProvider
import hiiragi283.ragium.common.datagen.recipe.RagiumPlantingRecipeBuilder
import hiiragi283.ragium.common.datagen.recipe.RagiumUtilitiesRecipeProvider
import hiiragi283.ragium.common.datagen.tag.RagiumBlockTagsProvider
import hiiragi283.ragium.common.datagen.tag.RagiumFluidTagsProvider
import hiiragi283.ragium.common.datagen.tag.RagiumItemTagsProvider
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask
import java.util.function.Consumer

/**
 * @see hiiragi283.core.common.datagen.HCServerResourceProvider
 */
object RagiumServerResourceProvider : HTDynamicResourceProvider.Server(RagiumAPI.MOD_ID) {
    override fun regenerateDynamicAssets(executor: Consumer<ResourceGenTask>) {
        HTDynamicResourceProvider.addMaterialIds(this::addSupportedNamespaces)

        // Dynamic Registry

        // Data Map
        executor.accept(RagiumDataMapProviders.MobHead)

        executor.accept(RagiumDataMapProviders.Coolant)
        executor.accept(RagiumDataMapProviders.MagmaticFuel)
        executor.accept(RagiumDataMapProviders.CombustionFuel)
        // Loot Table

        // Recipe
        executor.accept(RagiumAlloyingRecipeProvider)
        executor.accept(RagiumAssemblingRecipeProvider)
        executor.accept(RagiumChemicalRecipeProvider)
        executor.accept(RagiumCrushingRecipeProvider)
        executor.accept(RagiumHeatRecipeProvider)
        executor.accept(RagiumMaterialRecipeProvider)
        executor.accept(RagiumMiscRecipeProvider)
        executor.accept(RagiumOrganicRecipeProvider)
        executor.accept(RagiumPlantingRecipeBuilder)
        executor.accept(RagiumUtilitiesRecipeProvider)
        // Tag
        executor.accept(RagiumBlockTagsProvider)
        executor.accept(RagiumFluidTagsProvider)
        executor.accept(RagiumItemTagsProvider)
    }
}
