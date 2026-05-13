package hiiragi283.ragium.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.core.api.registry.HTItemLike
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
import net.minecraft.world.item.crafting.Ingredient
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
            define('A') { itemCreator.create(HiiragiCoreTags.Items.PLASTICS) }
            resultStack = RagiumItems.BLANK_DISC.toStack()
        }
        // Electric Igniter
        HTShapelessRecipeBuilder.create(output) {
            ingredients += itemCreator.create(CommonTagPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
            ingredients += itemCreator.create(Items.FLINT)
            resultStack = RagiumItems.ELECTRIC_IGNITER.toStack()
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
                ingredients += itemCreator.create(RagiumItems.MERCURY_BOTTLE)
            }
            ingredients += itemCreator.create(Tags.Items.BUCKETS_EMPTY)
            resultStack = RagiumFluids.MERCURY.getBucket().toStack()
            recipeId suffix "_from_bottles"
        }
        HTShapelessRecipeBuilder.create(output) {
            ingredients += itemCreator.create(RagiumFluids.MERCURY.bucketTag)
            repeat(4) {
                ingredients += itemCreator.create(Items.GLASS_BOTTLE)
            }
            resultStack = RagiumItems.MERCURY_BOTTLE.toStack(4)
            recipeId suffix "_from_bucket"
        }
        // Thermometer
        HTShapedRecipeBuilder.create(output) {
            pattern(
                " AB",
                "ACA",
                "DA ",
            )
            define('A') { itemCreator.create(Tags.Items.GLASS_PANES_COLORLESS) }
            define('B') { itemCreator.create(Tags.Items.DYES_RED) }
            define('C') { itemCreator.create(RagiumItems.MERCURY_BOTTLE) }
            define('D') { itemCreator.create(CommonTagPrefixes.PLATE, VanillaMaterialKeys.COPPER) }
            resultStack = RagiumItems.THERMOMETER.toStack()
        }
    }

    //    Machine    //

    @JvmStatic
    private fun machines() {
        // Basic
        basic(RagiumBlocks.ALLOY_SMELTER) { itemCreator.create(Items.FURNACE) }
        basic(RagiumBlocks.ASSEMBLER) { itemCreator.create(Items.CRAFTER) }
        basic(RagiumBlocks.AUTO_CHISEL) { itemCreator.create(Items.STONECUTTER) }
        basic(RagiumBlocks.COMPRESSOR) { itemCreator.create(Items.PISTON) }
        basic(RagiumBlocks.CRUSHER) { itemCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.DIAMOND) }
        basic(RagiumBlocks.CUTTING_MACHINE) { itemCreator.create(Items.IRON_AXE) }
        basic(RagiumBlocks.ELECTRIC_FURNACE) { itemCreator.create(Items.FURNACE) }
        basic(RagiumBlocks.PLANTER) { itemCreator.create(Tags.Items.GLASS_BLOCKS) }
        // Heat
        advanced(RagiumBlocks.FREEZER, HCMaterialKeys.AZURE_STEEL) { itemCreator.create(Items.SNOW_BLOCK) }
        advanced(RagiumBlocks.MELTER, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY) { itemCreator.create(Items.BLAST_FURNACE) }
        advanced(RagiumBlocks.PYROLYZER, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY) { itemCreator.create(Items.NETHER_BRICKS) }
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "ABA",
                "BCB",
            )
            define('A') { itemCreator.create(RagiumItems.THERMOMETER) }
            define('B') { itemCreator.create(Tags.Items.GLASS_BLOCKS) }
            define('C') { itemCreator.create(CommonTagPrefixes.GEAR, VanillaMaterialKeys.DIAMOND) }
            resultStack = RagiumBlocks.REFINERY.toStack()
        }
        advanced(RagiumBlocks.WASHER, HCMaterialKeys.AZURE_STEEL) { itemCreator.create(Items.IRON_BARS) }
        // Elite
        /*HTShapedRecipeBuilder.create(output) {
            pattern(
                "ABA",
                "BCB",
            )
            define('A') += RagiumItems.ELECTRIC_CIRCUIT
            define('B') += Tags.Items.GLASS_BLOCKS_TINTED
            define('C') += CommonTagPrefixes.GEAR to RagiumMaterialKeys.STAINLESS_STEEL
            resultStack += RagiumBlocks.CHEMICAL_WASHER
        }*/
        elite(RagiumBlocks.BREWERY) { itemCreator.create(Items.BREWING_STAND) }
        elite(RagiumBlocks.MIXER) { itemCreator.create(Items.COPPER_GRATE) }
        // Ultimate
        ultimate(RagiumBlocks.MASS_FABRICATOR) { itemCreator.create(Tags.Items.NETHER_STARS) }
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "ABA",
                "BCB",
            )
            define('A') { itemCreator.create(RagiumItems.ARTIFICIAL_ARTIFACT) }
            define('B') { itemCreator.create(RagiumBlocks.TANK) }
            define('C') { itemCreator.create(CommonTagPrefixes.GEAR, VanillaMaterialKeys.NETHERITE) }
            resultStack = RagiumBlocks.FLUID_DUPLICATOR.toStack()
        }
    }

    @JvmStatic
    private fun basic(block: HTItemLike<*>, consumer: () -> Ingredient) {
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "AAA",
                "BCB",
                "DDD",
            )
            define('A') { itemCreator.create(CommonTagPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY) }
            define('B', consumer)
            define('C') { itemCreator.create(CommonTagPrefixes.GEAR, VanillaMaterialKeys.COPPER) }
            define('D') { itemCreator.create(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON) }
            resultStack = block.toStack()
        }
    }

    @JvmStatic
    private fun advanced(block: HTItemLike<*>, material: HTMaterialLike, consumer: () -> Ingredient) {
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "AAA",
                "BCB",
                "DDD",
            )
            define('A') { itemCreator.create(CommonTagPrefixes.INGOT, material) }
            define('B', consumer)
            define('C') { itemCreator.create(RagiumItems.THERMOMETER) }
            define('D') { itemCreator.create(CommonTagPrefixes.INGOT, CommonMaterialKeys.STEEL) }
            resultStack = block.toStack()
        }
    }

    @JvmStatic
    private fun elite(block: HTItemLike<*>, consumer: () -> Ingredient) {
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "AAA",
                "BCB",
                "DDD",
            )
            define('A') { itemCreator.create(CommonTagPrefixes.PLATE, RagiumMaterialKeys.STAINLESS_STEEL) }
            define('B', consumer)
            define('C') { itemCreator.create(RagiumItems.ELECTRIC_CIRCUIT) }
            define('D') { itemCreator.create(CommonTagPrefixes.PLATE, CommonMaterialKeys.CARBON) }
            resultStack = block.toStack()
        }
    }

    @JvmStatic
    private fun ultimate(block: HTItemLike<*>, consumer: () -> Ingredient) {
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "AAA",
                "BCB",
                "DDD",
            )
            define('A') { itemCreator.create(CommonTagPrefixes.INGOT, VanillaMaterialKeys.NETHERITE) }
            define('B', consumer)
            define('C') { itemCreator.create(RagiumItems.ARTIFICIAL_ARTIFACT) }
            define('D') { itemCreator.create(Tags.Items.OBSIDIANS_CRYING) }
            resultStack = block.toStack()
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
            define('A') { itemCreator.create(CommonTagPrefixes.INGOT, CommonMaterialKeys.STEEL) }
            define('B') { itemCreator.create(CommonTagPrefixes.GEM, HCMaterialKeys.WARPED_CRYSTAL) }
            resultStack = RagiumBlocks.UNIVERSAL_CHEST.toStack()
        }

        for (color: HTDefaultColor in HTDefaultColor.entries) {
            HTShapelessRecipeBuilder.create(output) {
                ingredients += itemCreator.create(RagiumBlocks.UNIVERSAL_CHEST)
                ingredients += itemCreator.create(color.dyesTag)
                resultStack = createItemStack(RagiumBlocks.UNIVERSAL_CHEST, HCDataComponents.COLOR, color)
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
        val defaultPart: Ingredient = materialManager.getOrEmpty(top).getDefaultPart(top)?.let(itemCreator::create) ?: return
        HTShapedRecipeBuilder.create(output) {
            crossLayered()
            define('A') { itemCreator.create(CommonTagPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY) }
            define('B') { defaultPart }
            define('C') { itemCreator.create(Tags.Items.GLASS_BLOCKS) }
            define('D') { itemCreator.create(core) }
            resultStack = block.toStack()
        }
        // x10 Capacity
        HTShapedRecipeBuilder.create(output) {
            crossLayered()
            define('A') { itemCreator.create(CommonTagPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.RAGI_ALLOY) }
            define('B') { itemCreator.create(CommonTagPrefixes.STORAGE_BLOCK, top) }
            define('C') { itemCreator.create(Tags.Items.GLASS_BLOCKS) }
            define('D') { itemCreator.create(largeCore) }
            resultStack = createItemStack(block, RagiumDataComponents.CAPACITY_SCALE, 10)
            recipeId prefix "larger_"
        }
    }

    //    Other    //

    @JvmStatic
    private fun lootTickets() {
        HTShapedRecipeBuilder.create(output) {
            cross8()
            define('A') { itemCreator.create(RagiumItems.RAGI_MATTER) }
            define('B') { itemCreator.create(Tags.Items.DYES_RED) }
            define('C') { itemCreator.create(Items.PAPER) }
            resultStack = RagiumItems.RAGI_TICKET.toStack(4)
            category = CraftingBookCategory.EQUIPMENT
        }

        // End City
        addLootTicket(HTDefaultLootTickets.END_CITY) {
            it += itemCreator.create(Items.PURPUR_BLOCK)
            it += itemCreator.create(Items.SHULKER_SHELL)
        }
        // Simple Dungeon
        addLootTicket(HTDefaultLootTickets.DUNGEON) {
            it += itemCreator.create(Tags.Items.COBBLESTONES_MOSSY)
            it += itemCreator.create(Items.ROTTEN_FLESH)
        }
        // Mineshaft
        addLootTicket(HTDefaultLootTickets.MINESHAFT) {
            it += itemCreator.create(ItemTags.PLANKS)
            it += itemCreator.create(ItemTags.RAILS)
        }
        // Nether Fortress
        addLootTicket(HTDefaultLootTickets.NETHER_FORTRESS) {
            it += itemCreator.create(Items.NETHER_BRICKS)
            it += itemCreator.create(Tags.Items.CROPS_NETHER_WART)
        }

        // Desert Pyramid
        addLootTicket(HTDefaultLootTickets.DESERT_PYRAMID) {
            it += itemCreator.create(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS)
            it += itemCreator.create(CommonTagPrefixes.INGOT, VanillaMaterialKeys.GOLD)
        }
        // Jungle Temple
        addLootTicket(HTDefaultLootTickets.TEMPLE) {
            it += itemCreator.create(Tags.Items.COBBLESTONES_MOSSY)
            it += itemCreator.create(Items.VINE)
        }
        // Igloo Chest
        addLootTicket(HTDefaultLootTickets.IGLOO) {
            it += itemCreator.create(Items.SNOW_BLOCK)
            it += itemCreator.create(ItemTags.BEDS)
        }
        // Mansion
        addLootTicket(HTDefaultLootTickets.MANSION) {
            it += itemCreator.create(Items.DARK_OAK_PLANKS)
            it += itemCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.EMERALD)
        }

        // Buried Treasure
        addLootTicket(HTDefaultLootTickets.BURIED_TREASURE) {
            it += itemCreator.create(Tags.Items.SANDS_COLORLESS)
            it += itemCreator.create(Items.PUFFERFISH)
        }
        // Shipwreck
        addLootTicket(HTDefaultLootTickets.SHIPWRECK) {
            it += itemCreator.create(Tags.Items.CHESTS_WOODEN)
            it += itemCreator.create(Items.KELP)
        }
        // Bastion Remnant
        addLootTicket(HTDefaultLootTickets.BASTION_REMNANT) {
            it += itemCreator.create(Items.BLACKSTONE)
            it += itemCreator.create(CommonTagPrefixes.INGOT, VanillaMaterialKeys.GOLD)
        }
        // Ancient City
        addLootTicket(HTDefaultLootTickets.ANCIENT_CITY) {
            it += itemCreator.create(Items.DEEPSLATE_TILES)
            it += itemCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.ECHO)
        }
        // Ruined Portal
        addLootTicket(HTDefaultLootTickets.RUINED_PORTAL) {
            it += itemCreator.create(Tags.Items.OBSIDIANS_NORMAL)
            it += itemCreator.create(Tags.Items.CROPS_NETHER_WART)
        }
    }

    @JvmStatic
    private inline fun addLootTicket(lootTicket: HTDefaultLootTickets, action: (MutableList<Ingredient>) -> Unit) {
        HTShapelessRecipeBuilder.create(output) {
            ingredients += itemCreator.create(RagiumItems.RAGI_TICKET)
            action(ingredients)
            resultStack = HTDefaultLootTickets.getLootTicket(lootTicket)
            category = CraftingBookCategory.EQUIPMENT
            recipeId suffix "/${lootTicket.name.lowercase()}"
        }
    }
}
