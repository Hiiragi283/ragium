package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.resource.toId
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTItemToChancedRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTPressingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTSingleRecipeBuilder
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items

object RagiumBasicRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        bending()
        cutting()
        lathing()
        printing()
        squeezing()
    }

    @JvmStatic
    private fun bending() {}

    @JvmStatic
    private fun cutting() {
        // Stick
        HTItemToChancedRecipeBuilder.cutting(output) {
            ingredient = inputCreator.create(ItemTags.SAPLINGS)
            result = resultCreator.create(Items.STICK)
            recipeId suffix "_from_saplings"
        }
    }

    @JvmStatic
    private fun lathing() {
        // Stick
        HTSingleRecipeBuilder.lathing(output) {
            ingredient = inputCreator.create(ItemTags.WOODEN_SLABS)
            result = resultCreator.create(Items.STICK, 4)
            recipeId suffix "_from_wooden_slabs"
        }
    }

    @JvmStatic
    private fun printing() {
        // XX Banner
        for (color: HTDefaultColor in HTDefaultColor.entries) {
            val banner: HTItemHolderLike<*> = HTItemHolderLike.of(HTConst.MINECRAFT.toId("${color.serializedName}_banner"))
            HTPressingRecipeBuilder.printing(output) {
                top = inputCreator.create(banner)
                bottom = inputCreator.create(banner)
                result = resultCreator.create(banner)
            }
        }

        // Book -> Written Book
        HTPressingRecipeBuilder.printing(output) {
            top = inputCreator.create(Items.BOOK)
            bottom = inputCreator.create(Items.WRITTEN_BOOK)
            result = resultCreator.create(Items.WRITTEN_BOOK)
        }
        // Map -> Filled Map
        HTPressingRecipeBuilder.printing(output) {
            top = inputCreator.create(Items.MAP)
            bottom = inputCreator.create(Items.FILLED_MAP)
            result = resultCreator.create(Items.FILLED_MAP)
        }
    }

    @JvmStatic
    private fun squeezing() {
        // Jungle -> Latex
        HTSingleRecipeBuilder.squeezing(output) {
            ingredient = inputCreator.create(ItemTags.JUNGLE_LOGS)
            result = resultCreator.create(HCFluids.LATEX, 250)
            recipeId suffix "_from_jungle"
        }
        // Acacia -> Latex
        HTSingleRecipeBuilder.squeezing(output) {
            ingredient = inputCreator.create(ItemTags.ACACIA_LOGS)
            result = resultCreator.create(HCFluids.LATEX, 125)
            recipeId suffix "_from_acacia"
        }

        // Soul Sand/Soil -> Crude Oil
        HTSingleRecipeBuilder.squeezing(output) {
            ingredient = inputCreator.create(ItemTags.SOUL_FIRE_BASE_BLOCKS)
            result = resultCreator.create(RagiumFluids.CRUDE_OIL, 500)
            recipeId suffix "_from_soul"
        }
    }
}
