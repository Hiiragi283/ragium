package hiiragi283.ragium.data.server.recipe.compat

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeData
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.api.stack.toImmutableOrThrow
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.impl.data.recipe.HTChoppingBoardRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTPlantingRecipeBuilder
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

        HTPlantingRecipeBuilder
            .create(
                ModItems.CHILI_SEED.toHolderLike(),
                itemCreator.fromTagKey(RagiumModTags.Items.SOILS_DIRT),
                fluidCreator.water(125),
                resultHelper.item(ModItems.RED_CHILI, 3),
            ).save(output)

        HTPlantingRecipeBuilder
            .create(
                ModItems.WILD_RICE_SEED.toHolderLike(),
                itemCreator.fromTagKey(RagiumModTags.Items.SOILS_DIRT),
                fluidCreator.water(500),
                resultHelper.item(ModItems.RICE_PANICLE, 6),
            ).save(output)

        // Knives
        mapOf(
            RagiumMaterialKeys.RAGI_ALLOY to CommonMaterialPrefixes.INGOT,
            RagiumMaterialKeys.RAGI_CRYSTAL to CommonMaterialPrefixes.GEM,
        ).forEach { (key: HTMaterialKey, prefix: HTPrefixLike) ->
            HTShapedRecipeBuilder
                .create(RagiumIntegrationItems.getKitchenKnife(key))
                .pattern(
                    "AA",
                    "AB",
                ).define('A', prefix, key)
                .define('B', Tags.Items.RODS_WOODEN)
                .setCategory(CraftingBookCategory.EQUIPMENT)
                .save(output)
        }

        cherry()
        cake()
    }

    @JvmStatic
    private fun cherry() {
        choppingFromData(FoodMaterialRecipeData.RAGI_CHERRY_PULP)
        choppingFromData(FoodMaterialRecipeData.RAGI_CHERRY_PIE)
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
