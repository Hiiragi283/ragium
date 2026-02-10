package hiiragi283.ragium.common.datagen.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.resource.toId
import hiiragi283.ragium.common.data.recipe.HTItemToChancedRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTPressingRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items

object RagiumMiscRecipeProvider : HTRecipeProvider() {
    override fun buildRecipes() {
        cutting()
        printing()
    }

    @JvmStatic
    private fun cutting() {
        // Stick
        HTItemToChancedRecipeBuilder.cutting(output) {
            ingredient = inputCreator.create(ItemTags.WOODEN_SLABS)
            result = resultCreator.create(Items.STICK, 4)
            recipeId suffix "_from_wooden_slabs"
        }

        HTItemToChancedRecipeBuilder.cutting(output) {
            ingredient = inputCreator.create(ItemTags.SAPLINGS)
            result = resultCreator.create(Items.STICK)
            recipeId suffix "_from_saplings"
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
}
