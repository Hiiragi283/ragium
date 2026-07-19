package hiiragi283.ragium.data.recipe

import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTCombiningRecipeBuilder
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

class RagiumBasicRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun buildRecipes() {
        assembling()
        compressing()
        crushing()
        cutting()
    }

    //    Assembling    //

    private fun assembling() {
        // Blackstone + Gold -> Gilded Blackstone
        HTCombiningRecipeBuilder.assembling {
            result { +Items.GILDED_BLACKSTONE }
            ingredient { +Items.BLACKSTONE }
            ingredient {
                +tag(CommonTagPrefixes.DUST, VanillaMaterialKeys.GOLD)
                count = 8
            }
        }.save(exporter)
        // Dirt + Leaves -> Podzol
        HTCombiningRecipeBuilder.assembling {
            result { +Items.PODZOL }
            ingredient { +Items.DIRT }
            ingredient {
                +ItemTags.LEAVES
                count = 8
            }
        }.save(exporter)
        // Dirt + Mushroom -> Mycelium
        HTCombiningRecipeBuilder.assembling {
            result { +Items.MYCELIUM }
            ingredient { +Items.DIRT }
            ingredient { +Tags.Items.MUSHROOMS }
        }.save(exporter)
        // Crimson Nylium
        HTCombiningRecipeBuilder.assembling {
            result { +Items.CRIMSON_NYLIUM }
            ingredient { +Tags.Items.NETHERRACKS }
            ingredient { +Items.CRIMSON_FUNGUS }
        }.save(exporter)
        // Warped Nylium
        HTCombiningRecipeBuilder.assembling {
            result { +Items.WARPED_NYLIUM }
            ingredient { +Tags.Items.NETHERRACKS }
            ingredient { +Items.WARPED_FUNGUS }
        }.save(exporter)
        // String + Sticky -> Cobweb
        HTCombiningRecipeBuilder.assembling {
            result { +Items.COBWEB }
            ingredient {
                +Tags.Items.STRINGS
                count = 5
            }
            ingredient { +HiiragiCoreTags.Items.STICKY_BALLS }
        }.save(exporter)

        // Iron Ingot + Iron Nugget -> Chain
        HTCombiningRecipeBuilder.assembling {
            result {
                +Items.CHAIN
                count = 3
            }
            ingredient { +tag(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON) }
            ingredient {
                +tag(CommonTagPrefixes.NUGGET, VanillaMaterialKeys.IRON)
                count = 3
            }
        }.save(exporter)
        // Iron Ingot + Torch -> Lantern
        HTCombiningRecipeBuilder.assembling {
            result {
                +Items.LANTERN
                count = 2
            }
            ingredient { +tag(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON) }
            ingredient { +Items.TORCH }
        }.save(exporter)
        HTCombiningRecipeBuilder.assembling {
            result {
                +Items.SOUL_LANTERN
                count = 2
            }
            ingredient { +tag(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON) }
            ingredient { +Items.SOUL_TORCH }
        }.save(exporter)
        // Iron Ingot + Chest -> Hopper
        HTCombiningRecipeBuilder.assembling {
            result { +Items.HOPPER }
            ingredient {
                +tag(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON)
                count = 5
            }
            ingredient { +Tags.Items.CHESTS_WOODEN }
        }.save(exporter)
        // Dropper + Bow -> Dispenser
        HTCombiningRecipeBuilder.assembling {
            result { +Items.DISPENSER }
            ingredient { +Items.DROPPER }
            ingredient { +Tags.Items.TOOLS_BOW }
        }.save(exporter)
        // TNT
        HTCombiningRecipeBuilder.assembling {
            result {
                +Items.TNT
                count = 2
            }
            ingredient {
                +Tags.Items.SANDS
                count = 4
            }
            ingredient {
                +Tags.Items.GUNPOWDERS
                count = 5
            }
        }.save(exporter)

        // Leather + Iron Nugget -> Saddle
        HTCombiningRecipeBuilder.assembling {
            result { +Items.SADDLE }
            ingredient {
                +Tags.Items.LEATHERS
                count = 5
            }
            ingredient {
                +tag(CommonTagPrefixes.NUGGET, VanillaMaterialKeys.IRON)
                count = 2
            }
        }.save(exporter)
        // Head
        HTCombiningRecipeBuilder.assembling {
            result { +Items.ZOMBIE_HEAD }
            ingredient { +Items.SKELETON_SKULL }
            ingredient {
                +Items.ROTTEN_FLESH
                count = 8
            }
        }.save(exporter)
        HTCombiningRecipeBuilder.assembling {
            result { +Items.CREEPER_HEAD }
            ingredient { +Items.SKELETON_SKULL }
            ingredient {
                +Tags.Items.GUNPOWDERS
                count = 8
            }
        }.save(exporter)
        HTCombiningRecipeBuilder.assembling {
            result { +Items.PIGLIN_HEAD }
            ingredient { +Items.SKELETON_SKULL }
            ingredient {
                +Items.PORKCHOP
                count = 8
            }
        }.save(exporter)
    }

    //    Compressing    //

    private fun compressing() {
        // Snow Block -> Ice -> Packed Ice -> Blue Ice
        RagiumRecipeBuilder.compressing {
            ingredient {
                +Items.SNOW_BLOCK
                count = 4
            }
            result { +Items.ICE }
        }.save(exporter)
        RagiumRecipeBuilder.compressing {
            ingredient {
                +Items.ICE
                count = 6
            }
            result { +Items.PACKED_ICE }
        }.save(exporter)
        RagiumRecipeBuilder.compressing {
            ingredient {
                +Items.PACKED_ICE
                count = 6
            }
            result { +Items.BLUE_ICE }
        }.save(exporter)

        // Snow -> Snow Block
        RagiumRecipeBuilder.compressing {
            ingredient {
                +Items.SNOW
                count = 8
            }
            result { +Items.SNOW_BLOCK }
        }.save(exporter)
        // Moss Carpet -> Moss
        RagiumRecipeBuilder.compressing {
            ingredient {
                +Items.MOSS_CARPET
                count = 8
            }
            result { +Items.MOSS_BLOCK }
        }.save(exporter)
        // Sculk Vein -> Sculk
        RagiumRecipeBuilder.compressing {
            ingredient {
                +Items.SCULK_VEIN
                count = 8
            }
            result { +Items.SCULK }
        }.save(exporter)
    }

    //    Crushing    //

    private fun crushing() {
    }

    //    Cutting    //

    private fun cutting() {
        // Sapling -> Stick
        RagiumRecipeBuilder.cutting {
            ingredient { +ItemTags.SAPLINGS }
            result { +Items.STICK }
            recipeId suffix "_from_saplings"
        }.save(exporter)
        // Slab -> Stick
        RagiumRecipeBuilder.cutting {
            ingredient { +ItemTags.WOODEN_SLABS }
            result {
                +Items.STICK
                count = 2
            }
            recipeId suffix "_from_wooden_slabs"
        }.save(exporter)

        // Book -> Paper + Leather
        RagiumRecipeBuilder.cutting {
            ingredient { +Items.BOOK }
            result {
                +Items.PAPER
                count = 3
            }
            result { +Items.LEATHER }
            recipeId suffix "_from_book"
        }.save(exporter)
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

    override fun getName(): String = "Basic Processing Recipes"
}
