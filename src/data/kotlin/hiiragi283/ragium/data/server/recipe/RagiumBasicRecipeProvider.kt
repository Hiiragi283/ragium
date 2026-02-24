package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.fraction
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.data.recipe.HTItemToChancedRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemToItemRecipeBuilder
import hiiragi283.ragium.common.recipe.special.HTBannerCopyingRecipe
import hiiragi283.ragium.common.recipe.special.HTBookCopyingRecipe
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

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
        // Netherite Scrap
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.ORES_NETHERITE_SCRAP)
            result = resultCreator.material(CommonTagPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, 2)
            recipeId suffix "_from_ore"
        }

        // Beetroot -> Sugar + Molasses
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.CROPS_BEETROOT)
            result = resultCreator.create(Items.SUGAR, 2)
            extraResult += resultCreator.create(RagiumItems.MOLASSES)
            recipeId suffix "_from_beetroot"
        }
        // Sugar Cane -> Sugar + Molasses
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.CROPS_SUGAR_CANE)
            result = resultCreator.create(Items.SUGAR, 4)
            extraResult += resultCreator.create(RagiumItems.MOLASSES)
            recipeId suffix "_from_cane"
        }

        // Ice -> Snowball
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Items.ICE)
            result = resultCreator.create(Items.SNOWBALL, 4)
        }

        crushStones()
        crushWoods()
    }

    @JvmStatic
    private fun crushStones() {
        // Stone -> Cobblestone
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Items.STONE)
            result = resultCreator.create(Items.COBBLESTONE)
            recipeId suffix "_from_stone"
        }
        // Cobblestone -> Gravel
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(listOf(Tags.Items.COBBLESTONES_NORMAL, Tags.Items.COBBLESTONES_MOSSY))
            result = resultCreator.create(Items.GRAVEL)
            recipeId suffix "_from_cobblestone"
        }
        // Gravel -> Sand
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.GRAVELS)
            result = resultCreator.create(Items.SAND)
            recipeId suffix "_from_gravel"
        }
        // Sandstone -> Sand + Saltpeter
        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS)
            result = resultCreator.create(Items.SAND, 2)
            extraResult += resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.SALTPETER) to fraction(1, 4)
            recipeId suffix "_from_sandstone"
        }

        HTItemToChancedRecipeBuilder.crushing(output) {
            ingredient = inputCreator.create(Tags.Items.SANDSTONE_RED_BLOCKS)
            result = resultCreator.create(Items.RED_SAND, 2)
            extraResult += resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.SALTPETER) to fraction(1, 4)
            recipeId suffix "_from_sandstone"
        }
    }

    @JvmStatic
    private fun crushWoods() {
        // Wood Dust
        fun wood(tagKey: TagKey<Item>, input: Int, output: Int) {
            HTItemToChancedRecipeBuilder.crushing(this.output) {
                ingredient = inputCreator.create(tagKey, input)
                result = resultCreator.material(CommonTagPrefixes.DUST, VanillaMaterialKeys.WOOD, output)
                recipeId suffix "_from_${tagKey.location().path}"
            }
        }

        wood(ItemTags.BOATS, 1, 5)
        wood(ItemTags.LOGS_THAT_BURN, 1, 6)
        wood(ItemTags.WOODEN_BUTTONS, 1, 1)
        wood(ItemTags.WOODEN_DOORS, 1, 2)
        wood(ItemTags.WOODEN_PRESSURE_PLATES, 1, 2)
        wood(ItemTags.WOODEN_SLABS, 2, 1)
        wood(ItemTags.WOODEN_STAIRS, 4, 3)
        wood(ItemTags.WOODEN_TRAPDOORS, 1, 3)
        wood(Tags.Items.BARRELS_WOODEN, 1, 7)
        wood(Tags.Items.CHESTS_WOODEN, 1, 8)
        wood(Tags.Items.FENCE_GATES_WOODEN, 1, 4)
        wood(Tags.Items.FENCES_WOODEN, 1, 5)
        wood(Tags.Items.RODS_WOODEN, 2, 1)
    }

    //    Cutting    //

    @JvmStatic
    private fun cutting() {
        // Sapling -> Stick
        HTItemToChancedRecipeBuilder.cutting(output) {
            ingredient = inputCreator.create(ItemTags.SAPLINGS)
            result = resultCreator.create(Items.STICK)
            recipeId suffix "_from_saplings"
        }
        // Slab -> Stick
        HTItemToChancedRecipeBuilder.cutting(output) {
            ingredient = inputCreator.create(ItemTags.WOODEN_SLABS)
            result = resultCreator.create(Items.STICK, 2)
            recipeId suffix "_from_wooden_slabs"
        }

        // Book -> Paper + Leather
        HTItemToChancedRecipeBuilder.cutting(output) {
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
