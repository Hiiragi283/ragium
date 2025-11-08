package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.material.HTMaterialRecipeData
import hiiragi283.ragium.api.stack.toImmutableOrThrow
import hiiragi283.ragium.impl.data.recipe.HTAlloySmeltingRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.material.RagiumMaterialRecipeData

object RagiumEIORecipeProvider : HTRecipeProvider.Integration(RagiumConst.EIO_BASE) {
    override fun buildRecipeInternal() {
        alloys()
    }

    private fun alloys() {
        alloyFromData(RagiumMaterialRecipeData.RAGI_ALLOY, 4800).save(output)
        alloyFromData(RagiumMaterialRecipeData.ADVANCED_RAGI_ALLOY, 5600).save(output)

        alloyFromData(RagiumMaterialRecipeData.AZURE_SHARD, 3200).save(output)
        alloyFromData(RagiumMaterialRecipeData.AZURE_STEEL, 4800).save(output)

        alloyFromData(RagiumMaterialRecipeData.DEEP_STEEL, 5600).save(output)

        alloyFromData(RagiumMaterialRecipeData.ELDRITCH_PEARL, 5600).save(output)
        alloyFromData(RagiumMaterialRecipeData.ELDRITCH_PEARL_BULK, 5600).saveSuffixed(output, "_alt")

        alloyFromData(RagiumMaterialRecipeData.NIGHT_METAL, 4800).save(output)
        alloyFromData(RagiumMaterialRecipeData.IRIDESCENTIUM, 6400).save(output)
    }

    @JvmStatic
    private fun alloyFromData(data: HTMaterialRecipeData, energy: Int, exp: Float = 0.3f): HTAlloySmeltingRecipeBuilder {
        val builder = HTAlloySmeltingRecipeBuilder(data.getOutputStack().toImmutableOrThrow())
        // Inputs
        data.getSizedIngredients().forEach(builder::addIngredient)
        return builder.setEnergy(energy).setExp(exp)
    }
}
