@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.color.HTDefaultColor
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.item.HTSimpleItemLike
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.data.recipe.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.crafting.HTBatteryCombiningRecipe
import hiiragi283.ragium.common.crafting.HTTankCombiningRecipe
import hiiragi283.ragium.common.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.common.item.component.HTDefaultLootTickets
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import java.util.concurrent.CompletableFuture
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.neoforged.neoforge.common.Tags

class RagiumUtilitiesRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun buildRecipes() {
        parts()

        // Blueprint
        HTMixingRecipeBuilder.create {
            itemIngredient { +Items.PAPER }
            fluidIngredient {
                +HCFluids.DYES[HTDefaultColor.BLUE]
                amount = 250
            }
            itemResult { +HCItems.BLUEPRINT }
        }.save(exporter)
        // Blank Disc
        HTShapedRecipeBuilder.create {
            +" A "
            +"A A"
            +" A "
            define('A') { +HiiragiCoreTags.Items.PLASTICS }
            +RagiumItems.BLANK_DISC.toStack()
        }.save(exporter)
        // Electric Igniter
        HTShapelessRecipeBuilder.create {
            ingredient { +tag(CommonTagPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY) }
            ingredient { +Items.FLINT }
            result { +RagiumItems.ELECTRIC_IGNITER }
        }.save(exporter)

        // Loot Ticket
        lootTickets()

