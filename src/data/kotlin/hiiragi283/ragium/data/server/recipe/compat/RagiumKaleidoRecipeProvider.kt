package hiiragi283.ragium.data.server.recipe.compat

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeData
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.stack.toImmutableOrThrow
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.impl.data.recipe.HTChoppingBoardRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.material.FoodMaterialRecipeData
import hiiragi283.ragium.setup.RagiumIntegrationItems
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.neoforged.neoforge.common.Tags

object RagiumKaleidoRecipeProvider : HTRecipeProvider.Integration(RagiumConst.KALEIDO_COOKERY) {
    override fun buildRecipeInternal() {
        // Crops
        cropAndSeed(ModItems.LETTUCE_SEED, ModItems.LETTUCE)
        cropAndSeed(ModItems.TOMATO_SEED, ModItems.TOMATO)

        HTItemWithFluidToChancedItemRecipeBuilder
            .planting(
                itemCreator.fromItem(ModItems.CHILI_SEED),
                fluidCreator.water(125),
            ).addResult(resultHelper.item(ModItems.RED_CHILI, 2))
            .addResult(resultHelper.item(ModItems.RED_CHILI), 1 / 2f)
            .addResult(resultHelper.item(ModItems.GREEN_CHILI), 1 / 4f)
            .save(output)

        HTItemWithFluidToChancedItemRecipeBuilder
            .planting(
                itemCreator.fromItem(ModItems.WILD_RICE_SEED),
                fluidCreator.water(500),
            ).addResult(resultHelper.item(ModItems.RICE_PANICLE, 4))
            .addResult(resultHelper.item(ModItems.RICE_PANICLE), 1 / 2f)
            .addResult(resultHelper.item(ModItems.RICE_PANICLE), 1 / 4f)
            .save(output)

        knife()
        cherry()
        cake()
    }

    @JvmStatic
    private fun knife() {
        HTShapedRecipeBuilder
            .create(RagiumIntegrationItems.getKitchenKnife(RagiumMaterialKeys.RAGI_ALLOY))
            .pattern(
                "AA",
                "AB",
            ).define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
            .define('B', Tags.Items.RODS_WOODEN)
            .setCategory(CraftingBookCategory.EQUIPMENT)
            .save(output)

        createComponentUpgrade(
            HTComponentTier.ELITE,
            RagiumIntegrationItems.getKitchenKnife(RagiumMaterialKeys.RAGI_CRYSTAL),
            RagiumIntegrationItems.getKitchenKnife(RagiumMaterialKeys.RAGI_ALLOY),
        ).addIngredient(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
            .save(output)
    }

    @JvmStatic
    private fun cherry() {
        choppingFromData(FoodMaterialRecipeData.RAGI_CHERRY_PULP)
    }

    @JvmStatic
    private fun cake() {
        choppingFromData(FoodMaterialRecipeData.SWEET_BERRIES_CAKE_SLICE)
    }

    @JvmStatic
    private fun choppingFromData(data: HTRecipeData) {
        val (output: ItemStack) = data.getItemStacks()[0]
        HTChoppingBoardRecipeBuilder(output.toImmutableOrThrow())
            .addIngredient(data.getIngredients()[0])
            .setCutCount(output.count - 1)
            .saveModified(this.output, data.operator)
    }
}
