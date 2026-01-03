package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.common.data.recipe.builder.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.material.RagiumFoodMaterials
import hiiragi283.ragium.common.material.RagiumMaterial
import hiiragi283.ragium.setup.RagiumItems

object RagiumMaterialRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        raginite()
        meat()
    }

    @JvmStatic
    private fun raginite() {
        // Raginite + Copper -> Ragi-Alloy Compound
        HTShapedRecipeBuilder
            .create(RagiumItems.RAGI_ALLOY_COMPOUND)
            .hollow4()
            .define('A', HCMaterialPrefixes.DUST, RagiumMaterial.RAGINITE)
            .define('B', HCMaterialPrefixes.INGOT, HCMaterial.Metals.COPPER)
            .save(output)
        // Ragi-Alloy Compound -> Ragi-Alloy
        HTCookingRecipeBuilder
            .smeltingAndBlasting(RagiumItems.MATERIALS.getOrThrow(HCMaterialPrefixes.INGOT, RagiumMaterial.RAGI_ALLOY)) {
                addIngredient(RagiumItems.RAGI_ALLOY_COMPOUND)
                setExp(0.7f)
                saveSuffixed(output, "_from_compound")
            }
    }

    @JvmStatic
    private fun meat() {
        // Meat Dust/Ingot -> Cooked Meat Ingot
        HTCookingRecipeBuilder.smeltingAndSmoking(RagiumItems.COOKED_MEAT_INGOT) {
            addIngredient(RagiumFoodMaterials.MEAT, HCMaterialPrefixes.INGOT, HCMaterialPrefixes.DUST)
            setExp(0.35f)
            saveSuffixed(output, "_from_meat")
        }
    }
}
