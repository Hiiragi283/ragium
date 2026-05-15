package hiiragi283.ragium.data.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTCombiningRecipeBuilder
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumBasicRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        assembling()
        compressing()
        crushing()
        cutting()
    }

    //    Assembling    //

    @JvmStatic
    private fun assembling() {
        // Blackstone + Gold -> Gilded Blackstone
        HTCombiningRecipeBuilder.assembling(output) {
            result = resultCreator.create(Items.GILDED_BLACKSTONE)
            ingredients += inputCreator.create(Items.BLACKSTONE)
            ingredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.GOLD, 8)
        }
        // Dirt + Leaves -> Podzol
        HTCombiningRecipeBuilder.assembling(output) {
            result = resultCreator.create(Items.PODZOL)
            ingredients += inputCreator.create(Items.DIRT)
            ingredients += inputCreator.create(ItemTags.LEAVES, 8)
        }
        // Dirt + Mushroom -> Mycelium
        HTCombiningRecipeBuilder.assembling(output) {
            result = resultCreator.create(Items.MYCELIUM)
            ingredients += inputCreator.create(Items.DIRT)
            ingredients += inputCreator.create(Tags.Items.MUSHROOMS)
        }
        // Crimson Nylium
        HTCombiningRecipeBuilder.assembling(output) {
            result = resultCreator.create(Items.CRIMSON_NYLIUM)
            ingredients += inputCreator.create(Tags.Items.NETHERRACKS)
            ingredients += inputCreator.create(Items.CRIMSON_FUNGUS)
        }
        // Warped Nylium
        HTCombiningRecipeBuilder.assembling(output) {
            result = resultCreator.create(Items.WARPED_NYLIUM)
            ingredients += inputCreator.create(Tags.Items.NETHERRACKS)
            ingredients += inputCreator.create(Items.WARPED_FUNGUS)
        }
        // String + Sticky -> Cobweb
        HTCombiningRecipeBuilder.assembling(output) {
            result = resultCreator.create(Items.COBWEB)
            ingredients += inputCreator.create(Tags.Items.STRINGS, 5)
            ingredients += inputCreator.create(HiiragiCoreTags.Items.STICKY_BALLS)
        }

        // Iron Ingot + Iron Nugget -> Chain
        HTCombiningRecipeBuilder.assembling(output) {
            result = resultCreator.create(Items.CHAIN, 3)
            ingredients += inputCreator.create(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON)
            ingredients += inputCreator.create(CommonTagPrefixes.NUGGET, VanillaMaterialKeys.IRON, 3)
        }
        // Iron Ingot + Torch -> Lantern
        HTCombiningRecipeBuilder.assembling(output) {
            result = resultCreator.create(Items.LANTERN, 2)
            ingredients += inputCreator.create(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON)
            ingredients += inputCreator.create(Items.TORCH)
        }
        HTCombiningRecipeBuilder.assembling(output) {
            result = resultCreator.create(Items.SOUL_LANTERN, 2)
            ingredients += inputCreator.create(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON)
            ingredients += inputCreator.create(Items.SOUL_TORCH)
        }
        // Iron Ingot + Chest -> Hopper
        HTCombiningRecipeBuilder.assembling(output) {
            result = resultCreator.create(Items.HOPPER)
            ingredients += inputCreator.create(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON, 5)
            ingredients += inputCreator.create(Tags.Items.CHESTS_WOODEN)
        }
        // Dropper + Bow -> Dispenser
        HTCombiningRecipeBuilder.assembling(output) {
            result = resultCreator.create(Items.DISPENSER)
            ingredients += inputCreator.create(Items.DROPPER)
            ingredients += inputCreator.create(Tags.Items.TOOLS_BOW)
        }
        // TNT
        HTCombiningRecipeBuilder.assembling(output) {
            result = resultCreator.create(Items.TNT, 2)
            ingredients += inputCreator.create(Tags.Items.SANDS, 4)
            ingredients += inputCreator.create(Tags.Items.GUNPOWDERS, 5)
        }

        // Leather + Iron Nugget -> Saddle
        HTCombiningRecipeBuilder.assembling(output) {
            result = resultCreator.create(Items.SADDLE)
            ingredients += inputCreator.create(Tags.Items.LEATHERS, 5)
            ingredients += inputCreator.create(CommonTagPrefixes.NUGGET, VanillaMaterialKeys.IRON, 2)
        }
        // Head
        HTCombiningRecipeBuilder.assembling(output) {
            result = resultCreator.create(Items.ZOMBIE_HEAD)
            ingredients += inputCreator.create(Items.SKELETON_SKULL)
            ingredients += inputCreator.create(Items.ROTTEN_FLESH, 8)
        }
        HTCombiningRecipeBuilder.assembling(output) {
            result = resultCreator.create(Items.CREEPER_HEAD)
            ingredients += inputCreator.create(Items.SKELETON_SKULL)
            ingredients += inputCreator.create(Tags.Items.GUNPOWDERS, 8)
        }
        HTCombiningRecipeBuilder.assembling(output) {
            result = resultCreator.create(Items.PIGLIN_HEAD)
            ingredients += inputCreator.create(Items.SKELETON_SKULL)
            ingredients += inputCreator.create(Items.PORKCHOP, 8)
        }
    }

    //    Compressing    //

    @JvmStatic
    private fun compressing() {
        // Snow Block -> Ice -> Packed Ice -> Blue Ice
        RagiumRecipeBuilder.compressing(output) {
            ingredient = inputCreator.create(Items.SNOW_BLOCK, 4)
            result = resultCreator.create(Items.ICE)
        }
        RagiumRecipeBuilder.compressing(output) {
            ingredient = inputCreator.create(Items.ICE, 6)
            result = resultCreator.create(Items.PACKED_ICE)
        }
        RagiumRecipeBuilder.compressing(output) {
            ingredient = inputCreator.create(Items.PACKED_ICE, 6)
            result = resultCreator.create(Items.BLUE_ICE)
        }

        // Snow -> Snow Block
        RagiumRecipeBuilder.compressing(output) {
            ingredient = inputCreator.create(Items.SNOW, 8)
            result = resultCreator.create(Items.SNOW_BLOCK)
        }
        // Moss Carpet -> Moss
        RagiumRecipeBuilder.compressing(output) {
            ingredient = inputCreator.create(Items.MOSS_CARPET, 8)
            result = resultCreator.create(Items.MOSS_BLOCK)
        }
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
            results += resultCreator.create(Items.STICK)
            recipeId suffix "_from_saplings"
        }
        // Slab -> Stick
        RagiumRecipeBuilder.cutting(output) {
            ingredient = inputCreator.create(ItemTags.WOODEN_SLABS)
            results += resultCreator.create(Items.STICK, 2)
            recipeId suffix "_from_wooden_slabs"
        }

        // Book -> Paper + Leather
        RagiumRecipeBuilder.cutting(output) {
            ingredient = inputCreator.create(Items.BOOK)
            results += resultCreator.create(Items.PAPER, 3)
            results += resultCreator.create(Items.LEATHER)
            recipeId suffix "_from_book"
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
