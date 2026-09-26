package hiiragi283.ragium.data.recipe

import hiiragi283.lib.collection.nelOf
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.builder.VanillaRecipeBuilders
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
        vanilla()
        machine()
        storage()
        material()

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

    //    Vanilla    //

    private fun vanilla() {
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
        // Torch
        VanillaRecipeBuilders.shaped {
            +"A"
            +"B"
            define('A') { items { +RagiumItems.TAR } }
            define('B') { +holderSet(Tags.Items.RODS_WOODEN) }
            result {
                +Items.TORCH
                count = 3
            }
            recipeId suffix "_from_tar"
        }.save(exporter)
        VanillaRecipeBuilders.shaped {
            +"A"
            +"B"
            define('A') { +holderSet(HTCommonTags.Items.PITCH_COKE) }
            define('B') { +holderSet(Tags.Items.RODS_WOODEN) }
            result {
                +Items.TORCH
                count = 6
            }
            recipeId suffix "_from_pitch_coke"
        }.save(exporter)
        VanillaRecipeBuilders.shaped {
            +"A"
            +"B"
            define('A') { +holderSet(HTCommonTags.Items.COAL_COKE) }
            define('B') { +holderSet(Tags.Items.RODS_WOODEN) }
            result {
                +Items.TORCH
                count = 8
            }
            recipeId suffix "_from_coal_coke"
        }.save(exporter)
        // Fire Charge
        VanillaRecipeBuilders.shapeless {
            ingredient { +holderSet(Tags.Items.GUNPOWDERS) }
            ingredient { items { +Items.BLAZE_POWDER } }
            ingredient { +holderSet(HTCommonTags.Items.PITCH_COKE) }
            result {
                +Items.FIRE_CHARGE
                count = 4
            }
            recipeId suffix "_from_pitch_coke"
        }.save(exporter)
        VanillaRecipeBuilders.shapeless {
            ingredient { +holderSet(Tags.Items.GUNPOWDERS) }
            ingredient { items { +Items.BLAZE_POWDER } }
            ingredient { +holderSet(HTCommonTags.Items.COAL_COKE) }
            result {
                +Items.FIRE_CHARGE
                count = 6
            }
            recipeId suffix "_from_coal_coke"
        }.save(exporter)
        // Hopper
        VanillaRecipeBuilders.shaped {
            +"A A"
            +"ABA"
            +" A "
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.SOOTY_IRON) }
            define('B') { +holderSet(Tags.Items.CHESTS_WOODEN) }
            result {
                +Items.HOPPER
                count = 2
            }
            recipeId suffix "_by_sooty_iron"
        }.save(exporter)
        VanillaRecipeBuilders.shaped {
            +"A A"
            +"ABA"
            +" A "
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.BLACK_STEEL) }
            define('B') { +holderSet(Tags.Items.CHESTS_WOODEN) }
            result {
                +Items.HOPPER
                count = 4
            }
            recipeId suffix "_by_black_steel"
        }.save(exporter)
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
        chemical(RagiumBlocks.ELECTROLYZER) { +holderSet(ItemTags.LIGHTNING_RODS) }
        chemical(RagiumBlocks.MIXER) { +holderSet(Tags.Items.BUCKETS_EMPTY) }
        // Bio
        bio(RagiumBlocks.BREWERY) { items { +Items.BREWING_STAND } }
        bio(RagiumBlocks.PLANTER) { items { +Items.FLOWER_POT } }
        // Electronics
        electronics(RagiumBlocks.PRECISION_ASSEMBLER) { items { +RagiumBlocks.ASSEMBLER } }
        // Arcane
        arcane(RagiumBlocks.ENCHANTER) { items { +Items.ENCHANTING_TABLE } }

        // Bus
        VanillaRecipeBuilders.shapeless {
            ingredient { items { +RagiumBlocks.MACHINE_CASING } }
            ingredient { items { +RagiumBlocks.TANK } }
            result { +RagiumBlocks.FLUID_OUTPUT_BUS }
        }.save(exporter)
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
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.SOOTY_IRON) }
            define('B') { +holderSet(Tags.Items.BUCKETS_EMPTY) }
            result { +RagiumBlocks.TANK }
            recipeId suffix "_by_sooty_iron"
        }.save(exporter)
        VanillaRecipeBuilders.shaped {
            hollow4()
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.BLACK_STEEL) }
            define('B') { +holderSet(Tags.Items.BUCKETS_EMPTY) }
            result { +RagiumBlocks.TANK }
            recipeId suffix "_by_black_steel"
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
        gemBlock(RagiumMaterial.Gem.ECHO, RagiumBlocks.ECHO_BLOCK)

        gemBlock(RagiumMaterial.Gem.FLUORITE, RagiumBlocks.FLUORITE_BLOCK)
        registerSlabRecipes(RagiumBlocks.FLUORITE_SLAB, Ingredient.of(RagiumBlocks.FLUORITE_BLOCK))
        registerStairsRecipes(RagiumBlocks.FLUORITE_STAIRS, Ingredient.of(RagiumBlocks.FLUORITE_BLOCK))

        gemBlock(RagiumMaterial.Gem.CRYOLITE, RagiumBlocks.CRYOLITE_BLOCK)
        registerSlabRecipes(RagiumBlocks.CRYOLITE_SLAB, Ingredient.of(RagiumBlocks.CRYOLITE_BLOCK))
        registerStairsRecipes(RagiumBlocks.CRYOLITE_STAIRS, Ingredient.of(RagiumBlocks.CRYOLITE_BLOCK))

        nineToBlock(RagiumMaterial.Metal.ALUMINUM, HTItemPart.INGOT)
        nineToBlock(RagiumMaterial.Metal.SOOTY_IRON, HTItemPart.INGOT)
        nineToBlock(RagiumMaterial.Metal.BLACK_STEEL, HTItemPart.INGOT)
        nineToBlock(RagiumMaterial.Metal.VOID_METAL, HTItemPart.INGOT)
        // Ingot <-> Nugget
        ingotToNugget(RagiumMaterial.Metal.NETHERITE, ingot = HTSimpleDeferredItem(vanillaId("netherite_ingot")))
        ingotToNugget(RagiumMaterial.Metal.ALUMINUM)
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
            nineToBlock(fuel, Ingredient.of(base), base)
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

    private fun gemBlock(material: RagiumMaterial, block: HTSimpleDeferredBlockAndItem) {
        VanillaRecipeBuilders.shaped {
            +"AA"
            +"AA"
            define('A') { +holderSet(CommonTagPrefixes.GEM, material) }
            result { +block }
            category = RecipeCategory.BUILDING_BLOCKS
            group = block.idOrThrow.path
        }.save(exporter)
    }

    private fun nineToBlock(material: RagiumMaterial, basePart: HTItemPart) {
        val base: HTSimpleDeferredItem = RagiumItems.getOrThrow(basePart, material)
        val block: HTSimpleDeferredBlockAndItem = RagiumBlocks.getOrThrow(HTStorageBlockPart.DEFAULT, material)
        VanillaRecipeBuilders.shapeless {
            ingredient { +holderSet(CommonTagPrefixes.STORAGE_BLOCK, material) }
            result {
                +base
                count = 9
            }
            group = base.id.path
            recipeId suffix "_from_block"
        }.save(exporter)
        VanillaRecipeBuilders.shaped {
            hollow8()
            define('A') { +holderSet(basePart.tagPrefix, material) }
            define('B') { items { +base } }
            result { +block }
            category = RecipeCategory.BUILDING_BLOCKS
            group = block.item.id.path
        }.save(exporter)
    }

    private fun nineToBlock(material: RagiumMaterial, baseInput: Ingredient, base: HTSimpleDeferredItem) {
        val block: HTSimpleDeferredBlockAndItem = RagiumBlocks.STORAGE_BLOCKS[material] ?: return
        VanillaRecipeBuilders.shapeless {
            ingredient { +holderSet(CommonTagPrefixes.STORAGE_BLOCK, material) }
            result {
                +base
                count = 9
            }
            group = base.id.path
            recipeId suffix "_from_block"
        }.save(exporter)
        VanillaRecipeBuilders.shaped {
            hollow8()
            define('A') { +baseInput }
            define('B') { items { +base } }
            result { +block }
            category = RecipeCategory.BUILDING_BLOCKS
            group = block.item.id.path
        }.save(exporter)
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
