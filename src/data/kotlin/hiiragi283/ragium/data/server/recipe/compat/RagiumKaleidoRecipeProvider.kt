package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.common.integration.food.RagiumFoodAddon
import hiiragi283.ragium.common.integration.food.RagiumKaleidoCookeryAddon
import hiiragi283.ragium.common.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.impl.data.recipe.HTCookingBoardRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.neoforged.neoforge.common.Tags

object RagiumKaleidoRecipeProvider : HTRecipeProvider.Integration(RagiumConst.KALEIDO_COOKERY) {
    override fun buildRecipeInternal() {
        knife()
        cherry()
        cake()
    }

    @JvmStatic
    private fun knife() {
        HTShapedRecipeBuilder
            .equipment(RagiumKaleidoCookeryAddon.getKnife(RagiumMaterialType.RAGI_ALLOY))
            .pattern(
                "AA",
                "AB",
            ).define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.RAGI_ALLOY)
            .define('B', Tags.Items.RODS_WOODEN)
            .save(output)

        createComponentUpgrade(
            HTComponentTier.ELITE,
            RagiumKaleidoCookeryAddon.getKnife(RagiumMaterialType.RAGI_CRYSTAL),
            RagiumKaleidoCookeryAddon.getKnife(RagiumMaterialType.RAGI_ALLOY),
        ).addIngredient(HTItemMaterialVariant.GEM, RagiumMaterialType.RAGI_CRYSTAL)
            .save(output)
    }

    @JvmStatic
    private fun cherry() {
        HTCookingBoardRecipeBuilder
            .chopping(RagiumFoodAddon.RAGI_CHERRY_PULP, 2)
            .addIngredient(RagiumCommonTags.Items.FOODS_RAGI_CHERRY)
            .save(output)
    }

    @JvmStatic
    private fun cake() {
        HTCookingBoardRecipeBuilder
            .chopping(RagiumItems.SWEET_BERRIES_CAKE_SLICE, 7)
            .addIngredient(RagiumBlocks.SWEET_BERRIES_CAKE)
            .save(output)
    }
}
