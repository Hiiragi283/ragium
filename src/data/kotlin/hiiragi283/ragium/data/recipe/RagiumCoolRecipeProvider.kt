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
        // Water -> Ice
        HTFreezingRecipeBuilder.create(output) {
            ingredient = inputCreator.water()
            catalyst += HTBluePrintIngredient(0).toVanilla()
            result = resultCreator.create(Items.ICE)
        }
        // Ice -> Packed Ice
        HTFreezingRecipeBuilder.create(output) {
            ingredient = inputCreator.water(6000)
            catalyst += itemCreator.create(Items.PACKED_ICE)
            result = resultCreator.create(Items.PACKED_ICE)
            time *= 3
        }
        // Packed Ice -> Blue Ice
        HTFreezingRecipeBuilder.create(output) {
            ingredient = inputCreator.water(36000)
            catalyst += itemCreator.create(Items.BLUE_ICE)
            result = resultCreator.create(Items.BLUE_ICE)
            time *= 9
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
