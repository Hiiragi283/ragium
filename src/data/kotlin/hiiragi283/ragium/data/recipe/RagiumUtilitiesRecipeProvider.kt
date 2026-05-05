package hiiragi283.ragium.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.data.holder.HTIngredientHolder
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapelessRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.common.registry.HTDeferredBlockAndItem
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
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumUtilitiesRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        parts()

        // Blueprint
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(Items.PAPER)
            fluidIngredient = inputCreator.create(HCFluids.DyeContents[HTDefaultColor.BLUE], 250)

            result += resultCreator.create(HCItems.BLUEPRINT)
        }
        // Blank Disc
        HTShapedRecipeBuilder.create(output) {
            pattern(
                " A ",
                "A A",
                " A ",
            )
            define('A') += HiiragiCoreTags.Items.PLASTICS
            resultStack += RagiumItems.BLANK_DISC
        }
        // Electric Igniter
        HTShapelessRecipeBuilder.create(output) {
            ingredients += CommonTagPrefixes.INGOT to RagiumMaterialKeys.RAGI_ALLOY
            ingredients += Items.FLINT
            resultStack += RagiumItems.ELECTRIC_IGNITER
        }

        // Loot Ticket
        lootTickets()

        machines()
        devices()
        storages()
    }

    @JvmStatic
    private fun parts() {
        // Mercury Bottle <-> Mercury Bucket
        HTShapelessRecipeBuilder.create(output) {
            repeat(4) {
                ingredients += RagiumItems.MERCURY_BOTTLE
            }
            ingredients += Tags.Items.BUCKETS_EMPTY
            resultStack += RagiumFluids.MERCURY.getBucket()
            recipeId suffix "_from_bottles"
        }
        HTShapelessRecipeBuilder.create(output) {
            ingredients += RagiumFluids.MERCURY.bucketTag
            repeat(4) {
                ingredients += Items.GLASS_BOTTLE
            }
            resultStack += RagiumItems.MERCURY_BOTTLE to 4
            recipeId suffix "_from_bucket"
        }
        // Thermometer
        HTShapedRecipeBuilder.create(output) {
            pattern(
                " AB",
                "ACA",
                "DA ",
            )
            define('A') += Tags.Items.GLASS_PANES_COLORLESS
            define('B') += Tags.Items.DYES_RED
            define('C') += RagiumItems.MERCURY_BOTTLE
            define('D') += CommonTagPrefixes.PLATE to VanillaMaterialKeys.COPPER
            resultStack += RagiumItems.THERMOMETER
        }
    }

    //    Machine    //

    @JvmStatic
    private fun machines() {
        // Basic
        basic(RagiumBlocks.ALLOY_SMELTER) { it += Items.FURNACE }
        basic(RagiumBlocks.ASSEMBLER) { it += Items.CRAFTER }
        basic(RagiumBlocks.AUTO_CHISEL) { it += Items.STONECUTTER }
        basic(RagiumBlocks.COMPRESSOR) { it += Items.PISTON }
        basic(RagiumBlocks.CRUSHER) { it += CommonTagPrefixes.GEM to VanillaMaterialKeys.DIAMOND }
        basic(RagiumBlocks.CUTTING_MACHINE) { it += Items.IRON_AXE }
        basic(RagiumBlocks.ELECTRIC_FURNACE) { it += Items.FURNACE }
        basic(RagiumBlocks.PLANTER) { it += Tags.Items.GLASS_BLOCKS }
        // Heat
        advanced(RagiumBlocks.FREEZER, HCMaterialKeys.AZURE_STEEL) { it += Items.SNOW_BLOCK }
        advanced(RagiumBlocks.MELTER, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY) { it += Items.BLAST_FURNACE }
        advanced(RagiumBlocks.PYROLYZER, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY) { it += Items.NETHER_BRICKS }
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "ABA",
                "BCB",
            )
            define('A') += RagiumItems.THERMOMETER
            define('B') += Tags.Items.GLASS_BLOCKS
            define('C') += CommonTagPrefixes.GEAR to VanillaMaterialKeys.DIAMOND
            resultStack += RagiumBlocks.REFINERY
        }
        advanced(RagiumBlocks.WASHER, HCMaterialKeys.AZURE_STEEL) { it += Items.IRON_BARS }
        // Elite
        elite(RagiumBlocks.BREWERY) { it += Items.BREWING_STAND }
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "ABA",
                "BCB",
            )
            define('A') += RagiumItems.ELECTRIC_CIRCUIT
            define('B') += Tags.Items.GLASS_BLOCKS_TINTED
            define('C') += CommonTagPrefixes.GEAR to RagiumMaterialKeys.STAINLESS_STEEL
            resultStack += RagiumBlocks.CHEMICAL_WASHER
        }
        elite(RagiumBlocks.MIXER) { it += Items.COPPER_GRATE }
        // Ultimate
        ultimate(RagiumBlocks.MASS_FABRICATOR) { it += Tags.Items.NETHER_STARS }
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "ABA",
                "BCB",
            )
            define('A') += RagiumItems.ARTIFICIAL_ARTIFACT
            define('B') += RagiumBlocks.TANK
            define('C') += CommonTagPrefixes.GEAR to VanillaMaterialKeys.NETHERITE
            resultStack += RagiumBlocks.FLUID_DUPLICATOR
        }
    }

    @JvmStatic
    private fun basic(block: ItemLike, consumer: (HTIngredientHolder.Single) -> Unit) {
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "AAA",
                "BCB",
                "DDD",
            )
            define('A') += CommonTagPrefixes.INGOT to RagiumMaterialKeys.RAGI_ALLOY
            define('B').let(consumer)
            define('C') += CommonTagPrefixes.GEAR to VanillaMaterialKeys.COPPER
            define('D') += CommonTagPrefixes.INGOT to VanillaMaterialKeys.IRON
            resultStack += block
        }
    }

    @JvmStatic
    private fun advanced(block: ItemLike, material: HTMaterialLike, consumer: (HTIngredientHolder.Single) -> Unit) {
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "AAA",
                "BCB",
                "DDD",
            )
            define('A') += CommonTagPrefixes.INGOT to material
            define('B').let(consumer)
            define('C') += RagiumItems.THERMOMETER
            define('D') += CommonTagPrefixes.INGOT to CommonMaterialKeys.STEEL
            resultStack += block
        }
    }

    @JvmStatic
    private fun elite(block: ItemLike, consumer: (HTIngredientHolder.Single) -> Unit) {
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "AAA",
                "BCB",
                "DDD",
            )
            define('A') += CommonTagPrefixes.PLATE to RagiumMaterialKeys.STAINLESS_STEEL
            define('B').let(consumer)
            define('C') += RagiumItems.ELECTRIC_CIRCUIT
            define('D') += CommonTagPrefixes.PLATE to CommonMaterialKeys.CARBON
            resultStack += block
        }
    }

    @JvmStatic
    private fun ultimate(block: ItemLike, consumer: (HTIngredientHolder.Single) -> Unit) {
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "AAA",
                "BCB",
                "DDD",
            )
            define('A') += CommonTagPrefixes.INGOT to VanillaMaterialKeys.NETHERITE
            define('B').let(consumer)
            define('C') += RagiumItems.ARTIFICIAL_ARTIFACT
            define('D') += Tags.Items.OBSIDIANS_CRYING
            resultStack += block
        }
    }

    //    Device    //

    @JvmStatic
    private fun devices() {}

    //    Storage    //

    @JvmStatic
    private fun storages() {
        // Battery
        variableStorage(
            RagiumBlocks.BATTERY,
            VanillaMaterialKeys.GOLD,
            CommonTagPrefixes.GEM.itemTagKey(RagiumMaterialKeys.RAGI_CRYSTAL),
            CommonTagPrefixes.STORAGE_BLOCK.itemTagKey(RagiumMaterialKeys.RAGI_CRYSTAL),
        )
        save(id(HTConst.SHAPELESS, "combining", "battery"), HTBatteryCombiningRecipe(CraftingBookCategory.MISC))
        // Crate
        variableStorage(RagiumBlocks.CRATE, CommonMaterialKeys.PLASTIC, Tags.Items.CHESTS)
        // Tank
        variableStorage(RagiumBlocks.TANK, CommonMaterialKeys.RUBBER, Tags.Items.BUCKETS_EMPTY)
        save(id(HTConst.SHAPELESS, "combining", "tank"), HTTankCombiningRecipe(CraftingBookCategory.MISC))
        // Universal Chest
        HTShapedRecipeBuilder.create(output) {
            hollow8()
            define('A') += CommonTagPrefixes.INGOT to CommonMaterialKeys.STEEL
            define('B') += CommonTagPrefixes.GEM to HCMaterialKeys.WARPED_CRYSTAL
            resultStack += RagiumBlocks.UNIVERSAL_CHEST
        }

        for (color: HTDefaultColor in HTDefaultColor.entries) {
            HTShapelessRecipeBuilder.create(output) {
                ingredients += RagiumBlocks.UNIVERSAL_CHEST
                ingredients += color.dyesTag
                resultStack += createItemStack(RagiumBlocks.UNIVERSAL_CHEST, HCDataComponents.COLOR, color)
                recipeId prefix "${color.serializedName}_"
            }
        }
    }

    @JvmStatic
    private fun variableStorage(
        block: HTDeferredBlockAndItem<*, *>,
        top: HTMaterialLike,
        core: TagKey<Item>,
        largeCore: TagKey<Item> = core,
    ) {
        // Shaped
        HTShapedRecipeBuilder.create(output) {
            crossLayered()
            define('A') += CommonTagPrefixes.INGOT to RagiumMaterialKeys.RAGI_ALLOY
            define('B') += materialManager.getOrEmpty(top).getDefaultPart(top) ?: return
            define('C') += Tags.Items.GLASS_BLOCKS
            define('D') += core
            resultStack += block
        }
        // x10 Capacity
        HTShapedRecipeBuilder.create(output) {
            crossLayered()
            define('A') += CommonTagPrefixes.STORAGE_BLOCK to RagiumMaterialKeys.RAGI_ALLOY
            define('B') += CommonTagPrefixes.STORAGE_BLOCK to top
            define('C') += Tags.Items.GLASS_BLOCKS
            define('D') += largeCore
            resultStack += createItemStack(block, RagiumDataComponents.CAPACITY_SCALE, 10)
            recipeId prefix "larger_"
        }
    }

    //    Other    //

    @JvmStatic
    private fun lootTickets() {
        HTShapedRecipeBuilder.create(output) {
            cross8()
            define('A') += RagiumItems.RAGI_MATTER
            define('B') += Tags.Items.DYES_RED
            define('C') += Items.PAPER
            resultStack += RagiumItems.RAGI_TICKET to 4
            category = CraftingBookCategory.EQUIPMENT
        }

        // End City
        addLootTicket(HTDefaultLootTickets.END_CITY) {
            it += Items.PURPUR_BLOCK
            it += Items.SHULKER_SHELL
        }
        // Simple Dungeon
        addLootTicket(HTDefaultLootTickets.DUNGEON) {
            it += Tags.Items.COBBLESTONES_MOSSY
            it += Items.ROTTEN_FLESH
        }
        // Mineshaft
        addLootTicket(HTDefaultLootTickets.MINESHAFT) {
            it += ItemTags.PLANKS
            it += ItemTags.RAILS
        }
        // Nether Fortress
        addLootTicket(HTDefaultLootTickets.NETHER_FORTRESS) {
            it += Items.NETHER_BRICKS
            it += Tags.Items.CROPS_NETHER_WART
        }

        // Desert Pyramid
        addLootTicket(HTDefaultLootTickets.DESERT_PYRAMID) {
            it += Tags.Items.SANDSTONE_UNCOLORED_BLOCKS
            it += CommonTagPrefixes.INGOT to VanillaMaterialKeys.GOLD
        }
        // Jungle Temple
        addLootTicket(HTDefaultLootTickets.TEMPLE) {
            it += Tags.Items.COBBLESTONES_MOSSY
            it += Items.VINE
        }
        // Igloo Chest
        addLootTicket(HTDefaultLootTickets.IGLOO) {
            it += Items.SNOW_BLOCK
            it += ItemTags.BEDS
        }
        // Mansion
        addLootTicket(HTDefaultLootTickets.MANSION) {
            it += Items.DARK_OAK_PLANKS
            it += CommonTagPrefixes.GEM to VanillaMaterialKeys.EMERALD
        }

        // Buried Treasure
        addLootTicket(HTDefaultLootTickets.BURIED_TREASURE) {
            it += Tags.Items.SANDS_COLORLESS
            it += Items.PUFFERFISH
        }
        // Shipwreck
        addLootTicket(HTDefaultLootTickets.SHIPWRECK) {
            it += Tags.Items.CHESTS_WOODEN
            it += Items.KELP
        }
        // Bastion Remnant
        addLootTicket(HTDefaultLootTickets.BASTION_REMNANT) {
            it += Items.BLACKSTONE
            it += CommonTagPrefixes.INGOT to VanillaMaterialKeys.GOLD
        }
        // Ancient City
        addLootTicket(HTDefaultLootTickets.ANCIENT_CITY) {
            it += Items.DEEPSLATE_TILES
            it += CommonTagPrefixes.GEM to VanillaMaterialKeys.ECHO
        }
        // Ruined Portal
        addLootTicket(HTDefaultLootTickets.RUINED_PORTAL) {
            it += Tags.Items.OBSIDIANS_NORMAL
            it += Tags.Items.CROPS_NETHER_WART
        }
    }

    @JvmStatic
    private inline fun addLootTicket(lootTicket: HTDefaultLootTickets, consumer: (HTIngredientHolder.Multiple) -> Unit) {
        HTShapelessRecipeBuilder.create(output) {
            ingredients += RagiumItems.RAGI_TICKET
            ingredients.let(consumer)
            resultStack += HTDefaultLootTickets.getLootTicket(lootTicket)
            category = CraftingBookCategory.EQUIPMENT
            recipeId suffix "/${lootTicket.name.lowercase()}"
        }
    }
}
