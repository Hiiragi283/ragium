package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.builder.VanillaRecipeBuilders
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTCommonTags
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.registry.HTTieredContents
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class RagiumMachineRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) :
    HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun exportValues() {
        casings()
        parts()

        generators()
        processor()
    }

    override fun getName(): String = "Machine Recipes"

    data object MachineParts {
        val MECHANICAL: HTSimpleDeferredItem by lazy { RagiumItems.getParts(HTMachineType.MECHANICAL) }
        val HEAT: HTSimpleDeferredItem by lazy { RagiumItems.getParts(HTMachineType.HEAT) }
        val CHEMICAL: HTSimpleDeferredItem by lazy { RagiumItems.getParts(HTMachineType.CHEMICAL) }
        val RESOURCE: HTSimpleDeferredItem by lazy { RagiumItems.getParts(HTMachineType.RESOURCE) }
        val ELECTRONICS: HTSimpleDeferredItem by lazy { RagiumItems.getParts(HTMachineType.ELECTRONICS) }
        val ARCANE: HTSimpleDeferredItem by lazy { RagiumItems.getParts(HTMachineType.ARCANE) }
    }

    private fun casings() {
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
    }

    private fun parts() {
        // Mechanical: Metal + Gear + Circuit -> Parts
        VanillaRecipeBuilders.shapeless {
            ingredient { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.SOOTY_IRON) }
            ingredient { +holderSet(CommonTagPrefixes.GEAR, RagiumMaterial.Metal.COPPER) }
            ingredient { +holderSet(CommonTagPrefixes.CIRCUITS, HTTieredContents.MATERIAL.basic) }
            result {
                +MachineParts.MECHANICAL
                count = 3
            }
            recipeId suffix "_by_sooty_iron_and_copper"
        }.save(exporter)
        VanillaRecipeBuilders.shapeless {
            ingredient { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.SOOTY_IRON) }
            ingredient { +holderSet(CommonTagPrefixes.GEAR, RagiumMaterial.Alloy.INVAR) }
            ingredient { +holderSet(CommonTagPrefixes.CIRCUITS, HTTieredContents.MATERIAL.basic) }
            result {
                +MachineParts.MECHANICAL
                count = 6
            }
            recipeId suffix "_by_sooty_iron_and_invar"
        }.save(exporter)
        VanillaRecipeBuilders.shapeless {
            ingredient { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.BLACK_STEEL) }
            ingredient { +holderSet(CommonTagPrefixes.GEAR, RagiumMaterial.Metal.COPPER) }
            ingredient { +holderSet(CommonTagPrefixes.CIRCUITS, HTTieredContents.MATERIAL.basic) }
            result {
                +MachineParts.MECHANICAL
                count = 6
            }
            recipeId suffix "_by_black_steel_and_copper"
        }.save(exporter)
        VanillaRecipeBuilders.shapeless {
            ingredient { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Alloy.BLACK_STEEL) }
            ingredient { +holderSet(CommonTagPrefixes.GEAR, RagiumMaterial.Alloy.INVAR) }
            ingredient { +holderSet(CommonTagPrefixes.CIRCUITS, HTTieredContents.MATERIAL.basic) }
            result {
                +MachineParts.MECHANICAL
                count = 12
            }
            recipeId suffix "_by_black_steel_and_invar"
        }.save(exporter)
        // Heat
        // Chemical
        // Resource
        // Electronics
        // Arcane
    }

    private fun generators() {
        // Mechanical
        VanillaRecipeBuilders.shaped {
            cross8()
            define('A') { +holderSet(ItemTags.WOODEN_SLABS) }
            define('B') { +holderSet(ItemTags.PLANKS) }
            define('C') { items { +RagiumBlocks.MACHINE_CASINGS.basic } }
            result { +RagiumBlocks.WATER_MILL }
        }.save(exporter)
        VanillaRecipeBuilders.shaped {
            hollow4()
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.IRON) }
            define('B') { items { +RagiumBlocks.MACHINE_CASINGS.basic } }
            result { +RagiumBlocks.WIND_MILL }
        }.save(exporter)
        // Heat
        VanillaRecipeBuilders.shaped {
            +"AAA"
            +"ABA"
            +"CCC"
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.COPPER) }
            define('B') { +holderSet(Tags.Items.PLAYER_WORKSTATIONS_FURNACES) }
            define('C') { items { +Items.BRICKS } }
            result { +RagiumBlocks.SOLID_BOILER }
        }.save(exporter)
        VanillaRecipeBuilders.shaped {
            +"AAA"
            +"ABA"
            +"CCC"
            define('A') { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.SILVER) }
            define('B') { +holderSet(Tags.Items.PLAYER_WORKSTATIONS_FURNACES) }
            define('C') { items { +Items.BRICKS } }
            result { +RagiumBlocks.SOLAR_BOILER }
        }.save(exporter)
        // Chemical
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
        // Resource
        // Electronics
        // Arcane
    }

    private fun processor() {
        // Mechanical
        VanillaRecipeBuilders.shapeless {
            ingredient { items { +RagiumBlocks.MACHINE_CASINGS.basic } }
            ingredient { items { +Items.SMOOTH_STONE } }
            ingredient { items { +MachineParts.MECHANICAL } }
            result { +RagiumBlocks.COMPRESSOR }
        }.save(exporter)
        VanillaRecipeBuilders.shapeless {
            ingredient { items { +RagiumBlocks.MACHINE_CASINGS.basic } }
            ingredient { items { +Items.HOPPER } }
            ingredient { items { +MachineParts.MECHANICAL } }
            result { +RagiumBlocks.EXTRACTOR }
        }.save(exporter)
        VanillaRecipeBuilders.shapeless {
            ingredient { items { +RagiumBlocks.MACHINE_CASINGS.basic } }
            ingredient { items { +Items.FLINT } }
            ingredient { items { +MachineParts.MECHANICAL } }
            result { +RagiumBlocks.PULVERIZER }
        }.save(exporter)

        VanillaRecipeBuilders.shaped {
            crossLayered()
            define('A') { +holderSet(CommonTagPrefixes.CIRCUITS, HTTieredContents.MATERIAL.advanced) }
            define('B') { items { +MachineParts.MECHANICAL } }
            define('C') { items { +Items.CRAFTER } }
            define('D') { items { +RagiumBlocks.MACHINE_CASINGS.advanced } }
            result { +RagiumBlocks.ASSEMBLER }
        }.save(exporter)
        VanillaRecipeBuilders.shaped {
            crossLayered()
            define('A') { items { +Items.GRINDSTONE } }
            define('B') { items { +MachineParts.MECHANICAL } }
            define('C') { items { +RagiumBlocks.PULVERIZER } }
            define('D') { items { +RagiumBlocks.MACHINE_CASINGS.advanced } }
            result { +RagiumBlocks.CRUSHER }
        }.save(exporter)
        VanillaRecipeBuilders.shaped {
            crossLayered()
            define('A') { items { +Items.PISTON } }
            define('B') { items { +MachineParts.MECHANICAL } }
            define('C') { items { +RagiumBlocks.COMPRESSOR } }
            define('D') { items { +RagiumBlocks.MACHINE_CASINGS.advanced } }
            result { +RagiumBlocks.PRESS_MACHINE }
        }.save(exporter)
        // Heat
        VanillaRecipeBuilders.shapeless {
            ingredient { items { +RagiumBlocks.MACHINE_CASINGS.basic } }
            ingredient { +holderSet(Tags.Items.SANDS) }
            ingredient { items { +MachineParts.HEAT } }
            result { +RagiumBlocks.DRYER }
        }.save(exporter)
        VanillaRecipeBuilders.shapeless {
            ingredient { items { +RagiumBlocks.MACHINE_CASINGS.basic } }
            ingredient { +holderSet(Tags.Items.BUCKETS_WATER) }
            ingredient { items { +MachineParts.HEAT } }
            result { +RagiumBlocks.FREEZER }
        }.save(exporter)
        VanillaRecipeBuilders.shapeless {
            ingredient { items { +RagiumBlocks.MACHINE_CASINGS.basic } }
            ingredient { +holderSet(Tags.Items.BUCKETS_LAVA) }
            ingredient { items { +MachineParts.HEAT } }
            result { +RagiumBlocks.MELTER }
        }.save(exporter)
        VanillaRecipeBuilders.shapeless {
            ingredient { items { +RagiumBlocks.MACHINE_CASINGS.basic } }
            ingredient { +holderSet(Tags.Items.PLAYER_WORKSTATIONS_FURNACES) }
            ingredient { items { +MachineParts.HEAT } }
            result { +RagiumBlocks.SMELTER }
        }.save(exporter)

        VanillaRecipeBuilders.shaped {
            crossLayered()
            define('A') { +holderSet(Tags.Items.BRICKS_NORMAL) }
            define('B') { items { +MachineParts.HEAT } }
            define('C') { items { +RagiumBlocks.SMELTER } }
            define('D') { items { +RagiumBlocks.MACHINE_CASINGS.advanced } }
            result { +RagiumBlocks.ALLOY_SMELTER }
        }.save(exporter)
        VanillaRecipeBuilders.shaped {
            crossLayered()
            define('A') { +holderSet(Tags.Items.BRICKS_NETHER) }
            define('B') { items { +MachineParts.HEAT } }
            define('C') { items { +RagiumBlocks.SMELTER } }
            define('D') { items { +RagiumBlocks.MACHINE_CASINGS.advanced } }
            result { +RagiumBlocks.PYROLYZER }
        }.save(exporter)

        VanillaRecipeBuilders.shaped {
            crossLayered()
            define('A') { items { +RagiumBlocks.EXTRACTOR } }
            define('B') { items { +MachineParts.HEAT } }
            define('C') { items { +RagiumBlocks.PYROLYZER } }
            define('D') { items { +RagiumBlocks.MACHINE_CASINGS.industrial } }
            result { +RagiumBlocks.REFINERY }
        }.save(exporter)
        // Chemical
        VanillaRecipeBuilders.shapeless {
            ingredient { items { +RagiumBlocks.MACHINE_CASINGS.basic } }
            ingredient { +holderSet(HTCommonTags.BlockItems.GLASS_BLOCKS_QUARTZ.item) }
            ingredient { items { +MachineParts.CHEMICAL } }
            result { +RagiumBlocks.AUTOCLAVE }
        }.save(exporter)
        VanillaRecipeBuilders.shapeless {
            ingredient { items { +RagiumBlocks.MACHINE_CASINGS.basic } }
            ingredient { items { +Items.CAULDRON } }
            ingredient { items { +MachineParts.CHEMICAL } }
            result { +RagiumBlocks.CHEMICAL_BATH }
        }.save(exporter)
        // Resource
        VanillaRecipeBuilders.shapeless {
            ingredient { items { +RagiumBlocks.MACHINE_CASINGS.basic } }
            ingredient { items { +Items.BREWING_STAND } }
            ingredient { items { +MachineParts.RESOURCE } }
            result { +RagiumBlocks.BREWERY }
        }.save(exporter)
        // Electronics
        // Arcane
    }
}
