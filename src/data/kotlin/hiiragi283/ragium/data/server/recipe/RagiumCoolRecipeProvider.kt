package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTItemOrFluidRecipeBuilder
import net.minecraft.world.item.Items

object RagiumCoolRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        freezing()
    }

    @JvmStatic
    private fun freezing() {
        // Water -> Ice
        HTItemOrFluidRecipeBuilder.freezing(output) {
            ingredient += inputCreator.water(1000)
            result += resultCreator.create(Items.ICE)
        }
        // Ice -> Packed Ice
        HTItemOrFluidRecipeBuilder.freezing(output) {
            ingredient += inputCreator.create(Items.ICE, 6)
            result += resultCreator.create(Items.PACKED_ICE)
        }
        // Packed Ice -> Blue Ice
        HTItemOrFluidRecipeBuilder.freezing(output) {
            ingredient += inputCreator.create(Items.PACKED_ICE, 6)
            result += resultCreator.create(Items.BLUE_ICE)
        }

        // Lava -> Obsidian
        HTItemOrFluidRecipeBuilder.freezing(output) {
            ingredient += inputCreator.lava(1000)
            result += resultCreator.create(Items.OBSIDIAN)
        }

        // Honey -> Honey Block
        HTItemOrFluidRecipeBuilder.freezing(output) {
            ingredient += inputCreator.create(HCFluids.HONEY)
            result += resultCreator.create(Items.HONEY_BLOCK)
        }
    }
}
