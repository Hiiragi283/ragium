package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumPlantingRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        crops()
        trees()
    }

    @JvmStatic
    private fun crops() {
        // Beetroot
        HTItemWithFluidToChancedItemRecipeBuilder
            .planting(
                ingredientHelper.item(Tags.Items.SEEDS_BEETROOT),
                ingredientHelper.water(125),
            ).addResult(resultHelper.item(Items.BEETROOT, 3))
            .addResult(resultHelper.item(Items.BEETROOT_SEEDS))
            .save(output)
        // Carrot
        HTItemWithFluidToChancedItemRecipeBuilder
            .planting(
                ingredientHelper.item(Tags.Items.CROPS_CARROT),
                ingredientHelper.water(125),
            ).addResult(resultHelper.item(Items.CARROT, 3))
            .addResult(resultHelper.item(Items.CARROT), 1 / 3f)
            .save(output)
        // Melon
        HTItemWithFluidToChancedItemRecipeBuilder
            .planting(
                ingredientHelper.item(Tags.Items.SEEDS_MELON),
                ingredientHelper.water(125),
            ).addResult(resultHelper.item(Items.MELON))
            .save(output)
        // Potato
        HTItemWithFluidToChancedItemRecipeBuilder
            .planting(
                ingredientHelper.item(Tags.Items.CROPS_POTATO),
                ingredientHelper.water(125),
            ).addResult(resultHelper.item(Items.POTATO, 3))
            .addResult(resultHelper.item(Items.POTATO), 1 / 3f)
            .save(output)
        // Pumpkin
        HTItemWithFluidToChancedItemRecipeBuilder
            .planting(
                ingredientHelper.item(Tags.Items.SEEDS_PUMPKIN),
                ingredientHelper.water(125),
            ).addResult(resultHelper.item(Items.PUMPKIN))
            .save(output)
        // Sweet Berries
        HTItemWithFluidToChancedItemRecipeBuilder
            .planting(
                ingredientHelper.item(Items.SWEET_BERRIES),
                ingredientHelper.water(125),
            ).addResult(resultHelper.item(Items.SWEET_BERRIES, 3))
            .save(output)
        // Wheat
        HTItemWithFluidToChancedItemRecipeBuilder
            .planting(
                ingredientHelper.item(Tags.Items.SEEDS_WHEAT),
                ingredientHelper.water(125),
            ).addResult(resultHelper.item(Items.WHEAT, 2))
            .addResult(resultHelper.item(Items.WHEAT_SEEDS))
            .save(output)
    }

    @JvmStatic
    private fun trees() {
        tree(Items.OAK_SAPLING, Items.OAK_LOG)
        tree(Items.SPRUCE_SAPLING, Items.SPRUCE_LOG)
        tree(Items.BIRCH_SAPLING, Items.BIRCH_LOG)
        tree(Items.JUNGLE_SAPLING, Items.JUNGLE_LOG)
        tree(Items.ACACIA_SAPLING, Items.ACACIA_LOG)
        tree(Items.CHERRY_SAPLING, Items.CHERRY_LOG)
        tree(Items.DARK_OAK_SAPLING, Items.DARK_OAK_LOG)
        tree(Items.MANGROVE_PROPAGULE, Items.MANGROVE_LOG)

        tree(Items.CRIMSON_FUNGUS, Items.CRIMSON_STEM, ingredientHelper.lava(250))
        tree(Items.WARPED_FUNGUS, Items.WARPED_STEM, ingredientHelper.lava(250))
    }

    @JvmStatic
    private fun tree(sapling: ItemLike, log: ItemLike, fluid: HTFluidIngredient = ingredientHelper.water(250)) {
        HTItemWithFluidToChancedItemRecipeBuilder
            .planting(
                ingredientHelper.item(sapling),
                fluid,
            ).addResult(resultHelper.item(log, 6))
            .addResult(resultHelper.item(sapling), 1 / 6f)
            .save(output)
    }
}
