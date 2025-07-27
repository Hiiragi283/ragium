package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.toStack
import hiiragi283.ragium.util.HTBuildingBlockSets
import hiiragi283.ragium.util.HTLootTicketHelper
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumCreativeTabs {
    @JvmField
    val REGISTER: DeferredRegister<CreativeModeTab> =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RagiumAPI.MOD_ID)

    @JvmField
    val COMMON: DeferredHolder<CreativeModeTab, CreativeModeTab> =
        register(
            "common",
            "Ragium",
            RagiumItems.getForgeHammer(RagiumToolTiers.RAGI_ALLOY),
        ) { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
            // Fluid Buckets
            output.acceptItems(RagiumFluidContents.REGISTER.itemEntries)
            // Natural Resources
            output.accept(RagiumBlocks.SILT)
            // Gems
            output.acceptItems(RagiumBlocks.RAGI_CRYSTAL_ORES.getItems())
            output.accept(RagiumBlocks.RAGI_CRYSTAL_BLOCK)
            output.accept(RagiumItems.RAGI_CRYSTAL)
            output.accept(RagiumItems.RAGI_MAGNET)
            output.accept(RagiumItems.RAGI_LANTERN)

            output.accept(RagiumBlocks.CRIMSON_CRYSTAL_BLOCK)
            output.accept(RagiumItems.CRIMSON_CRYSTAL)
            output.accept(RagiumBlocks.CRIMSON_SOIL)
            output.accept(RagiumItems.BLAST_CHARGE)

            output.accept(RagiumBlocks.WARPED_CRYSTAL_BLOCK)
            output.accept(RagiumItems.WARPED_CRYSTAL)

            output.accept(RagiumBlocks.ELDRITCH_PEARL_BLOCK)
            output.accept(RagiumItems.ELDRITCH_ORB)
            output.accept(RagiumItems.ELDRITCH_PEARL)
            output.accept(RagiumItems.ENDER_BUNDLE)
            output.accept(RagiumItems.ELDRITCH_EGG)
            // Ingots
            output.accept(RagiumBlocks.RAGI_ALLOY_BLOCK)
            output.accept(RagiumItems.RAGI_ALLOY_COMPOUND)
            output.accept(RagiumItems.RAGI_ALLOY_INGOT)
            output.accept(RagiumItems.RAGI_ALLOY_NUGGET)

            output.accept(RagiumBlocks.ADVANCED_RAGI_ALLOY_BLOCK)
            output.accept(RagiumItems.ADVANCED_RAGI_ALLOY_COMPOUND)
            output.accept(RagiumItems.ADVANCED_RAGI_ALLOY_INGOT)
            output.accept(RagiumItems.ADVANCED_RAGI_ALLOY_NUGGET)
            output.accept(RagiumItems.ADVANCED_RAGI_ALLOY_UPGRADE_SMITHING_TEMPLATE)
            output.accept(RagiumItems.ADVANCED_RAGI_MAGNET)

            output.accept(RagiumBlocks.AZURE_STEEL_BLOCK)
            output.accept(RagiumItems.AZURE_STEEL_COMPOUND)
            output.accept(RagiumItems.AZURE_STEEL_INGOT)
            output.accept(RagiumItems.AZURE_STEEL_NUGGET)
            output.accept(RagiumItems.AZURE_SHARD)
            output.accept(RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE)
            output.acceptItems(RagiumItems.AZURE_STEEL_ARMORS.itemHolders)
            output.acceptItems(RagiumItems.AZURE_STEEL_TOOLS.itemHolders)

            output.accept(RagiumBlocks.RESONANT_DEBRIS)
            output.accept(RagiumBlocks.DEEP_STEEL_BLOCK)
            output.accept(RagiumItems.DEEP_STEEL_INGOT)
            output.accept(RagiumItems.DEEP_SCRAP)
            output.accept(RagiumItems.DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE)
            output.acceptItems(RagiumItems.DEEP_STEEL_ARMORS.itemHolders)
            output.acceptItems(RagiumItems.DEEP_STEEL_TOOLS.itemHolders)

            output.accept(RagiumBlocks.CHOCOLATE_BLOCK)
            output.accept(RagiumItems.CHOCOLATE_INGOT)

            output.accept(RagiumBlocks.MEAT_BLOCK)
            output.accept(RagiumItems.MEAT_INGOT)
            output.accept(RagiumItems.MINCED_MEAT)

            output.accept(RagiumBlocks.COOKED_MEAT_BLOCK)
            output.accept(RagiumItems.COOKED_MEAT_INGOT)
            output.accept(RagiumItems.CANNED_COOKED_MEAT)
            // Dusts
            output.acceptItems(RagiumBlocks.RAGINITE_ORES.getItems())
            output.accept(RagiumItems.RAGINITE_DUST)
            output.accept(RagiumItems.RAGI_COKE)

            output.accept(RagiumBlocks.ASH_LOG)
            output.accept(RagiumItems.ASH_DUST)

            output.accept(RagiumBlocks.MYSTERIOUS_OBSIDIAN)
            output.accept(RagiumItems.OBSIDIAN_DUST)

            output.accept(RagiumItems.CINNABAR_DUST)
            output.accept(RagiumItems.QUARTZ_DUST)
            output.accept(RagiumItems.SALTPETER_DUST)
            output.accept(RagiumItems.SULFUR_DUST)

            output.accept(RagiumItems.SAWDUST)
            output.accept(RagiumItems.COMPRESSED_SAWDUST)

            output.accept(RagiumItems.TAR)
            // Decorations
            for (items: List<Item> in RagiumBlocks.DECORATIONS.map(HTBuildingBlockSets::getItems)) {
                output.acceptItems(items)
            }

            output.acceptItems(RagiumBlocks.GLASSES)
            output.acceptItems(RagiumBlocks.LED_BLOCKS.values)
            // Machines
            output.acceptItems(RagiumBlocks.CASINGS)
            output.acceptItems(RagiumBlocks.MACHINES)
            output.acceptItems(RagiumBlocks.DEVICES)
            output.acceptItems(RagiumBlocks.DRUMS)
            // Tools
            output.acceptItems(RagiumItems.FORGE_HAMMERS.values)

            output.accept(RagiumItems.POTION_BUNDLE)
            output.accept(RagiumItems.SLOT_COVER)
            output.accept(RagiumItems.TRADER_CATALOG)
            // Tickets
            output.accept(RagiumItems.BLANK_TICKET)

            output.accept(RagiumItems.RAGI_TICKET)
            output.acceptAll(HTLootTicketHelper.DEFAULT_LOOT_TICKETS.values)

            output.accept(RagiumItems.AZURE_TICKET)
            output.accept(RagiumItems.BLOODY_TICKET)
            output.accept(RagiumItems.TELEPORT_TICKET)
            output.accept(RagiumItems.ELDRITCH_TICKET)

            output.accept(RagiumItems.DAYBREAK_TICKET)
            output.accept(RagiumItems.ETERNAL_TICKET)
            // Foods
            output.accept(RagiumItems.ICE_CREAM)
            output.accept(RagiumItems.ICE_CREAM_SODA)

            output.accept(RagiumBlocks.COOKED_MEAT_ON_THE_BONE)

            output.accept(RagiumItems.MELON_PIE)

            output.accept(RagiumBlocks.SWEET_BERRIES_CAKE)
            output.accept(RagiumItems.SWEET_BERRIES_CAKE_SLICE)

            output.accept(RagiumItems.RAGI_CHERRY)
            output.accept(RagiumItems.RAGI_CHERRY_JAM)
            output.accept(RagiumItems.FEVER_CHERRY)

            output.accept(RagiumItems.BOTTLED_BEE)
            output.accept(RagiumItems.EXP_BERRIES)
            output.accept(RagiumItems.WARPED_WART)
            output.accept(RagiumItems.AMBROSIA)
            // Ingredients
            output.accept(RagiumItems.ELDER_HEART)

            output.accept(RagiumItems.LUMINOUS_PASTE)
            output.accept(RagiumItems.LED)

            output.accept(RagiumItems.SOLAR_PANEL)
            output.accept(RagiumItems.REDSTONE_BOARD)

            output.accept(RagiumItems.POLYMER_RESIN)
            output.accept(RagiumItems.PLASTIC_PLATE)
            output.accept(RagiumItems.SYNTHETIC_FIBER)
            output.accept(RagiumItems.SYNTHETIC_LEATHER)

            output.accept(RagiumItems.CIRCUIT_BOARD)
            output.accept(RagiumItems.BASIC_CIRCUIT)
            output.accept(RagiumItems.ADVANCED_CIRCUIT)
            output.accept(RagiumItems.ELITE_CIRCUIT)
            output.accept(RagiumItems.ULTIMATE_CIRCUIT)
        }

    @JvmStatic
    private fun register(
        name: String,
        title: String,
        icon: ItemLike,
        action: CreativeModeTab.DisplayItemsGenerator,
    ): DeferredHolder<CreativeModeTab, CreativeModeTab> = REGISTER.register(name) { _: ResourceLocation ->
        CreativeModeTab
            .builder()
            .title(Component.literal(title))
            .icon(icon::toStack)
            .displayItems(action)
            .build()
    }

    @JvmStatic
    private fun CreativeModeTab.Output.acceptItems(items: Iterable<ItemLike>) {
        items.forEach(this::accept)
    }
}
