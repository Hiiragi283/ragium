package hiiragi283.ragium.data.recipe

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.data.recipe.HTShapedRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

class RagiumMatterRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun buildRecipes() {
        // Liquid Ragi-Matter
        HTMixingRecipeBuilder.create {
            itemIngredient { +RagiumItems.RAGI_MATTER }
            fluidIngredient {
                +HiiragiCoreTags.Fluids.ELDRITCH
                amount = 250
            }
            fluidResult {
                +RagiumFluids.RAGI_MATTER
                amount = 250
            }
            time *= 10
        }.save(exporter)

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

    private fun matterRecipe(part: HTPartLike, material: HTMaterialLike, vararg pattern: String, count: Int = 1) {
        val result: ItemLike = HiiragiCoreAccess.INSTANCE.getMaterialBlockOrItem(part, material) ?: return
        matterRecipe(result, *pattern, count = count)
    }

    private fun matterRecipe(result: ItemLike, vararg pattern: String, count: Int = 1) {
        HTShapedRecipeBuilder.create {
            pattern.forEach { +it }
            define('A') { +RagiumItems.RAGI_MATTER }
            +ItemStack(result, count)
            recipeId suffix "_from_matter"
        }.save(exporter)
    }

    override fun getName(): String = "Matter Recipes"
}
