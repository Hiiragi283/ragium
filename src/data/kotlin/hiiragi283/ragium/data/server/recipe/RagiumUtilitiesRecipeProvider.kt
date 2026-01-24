package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.data.recipe.builder.HTClearComponentRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapedRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTShapelessRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTStonecuttingRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.common.registry.HTDeferredBlock
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
        HTShapedRecipeBuilder
            .create(HTMoldType.BLANK)
            .storage4()
            .define('A', CommonTagPrefixes.PLATE, CommonMaterialKeys.STEEL)
            .save(output)

        for (moldType: HTMoldType in HTMoldType.entries) {
            HTStonecuttingRecipeBuilder
                .create(moldType)
                .addIngredient(RagiumTags.Items.MOLDS)
                .save(output)
        }

        // Location Ticket
        HTShapedRecipeBuilder
            .create(RagiumItems.LOCATION_TICKET, 8)
            .hollow8()
            .define('A', Items.PAPER)
            .define('B', Tags.Items.ENDER_PEARLS)
            .saveSuffixed(output, "_with_ender")

        HTShapedRecipeBuilder
            .create(RagiumItems.LOCATION_TICKET, 8)
            .hollow8()
            .define('A', Items.PAPER)
            .define('B', CommonTagPrefixes.GEM, HCMaterialKeys.WARPED_CRYSTAL)
            .saveSuffixed(output, "_with_warped")
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
        fun basic(block: ItemLike): HTShapedRecipeBuilder = HTShapedRecipeBuilder
            .create(block)
            .pattern(
                "AAA",
                "BCB",
                "DDD",
            ).define('A', CommonTagPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
            .define('C', CommonTagPrefixes.GEAR, VanillaMaterialKeys.COPPER)
            .define('D', CommonTagPrefixes.INGOT, CommonMaterialKeys.STEEL)

        basic(RagiumBlocks.ALLOY_SMELTER)
            .define('B', Items.FURNACE)
            .save(output)
        basic(RagiumBlocks.CRUSHER)
            .define('B', CommonTagPrefixes.GEM, VanillaMaterialKeys.DIAMOND)
            .save(output)
        basic(RagiumBlocks.CUTTING_MACHINE)
            .define('B', Items.IRON_AXE)
            .save(output)
        basic(RagiumBlocks.FORMING_PRESS)
            .define('B', Items.PISTON)
            .save(output)

        // Advanced
        fun advanced(block: ItemLike): HTShapedRecipeBuilder = HTShapedRecipeBuilder
            .create(block)
            .pattern(
                "AAA",
                "BCB",
                "DDD",
            ).define('A', CommonTagPrefixes.INGOT, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY)
            .define('C', CommonTagPrefixes.GEAR, VanillaMaterialKeys.GOLD)
            .define('D', CommonTagPrefixes.INGOT, CommonMaterialKeys.STEEL)

        advanced(RagiumBlocks.DRYER)
            .define('B', ItemTags.SOUL_FIRE_BASE_BLOCKS)
            .save(output)
        advanced(RagiumBlocks.MELTER)
            .define('B', Items.BLAST_FURNACE)
            .save(output)
        advanced(RagiumBlocks.MIXER)
            .define('B', Items.CAULDRON)
            .save(output)
        advanced(RagiumBlocks.PYROLYZER)
            .define('B', Items.NETHER_BRICKS)
            .save(output)
        advanced(RagiumBlocks.SOLIDIFIER)
            .define('B', RagiumTags.Items.MOLDS)
            .save(output)
    }

    @JvmStatic
    private fun devices() {
        // Basic
        fun basic(block: ItemLike): HTShapedRecipeBuilder = HTShapedRecipeBuilder
            .create(block)
            .pattern(
                "AAA",
                "BCB",
                "DDD",
            ).define('A', CommonTagPrefixes.INGOT, HCMaterialKeys.AZURE_STEEL)
            .define('C', CommonTagPrefixes.GEAR, VanillaMaterialKeys.IRON)
            .define('D', CommonTagPrefixes.INGOT, CommonMaterialKeys.STEEL)

        basic(RagiumBlocks.FERMENTER)
            .define('B', Items.COMPOSTER)
            .save(output)
        basic(RagiumBlocks.PLANTER)
            .define('B', Tags.Items.GLASS_BLOCKS)
            .save(output)

        // Advanced
        fun advanced(block: ItemLike): HTShapedRecipeBuilder = HTShapedRecipeBuilder
            .create(block)
            .pattern(
                "AAA",
                "BCB",
                "DDD",
            ).define('A', CommonTagPrefixes.INGOT, HCMaterialKeys.DEEP_STEEL)
            .define('C', CommonTagPrefixes.GEAR, VanillaMaterialKeys.NETHERITE)
            .define('D', CommonTagPrefixes.INGOT, CommonMaterialKeys.STEEL)

        // Enchanting
        fun enchanting(block: ItemLike): HTShapedRecipeBuilder = HTShapedRecipeBuilder
            .create(block)
            .pattern(
                "AAA",
                "BCB",
                "DDD",
            ).define('A', CommonTagPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
            .define('C', CommonTagPrefixes.GEM, VanillaMaterialKeys.DIAMOND)
            .define('D', Tags.Items.OBSIDIANS_CRYING)
    }

    @JvmStatic
    private fun storages() {
        // Battery, Crate, Tank
        val ragiCrystal: TagKey<Item> = CommonTagPrefixes.GEM.itemTagKey(RagiumMaterialKeys.RAGI_CRYSTAL)
        listOf(
            Triple(RagiumBlocks.BATTERY, ragiCrystal, HCDataComponents.ENERGY),
            Triple(RagiumBlocks.CRATE, Tags.Items.CHESTS, HCDataComponents.ITEM),
            Triple(RagiumBlocks.TANK, Tags.Items.BUCKETS_EMPTY, HCDataComponents.FLUID),
        ).forEach { (block: HTDeferredBlock<*, *>, core: TagKey<Item>, component: DataComponentType<*>) ->
            // Shaped
            HTShapedRecipeBuilder
                .create(block)
                .crossLayered()
                .define('A', CommonTagPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
                .define('B', CommonTagPrefixes.PLATE, CommonMaterialKeys.RUBBER)
                .define('C', Tags.Items.GLASS_BLOCKS)
                .define('D', core)
                .save(output)
            // Clear Component
            HTClearComponentRecipeBuilder(block.itemHolder)
                .setTargets(component)
                .save(output)
        }
        // Resonant Interface
        // Universal Chest
        HTShapedRecipeBuilder
            .create(RagiumBlocks.UNIVERSAL_CHEST)
            .hollow8()
            .define('A', CommonTagPrefixes.INGOT, CommonMaterialKeys.STEEL)
            .define('B', CommonTagPrefixes.GEM, HCMaterialKeys.WARPED_CRYSTAL)
            .save(output)

        for (color: DyeColor in DyeColor.entries) {
            HTShapelessRecipeBuilder(createItemStack(RagiumBlocks.UNIVERSAL_CHEST, RagiumDataComponents.COLOR, color))
                .addIngredient(RagiumBlocks.UNIVERSAL_CHEST)
                .addIngredient(color.tag)
                .savePrefixed(output, "${color.serializedName}_")
        }
    }

    @JvmStatic
    private fun lootTickets() {
        HTShapedRecipeBuilder
            .create(RagiumItems.LOOT_TICKET)
            .cross8()
            .define('A', CommonTagPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
            .define('B', Tags.Items.DYES_RED)
            .define('C', Items.PAPER)
            .setCategory(CraftingBookCategory.EQUIPMENT)
            .save(output)

        // End City
        addLootTicket(HTDefaultLootTickets.END_CITY) {
            addIngredient(Items.PURPUR_BLOCK)
            addIngredient(Items.SHULKER_SHELL)
        }
        // Simple Dungeon
        addLootTicket(HTDefaultLootTickets.DUNGEON) {
            addIngredient(Tags.Items.COBBLESTONES_MOSSY)
            addIngredient(Items.ROTTEN_FLESH)
        }
        // Mineshaft
        addLootTicket(HTDefaultLootTickets.MINESHAFT) {
            addIngredient(ItemTags.PLANKS)
            addIngredient(ItemTags.RAILS)
        }
        // Nether Fortress
        addLootTicket(HTDefaultLootTickets.NETHER_FORTRESS) {
            addIngredient(Items.NETHER_BRICKS)
            addIngredient(Tags.Items.CROPS_NETHER_WART)
        }

        // Desert Pyramid
        addLootTicket(HTDefaultLootTickets.DESERT_PYRAMID) {
            addIngredient(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS)
            addIngredient(CommonTagPrefixes.INGOT, VanillaMaterialKeys.GOLD)
        }
        // Jungle Temple
        addLootTicket(HTDefaultLootTickets.TEMPLE) {
            addIngredient(Tags.Items.COBBLESTONES_MOSSY)
            addIngredient(Items.VINE)
        }
        // Igloo Chest
        addLootTicket(HTDefaultLootTickets.IGLOO) {
            addIngredient(Items.SNOW_BLOCK)
            addIngredient(ItemTags.BEDS)
        }
        // Mansion
        addLootTicket(HTDefaultLootTickets.MANSION) {
            addIngredient(Items.DARK_OAK_PLANKS)
            addIngredient(CommonTagPrefixes.GEM, VanillaMaterialKeys.EMERALD)
        }

        // Buried Treasure
        addLootTicket(HTDefaultLootTickets.BURIED_TREASURE) {
            addIngredient(Tags.Items.SANDS_COLORLESS)
            addIngredient(Items.PUFFERFISH)
        }
        // Shipwreck
        addLootTicket(HTDefaultLootTickets.SHIPWRECK) {
            addIngredient(Tags.Items.CHESTS_WOODEN)
            addIngredient(Items.KELP)
        }
        // Bastion Remnant
        addLootTicket(HTDefaultLootTickets.BASTION_REMNANT) {
            addIngredient(Items.BLACKSTONE)
            addIngredient(CommonTagPrefixes.INGOT, VanillaMaterialKeys.GOLD)
        }
        // Ancient City
        addLootTicket(HTDefaultLootTickets.ANCIENT_CITY) {
            addIngredient(Items.DEEPSLATE_TILES)
            addIngredient(CommonTagPrefixes.GEM, VanillaMaterialKeys.ECHO)
        }
        // Ruined Portal
        addLootTicket(HTDefaultLootTickets.RUINED_PORTAL) {
            addIngredient(Tags.Items.OBSIDIANS_NORMAL)
            addIngredient(Tags.Items.CROPS_NETHER_WART)
        }
    }

    @HTBuilderMarker
    @JvmStatic
    private inline fun addLootTicket(lootTicket: HTDefaultLootTickets, builderAction: HTShapelessRecipeBuilder.() -> Unit) {
        HTShapelessRecipeBuilder(HTDefaultLootTickets.getLootTicket(lootTicket))
            .addIngredient(RagiumItems.LOOT_TICKET)
            .apply(builderAction)
            .setCategory(CraftingBookCategory.EQUIPMENT)
            .saveSuffixed(output, "/${lootTicket.name.lowercase()}")
    }
}