        machines()
        devices()
        storages()
    }

    private fun parts() {
        // Mercury Bottle <-> Mercury Bucket
        HTShapelessRecipeBuilder.create {
            repeat(4) { ingredient { +RagiumItems.MERCURY_BOTTLE } }
            ingredient { +Tags.Items.BUCKETS_EMPTY }
            +RagiumFluids.MERCURY.bucketHolder.toStack()
            recipeId suffix "_from_bottles"
        }.save(exporter)
        HTShapelessRecipeBuilder.create {
            ingredient { +RagiumFluids.MERCURY.bucketTag }
            repeat(4) { ingredient { +Items.GLASS_BOTTLE } }
            +RagiumItems.MERCURY_BOTTLE.toStack(4)
            recipeId suffix "_from_bucket"
        }.save(exporter)
        // Thermometer
        HTShapedRecipeBuilder.create {
            +" AB"
            +"ACA"
            +"DA "
            define('A') { +Tags.Items.GLASS_PANES_COLORLESS }
            define('B') { +Tags.Items.DYES_RED }
            define('C') { +RagiumItems.MERCURY_BOTTLE }
            define('D') { +tag(CommonTagPrefixes.PLATE, VanillaMaterialKeys.COPPER) }
            +RagiumItems.THERMOMETER.toStack()
        }.save(exporter)
    }

    //    Machine    //

    private fun machines() {
        // Basic
        basic(RagiumBlocks.ALLOY_SMELTER) { +Items.FURNACE }
        basic(RagiumBlocks.ASSEMBLER) { +Items.CRAFTER }
        basic(RagiumBlocks.AUTO_CHISEL) { +Items.STONECUTTER }
        basic(RagiumBlocks.COMPRESSOR) { +Items.PISTON }
        basic(RagiumBlocks.CRUSHER) { +tag(CommonTagPrefixes.GEM, VanillaMaterialKeys.DIAMOND) }
        basic(RagiumBlocks.CUTTING_MACHINE) { +Items.IRON_AXE }
        basic(RagiumBlocks.ELECTRIC_FURNACE) { +Items.FURNACE }
        basic(RagiumBlocks.PLANTER) { +Tags.Items.GLASS_BLOCKS }
        // Heat
        advanced(RagiumBlocks.FREEZER, HCMaterialKeys.AZURE_STEEL) { +Items.SNOW_BLOCK }
        advanced(RagiumBlocks.MELTER, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY) { +Items.BLAST_FURNACE }
        advanced(RagiumBlocks.PYROLYZER, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY) { +Items.NETHER_BRICKS }
        HTShapedRecipeBuilder.create {
            +"ABA"
            +"BCB"
            define('A') { +RagiumItems.THERMOMETER }
            define('B') { +Tags.Items.GLASS_BLOCKS }
            define('C') { +tag(CommonTagPrefixes.GEAR, VanillaMaterialKeys.DIAMOND) }
            +RagiumBlocks.REFINERY.toStack()
        }.save(exporter)
        advanced(RagiumBlocks.WASHER, HCMaterialKeys.AZURE_STEEL) { +Items.IRON_BARS }
        // Elite
        /*HTShapedRecipeBuilder.create) {
            pattern(
                "ABA",
                "BCB",
            )
            define('A') += RagiumItems.ELECTRIC_CIRCUIT
            define('B') += Tags.Items.GLASS_BLOCKS_TINTED
            define('C') += CommonTagPrefixes.GEAR to RagiumMaterialKeys.STAINLESS_STEEL
            resultStack += RagiumBlocks.CHEMICAL_WASHER
        }*/
        elite(RagiumBlocks.BREWERY) { +Items.BREWING_STAND }
        elite(RagiumBlocks.MIXER) { +Items.COPPER_GRATE }
        // Ultimate
        ultimate(RagiumBlocks.MASS_FABRICATOR) { +Tags.Items.NETHER_STARS }
        HTShapedRecipeBuilder.create {
            +"ABA"
            +"BCB"
            define('A') { +RagiumItems.ARTIFICIAL_ARTIFACT }
            define('B') { +RagiumBlocks.TANK }
            define('C') { +tag(CommonTagPrefixes.GEAR, VanillaMaterialKeys.NETHERITE) }
            +RagiumBlocks.FLUID_DUPLICATOR.toStack()
        }.save(exporter)
    }

    private inline fun basic(block: HTSimpleItemLike, builderAction: IngredientBuilder.() -> Unit) {
        HTShapedRecipeBuilder.create {
            +"AAA"
            +"BCB"
            +"DDD"
            define('A') { +tag(CommonTagPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY) }
            define('B', builderAction)
            define('C') { +tag(CommonTagPrefixes.GEAR, VanillaMaterialKeys.COPPER) }
            define('D') { +tag(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON) }
            +block.toStack()
        }.save(exporter)
    }

    private inline fun advanced(block: HTSimpleItemLike, key: HTMaterialKey, builderAction: IngredientBuilder.() -> Unit) {
        HTShapedRecipeBuilder.create {
            +"AAA"
            +"BCB"
            +"DDD"
            define('A') { +tag(CommonTagPrefixes.INGOT, key) }
            define('B', builderAction)
            define('C') { +RagiumItems.THERMOMETER }
            define('D') { +tag(CommonTagPrefixes.INGOT, CommonMaterialKeys.STEEL) }
            +block.toStack()
        }.save(exporter)
    }

    private inline fun elite(block: HTSimpleItemLike, builderAction: IngredientBuilder.() -> Unit) {
        HTShapedRecipeBuilder.create {
            +"AAA"
            +"BCB"
            +"DDD"
            define('A') { +tag(CommonTagPrefixes.PLATE, RagiumMaterialKeys.STAINLESS_STEEL) }
            define('B', builderAction)
            define('C') { +RagiumItems.ELECTRIC_CIRCUIT }
            define('D') { +tag(CommonTagPrefixes.PLATE, CommonMaterialKeys.CARBON) }
            +block.toStack()
        }.save(exporter)
    }

    private inline fun ultimate(block: HTSimpleItemLike, builderAction: IngredientBuilder.() -> Unit) {
        HTShapedRecipeBuilder.create {
            +"AAA"
            +"BCB"
            +"DDD"
            define('A') { +tag(CommonTagPrefixes.INGOT, VanillaMaterialKeys.NETHERITE) }
            define('B', builderAction)
            define('C') { +RagiumItems.ARTIFICIAL_ARTIFACT }
            define('D') { +Tags.Items.OBSIDIANS_CRYING }
            +block.toStack()
        }.save(exporter)
    }

    //    Device    //

    private fun devices() {}

    //    Storage    //

    private fun storages() {
        // Battery
        variableStorage(
            RagiumBlocks.BATTERY,
            VanillaMaterialKeys.GOLD,
            CommonTagPrefixes.GEM.itemTagKey(RagiumMaterialKeys.RAGI_CRYSTAL),
            CommonTagPrefixes.STORAGE_BLOCK.itemTagKey(RagiumMaterialKeys.RAGI_CRYSTAL),
        )
        exporter.accept(id(HTConst.SHAPELESS, "combining", "battery"), HTBatteryCombiningRecipe(CraftingBookCategory.MISC))
        // Crate
        variableStorage(RagiumBlocks.CRATE, CommonMaterialKeys.PLASTIC, Tags.Items.CHESTS)
        // Tank
        variableStorage(RagiumBlocks.TANK, CommonMaterialKeys.RUBBER, Tags.Items.BUCKETS_EMPTY)
        exporter.accept(id(HTConst.SHAPELESS, "combining", "tank"), HTTankCombiningRecipe(CraftingBookCategory.MISC))
        // Universal Chest
        HTShapedRecipeBuilder.create {
            hollow8()
            define('A') { +tag(CommonTagPrefixes.INGOT, CommonMaterialKeys.STEEL) }
            define('B') { +tag(CommonTagPrefixes.GEM, HCMaterialKeys.WARPED_CRYSTAL) }
            +RagiumBlocks.UNIVERSAL_CHEST.toStack()
        }.save(exporter)

        for (color: HTDefaultColor in HTDefaultColor.entries) {
            HTShapelessRecipeBuilder.create {
                ingredient { +RagiumBlocks.UNIVERSAL_CHEST }
                ingredient { +color.dyesTag }
                +createItemStack(RagiumBlocks.UNIVERSAL_CHEST, HCDataComponents.COLOR, color)
                recipeId prefix "${color.serializedName}_"
            }.save(exporter)
        }
    }

    private fun variableStorage(
        block: HTSimpleItemLike,
        top: HTMaterialKey,
        core: TagKey<Item>,
        largeCore: TagKey<Item> = core,
    ) {
        // Shaped
        val defaultPart: TagKey<Item> = materialManager[top]?.getDefaultPart(top) ?: return
        HTShapedRecipeBuilder.create {
            crossLayered()
            define('A') { +tag(CommonTagPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY) }
            define('B') { +defaultPart }
            define('C') { +Tags.Items.GLASS_BLOCKS }
            define('D') { +core }
            +block.toStack()
        }.save(exporter)
        // x10 Capacity
        HTShapedRecipeBuilder.create {
            crossLayered()
            define('A') { +tag(CommonTagPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.RAGI_ALLOY) }
            define('B') { +tag(CommonTagPrefixes.STORAGE_BLOCK, top) }
            define('C') { +Tags.Items.GLASS_BLOCKS }
            define('D') { +largeCore }
            +createItemStack(block, RagiumDataComponents.CAPACITY_SCALE, 10)
            recipeId prefix "larger_"
        }.save(exporter)
    }

    //    Other    //

    private fun lootTickets() {
        HTShapedRecipeBuilder.create {
            cross8()
            define('A') { +RagiumItems.RAGI_MATTER }
            define('B') { +Tags.Items.DYES_RED }
            define('C') { +Items.PAPER }
            +RagiumItems.RAGI_TICKET.toStack(4)
            category = RecipeCategory.TOOLS
        }.save(exporter)

        // End City
        addLootTicket(HTDefaultLootTickets.END_CITY) {
            ingredient { +Items.PURPUR_BLOCK }
            ingredient { +Items.SHULKER_SHELL }
        }
        // Simple Dungeon
        addLootTicket(HTDefaultLootTickets.DUNGEON) {
            ingredient { +Tags.Items.COBBLESTONES_MOSSY }
            ingredient { +Items.ROTTEN_FLESH }
        }
        // Mineshaft
        addLootTicket(HTDefaultLootTickets.MINESHAFT) {
            ingredient { +ItemTags.PLANKS }
            ingredient { +ItemTags.RAILS }
        }
        // Nether Fortress
        addLootTicket(HTDefaultLootTickets.NETHER_FORTRESS) {
            ingredient { +Items.NETHER_BRICKS }
            ingredient { +Tags.Items.CROPS_NETHER_WART }
        }

        // Desert Pyramid
        addLootTicket(HTDefaultLootTickets.DESERT_PYRAMID) {
            ingredient { +Tags.Items.SANDSTONE_UNCOLORED_BLOCKS }
            ingredient { +tag(CommonTagPrefixes.INGOT, VanillaMaterialKeys.GOLD) }
        }
        // Jungle Temple
        addLootTicket(HTDefaultLootTickets.TEMPLE) {
            ingredient { +Tags.Items.COBBLESTONES_MOSSY }
            ingredient { +Items.VINE }
        }
        // Igloo Chest
        addLootTicket(HTDefaultLootTickets.IGLOO) {
            ingredient { +Items.SNOW_BLOCK }
            ingredient { +ItemTags.BEDS }
        }
        // Mansion
        addLootTicket(HTDefaultLootTickets.MANSION) {
            ingredient { +Items.DARK_OAK_PLANKS }
            ingredient { +tag(CommonTagPrefixes.GEM, VanillaMaterialKeys.EMERALD) }
        }

        // Buried Treasure
        addLootTicket(HTDefaultLootTickets.BURIED_TREASURE) {
            ingredient { +Tags.Items.SANDS_COLORLESS }
            ingredient { +Items.PUFFERFISH }
        }
        // Shipwreck
        addLootTicket(HTDefaultLootTickets.SHIPWRECK) {
            ingredient { +Tags.Items.CHESTS_WOODEN }
            ingredient { +Items.KELP }
        }
        // Bastion Remnant
        addLootTicket(HTDefaultLootTickets.BASTION_REMNANT) {
            ingredient { +Items.BLACKSTONE }
            ingredient { +tag(CommonTagPrefixes.INGOT, VanillaMaterialKeys.GOLD) }
        }
        // Ancient City
        addLootTicket(HTDefaultLootTickets.ANCIENT_CITY) {
            ingredient { +Items.DEEPSLATE_TILES }
            ingredient { +tag(CommonTagPrefixes.GEM, VanillaMaterialKeys.ECHO) }
        }
        // Ruined Portal
        addLootTicket(HTDefaultLootTickets.RUINED_PORTAL) {
            ingredient { +Tags.Items.OBSIDIANS_NORMAL }
            ingredient { +Tags.Items.CROPS_NETHER_WART }
        }
    }

    private inline fun addLootTicket(lootTicket: HTDefaultLootTickets, builderAction: HTShapelessRecipeBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        HTShapelessRecipeBuilder.create {
            ingredient { +RagiumItems.RAGI_TICKET }
            builderAction()
            +HTDefaultLootTickets.getLootTicket(lootTicket)
            recipeId suffix "/${lootTicket.name.lowercase()}"
            category = RecipeCategory.TOOLS
        }.save(exporter)
    }

    override fun getName(): String = "Utilities Recipes"
}
