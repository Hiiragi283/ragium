package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTCombiningRecipeBuilder
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
import hiiragi283.ragium.common.data.recipe.blueprint
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items

object RagiumBasicRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        assembling()
        crushing()
        cutting()
        pressing()
    }

    //    Compressing    //

    @JvmStatic
    private fun assembling() {
        // Sculk Vein -> Sculk
        HTCombiningRecipeBuilder.assembling(output) {
            result = resultCreator.create(Items.SCULK)
            ingredients += inputCreator.create(Items.SCULK_VEIN, 8)
            ingredients += inputCreator.blueprint(0)
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
        HTCombiningRecipeBuilder.assembling(output) {
            result = resultCreator.create(HCItems.PARTICLE_BOARD)
            ingredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.WOOD, 2)
            ingredients += inputCreator.blueprint(5)
        }
    }

    //    Printing    //

    /*private fun printing() {
        // Banner
        for ((_, banner: HTSimpleItemHolderLike) in ColoredMaterials.BANNER) {
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
                Items.FILLED_MAP.toLike(),
                HTPrintingRecipe.CopyStrategy.ORIGIN,
            ),
        )
        // Blank Disc -> Disc
        save(
            id(RagiumConst.PRINTING, "disc"),
            HTPrintingRecipe(
                inputCreator.create(Tags.Items.MUSIC_DISCS),
                RagiumItems.BLANK_DISC,
                HTPrintingRecipe.CopyStrategy.INPUT,
            ),
        )

        // Writable Book -> Written Book
        save(id(RagiumConst.PRINTING, "book_cloning"), HTBookCloningRecipe)
    }*/
}
