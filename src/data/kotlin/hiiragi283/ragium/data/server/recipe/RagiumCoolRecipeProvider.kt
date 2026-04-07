package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.common.data.recipe.blueprint
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
            itemIngredient = inputCreator.blueprint(0)
            fluidIngredient = inputCreator.water()
            result = resultCreator.create(Items.ICE)
        }
        // Ice -> Packed Ice
        HTFreezingRecipeBuilder.create(output) {
            itemIngredient = inputCreator.create(Items.ICE, 6)
            fluidIngredient = inputCreator.water()
            result = resultCreator.create(Items.PACKED_ICE)
        }
        // Packed Ice -> Blue Ice
        HTFreezingRecipeBuilder.create(output) {
            itemIngredient = inputCreator.create(Items.PACKED_ICE, 6)
            fluidIngredient = inputCreator.water()
            result = resultCreator.create(Items.BLUE_ICE)
        }

        // Lava -> Obsidian
        HTFreezingRecipeBuilder.create(output) {
            itemIngredient = inputCreator.blueprint(0)
            fluidIngredient = inputCreator.lava()
            result = resultCreator.create(Items.OBSIDIAN)
        }
        // Lava -> Magma Block
        HTFreezingRecipeBuilder.create(output) {
            itemIngredient = inputCreator.blueprint(1)
            fluidIngredient = inputCreator.lava(250)
            result = resultCreator.create(Items.MAGMA_BLOCK)
        }

        // Honey -> Honey Block
        HTFreezingRecipeBuilder.create(output) {
            itemIngredient = inputCreator.blueprint(0)
            fluidIngredient = inputCreator.create(HCFluids.HONEY)
            result = resultCreator.create(Items.HONEY_BLOCK)
        }
    }
}
