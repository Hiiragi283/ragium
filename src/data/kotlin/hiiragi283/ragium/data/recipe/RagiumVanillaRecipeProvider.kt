package hiiragi283.ragium.data.recipe

import hiiragi283.lib.color.HTDefaultColor
import hiiragi283.lib.data.recipe.HTCookingRecipeBuilder
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.HTShapedRecipeBuilder
import hiiragi283.lib.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.lib.data.recipe.HTStonecuttingRecipeBuilder
import hiiragi283.lib.material.CommonMaterials
import hiiragi283.lib.material.HTMaterial
import hiiragi283.lib.material.HTMaterialCategory
import hiiragi283.lib.material.VanillaMaterials
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTTagPrefix
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.HTBlockPart
import hiiragi283.ragium.api.tag.HTItemPart
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
            ingredient { +holderSet(CommonTagPrefixes.DUST, VanillaMaterials.COAL, VanillaMaterials.CHARCOAL) }
            ingredient { +holderSet(CommonTagPrefixes.DUST, CommonMaterials.SULFUR) }
            ingredient { +holderSet(CommonTagPrefixes.DUST, CommonMaterials.NITER) }
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
            define('A') { +holderSet(CommonTagPrefixes.DUST, VanillaMaterials.WOOD) }
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
            VanillaMaterials.GLOWSTONE to Items.GLOWSTONE_DUST,
            VanillaMaterials.QUARTZ to Items.QUARTZ,
            VanillaMaterials.AMETHYST to Items.AMETHYST_SHARD,
        ).forEach { (material: VanillaMaterials, item: Item) ->
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
        baseToBlock(VanillaMaterials.ECHO, CommonTagPrefixes.GEM, Items.ECHO_SHARD, size = StorageBlockSize.FOUR)
        baseToBlock(CommonMaterials.STEEL, HTItemPart.INGOT)
        // Ingot <-> Nugget
        ingotToNugget(VanillaMaterials.NETHERITE, ingot = Items.NETHERITE_INGOT)
        ingotToNugget(CommonMaterials.STEEL)

        // Alloy Dust
        HTShapelessRecipeBuilder.create {
            repeat(3) { ingredient { +holderSet(CommonTagPrefixes.DUST, VanillaMaterials.IRON) } }
            ingredient { +holderSet(CommonTagPrefixes.DUST, CommonMaterials.COAL_COKE) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, CommonMaterials.STEEL)
                count = 4
            }
        }.save(exporter)

        // Gear
        HTShapedRecipeBuilder.create {
            hollow4()
            define('A') { +holderSet(ItemTags.PLANKS) }
            define('B') { +holderSet(ItemTags.WOODEN_BUTTONS) }
            result { +RagiumItems.getOrThrow(HTItemPart.GEAR, VanillaMaterials.WOOD) }
        }.save(exporter)

        gear(CommonTagPrefixes.GEM, VanillaMaterials.DIAMOND)
        gear(CommonTagPrefixes.GEM, VanillaMaterials.EMERALD)
        gear(CommonTagPrefixes.INGOT, VanillaMaterials.COPPER)
        gear(CommonTagPrefixes.INGOT, VanillaMaterials.IRON)
        gear(CommonTagPrefixes.INGOT, VanillaMaterials.GOLD)

        netheriteUpgrade {
            base { +holderSet(CommonTagPrefixes.GEAR, VanillaMaterials.DIAMOND) }
            result { +RagiumItems.getOrThrow(HTItemPart.GEAR, VanillaMaterials.NETHERITE) }
        }.save(exporter)

        // Dust -> Ingot
        for (metal: HTMaterial in RagiumMaterialHelper.MANAGER[HTMaterialCategory.METAL]) {
            val dust: HTSimpleDeferredItem = RagiumItems.MATERIAL_ITEMS[HTItemPart.DUST, metal] ?: continue
            val item: ItemLike = when (metal) {
                VanillaMaterials.COPPER -> Items.COPPER_INGOT
                VanillaMaterials.IRON -> Items.IRON_INGOT
                VanillaMaterials.GOLD -> Items.GOLD_INGOT
                VanillaMaterials.NETHERITE -> Items.NETHERITE_INGOT
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
        for (fuel: HTMaterial in RagiumMaterialHelper.MANAGER[HTMaterialCategory.FUEL]) {
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
        material: HTMaterial,
        basePrefix: HTTagPrefix,
        base: ItemLike,
        block: ItemLike? = RagiumBlocks.MATERIAL_BLOCKS[HTBlockPart.STORAGE_BLOCK, material],
        size: StorageBlockSize = StorageBlockSize.NINE,
    ) {
        baseToBlock(material, Ingredient.of(holderSet(basePrefix, material)), base, block, size)
    }

    private fun baseToBlock(
        material: HTMaterial,
        basePart: HTItemPart,
        block: ItemLike? = RagiumBlocks.MATERIAL_BLOCKS[HTBlockPart.STORAGE_BLOCK, material],
        size: StorageBlockSize = StorageBlockSize.NINE,
    ) {
        val base: ItemLike = RagiumItems.MATERIAL_ITEMS[basePart, material] ?: return
        baseToBlock(material, basePart.tagPrefix, base, block, size)
    }

    private fun baseToBlock(
        material: HTMaterial,
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
        material: HTMaterial,
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

    private fun gear(basePrefix: HTTagPrefix, material: HTMaterial) {
        HTShapedRecipeBuilder.create {
            hollow4()
            define('A') { +holderSet(basePrefix, material) }
            define('B') { +holderSet(CommonTagPrefixes.GEAR, VanillaMaterials.WOOD) }
            result { +RagiumItems.getOrThrow(HTItemPart.GEAR, material) }
        }.save(exporter)
    }

    override fun getName(): String = "Vanilla Recipes"
}
