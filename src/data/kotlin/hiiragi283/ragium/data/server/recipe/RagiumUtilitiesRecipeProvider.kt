package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.holder.HTIngredientHolder
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.data.recipe.builder.HTClearComponentRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapelessRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTStonecuttingRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumTags
import hiiragi283.ragium.common.crafting.HTPotionDropRecipe
import hiiragi283.ragium.common.item.HTMoldType
import hiiragi283.ragium.common.item.component.HTDefaultLootTickets
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.component.DataComponentType
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumUtilitiesRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Molds
        HTShapedRecipeBuilder.create(output) {
            storage4()
            define('A') += CommonTagPrefixes.PLATE to CommonMaterialKeys.STEEL
            resultStack += HTMoldType.BLANK
        }

        for (moldType: HTMoldType in HTMoldType.entries) {
            HTStonecuttingRecipeBuilder.create(output) {
                ingredient += RagiumTags.Items.MOLDS
                resultStack += moldType
            }
        }

        // Location Ticket
        HTShapedRecipeBuilder.create(output) {
            hollow8()
            define('A') += Items.PAPER
            define('B') += Tags.Items.ENDER_PEARLS
            resultStack += RagiumItems.LOCATION_TICKET to 8
            recipeId suffix "_with_ender"
        }

        HTShapedRecipeBuilder.create(output) {
            hollow8()
            define('A') += Items.PAPER
            define('B') += CommonTagPrefixes.GEM to HCMaterialKeys.WARPED_CRYSTAL
            resultStack += RagiumItems.LOCATION_TICKET to 8
            recipeId suffix "_with_warped"
        }
        // Loot Ticket
        lootTickets()
        // Potion Drop -> Potion
        save(id("shapeless/potion_from_drop"), HTPotionDropRecipe(CraftingBookCategory.MISC))

        machines()
        devices()
        storages()
    }

    @JvmStatic
    private fun machines() {
        // Basic
        fun basic(block: ItemLike, consumer: (HTIngredientHolder.Single) -> Unit) {
            HTShapedRecipeBuilder.create(output) {
                pattern(
                    "AAA",
                    "BCB",
                    "DDD",
                )
                define('A') += CommonTagPrefixes.INGOT to RagiumMaterialKeys.RAGI_ALLOY
                define('B').let(consumer)
                define('C') += CommonTagPrefixes.GEAR to VanillaMaterialKeys.COPPER
                define('D') += CommonTagPrefixes.INGOT to CommonMaterialKeys.STEEL
                resultStack += block
            }
        }

        basic(RagiumBlocks.ALLOY_SMELTER) { it += Items.FURNACE }
        basic(RagiumBlocks.CRUSHER) { it += CommonTagPrefixes.GEM to VanillaMaterialKeys.DIAMOND }
        basic(RagiumBlocks.CUTTING_MACHINE) { it += Items.IRON_AXE }
        basic(RagiumBlocks.ELECTRIC_FURNACE) { it += Items.FURNACE }
        basic(RagiumBlocks.FORMING_PRESS) { it += Items.PISTON }

        // Heat
        fun heat(block: ItemLike, consumer: (HTIngredientHolder.Single) -> Unit) {
            HTShapedRecipeBuilder.create(output) {
                pattern(
                    "AAA",
                    "BCB",
                    "DDD",
                )
                define('A') += CommonTagPrefixes.INGOT to RagiumMaterialKeys.ADVANCED_RAGI_ALLOY
                define('B').let(consumer)
                define('C') += CommonTagPrefixes.GEAR to VanillaMaterialKeys.GOLD
                define('D') += CommonTagPrefixes.INGOT to CommonMaterialKeys.STEEL
                resultStack += block
            }
        }

        heat(RagiumBlocks.MELTER) { it += Items.BLAST_FURNACE }
        heat(RagiumBlocks.PYROLYZER) { it += Items.NETHER_BRICKS }
        heat(RagiumBlocks.SOLIDIFIER) { it += RagiumTags.Items.MOLDS }

        // Chemical
        heat(RagiumBlocks.MIXER) { it += Items.CAULDRON }

        // Matter
    }

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

        basic(RagiumBlocks.FERMENTER) { it += Items.COMPOSTER }
        basic(RagiumBlocks.PLANTER) { it += Tags.Items.GLASS_BLOCKS }

        // Advanced
        fun advanced(block: ItemLike, consumer: (HTIngredientHolder.Single) -> Unit) {
            HTShapedRecipeBuilder.create(output) {
                pattern(
                    "AAA",
                    "BCB",
                    "DDD",
                )
                define('A') += CommonTagPrefixes.INGOT to HCMaterialKeys.DEEP_STEEL
                define('B').let(consumer)
                define('C') += CommonTagPrefixes.GEAR to VanillaMaterialKeys.NETHERITE
                define('D') += CommonTagPrefixes.INGOT to CommonMaterialKeys.STEEL
                resultStack += block
            }
        }

        // Enchanting
        fun enchanting(block: ItemLike, consumer: (HTIngredientHolder.Single) -> Unit) {
            HTShapedRecipeBuilder.create(output) {
                pattern(
                    "AAA",
                    "BCB",
                    "DDD",
                )
                define('A') += CommonTagPrefixes.INGOT to RagiumMaterialKeys.RAGI_ALLOY
                define('B').let(consumer)
                define('C') += CommonTagPrefixes.GEAR to VanillaMaterialKeys.DIAMOND
                define('D') += Tags.Items.OBSIDIANS_CRYING
                resultStack += block
            }
        }
    }

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

        for (color: DyeColor in DyeColor.entries) {
            HTShapelessRecipeBuilder.create(output) {
                ingredients += RagiumBlocks.UNIVERSAL_CHEST
                ingredients += color.tag
                resultStack += createItemStack(RagiumBlocks.UNIVERSAL_CHEST, RagiumDataComponents.COLOR, color)
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
