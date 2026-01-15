package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.common.data.recipe.builder.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.item.HTFoodCanType
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

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
            .define('A', HCMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            .define('B', HCMaterialPrefixes.INGOT, VanillaMaterialKeys.COPPER)
            .save(output)
        // Ragi-Alloy Compound -> Ragi-Alloy
        HTCookingRecipeBuilder
            .smeltingAndBlasting(RagiumItems.MATERIALS.getOrThrow(HCMaterialPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY)) {
                addIngredient(RagiumItems.RAGI_ALLOY_COMPOUND)
                setExp(0.7f)
                saveSuffixed(output, "_from_compound")
            }
    }

    @JvmStatic
    private fun meat() {
        // Meat + Bone -> Bone with Meat
        HTShapedRecipeBuilder
            .create(RagiumBlocks.MEAT_BLOCK)
            .hollow8()
            .define('A', HCMaterialPrefixes.INGOT, RagiumMaterialKeys.MEAT)
            .define('B', Tags.Items.BONES)
            .save(output)

        HTShapedRecipeBuilder
            .create(RagiumBlocks.COOKED_MEAT_BLOCK)
            .hollow8()
            .define('A', HCMaterialPrefixes.INGOT, RagiumMaterialKeys.COOKED_MEAT)
            .define('B', Tags.Items.BONES)
            .save(output)

        HTCookingRecipeBuilder.smeltingAndSmoking(RagiumBlocks.COOKED_MEAT_BLOCK) {
            addIngredient(RagiumBlocks.MEAT_BLOCK)
            setTime(20 * 30)
            setExp(0.7f)
            saveSuffixed(output, "_from_raw")
        }

        // Food Cans
        for ((canType: HTFoodCanType, item: ItemLike) in RagiumItems.FOOD_CANS) {
        }
    }
}
