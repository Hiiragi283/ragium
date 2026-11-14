package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.builder.VanillaRecipeBuilders
import hiiragi283.lib.data.recipe.builder.ingredient
import hiiragi283.lib.data.recipe.builder.result
import hiiragi283.lib.data.recipe.ingredient.IngredientBuilder
import hiiragi283.lib.item.component.HTToolCollection
import hiiragi283.lib.item.component.HTToolType
import hiiragi283.lib.registry.HTDecorationContent
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.resource.vanillaId
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTCommonTags
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.HTStorageBlockPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.item.RagiumItems
import hiiragi283.ragium.common.item.component.RagiumToolMaterials
import hiiragi283.ragium.common.material.RagiumMaterialHelper
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.tags.ItemTags
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
        decoration()
        machine()
        storage()
        upgrade()
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
            define('B') { +holderSet(Tags.Items.SLIME_BALLS) }
            result {
                +RagiumItems.PARTICLE_BOARD
                count = 4
            }
        }.save(exporter)
        // Cement
        VanillaRecipeBuilders.shapeless {
            ingredient { +holderSet(Tags.Items.SANDS) }
            ingredient { +holderSet(Tags.Items.GRAVELS) }
            result {
                +RagiumItems.CEMENT
                count = 2
            }
        }.save(exporter)
        VanillaRecipeBuilders.shapeless {
            ingredient { +holderSet(Tags.Items.SANDS) }
            ingredient { +holderSet(Tags.Items.GRAVELS) }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.QUICK_LIME) }
            result {
                +RagiumItems.CEMENT
                count = 4
            }
            recipeId suffix "_with_lime"
        }.save(exporter)

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
        // Blank Music Disc
        VanillaRecipeBuilders.shaped {
            +" A "
            +"A A"
            +" A "
            define('A') { +holderSet(HTCommonTags.Items.PLASTICS) }
            result { +RagiumItems.BLANK_MUSIC_DISC }
        }.save(exporter)
        // Memory Disc
        VanillaRecipeBuilders.shaped {
            hollow4()
            define('A') { +holderSet(HTCommonTags.Items.PLASTICS) }
            define('B') { items { +RagiumItems.SILICON_WAFER } }
            result { +RagiumItems.MEMORY_DISC }
        }.save(exporter)

        // Wither Doll
        VanillaRecipeBuilders.shaped {
            +"AAA"
            +"BBB"
            +" B "
            define('A') { items { +Items.WITHER_SKELETON_SKULL } }
            define('B') { +holderSet(ItemTags.SOUL_FIRE_BASE_BLOCKS) }
            result { +RagiumItems.WITHER_DOLL }
        }.save(exporter)

        // XX Press Die
        VanillaRecipeBuilders.shaped {
            +"AA"
            +"AA"
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.SOOTY_IRON) }
            result { +RagiumItems.BLANK_DIE }
        }.save(exporter)
        for (pressDie: HTSimpleDeferredItem in RagiumItems.PRESS_DIES) {
            VanillaRecipeBuilders.stonecutting {
                ingredient { +holderSet(RagiumTags.Items.PRESS_DIES) }
                result { +pressDie }
            }.save(exporter)
        }
        // XX Mold
        VanillaRecipeBuilders.shaped {
            +"AA"
            +"AA"
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.GOLD) }
            result { +RagiumItems.BLANK_MOLD }
        }.save(exporter)
        for (mold: HTSimpleDeferredItem in RagiumItems.MOLDS) {
            VanillaRecipeBuilders.stonecutting {
                ingredient { +holderSet(RagiumTags.Items.MOLDS) }
                result { +mold }
            }.save(exporter)
        }

        // XX Tools
        registerTools(RagiumItems.SOOTY_IRON_TOOLS, RagiumToolMaterials.SOOTY_IRON)
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
                +setOf(RagiumMaterial.Fuel.COAL, RagiumMaterial.Fuel.CHARCOAL)
                    .map(CommonTagPrefixes.DUST::itemTagKey)
                    .let(::holderSet)
            }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.SULFUR) }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.NITER) }
            result {
                +Items.GUNPOWDER
                count = 3
            }
        }.save(exporter)
        // Blaze Rod
        VanillaRecipeBuilders.shaped {
            layered()
            define('A') { +holderSet(CommonTagPrefixes.GEM, RagiumMaterial.Gem.AMETHYST) }
            define('B') { items { +Items.MAGMA_BLOCK } }
            define('C') { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.SULFUR) }
            result { +Items.BLAZE_ROD }
        }.save(exporter)
        // Breeze Rod
        VanillaRecipeBuilders.shaped {
            layered()
            define('A') { +holderSet(CommonTagPrefixes.GEM, RagiumMaterial.Gem.AMETHYST) }
            define('B') { items { +Items.ICE } }
            define('C') { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.NITER) }
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
        // Fossil Fuels
        fun fossilFuels(outputs: Int, suffix: String, ingredient: IngredientBuilder.() -> Unit) {
            // Torch
            VanillaRecipeBuilders.shaped {
                +"A"
                +"B"
                define('A', ingredient)
                define('B') { +holderSet(Tags.Items.RODS_WOODEN) }
                result {
                    +Items.TORCH
                    count = outputs
                }
                recipeId suffix "_from_$suffix"
            }.save(exporter)
            // Fire Charge
            VanillaRecipeBuilders.shapeless {
                ingredient { +holderSet(Tags.Items.GUNPOWDERS) }
                ingredient { items { +Items.BLAZE_POWDER } }
                ingredient(ingredient)
                result {
                    +Items.FIRE_CHARGE
                    count = outputs - 2
                }
                recipeId suffix "_from_$suffix"
            }.save(exporter)
        }

        fossilFuels(3, "tar") { items { +RagiumItems.TAR } }
        fossilFuels(6, "pitch_coke") { +holderSet(HTCommonTags.Items.PITCH_COKE) }
        fossilFuels(8, "coal_coke") { +holderSet(HTCommonTags.Items.COAL_COKE) }
        // Iron Alt
        fun ironAlt(material: HTMaterialLike, modifier: Float) {
            // Hopper
            VanillaRecipeBuilders.shaped {
                +"A A"
                +"ABA"
                +" A "
                define('A') { +holderSet(CommonTagPrefixes.INGOT, material) }
                define('B') { +holderSet(Tags.Items.CHESTS_WOODEN) }
                result {
                    +Items.HOPPER
                    count = (count * modifier).toInt()
                }
                recipeId suffix "_by_${material.materialName}"
            }.save(exporter)
        }

        ironAlt(RagiumMaterial.Alloy.SOOTY_IRON, 2f)
        ironAlt(RagiumMaterial.Alloy.INVAR, 3f)
        ironAlt(RagiumMaterial.Alloy.BLACK_STEEL, 4f)
    }

    //    Decoration    //

    private fun decoration() {
        RagiumBlocks.ALL_DECORATIONS.forEach(::registerDecoration)

        // Quartz Glass Pane
        VanillaRecipeBuilders.shaped {
            +"AAA"
            +"AAA"
            define('A') { items { +RagiumBlocks.QUARTZ_GLASS } }
            result {
                +RagiumBlocks.QUARTZ_GLASS_PANE
                count = 8
            }
        }.save(exporter)
    }

    private fun registerDecoration(content: HTDecorationContent) {
        val (base, slab, stairs, wall) = content
        val stonecutterInput: IngredientBuilder.() -> Unit = {
            items {
                +base
                content.parent()?.base?.let { +it }
            }
        }
        // Slab
        VanillaRecipeBuilders.shaped {
            +"AAA"
            define('A') { items { +base } }
            result {
                +content.slab
                count = 6
            }
            group = content.slab.id().path
        }.save(exporter)
        VanillaRecipeBuilders.stonecutting {
            ingredient(stonecutterInput)
            result {
                +slab
                count = 2
            }
            group = slab.id().path
        }.save(exporter)
        // Stairs
        if (stairs != null) {
            VanillaRecipeBuilders.shaped {
                +"A  "
                +"AA "
                +"AAA"
                define('A') { items { +base } }
                result {
                    +stairs
                    count = 4
                }
                group = stairs.id().path
            }.save(exporter)
            VanillaRecipeBuilders.stonecutting {
                ingredient(stonecutterInput)
                result { +stairs }
                group = stairs.id().path
            }.save(exporter)
        }
        // Wall
        if (wall != null) {
            VanillaRecipeBuilders.shaped {
                +"AAA"
                +"AAA"
                define('A') { items { +base } }
                result {
                    +wall
                    count = 6
                }
                group = wall.id().path
            }.save(exporter)
            VanillaRecipeBuilders.stonecutting {
                ingredient(stonecutterInput)
                result { +wall }
                group = wall.id().path
            }.save(exporter)
        }
    }

    //    Machine    //

    private fun machine() {
        // Basic
        VanillaRecipeBuilders.shaped {
            hollow()
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.SOOTY_IRON) }
            result { +RagiumBlocks.MACHINE_CASINGS.basic }
            recipeId suffix "_from_sooty_iron"
        }.save(exporter)
        VanillaRecipeBuilders.shaped {
            hollow()
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.BLACK_STEEL) }
            result {
                +RagiumBlocks.MACHINE_CASINGS.basic
                count = 2
            }
            recipeId suffix "_from_black_steel"
        }.save(exporter)
        // Advanced
        // Elite

        // Decoration
        VanillaRecipeBuilders.shaped {
            +"ABA"
            +"B B"
            +"ABA"
            define('A') { +holderSet(CommonTagPrefixes.NUGGET, RagiumMaterial.Alloy.SOOTY_IRON) }
            define('B') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.SOOTY_IRON) }
            result {
                +RagiumBlocks.DECORATION_CASINGS.basic
                count = 4
            }
        }.save(exporter)

        sequenceOf(RagiumBlocks.DECORATION_CASINGS, RagiumBlocks.DECORATION_HULLS.values).flatten().forEach { block: HTSimpleDeferredBlockAndItem ->
            VanillaRecipeBuilders.stonecutting {
                ingredient { +holderSet(RagiumTags.BlockItems.DECORATIONS_MACHINE.item) }
                result { +block }
            }.save(exporter)
        }

        dynamo()
    }

    private fun dynamo() {
        // Fuel
        VanillaRecipeBuilders.shaped {
            +" A "
            +"BCB"
            +"DED"
            define('A') { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.REDSTONE) }
            define('B') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.SOOTY_IRON) }
            define('C') { +holderSet(CommonTagPrefixes.GEAR, RagiumMaterial.Metal.COPPER) }
            define('D') { items { +RagiumItems.getParts(HTMachineType.MECHANICAL) } }
            define('E') { +holderSet(Tags.Items.PLAYER_WORKSTATIONS_FURNACES) }
            result { +RagiumBlocks.FUEL_DYNAMO }
        }.save(exporter)
        // Magmatic
        VanillaRecipeBuilders.shaped {
            +" A "
            +"BCB"
            +"DED"
            define('A') { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.REDSTONE) }
            define('B') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.SOOTY_IRON) }
            define('C') { +holderSet(CommonTagPrefixes.GEAR, RagiumMaterial.Metal.IRON) }
            define('D') { items { +RagiumItems.getParts(HTMachineType.HEAT) } }
            define('E') { +holderSet(Tags.Items.BUCKETS_LAVA) }
            result { +RagiumBlocks.MAGMATIC_DYNAMO }
        }.save(exporter)
        // Combustion
        VanillaRecipeBuilders.shaped {
            +" A "
            +"BCB"
            +"DED"
            define('A') { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.REDSTONE) }
            define('B') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.BLACK_STEEL) }
            define('C') { +holderSet(CommonTagPrefixes.GEAR, RagiumMaterial.Metal.GOLD) }
            define('D') { items { +RagiumItems.getParts(HTMachineType.CHEMICAL) } }
            define('E') { items { +RagiumBlocks.TANK } }
            result { +RagiumBlocks.COMBUSTION_DYNAMO }
        }.save(exporter)
        // Thermal Generator
        // Photovoltaic
        // Fission
        // Disenchantment
    }

    //    Storage    //

    private fun storage() {
        // Tank
        VanillaRecipeBuilders.shaped {
            hollow8()
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.SOOTY_IRON) }
            define('B') { +holderSet(Tags.Items.BUCKETS_EMPTY) }
            result { +RagiumBlocks.TANK }
            recipeId suffix "_by_sooty_iron"
        }.save(exporter)
        VanillaRecipeBuilders.shaped {
            hollow4()
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.BLACK_STEEL) }
            define('B') { +holderSet(Tags.Items.BUCKETS_EMPTY) }
            result { +RagiumBlocks.TANK }
            recipeId suffix "_by_black_steel"
        }.save(exporter)
        // Potion Tank
        VanillaRecipeBuilders.shaped {
            hollow4()
            define('A') { +holderSet(Tags.Items.RODS_BLAZE) }
            define('B') { +holderSet(Tags.Items.BUCKETS_EMPTY) }
            result { +RagiumBlocks.POTION_TANK }
        }.save(exporter)
        // Void Tank
        VanillaRecipeBuilders.shapeless {
            ingredient { items { +RagiumBlocks.TANK } }
            ingredient { +holderSet(Tags.Items.ENDER_PEARLS) }
            result { +RagiumBlocks.VOID_TANK }
        }.save(exporter)
    }

    //    Upgrade    //

    private fun upgrade() {
        // Upgrade Station
        VanillaRecipeBuilders.shaped {
            cross8()
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.SOOTY_IRON) }
            define('B') { +holderSet(CommonTagPrefixes.GEAR, RagiumMaterial.Metal.IRON) }
            define('C') { +holderSet(Tags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES) }
            result { +RagiumBlocks.UPGRADE_STATION }
        }.save(exporter)

        // Basic
        VanillaRecipeBuilders.shaped {
            crossLayered()
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.IRON) }
            define('B') { +holderSet(Tags.Items.GLASS_BLOCKS_CHEAP) }
            define('C') { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.REDSTONE) }
            define('D') { +holderSet(CommonTagPrefixes.GEAR, RagiumMaterial.Metal.COPPER) }
            result { +RagiumItems.BASIC_UPGRADE }
        }.save(exporter)
        // Advanced
        VanillaRecipeBuilders.shaped {
            crossLayered()
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.GOLD) }
            define('B') { +holderSet(CommonTagPrefixes.GEM, RagiumMaterial.Gem.QUARTZ) }
            define('C') { +holderSet(CommonTagPrefixes.GEAR, RagiumMaterial.Metal.IRON) }
            define('D') { items { +RagiumItems.BASIC_UPGRADE } }
            result { +RagiumItems.ADVANCED_UPGRADE }
        }.save(exporter)
        // Elite
        VanillaRecipeBuilders.shaped {
            crossLayered()
            define('A') { +holderSet(CommonTagPrefixes.GEM, RagiumMaterial.Gem.DIAMOND) }
            define('B') { +holderSet(HTCommonTags.BlockItems.GLASS_BLOCKS_QUARTZ.item) }
            define('C') { +holderSet(CommonTagPrefixes.GEAR, RagiumMaterial.Metal.GOLD) }
            define('D') { items { +RagiumItems.ADVANCED_UPGRADE } }
            result { +RagiumItems.ELITE_UPGRADE }
        }.save(exporter)
        // Ultimate
        netheriteUpgrading {
            base { items { +RagiumItems.ELITE_UPGRADE } }
            result { +RagiumItems.ULTIMATE_UPGRADE }
        }
    }

    //    Material    //

    private fun material() {
        // XX <-> Storage Block
        gemBlock(RagiumMaterial.Gem.ECHO, RagiumBlocks.ECHO_BLOCK)
        gemBlock(RagiumMaterial.Gem.FLUORITE, RagiumBlocks.FLUORITE_BLOCK)
        // gemBlock(RagiumMaterial.Gem.CRYOLITE, RagiumBlocks.CRYOLITE_BLOCK)

        nineToBlock(RagiumMaterial.Metal.ALUMINUM, HTItemPart.INGOT)
        nineToBlock(RagiumMaterial.Metal.SILVER, HTItemPart.INGOT)
        nineToBlock(RagiumMaterial.Alloy.CONSTANTAN, HTItemPart.INGOT)
        nineToBlock(RagiumMaterial.Alloy.INVAR, HTItemPart.INGOT)
        nineToBlock(RagiumMaterial.Alloy.SOOTY_IRON, HTItemPart.INGOT)
        nineToBlock(RagiumMaterial.Alloy.BLACK_STEEL, HTItemPart.INGOT)
        nineToBlock(RagiumMaterial.Alloy.SHADOW_STEEL, HTItemPart.INGOT)
        // Ingot <-> Nugget
        ingotToNugget(RagiumMaterial.Metal.ALUMINUM)
        ingotToNugget(RagiumMaterial.Metal.SILVER)
        ingotToNugget(RagiumMaterial.Alloy.NETHERITE, ingot = HTSimpleDeferredItem(vanillaId("netherite_ingot")))
        ingotToNugget(RagiumMaterial.Alloy.CONSTANTAN)
        ingotToNugget(RagiumMaterial.Alloy.INVAR)
        ingotToNugget(RagiumMaterial.Alloy.SOOTY_IRON)
        ingotToNugget(RagiumMaterial.Alloy.BLACK_STEEL)
        ingotToNugget(RagiumMaterial.Alloy.SHADOW_STEEL)
        // 4x Small Dust -> Dust
        RagiumMaterial.entries.forEach { material: RagiumMaterial ->
            val smallDust: HTSimpleDeferredItem = RagiumItems.MATERIAL_ITEMS[HTItemPart.SMALL_DUST, material] ?: return@forEach
            val dust: HTSimpleDeferredItem = RagiumItems.MATERIAL_ITEMS[HTItemPart.DUST, material] ?: return@forEach
            VanillaRecipeBuilders.shaped {
                +"AA"
                +"AB"
                define('A') { +holderSet(CommonTagPrefixes.SMALL_DUST, material) }
                define('B') { items { +smallDust } }
                result { +dust }
                recipeId suffix "_from_small"
            }.save(exporter)
        }
        // Gear
        RagiumItems.getOrThrow(HTItemPart.GEAR, RagiumMaterial.Other.WOOD).let { gear: HTSimpleDeferredItem ->
            val inputs: HolderSet<Item> = holderSet(ItemTags.PLANKS)
            // Crafting
            VanillaRecipeBuilders.shaped {
                hollow4()
                define('A') { +inputs }
                define('B') { +holderSet(ItemTags.WOODEN_BUTTONS) }
                result { +gear }
            }.save(exporter)
            // Pressing
            RagiumRecipeBuilders.pressing {
                ingredient {
                    +inputs
                    count = 4
                }
                catalyst { items { +RagiumItems.GEAR_DIE } }
                result { +gear }
            }.save(exporter)
        }
        gear(CommonTagPrefixes.GEM, RagiumMaterial.Gem.DIAMOND)
        gear(CommonTagPrefixes.GEM, RagiumMaterial.Gem.EMERALD)
        gear(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.COPPER)
        gear(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.IRON)
        gear(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.GOLD)
        RagiumItems.MATERIAL_ITEMS[HTItemPart.GEAR, RagiumMaterial.Alloy.NETHERITE]?.let {
            netheriteUpgrading {
                base { +holderSet(CommonTagPrefixes.GEAR, RagiumMaterial.Gem.DIAMOND) }
                result { +it }
            }
        }
        gear(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.INVAR)
        // Dust -> Ingot
        for (metalLike: RagiumMaterial.MetalLike in RagiumMaterial.MetalLike.entries) {
            val dust: HTSimpleDeferredItem = RagiumItems.MATERIAL_ITEMS[HTItemPart.DUST, metalLike] ?: continue
            val item: HTSimpleDeferredItem = RagiumMaterialHelper.getIngot(metalLike) ?: continue
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
            val base: HTSimpleDeferredItem = RagiumMaterialHelper.getBase(fuel)
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
            RagiumItems.getOrThrow(HTItemPart.INGOT, RagiumMaterial.Alloy.SOOTY_IRON)
        VanillaRecipeBuilders.shaped {
            hollow8()
            define('A') {
                +setOf(RagiumMaterial.Fuel.COAL, RagiumMaterial.Fuel.CHARCOAL)
                    .map(CommonTagPrefixes.TINY::itemTagKey)
                    .let(::holderSet)
            }
            define('B') { +ironIngot }
            result { +sootyIronIngot }
            group = sootyIronIngot.id.path
        }.save(exporter)
        VanillaRecipeBuilders.shaped {
            hollow4()
            define('A') {
                +setOf(RagiumMaterial.Fuel.COAL_COKE, RagiumMaterial.Fuel.PITCH_COKE)
                    .map(CommonTagPrefixes.TINY::itemTagKey)
                    .let(::holderSet)
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
            group = block.id().path
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
        nugget: HTSimpleDeferredItem? = RagiumItems.MATERIAL_ITEMS[HTItemPart.NUGGET, material],
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
        val inputs: HolderSet<Item> = holderSet(basePrefix, material)
        val gear: HTSimpleDeferredItem = RagiumItems.getOrThrow(HTItemPart.GEAR, material)
        // Crafting
        VanillaRecipeBuilders.shaped {
            hollow4()
            define('A') { +inputs }
            define('B') { +holderSet(CommonTagPrefixes.GEAR, RagiumMaterial.Other.WOOD) }
            result { +gear }
        }.save(exporter)
        // Pressing
        RagiumRecipeBuilders.pressing {
            ingredient {
                +inputs
                count = 4
            }
            catalyst { items { +RagiumItems.GEAR_DIE } }
            result { +gear }
        }.save(exporter)
    }

    override fun getName(): String = "Vanilla Recipes"
}
