package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.common.data.recipe.builder.HTCookingRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
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
        // Meat Dust/Ingot -> Cooked Meat Ingot
        HTCookingRecipeBuilder.smeltingAndSmoking(RagiumItems.COOKED_MEAT_INGOT) {
            addIngredient(RagiumMaterialKeys.MEAT, HCMaterialPrefixes.INGOT, HCMaterialPrefixes.DUST)
            setExp(0.35f)
            saveSuffixed(output, "_from_raw")
        }

        // Food Cans
        mapOf(
            RagiumItems.FISH_CAN to Tags.Items.FOODS_COOKED_FISH,
            RagiumItems.FRUIT_CAN to Tags.Items.FOODS_FRUIT,
            RagiumItems.MEAT_CAN to Tags.Items.FOODS_COOKED_MEAT,
            RagiumItems.SOUP_CAN to Tags.Items.FOODS_SOUP,
        ).forEach { (can: ItemLike, food: TagKey<Item>) ->
            HTShapedRecipeBuilder
                .create(can, 8)
                .hollow8()
                .define('A', food)
                .define('B', HCMaterialPrefixes.PLATE, VanillaMaterialKeys.IRON)
                .save(output)
        }
    }
}
