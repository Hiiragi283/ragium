package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.data.recipe.HTItemAndItemRecipeBuilder
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
import hiiragi283.ragium.common.item.HTMoldType
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
        pressing()
        printing()
    }

    //    Bending    //

    @JvmStatic
    private fun bending() {}

    //    Compressing    //

    @JvmStatic
    private fun compressing() {
        // Sculk Vein -> Sculk
        RagiumRecipeBuilder.compressing(output) {
            ingredient = inputCreator.create(Items.SCULK_VEIN, 8)
            result = resultCreator.create(Items.SCULK)
        }

        // Sawdust -> Compressed
        RagiumRecipeBuilder.compressing(output) {
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

    //    Pressing    //

    @JvmStatic
    private fun pressing() {
        // Sawdust -> Particle Board
        HTItemAndItemRecipeBuilder.pressing(output) {
            first = inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.WOOD, 2)
            second = inputCreator.create(HTMoldType.PLATE)
            result = resultCreator.create(HCItems.PARTICLE_BOARD)
        }
    }

    //    Printing    //

    @JvmStatic
    private fun printing() {
        for (color: HTDefaultColor in HTDefaultColor.entries) {
            save(id(RagiumConst.PRINTING, "banner_copying", color.serializedName), HTBannerCopyingRecipe(color))
        }

        save(id(RagiumConst.PRINTING, "book_copying"), HTBookCopyingRecipe)
    }
}
