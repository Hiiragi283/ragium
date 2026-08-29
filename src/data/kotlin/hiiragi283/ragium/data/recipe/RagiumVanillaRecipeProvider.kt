@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.color.HTDefaultColor
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.item.HTItemInstanceLike
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
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.crafting.HTBatteryCombiningRecipe
import hiiragi283.ragium.common.crafting.HTTankCombiningRecipe
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
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
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.neoforged.neoforge.common.Tags

class RagiumVanillaRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun buildRecipes() {
        // Heating Coil
        HTShapedRecipeBuilder.create {
            hollow()
            define('A') { +tag(CommonTagPrefixes.PLATE, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY) }
            +RagiumBlocks.HEATING_COIL.toStack()
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

        machines()
        storages()

        lootTickets()
    }

    override fun getName(): String = "Vanilla Recipes"

    //    Machine    //

    private fun machines() {
        // Mechanical
        mechanical(RagiumBlocks.ALLOY_SMELTER) { +Items.FURNACE }
        mechanical(RagiumBlocks.ASSEMBLER) { +Items.CRAFTER }
        mechanical(RagiumBlocks.AUTO_CHISEL) { +Items.STONECUTTER }
        mechanical(RagiumBlocks.COMPRESSOR) { +Items.PISTON }
        mechanical(RagiumBlocks.CRUSHER) { +tag(CommonTagPrefixes.GEM, VanillaMaterialKeys.DIAMOND) }
        mechanical(RagiumBlocks.CUTTING_MACHINE) { +Items.IRON_AXE }
        mechanical(RagiumBlocks.ELECTRIC_FURNACE) { +Items.FURNACE }
        // Heat
        cold(RagiumBlocks.FREEZER) { +Items.SNOW_BLOCK }
        heat(RagiumBlocks.MELTER) { +Items.BLAST_FURNACE }
        heat(RagiumBlocks.PYROLYZER) { +Items.NETHER_BRICKS }
        HTShapedRecipeBuilder.create {
            +"ABA"
            +"BCB"
            define('A') { +RagiumBlocks.HEATING_COIL }
            define('B') { +Tags.Items.GLASS_BLOCKS }
            define('C') { +tag(CommonTagPrefixes.GEAR, VanillaMaterialKeys.DIAMOND) }
            +RagiumBlocks.REFINERY.toStack()
        }.save(exporter)
        // Chemical
        chemical(RagiumBlocks.MIXER) { +Items.COPPER_GRATE }
        chemical(RagiumBlocks.WASHER) { +Items.IRON_BARS }
        // Bio
        chemical(RagiumBlocks.BREWERY) { +Items.BREWING_STAND }
        mechanical(RagiumBlocks.PLANTER) { +Tags.Items.GLASS_BLOCKS }
        // Electronics
        electronics(RagiumBlocks.PRINTER) { +RagiumItems.ELECTRIC_CIRCUIT }
        electronics(RagiumBlocks.SCANNER) { +RagiumItems.MEMORY_DISC }
        // Arcane
        arcane(RagiumBlocks.MASS_FABRICATOR) { +Tags.Items.NETHER_STARS }
        HTShapedRecipeBuilder.create {
            +"ABA"
            +"BCB"
            define('A') { +RagiumItems.ARTIFICIAL_ARTIFACT }
            define('B') { +RagiumBlocks.TANK }
            define('C') { +tag(CommonTagPrefixes.GEAR, VanillaMaterialKeys.NETHERITE) }
            +RagiumBlocks.FLUID_DUPLICATOR.toStack()
        }.save(exporter)
    }

    private inline fun mechanical(block: HTItemInstanceLike, builderAction: IngredientBuilder.() -> Unit) {
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

    private inline fun heat(block: HTItemInstanceLike, builderAction: IngredientBuilder.() -> Unit) {
        HTShapedRecipeBuilder.create {
            +"AAA"
            +"BCB"
            +"DDD"
            define('A') { +tag(CommonTagPrefixes.INGOT, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY) }
            define('B', builderAction)
            define('C') { +RagiumBlocks.HEATING_COIL }
            define('D') { +tag(CommonTagPrefixes.INGOT, CommonMaterialKeys.STEEL) }
            +block.toStack()
        }.save(exporter)
    }

    private inline fun chemical(block: HTItemInstanceLike, builderAction: IngredientBuilder.() -> Unit) {
        HTShapedRecipeBuilder.create {
            +"AAA"
            +"BCB"
            +"DDD"
            define('A') { +tag(CommonTagPrefixes.INGOT, RagiumMaterialKeys.STAINLESS_STEEL) }
            define('B', builderAction)
            define('C') { +RagiumItems.THERMOMETER }
            define('D') { +tag(CommonTagPrefixes.PLATE, CommonMaterialKeys.CARBON) }
            +block.toStack()
        }.save(exporter)
    }

    private inline fun cold(block: HTItemInstanceLike, builderAction: IngredientBuilder.() -> Unit) {
        HTShapedRecipeBuilder.create {
            +"AAA"
            +"BCB"
            +"DDD"
            define('A') { +tag(CommonTagPrefixes.INGOT, HCMaterialKeys.AZURE_STEEL) }
            define('B', builderAction)
            define('C') { +RagiumBlocks.HEATING_COIL }
            define('D') { +tag(CommonTagPrefixes.INGOT, CommonMaterialKeys.STEEL) }
            +block.toStack()
        }.save(exporter)
    }

    private inline fun electronics(block: HTItemInstanceLike, builderAction: IngredientBuilder.() -> Unit) {
        HTShapedRecipeBuilder.create {
            +"AAA"
            +"BCB"
            +"DDD"
            define('A') { +tag(CommonTagPrefixes.INGOT, RagiumMaterialKeys.STAINLESS_STEEL) }
            define('B', builderAction)
            define('C') { +RagiumItems.LASER_EMITTER }
            define('D') { +tag(CommonTagPrefixes.INGOT, CommonMaterialKeys.ALUMINUM) }
            +block.toStack()
        }.save(exporter)
    }

    private inline fun arcane(block: HTItemInstanceLike, builderAction: IngredientBuilder.() -> Unit) {
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
        block: HTItemInstanceLike,
        top: HTMaterialKey,
        core: TagKey<Item>,
        largeCore: TagKey<Item> = core,
    ) {
        // Shaped
        val defaultPart: TagKey<Item> = materialManager[top]?.getDefaultPart(top) ?: return
        val stack: ItemStack = block.toStack()
        HTShapedRecipeBuilder.create {
            crossLayered()
            define('A') { +tag(CommonTagPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY) }
            define('B') { +defaultPart }
            define('C') { +Tags.Items.GLASS_BLOCKS }
            define('D') { +core }
            +stack.copy()
        }.save(exporter)
        // x10 Capacity
        HTShapedRecipeBuilder.create {
            crossLayered()
            define('A') { +tag(CommonTagPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.RAGI_ALLOY) }
            define('B') { +tag(CommonTagPrefixes.STORAGE_BLOCK, top) }
            define('C') { +Tags.Items.GLASS_BLOCKS }
            define('D') { +largeCore }
            stack.set(RagiumDataComponents.CAPACITY_SCALE, 10)
            +stack
            recipeId prefix "larger_"
        }.save(exporter)
    }

    //    Loot Tickets    //

    private fun lootTickets() {
        RagiumRecipeBuilder.bathing {
            itemIngredient { +Items.PAPER }
            fluidIngredient {
                +RagiumFluids.RAGI_MATTER
                amount = 250
            }
            result { +RagiumItems.RAGI_TICKET }
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
}
