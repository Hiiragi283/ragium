package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.data.recipe.HTItemToItemRecipeBuilder
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
import hiiragi283.ragium.common.recipe.special.HTBannerCopyingRecipe
import hiiragi283.ragium.common.recipe.special.HTBookCopyingRecipe
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items

object RagiumBasicRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        bending()
        compressing()
        crushing()
        cutting()
        printing()
    }

    //    Bending    //

    @JvmStatic
    private fun bending() {}

    //    Compressing    //

    @JvmStatic
    private fun compressing() {
        // Sculk Vein -> Sculk
        HTItemToItemRecipeBuilder.compressing(output) {
            ingredient = inputCreator.create(Items.SCULK_VEIN, 8)
            result = resultCreator.create(Items.SCULK)
        }

        // Sawdust -> Compressed
        HTItemToItemRecipeBuilder.compressing(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.WOOD, 8)
            result = resultCreator.create(HCItems.COMPRESSED_SAWDUST)
        }
    }

    //    Crushing    //

    @JvmStatic
    private fun crushing() {
    }

    //    Cutting    //

    @JvmStatic
    private fun cutting() {
        // Sapling -> Stick
        RagiumRecipeBuilder.cutting(output) {
            ingredient = inputCreator.create(ItemTags.SAPLINGS)
            result = resultCreator.create(Items.STICK)
            recipeId suffix "_from_saplings"
        }
        // Slab -> Stick
        RagiumRecipeBuilder.cutting(output) {
            ingredient = inputCreator.create(ItemTags.WOODEN_SLABS)
            result = resultCreator.create(Items.STICK, 2)
            recipeId suffix "_from_wooden_slabs"
        }

        // Book -> Paper + Leather
        RagiumRecipeBuilder.cutting(output) {
            ingredient = inputCreator.create(Items.BOOK)
            result = resultCreator.create(Items.PAPER, 3)
            extraResult += resultCreator.create(Items.LEATHER)
            recipeId suffix "_from_book"
        }
    }

    //    Printing    //

    private fun printing() {
        for (color: HTDefaultColor in HTDefaultColor.entries) {
            save(id(RagiumConst.PRINTING, "banner_copying", color.serializedName), HTBannerCopyingRecipe(color))
        }

        save(id(RagiumConst.PRINTING, "book_copying"), HTBookCopyingRecipe)
    }

    /*private fun printing() {
        // XX Banner
        for (color: HTDefaultColor in HTDefaultColor.entries) {
            val banner = HTSimpleDeferredItem(HTConst.MINECRAFT.toId("${color.serializedName}_banner"))
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
    }*/
}
