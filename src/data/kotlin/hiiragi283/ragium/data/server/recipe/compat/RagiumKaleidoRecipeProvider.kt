package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.material.HTMaterialRecipeData
import hiiragi283.ragium.common.integration.RagiumKaleidoCookeryAddon
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.impl.data.recipe.HTChoppingBoardRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.material.FoodMaterialRecipeData
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
            .equipment(RagiumKaleidoCookeryAddon.getKnife(RagiumMaterialKeys.RAGI_ALLOY))
            .pattern(
                "AA",
                "AB",
            ).define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
            .define('B', Tags.Items.RODS_WOODEN)
            .save(output)

        createComponentUpgrade(
            HTComponentTier.ELITE,
            RagiumKaleidoCookeryAddon.getKnife(RagiumMaterialKeys.RAGI_CRYSTAL),
            RagiumKaleidoCookeryAddon.getKnife(RagiumMaterialKeys.RAGI_ALLOY),
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
    private fun choppingFromData(data: HTMaterialRecipeData) {
        val output: HTMaterialRecipeData.OutputEntry = data.outputs[0]
        HTChoppingBoardRecipeBuilder(output.toImmutable())
            .addIngredient(data.getIngredient(0))
            .setCutCount(output.count - 1)
            .save(this.output)
    }
}
