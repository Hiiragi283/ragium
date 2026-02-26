package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.data.holder.HTIngredientHolder
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.data.recipe.builder.HTClearComponentRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapelessRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.crafting.HTBlueprintCloningRecipe
import hiiragi283.ragium.common.data.recipe.HTChemicalRecipeBuilder
import hiiragi283.ragium.common.item.component.HTDefaultLootTickets
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.component.DataComponentType
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumUtilitiesRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Blueprint
        HTShapelessRecipeBuilder.create(output) {
            ingredients += Items.PAPER
            ingredients += Tags.Items.DYES_WHITE
            ingredients += Tags.Items.DYES_BLUE
            resultStack += RagiumItems.BLUEPRINT
        }
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(Items.PAPER)
            fluidIngredients += inputCreator.create(HCFluids.getDye(HTDefaultColor.BLUE), 250)

            itemResults += resultCreator.create(RagiumItems.BLUEPRINT)
        }

        save(id(HTConst.SHAPELESS, "blueprint_cloning"), HTBlueprintCloningRecipe(CraftingBookCategory.MISC))
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

        // Loot Ticket
        lootTickets()

        machines()
        devices()
        storages()
    }

    //    Machine    //

    @JvmStatic
    private fun machines() {
        // Basic
        basic(RagiumBlocks.ALLOY_SMELTER) { it += Items.FURNACE }
        basic(RagiumBlocks.AUTO_CHISEL) { it += Items.STONECUTTER }
        basic(RagiumBlocks.COMPRESSOR) { it += Items.PISTON }
        basic(RagiumBlocks.CRUSHER) { it += CommonTagPrefixes.GEM to VanillaMaterialKeys.DIAMOND }
        basic(RagiumBlocks.CUTTING_MACHINE) { it += Items.IRON_AXE }
        basic(RagiumBlocks.ELECTRIC_FURNACE) { it += Items.FURNACE }
        basic(RagiumBlocks.FORMING_PRESS) { it += Items.PISTON }
        // Heat
        advanced(RagiumBlocks.MELTER, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY) { it += Items.BLAST_FURNACE }
        advanced(RagiumBlocks.PYROLYZER, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY) { it += Items.NETHER_BRICKS }
        // Cool
        advanced(RagiumBlocks.FREEZER, HCMaterialKeys.AZURE_STEEL) { it += Items.SNOW_BLOCK }
        // Chemical
        chemical(RagiumBlocks.CANNING_MACHINE) { it += Items.GLASS_BOTTLE }
        chemical(RagiumBlocks.MIXER) { it += RagiumBlocks.TANK }
        chemical(RagiumBlocks.WASHER) { it += Items.CAULDRON }
        // Matter
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
            define('C') += CommonTagPrefixes.GEAR to VanillaMaterialKeys.IRON
            define('D') += CommonTagPrefixes.INGOT to CommonMaterialKeys.STEEL
            resultStack += block
        }
    }

    @JvmStatic
    private fun chemical(block: ItemLike, consumer: (HTIngredientHolder.Single) -> Unit) {
        HTShapedRecipeBuilder.create(output) {
            pattern(
                "AAA",
                "BCB",
                "DDD",
            )
            define('A') += CommonTagPrefixes.PLATE to RagiumMaterialKeys.STAINLESS_STEEL
            define('B').let(consumer)
            define('C') += CommonTagPrefixes.GEAR to VanillaMaterialKeys.GOLD
            define('D') += CommonTagPrefixes.PLATE to CommonMaterialKeys.CARBON
            resultStack += block
        }
    }

    //    Device    //

    @JvmStatic
    private fun devices() {
        // Basic
        fun basic(block: ItemLike, consumer: (HTIngredientHolder.Single) -> Unit) {
            HTShapedRecipeBuilder.create(output) {
                pattern(
                    "AAA",
                    "BCB",
                    "DDD",
                )
                define('A') += CommonTagPrefixes.INGOT to HCMaterialKeys.AZURE_STEEL
                define('B').let(consumer)
                define('C') += CommonTagPrefixes.GEAR to VanillaMaterialKeys.IRON
                define('D') += CommonTagPrefixes.INGOT to CommonMaterialKeys.STEEL
                resultStack += block
            }
        }

        basic(RagiumBlocks.PLANTER) { it += Tags.Items.GLASS_BLOCKS }

        // Enchanting
        fun enchanting(block: ItemLike, consumer: (HTIngredientHolder.Single) -> Unit) {
            HTShapedRecipeBuilder.create(output) {
                pattern(
                    "AAA",
                    "BCB",
                    "DDD",
                )
                define('A') += CommonTagPrefixes.INGOT to CommonMaterialKeys.CARBON
                define('B').let(consumer)
                define('C') += CommonTagPrefixes.GEAR to VanillaMaterialKeys.DIAMOND
                define('D') += CommonTagPrefixes.INGOT to CommonMaterialKeys.STEEL
                resultStack += block
            }
        }
    }

    //    Storage    //

    @JvmStatic
    private fun storages() {
        // Battery
        variableStorage(
            RagiumBlocks.BATTERY,
            VanillaMaterialKeys.GOLD,
            CommonTagPrefixes.GEM.itemTagKey(RagiumMaterialKeys.RAGI_CRYSTAL),
            HCDataComponents.ENERGY,
        )
        // Crate
        variableStorage(
            RagiumBlocks.CRATE,
            CommonMaterialKeys.PLASTIC,
            Tags.Items.CHESTS,
            HCDataComponents.ITEM,
        )
        // Tank
        variableStorage(
            RagiumBlocks.TANK,
            CommonMaterialKeys.RUBBER,
            Tags.Items.BUCKETS_EMPTY,
            HCDataComponents.FLUID,
        )
        // Resonant Interface
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
        block: HTItemHolderLike<*>,
        top: HTMaterialLike,
        core: TagKey<Item>,
        component: DataComponentType<*>,
    ) {
        // Shaped
        HTShapedRecipeBuilder.create(output) {
            crossLayered()
            define('A') += CommonTagPrefixes.INGOT to RagiumMaterialKeys.RAGI_ALLOY
            define('B') += CommonTagPrefixes.PLATE to top
            define('C') += Tags.Items.GLASS_BLOCKS
            define('D') += core
            resultStack += block
        }
        // Clear Component
        HTClearComponentRecipeBuilder.create(output) {
            item = block
            targets += listOf(component)
        }
    }

    //    Other    //

    @JvmStatic
    private fun lootTickets() {
        HTShapedRecipeBuilder.create(output) {
            cross8()
            define('A') += CommonTagPrefixes.GEM to RagiumMaterialKeys.RAGI_CRYSTAL
            define('B') += Tags.Items.DYES_RED
            define('C') += Items.PAPER
            resultStack += RagiumItems.LOOT_TICKET to 4
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

    @HTBuilderMarker
    @JvmStatic
    private inline fun addLootTicket(lootTicket: HTDefaultLootTickets, consumer: (HTIngredientHolder.Multiple) -> Unit) {
        HTShapelessRecipeBuilder.create(output) {
            ingredients += RagiumItems.LOOT_TICKET
            ingredients.let(consumer)
            resultStack += HTDefaultLootTickets.getLootTicket(lootTicket)
            category = CraftingBookCategory.EQUIPMENT
            recipeId suffix "/${lootTicket.name.lowercase()}"
        }
    }
}
