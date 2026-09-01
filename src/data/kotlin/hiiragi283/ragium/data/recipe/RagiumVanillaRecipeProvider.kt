package hiiragi283.ragium.data.recipe

import hiiragi283.lib.color.HTDefaultColor
import hiiragi283.lib.data.recipe.HTCookingRecipeBuilder
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.HTShapedRecipeBuilder
import hiiragi283.lib.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.lib.data.recipe.HTStonecuttingRecipeBuilder
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTTagPrefix
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTBlockPart
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import hiiragi283.ragium.common.material.RagiumMaterialHelper
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

class RagiumVanillaRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun buildRecipes() {
        machine()
        material()

        // Gunpowder
        HTShapelessRecipeBuilder.create {
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Fuel.COAL, RagiumMaterial.Fuel.CHARCOAL) }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.SULFUR) }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.NITER) }
            result {
                +Items.GUNPOWDER
                count = 3
            }
        }.save(exporter)

        // Bamboo Charcoal
        HTCookingRecipeBuilder.smelting {
            ingredient { items { +Items.BAMBOO } }
            result { +RagiumItems.BAMBOO_CHARCOAL }
            exp = 0.5f
        }.save(exporter)
        // Particle Board
        HTShapedRecipeBuilder.create {
            hollow8()
            define('A') { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.WOOD) }
            define('B') { +holderSet(RagiumTags.Items.STICKY_BALLS) }
            result {
                +RagiumItems.PARTICLE_BOARD
                count = 4
            }
        }.save(exporter)
        // Synthetic
        for (item: HTSimpleDeferredItem in listOf(RagiumItems.SYNTHETIC_FEATHER, RagiumItems.SYNTHETIC_FIBER, RagiumItems.SYNTHETIC_LEATHER)) {
            HTStonecuttingRecipeBuilder.create {
                ingredient { +holderSet(RagiumTags.Items.PLASTICS) }
                result { +item }
            }.save(exporter)
        }
        // XX Shape Pattern
        for (item: HTSimpleDeferredItem in RagiumItems.SHAPE_PATTERNS) {
            HTStonecuttingRecipeBuilder.create {
                ingredient { +holderSet(RagiumTags.Items.SHAPE_PATTERNS) }
                result { +item }
            }.save(exporter)
        }

        // XX Dye Bucket
        for (color: HTDefaultColor in HTDefaultColor.entries) {
            HTShapelessRecipeBuilder.create {
                ingredient { +holderSet(Tags.Items.BUCKETS_WATER) }
                repeat(4) { ingredient { +holderSet(color.dyesTag) } }
                result { +RagiumFluids.DYES[color].bucketHolder }
            }.save(exporter)
        }
    }

    //    Machine    //

    private fun machine() {
        // Mechanical
        // Heat
        // Chemical
        // Bio
        // Electronics
        // Arcane
    }

    //    Material    //

    private fun material() {
        // XX Block -> XX
        setOf(
            RagiumMaterial.Mineral.GLOWSTONE to Items.GLOWSTONE_DUST,
            RagiumMaterial.Gem.QUARTZ to Items.QUARTZ,
            RagiumMaterial.Gem.AMETHYST to Items.AMETHYST_SHARD,
        ).forEach { (material: RagiumMaterial, item: Item) ->
            HTShapelessRecipeBuilder.create {
                ingredient { +holderSet(CommonTagPrefixes.STORAGE_BLOCK, material) }
                result {
                    +item
                    count = 4
                }
                recipeId suffix "_from_block"
            }.save(exporter)
        }

        // XX <-> Storage Block
        baseToBlock(RagiumMaterial.Gem.ECHO, CommonTagPrefixes.GEM, Items.ECHO_SHARD, size = StorageBlockSize.FOUR)
        baseToBlock(RagiumMaterial.Metal.STEEL, HTItemPart.INGOT)
        // Ingot <-> Nugget
        ingotToNugget(RagiumMaterial.Metal.NETHERITE, ingot = Items.NETHERITE_INGOT)
        ingotToNugget(RagiumMaterial.Metal.STEEL)

        // Alloy Dust
        HTShapelessRecipeBuilder.create {
            repeat(3) { ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Metal.IRON) } }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Fuel.COAL_COKE) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Metal.STEEL)
                count = 4
            }
        }.save(exporter)

        // Gear
        HTShapedRecipeBuilder.create {
            hollow4()
            define('A') { +holderSet(ItemTags.PLANKS) }
            define('B') { +holderSet(ItemTags.WOODEN_BUTTONS) }
            result { +RagiumItems.getOrThrow(HTItemPart.GEAR, RagiumMaterial.Other.WOOD) }
        }.save(exporter)

        gear(CommonTagPrefixes.GEM, RagiumMaterial.Gem.DIAMOND)
        gear(CommonTagPrefixes.GEM, RagiumMaterial.Gem.EMERALD)
        gear(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.COPPER)
        gear(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.IRON)
        gear(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.GOLD)

        netheriteUpgrade {
            base { +holderSet(CommonTagPrefixes.GEAR, RagiumMaterial.Gem.DIAMOND) }
            result { +RagiumItems.getOrThrow(HTItemPart.GEAR, RagiumMaterial.Metal.NETHERITE) }
        }.save(exporter)

        // Dust -> Ingot
        for (metal: RagiumMaterial.Metal in RagiumMaterial.Metal.entries) {
            val dust: HTSimpleDeferredItem = RagiumItems.MATERIAL_ITEMS[HTItemPart.DUST, metal] ?: continue
            val item: ItemLike = when (metal) {
                RagiumMaterial.Metal.COPPER -> Items.COPPER_INGOT
                RagiumMaterial.Metal.IRON -> Items.IRON_INGOT
                RagiumMaterial.Metal.GOLD -> Items.GOLD_INGOT
                RagiumMaterial.Metal.NETHERITE -> Items.NETHERITE_INGOT
                else -> RagiumItems.MATERIAL_ITEMS[HTItemPart.INGOT, metal]
            } ?: continue
            HTCookingRecipeBuilder.smeltingAndBlasting {
                ingredient { items { +dust } }
                result { +item.asItem() }
                exp = 0.35f
                recipeId suffix "_from_dust"
            }.forEach { it.save(exporter) }
        }

        // Fuel
        for (fuel: RagiumMaterial.Fuel in RagiumMaterial.Fuel.entries) {
            val base: HTSimpleDeferredItem = RagiumMaterialHelper.getFuelBase(fuel) ?: continue
            // Storage
            baseToBlock(fuel, Ingredient.of(base), base)
            // Tiny
            HTShapelessRecipeBuilder.create {
                ingredient { items { +base } }
                result {
                    +RagiumItems.getOrThrow(HTItemPart.TINY, fuel)
                    count = 8
                }
            }.save(exporter)
            HTShapedRecipeBuilder.create {
                hollow()
                define('A') { +holderSet(CommonTagPrefixes.TINY, fuel) }
                result { +base }
                recipeId suffix "_from_tiny"
            }.save(exporter)
        }
    }

    private fun baseToBlock(
        material: RagiumMaterial,
        basePrefix: HTTagPrefix,
        base: ItemLike,
        block: ItemLike? = RagiumBlocks.MATERIAL_BLOCKS[HTBlockPart.STORAGE_BLOCK, material],
        size: StorageBlockSize = StorageBlockSize.NINE,
    ) {
        baseToBlock(material, Ingredient.of(holderSet(basePrefix, material)), base, block, size)
    }

    private fun baseToBlock(
        material: RagiumMaterial,
        basePart: HTItemPart,
        block: ItemLike? = RagiumBlocks.MATERIAL_BLOCKS[HTBlockPart.STORAGE_BLOCK, material],
        size: StorageBlockSize = StorageBlockSize.NINE,
    ) {
        val base: ItemLike = RagiumItems.MATERIAL_ITEMS[basePart, material] ?: return
        baseToBlock(material, basePart.tagPrefix, base, block, size)
    }

    private fun baseToBlock(
        material: RagiumMaterial,
        baseInput: Ingredient,
        base: ItemLike,
        block: ItemLike? = RagiumBlocks.MATERIAL_BLOCKS[HTBlockPart.STORAGE_BLOCK, material],
        size: StorageBlockSize = StorageBlockSize.NINE,
    ) {
        if (block == null) return
        HTShapelessRecipeBuilder.create {
            ingredient { +holderSet(CommonTagPrefixes.STORAGE_BLOCK, material) }
            result {
                +base.asItem()
                count = size.count
            }
            recipeId suffix "_from_block"
        }.save(exporter)
        HTShapedRecipeBuilder.create {
            size.pattern.invoke(this)
            define('A') { +baseInput }
            define('B') { items { +base.asItem() } }
            result { +block.asItem() }
        }.save(exporter)
    }

    private enum class StorageBlockSize(val count: Int, val pattern: (HTShapedRecipeBuilder).() -> Unit) {
        FOUR(4, {
            +"AA"
            +"AB"
        }),
        NINE(9, HTShapedRecipeBuilder::hollow8),
    }

    private fun ingotToNugget(
        material: RagiumMaterial,
        ingot: ItemLike? = RagiumItems.MATERIAL_ITEMS[HTItemPart.INGOT, material],
        nugget: ItemLike? = RagiumItems.MATERIAL_ITEMS[HTItemPart.NUGGET, material],
    ) {
        if (ingot == null || nugget == null) return
        HTShapelessRecipeBuilder.create {
            ingredient { +holderSet(CommonTagPrefixes.INGOT, material) }
            result {
                +nugget.asItem()
                count = 9
            }
            recipeId suffix "_from_ingot"
        }.save(exporter)
        HTShapedRecipeBuilder.create {
            hollow8()
            define('A') { +holderSet(CommonTagPrefixes.NUGGET, material) }
            define('B') { items { +nugget.asItem() } }
            result { +ingot.asItem() }
            recipeId suffix "_from_nugget"
        }.save(exporter)
    }

    private fun gear(basePrefix: HTTagPrefix, material: RagiumMaterial) {
        HTShapedRecipeBuilder.create {
            hollow4()
            define('A') { +holderSet(basePrefix, material) }
            define('B') { +holderSet(CommonTagPrefixes.GEAR, RagiumMaterial.Other.WOOD) }
            result { +RagiumItems.getOrThrow(HTItemPart.GEAR, material) }
        }.save(exporter)
    }

    override fun getName(): String = "Vanilla Recipes"
}
