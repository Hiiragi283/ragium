package hiiragi283.ragium.data.recipe

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTItemOrFluidRecipeBuilder
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

object RagiumMatterRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Liquid Ragi-Matter
        HTItemOrFluidRecipeBuilder.chemicalWashing(output) {
            ingredient += inputCreator.create(RagiumItems.RAGI_MATTER)
            ingredient += inputCreator.create(HiiragiCoreTags.Fluids.ELDRITCH, 250)
            result += resultCreator.create(RagiumFluids.RAGI_MATTER, 250)
            time *= 10
        }

        // 1x
        matterRecipe(Items.STONE, "A", count = 16)
        // 2x
        matterRecipe(Items.GRASS_BLOCK, "A", "A", count = 16)
        matterRecipe(Items.OAK_LOG, "A ", " A", count = 8)
        // 3x
        // 4x
        // 5x
        // 6x
        // 7x
        matterRecipe(CommonParts.DUST, CommonMaterialKeys.RUTHENIUM, "A  ", "AAA", "AAA")
        matterRecipe(CommonParts.DUST, CommonMaterialKeys.RHODIUM, " A ", "AAA", "AAA")
        matterRecipe(CommonParts.DUST, CommonMaterialKeys.PALLADIUM, "  A", "AAA", "AAA")

        matterRecipe(CommonParts.DUST, CommonMaterialKeys.OSMIUM, "AAA", "A  ", "AAA")
        matterRecipe(CommonParts.DUST, CommonMaterialKeys.IRIDIUM, "AAA", " A ", "AAA")
        matterRecipe(CommonParts.DUST, CommonMaterialKeys.PLATINUM, "AAA", "  A", "AAA")
        // 8x
        // 9x
        matterRecipe(CommonParts.GEM, VanillaMaterialKeys.DIAMOND, "AAA", "AAA", "AAA")
    }

    @JvmStatic
    private fun matterRecipe(
        part: HTPartLike,
        material: HTMaterialLike,
        vararg pattern: String,
        count: Int = 1,
    ) {
        val result: ItemLike = HiiragiCoreAccess.INSTANCE.getMaterialBlockOrItem(part, material) ?: return
        matterRecipe(result, *pattern, count = count)
    }

    @JvmStatic
    private fun matterRecipe(result: ItemLike, vararg pattern: String, count: Int = 1) {
        HTShapedRecipeBuilder.create(output) {
            pattern(*pattern)
            define('A') += RagiumItems.RAGI_MATTER
            resultStack += result to count
            recipeId suffix "_from_matter"
        }
    }
}
