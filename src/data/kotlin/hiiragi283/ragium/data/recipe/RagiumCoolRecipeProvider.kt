package hiiragi283.ragium.data.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.common.recipe.ingredient.HTBluePrintIngredient
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTFreezingRecipeBuilder
import net.minecraft.world.item.Items

object RagiumCoolRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        freezing()
    }

    @JvmStatic
    private fun freezing() {
        // Water -> Snowball
        HTFreezingRecipeBuilder.create(output) {
            ingredient = inputCreator.water()
            catalyst += HTBluePrintIngredient(0).toVanilla()
            result = resultCreator.create(Items.SNOWBALL, 4)
            time /= 4
        }
        // Water -> Ice
        HTFreezingRecipeBuilder.create(output) {
            ingredient = inputCreator.water()
            catalyst += HTBluePrintIngredient(1).toVanilla()
            result = resultCreator.create(Items.ICE)
        }

        // Lava -> Obsidian
        HTFreezingRecipeBuilder.create(output) {
            ingredient = inputCreator.lava()
            catalyst += HTBluePrintIngredient(0).toVanilla()
            result = resultCreator.create(Items.OBSIDIAN)
        }
        // Lava -> Magma Block
        HTFreezingRecipeBuilder.create(output) {
            ingredient = inputCreator.lava(250)
            catalyst += HTBluePrintIngredient(1).toVanilla()
            result = resultCreator.create(Items.MAGMA_BLOCK)
        }

        // Honey -> Honey Block
        HTFreezingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(HCFluids.HONEY)
            catalyst += HTBluePrintIngredient(0).toVanilla()
            result = resultCreator.create(Items.HONEY_BLOCK)
        }
    }
}
