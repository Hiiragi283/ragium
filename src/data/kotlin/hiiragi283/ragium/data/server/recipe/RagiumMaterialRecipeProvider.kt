package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.common.data.recipe.builder.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.material.RagiumMaterial
import hiiragi283.ragium.setup.RagiumItems

object RagiumMaterialRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Raginite + Copper -> Ragi-Alloy Compound
        HTShapedRecipeBuilder
            .create(RagiumItems.RAGI_ALLOY_COMPOUND)
            .hollow4()
            .define('A', HCMaterialPrefixes.DUST, RagiumMaterial.RAGINITE)
            .define('B', HCMaterialPrefixes.INGOT, HCMaterial.Metals.COPPER)
            .save(output)
        // Ragi-Alloy Compound -> Ragi-Alloy
        HTCookingRecipeBuilder
            .smeltingAndBlasting(getItem(HCMaterialPrefixes.INGOT, RagiumMaterial.RAGI_ALLOY)) {
                addIngredient(RagiumItems.RAGI_ALLOY_COMPOUND)
                setExp(0.7f)
                saveSuffixed(output, "_from_compound")
            }

        // Raginite + Diamond -> Ragi-Crystal
        HTShapedRecipeBuilder
            .create(getItem(HCMaterialPrefixes.GEM, RagiumMaterial.RAGI_CRYSTAL))
            .hollow8()
            .define('A', HCMaterialPrefixes.DUST, RagiumMaterial.RAGINITE)
            .define('B', HCMaterialPrefixes.GEM, HCMaterial.Gems.DIAMOND)
            .save(output)
    }

    @JvmStatic
    private fun getItem(prefix: HTPrefixLike, material: HTMaterialLike): HTItemHolderLike<*> =
        RagiumItems.MATERIALS.getOrThrow(prefix, material)
}
