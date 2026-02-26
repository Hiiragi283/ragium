package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.ColoredMaterials
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.data.recipe.HTItemAndItemRecipeBuilder
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
import hiiragi283.ragium.common.data.recipe.blueprint
import hiiragi283.ragium.common.recipe.special.HTBookCloningRecipe
import hiiragi283.ragium.common.recipe.special.HTPrintingRecipe
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

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
            second = inputCreator.blueprint(5)
            result = resultCreator.create(HCItems.PARTICLE_BOARD)
        }
    }

    //    Printing    //

    @JvmStatic
    private fun printing() {
        // Banner
        for ((_, banner: HTItemHolderLike<*>) in ColoredMaterials.BANNER) {
            save(
                banner.getId().withPrefix("${RagiumConst.PRINTING}/"),
                HTPrintingRecipe(
                    inputCreator.create(banner),
                    banner,
                    HTPrintingRecipe.CopyStrategy.ORIGIN,
                ),
            )
        }
        // Map -> Filled Map
        save(
            id(RagiumConst.PRINTING, "map"),
            HTPrintingRecipe(
                inputCreator.create(Items.MAP),
                HTItemHolderLike.of(Items.FILLED_MAP),
                HTPrintingRecipe.CopyStrategy.ORIGIN,
            ),
        )
        // Blank Disc -> Disc
        save(
            id(RagiumConst.PRINTING, "disc"),
            HTPrintingRecipe(
                inputCreator.create(Tags.Items.MUSIC_DISCS),
                HTItemHolderLike.of(RagiumItems.BLANK_DISC),
                HTPrintingRecipe.CopyStrategy.INPUT,
            ),
        )

        // Writable Book -> Written Book
        save(id(RagiumConst.PRINTING, "book_cloning"), HTBookCloningRecipe)
    }
}
