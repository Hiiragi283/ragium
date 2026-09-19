package hiiragi283.ragium.data.recipe

import hiiragi283.lib.collection.nelOf
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.builder.VanillaRecipeBuilders
import hiiragi283.lib.data.recipe.builder.vanilla.HTShapedRecipeBuilder
import hiiragi283.lib.data.recipe.ingredient.IngredientBuilder
import hiiragi283.lib.item.component.HTToolCollection
import hiiragi283.lib.item.component.HTToolType
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.resource.vanillaId
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTCommonTags
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.HTStorageBlockPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.HTMachineType
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import hiiragi283.ragium.common.item.component.RagiumToolMaterials
import hiiragi283.ragium.common.material.RagiumMaterialHelper
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.ToolMaterial
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture
import kotlin.collections.forEach

class RagiumVanillaRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) :
    HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun exportValues() {
        machine()
        storage()
        material()

        // Prismarine Bricks -> 9x Prismarine Shard
        VanillaRecipeBuilders.shapeless {
            ingredient { items { +Items.PRISMARINE_BRICKS } }

            result {
                +Items.PRISMARINE_SHARD
                count = 9
            }
            category = RecipeCategory.BUILDING_BLOCKS
            group = "prismarine_shard"
            recipeId suffix "_from_bricks"
        }.save(exporter)
        // Gunpowder
        VanillaRecipeBuilders.shapeless {
            ingredient {
                +materialTags(CommonTagPrefixes.DUST, nelOf(RagiumMaterial.Fuel.COAL, RagiumMaterial.Fuel.CHARCOAL))
            }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.SULFUR) }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.NITER) }
            result {
                +Items.GUNPOWDER
                count = 3
            }
        }.save(exporter)
        // Blaze Rod
        VanillaRecipeBuilders.shaped {
            layered()
            define('A') { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Gem.AMETHYST) }
            define('B') { items { +Items.MAGMA_BLOCK } }
            define('C') { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.SULFUR) }
            result { +Items.BLAZE_ROD }
        }.save(exporter)
        // Breeze Rod
        VanillaRecipeBuilders.shaped {
            layered()
            define('A') { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Gem.AMETHYST) }
            define('B') { items { +Items.ICE } }
            define('C') { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.NITER) }
            result { +Items.BREEZE_ROD }
        }.save(exporter)
        // Candle
        VanillaRecipeBuilders.shaped {
            +"A"
            +"B"
            define('A') { +holderSet(Tags.Items.STRINGS) }
            define('B') { items { +RagiumItems.BEESWAX } }
            result { +Items.CANDLE }
            category = RecipeCategory.DECORATIONS
        }.save(exporter)

        // Bamboo Charcoal
        VanillaRecipeBuilders.smelting {
            ingredient { items { +Items.BAMBOO } }
            result { +RagiumItems.BAMBOO_CHARCOAL }
            exp = 0.5f
        }.save(exporter)
        // Particle Board
        VanillaRecipeBuilders.shaped {
            hollow8()
            define('A') { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.WOOD) }
            define('B') { +holderSet(HTCommonTags.Items.STICKY_BALLS) }
            result {
                +RagiumItems.PARTICLE_BOARD
                count = 4
            }
        }.save(exporter)
        // Synthetic
        listOf(
            RagiumItems.SYNTHETIC_FEATHER,
            RagiumItems.SYNTHETIC_FIBER,
            RagiumItems.SYNTHETIC_LEATHER
        ).forEach {
            VanillaRecipeBuilders.stonecutting {
                ingredient { +holderSet(HTCommonTags.Items.PLASTICS) }
                result { +it }
            }.save(exporter)
        }
        // Splash Bottle
        VanillaRecipeBuilders.shapeless {
            ingredient { +holderSet(Tags.Items.GUNPOWDERS) }
            repeat(4) { ingredient { items { +Items.GLASS_BOTTLE } } }
            result {
                +RagiumItems.SPLASH_BOTTLE
                count = 4
            }
        }.save(exporter)
        // Lingering Bottle
        VanillaRecipeBuilders.shapeless {
            ingredient { items { +Items.DRAGON_BREATH } }
            repeat(4) { ingredient { items { +Items.GLASS_BOTTLE } } }
            result {
                +RagiumItems.LINGERING_BOTTLE
                count = 4
            }
        }.save(exporter)

        // XX Tools
        registerTools(RagiumItems.SOOTY_IRON_TOOLS, RagiumToolMaterials.SOOTY_IRON)

        // XX Dye Bucket
        for (color: DyeColor in DyeColor.entries) {
            VanillaRecipeBuilders.shapeless {
                ingredient { +holderSet(Tags.Items.BUCKETS_WATER) }
                repeat(4) { ingredient { +holderSet(color.tag) } }
                result { +RagiumFluids.DYES[color].bucketHolder }
            }.save(exporter)
        }
    }

    private fun registerTools(tools: HTToolCollection<HTSimpleDeferredItem>, material: ToolMaterial) {
        fun registerTool(toolType: HTToolType, patterns: Iterable<String>) {
            VanillaRecipeBuilders.shaped {
                this.pattern(patterns)
                define('A') { +holderSet(material.repairItems) }
                define('B') { +holderSet(Tags.Items.RODS_WOODEN) }
                result { +tools[toolType] }
                category = RecipeCategory.TOOLS
            }.save(exporter)
        }

        registerTool(HTToolType.SWORD, listOf("B", "A", "A"))
        registerTool(HTToolType.SHOVEL, listOf("B", "B", "A"))
        registerTool(HTToolType.PICKAXE, listOf(" B ", " B ", "AAA"))
        registerTool(HTToolType.AXE, listOf("B ", "BA", "AA"))
        registerTool(HTToolType.HOE, listOf("B ", "B ", "AA"))
    }

    //    Machine    //

    private fun machine() {
        // Mechanical
        VanillaRecipeBuilders.shaped {
            layered2()
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.SOOTY_IRON) }
            define('B') { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.REDSTONE) }
            result {
                +RagiumItems.getParts(HTMachineType.MECHANICAL)
                count = 3
            }
        }.save(exporter)

        mechanical(RagiumBlocks.ASSEMBLER) { items { +Items.CRAFTER } }
        mechanical(RagiumBlocks.CRUSHER) { items { +Items.GRINDSTONE } }
        mechanical(RagiumBlocks.COMPRESSOR) { +holderSet(ItemTags.ANVIL) }
        mechanical(RagiumBlocks.CUTTING_MACHINE) { items { +Items.STONECUTTER } }
        // Heat
        heat(RagiumBlocks.ALLOY_SMELTER) { items { +Items.BLAST_FURNACE } }
        heat(RagiumBlocks.FREEZER) { +holderSet(Tags.Items.BUCKETS_WATER) }
        heat(RagiumBlocks.MELTER) { +holderSet(Tags.Items.BUCKETS_LAVA) }

        heat(RagiumBlocks.SMELTER) { items { +Items.FURNACE } }
        // Chemical
        chemical(RagiumBlocks.CHEMICAL_BATH) { items { +Items.CAULDRON } }
        chemical(RagiumBlocks.MIXER) { +holderSet(Tags.Items.BUCKETS_EMPTY) }
        // Bio
        bio(RagiumBlocks.BREWERY) { items { +Items.BREWING_STAND } }
        bio(RagiumBlocks.PLANTER) { items { +Items.FLOWER_POT } }
        // Electronics
        // Arcane

        // Decoration
        VanillaRecipeBuilders.shaped {
            +"ABA"
            +"B B"
            +"ABA"
            define('A') { +holderSet(CommonTagPrefixes.NUGGET, RagiumMaterial.Metal.SOOTY_IRON) }
            define('B') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.SOOTY_IRON) }
            result {
                +RagiumBlocks.MACHINE_CASING
                count = 4
            }
        }.save(exporter)

        for (block: HTSimpleDeferredBlockAndItem in RagiumBlocks.MACHINE_CASINGS.values) {
            VanillaRecipeBuilders.stonecutting {
                ingredient { items { +RagiumBlocks.MACHINE_CASING } }
                result { +block }
            }.save(exporter)
        }
    }

    private inline fun machine(
        machineType: HTMachineType,
        material: HTMaterialLike,
        gear: HTMaterialLike,
        result: HTSimpleDeferredBlockAndItem,
        builderAction: IngredientBuilder.() -> Unit
    ) {
        VanillaRecipeBuilders.shaped {
            +"ABA"
            +"BCB"
            +"ADA"
            define('A') { +holderSet(CommonTagPrefixes.NUGGET, material) }
            define('B') { items { +RagiumItems.getParts(machineType) } }
            define('C') { +holderSet(CommonTagPrefixes.GEAR, gear) }
            define('D', builderAction)
            result { +result }
        }.save(exporter)
    }

    private inline fun mechanical(result: HTSimpleDeferredBlockAndItem, builderAction: IngredientBuilder.() -> Unit) {
        machine(
            HTMachineType.MECHANICAL,
            RagiumMaterial.Metal.SOOTY_IRON,
            RagiumMaterial.Metal.COPPER,
            result,
            builderAction
        )
    }

    private inline fun heat(result: HTSimpleDeferredBlockAndItem, builderAction: IngredientBuilder.() -> Unit) {
        machine(
            HTMachineType.HEAT,
            RagiumMaterial.Metal.SOOTY_IRON,
            RagiumMaterial.Metal.IRON,
            result,
            builderAction
        )
    }

    private inline fun chemical(result: HTSimpleDeferredBlockAndItem, builderAction: IngredientBuilder.() -> Unit) {
        machine(
            HTMachineType.CHEMICAL,
            RagiumMaterial.Metal.BLACK_STEEL,
            RagiumMaterial.Metal.GOLD,
            result,
            builderAction
        )
    }

    private inline fun bio(result: HTSimpleDeferredBlockAndItem, builderAction: IngredientBuilder.() -> Unit) {
        machine(
            HTMachineType.BIO,
            RagiumMaterial.Metal.BLACK_STEEL,
            RagiumMaterial.Gem.EMERALD,
            result,
            builderAction
        )
    }

    private inline fun electronics(result: HTSimpleDeferredBlockAndItem, builderAction: IngredientBuilder.() -> Unit) {
        machine(
            HTMachineType.ELECTRONICS,
            RagiumMaterial.Metal.VOID_METAL,
            RagiumMaterial.Gem.DIAMOND,
            result,
            builderAction
        )
    }

    private inline fun arcane(result: HTSimpleDeferredBlockAndItem, builderAction: IngredientBuilder.() -> Unit) {
        machine(
            HTMachineType.ARCANE,
            RagiumMaterial.Metal.VOID_METAL,
            RagiumMaterial.Metal.NETHERITE,
            result,
            builderAction
        )
    }

    //    Machine    //

    private fun storage() {
        // Tank
        VanillaRecipeBuilders.shaped {
            hollow8()
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.COPPER) }
            define('B') { +holderSet(Tags.Items.BUCKETS_EMPTY) }
            result { +RagiumBlocks.TANK }
        }.save(exporter)
        // Void Tank
        VanillaRecipeBuilders.shaped {
            hollow8()
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.VOID_METAL) }
            define('B') { +holderSet(Tags.Items.BUCKETS_EMPTY) }
            result { +RagiumBlocks.VOID_TANK }
        }.save(exporter)
    }

    //    Material    //

    private fun material() {
        // XX <-> Storage Block
        baseToBlock(
            RagiumMaterial.Gem.ECHO,
            CommonTagPrefixes.GEM,
            HTSimpleDeferredItem(vanillaId("echo_shard")),
            size = StorageBlockSize.FOUR
        )
        baseToBlock(RagiumMaterial.Metal.SOOTY_IRON, HTItemPart.INGOT)
        baseToBlock(RagiumMaterial.Metal.BLACK_STEEL, HTItemPart.INGOT)
        baseToBlock(RagiumMaterial.Metal.VOID_METAL, HTItemPart.INGOT)
        // Ingot <-> Nugget
        ingotToNugget(RagiumMaterial.Metal.NETHERITE, ingot = HTSimpleDeferredItem(vanillaId("netherite_ingot")))
        ingotToNugget(RagiumMaterial.Metal.SOOTY_IRON)
        ingotToNugget(RagiumMaterial.Metal.BLACK_STEEL)
        ingotToNugget(RagiumMaterial.Metal.VOID_METAL)
        // Gear
        RagiumItems.getOrThrow(HTItemPart.GEAR, RagiumMaterial.Other.WOOD).let { gear: HTSimpleDeferredItem ->
            VanillaRecipeBuilders.shaped {
                hollow4()
                define('A') { +holderSet(ItemTags.PLANKS) }
                define('B') { +holderSet(ItemTags.WOODEN_BUTTONS) }
                result { +gear }
            }.save(exporter)
        }
        gear(CommonTagPrefixes.GEM, RagiumMaterial.Gem.DIAMOND)
        gear(CommonTagPrefixes.GEM, RagiumMaterial.Gem.EMERALD)
        gear(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.COPPER)
        gear(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.IRON)
        gear(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.GOLD)
        RagiumItems.MATERIAL_ITEMS[HTItemPart.GEAR, RagiumMaterial.Metal.NETHERITE]?.let {
            VanillaRecipeBuilders.smithing {
                template { items { +Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE } }
                base { +holderSet(CommonTagPrefixes.GEAR, RagiumMaterial.Gem.DIAMOND) }
                addition { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.NETHERITE) }
                result { +it }
            }.save(exporter)
        }
        // Dust -> Ingot
        for (metal: RagiumMaterial.Metal in RagiumMaterial.Metal.entries) {
            val dust: HTSimpleDeferredItem = RagiumItems.MATERIAL_ITEMS[HTItemPart.DUST, metal] ?: continue
            val item: HTSimpleDeferredItem = when (metal) {
                RagiumMaterial.Metal.COPPER -> HTSimpleDeferredItem(vanillaId("copper_ingot"))
                RagiumMaterial.Metal.IRON -> HTSimpleDeferredItem(vanillaId("iron_ingot"))
                RagiumMaterial.Metal.GOLD -> HTSimpleDeferredItem(vanillaId("gold_ingot"))
                RagiumMaterial.Metal.NETHERITE -> HTSimpleDeferredItem(vanillaId("netherite_ingot"))
                else -> RagiumItems.MATERIAL_ITEMS[HTItemPart.INGOT, metal]
            } ?: continue
            VanillaRecipeBuilders.smeltingAndBlasting(exporter) {
                ingredient { items { +dust } }
                result { +item }
                exp = 0.35f
                group = item.id.path
                recipeId suffix "_from_dust"
            }
        }
        // Fuel
        for (fuel: RagiumMaterial.Fuel in RagiumMaterial.Fuel.entries) {
            val base: HTSimpleDeferredItem = RagiumMaterialHelper.getFuelBase(fuel)
            // Storage
            baseToBlock(fuel, Ingredient.of(base), base)
            // Tiny
            val tiny: HTSimpleDeferredItem = RagiumItems.getOrThrow(HTItemPart.TINY, fuel)
            VanillaRecipeBuilders.shapeless {
                ingredient { items { +base } }
                result {
                    +tiny
                    count = 8
                }
                group = tiny.id.path
            }.save(exporter)
            VanillaRecipeBuilders.shaped {
                hollow()
                define('A') { +holderSet(CommonTagPrefixes.TINY, fuel) }
                result { +base }
                group = base.id.path
                recipeId suffix "_from_tiny"
            }.save(exporter)
        }
        // Sooty Iron
        val ironIngot: HolderSet<Item> = holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.IRON)
        val sootyIronIngot: HTSimpleDeferredItem =
            RagiumItems.getOrThrow(HTItemPart.INGOT, RagiumMaterial.Metal.SOOTY_IRON)
        VanillaRecipeBuilders.shaped {
            hollow8()
            define('A') {
                +materialTags(CommonTagPrefixes.TINY, nelOf(RagiumMaterial.Fuel.COAL, RagiumMaterial.Fuel.CHARCOAL))
            }
            define('B') { +ironIngot }
            result { +sootyIronIngot }
            group = sootyIronIngot.id.path
        }.save(exporter)
        VanillaRecipeBuilders.shaped {
            hollow4()
            define('A') {
                +materialTags(
                    CommonTagPrefixes.TINY,
                    nelOf(RagiumMaterial.Fuel.COAL_COKE, RagiumMaterial.Fuel.PITCH_COKE)
                )
            }
            define('B') { +ironIngot }
            result { +sootyIronIngot }
            group = sootyIronIngot.id.path
            recipeId suffix "_from_coke"
        }.save(exporter)
    }

    private fun baseToBlock(
        material: RagiumMaterial,
        basePrefix: HTTagPrefix,
        base: HTSimpleDeferredItem,
        block: HTSimpleDeferredBlockAndItem? = RagiumBlocks.MATERIAL_BLOCKS[HTStorageBlockPart.DEFAULT, material],
        size: StorageBlockSize = StorageBlockSize.NINE
    ) {
        baseToBlock(material, Ingredient.of(holderSet(basePrefix, material)), base, block, size)
    }

    private fun baseToBlock(
        material: RagiumMaterial,
        basePart: HTItemPart,
        block: HTSimpleDeferredBlockAndItem? = RagiumBlocks.MATERIAL_BLOCKS[HTStorageBlockPart.DEFAULT, material],
        size: StorageBlockSize = StorageBlockSize.NINE
    ) {
        val base: HTSimpleDeferredItem = RagiumItems.MATERIAL_ITEMS[basePart, material] ?: return
        baseToBlock(material, basePart.tagPrefix, base, block, size)
    }

    private fun baseToBlock(
        material: RagiumMaterial,
        baseInput: Ingredient,
        base: HTSimpleDeferredItem,
        block: HTSimpleDeferredBlockAndItem? = RagiumBlocks.MATERIAL_BLOCKS[HTStorageBlockPart.DEFAULT, material],
        size: StorageBlockSize = StorageBlockSize.NINE
    ) {
        if (block == null) return
        VanillaRecipeBuilders.shapeless {
            ingredient { +holderSet(CommonTagPrefixes.STORAGE_BLOCK, material) }
            result {
                +base
                count = size.count
            }
            group = base.id.path
            recipeId suffix "_from_block"
        }.save(exporter)
        VanillaRecipeBuilders.shaped {
            size.pattern.invoke(this)
            define('A') { +baseInput }
            define('B') { items { +base } }
            result { +block }
            category = RecipeCategory.BUILDING_BLOCKS
            group = block.item.id.path
        }.save(exporter)
    }

    private enum class StorageBlockSize(val count: Int, val pattern: HTShapedRecipeBuilder.() -> Unit) {
        FOUR(4, {
            +"AA"
            +"AB"
        }),
        NINE(9, HTShapedRecipeBuilder::hollow8)
    }

    private fun ingotToNugget(
        material: RagiumMaterial,
        ingot: HTSimpleDeferredItem? = RagiumItems.MATERIAL_ITEMS[HTItemPart.INGOT, material],
        nugget: HTSimpleDeferredItem? = RagiumItems.MATERIAL_ITEMS[HTItemPart.NUGGET, material]
    ) {
        if (ingot == null || nugget == null) return
        VanillaRecipeBuilders.shapeless {
            ingredient { +holderSet(CommonTagPrefixes.INGOT, material) }
            result {
                +nugget
                count = 9
            }
            group = nugget.id.path
            recipeId suffix "_from_ingot"
        }.save(exporter)
        VanillaRecipeBuilders.shaped {
            hollow8()
            define('A') { +holderSet(CommonTagPrefixes.NUGGET, material) }
            define('B') { items { +nugget } }
            result { +ingot }
            group = ingot.id.path
            recipeId suffix "_from_nugget"
        }.save(exporter)
    }

    private fun gear(basePrefix: HTTagPrefix, material: RagiumMaterial) {
        VanillaRecipeBuilders.shaped {
            hollow4()
            define('A') { +holderSet(basePrefix, material) }
            define('B') { +holderSet(CommonTagPrefixes.GEAR, RagiumMaterial.Other.WOOD) }
            result { +RagiumItems.getOrThrow(HTItemPart.GEAR, material) }
        }.save(exporter)
    }

    override fun getName(): String = "Vanilla Recipes"
}
