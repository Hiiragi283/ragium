package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.util.HTBuildingBlockSets
import hiiragi283.ragium.util.HTLootTicketHelper
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import kotlin.enums.enumEntries

object RagiumCreativeTabs {
    @JvmField
    val REGISTER: DeferredRegister<CreativeModeTab> =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RagiumAPI.MOD_ID)

    @JvmField
    val BLOCKS: DeferredHolder<CreativeModeTab, CreativeModeTab> = register(
        "blocks",
        "crusher",
    ) { _: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
        // Natural Resources
        output.accept(RagiumBlocks.ASH_LOG)
        output.accept(RagiumBlocks.SILT)
        output.accept(RagiumBlocks.CRIMSON_SOIL)
        output.accept(RagiumBlocks.MYSTERIOUS_OBSIDIAN)

        output.acceptItems<RagiumBlocks.Ores>()
        output.acceptItems<RagiumBlocks.DeepOres>()
        output.acceptItems<RagiumBlocks.NetherOres>()
        output.acceptItems<RagiumBlocks.EndOres>()
        output.accept(RagiumBlocks.RESONANT_DEBRIS)
        // Storage Blocks
        output.acceptItems<RagiumBlocks.StorageBlocks>()
        // Machines
        output.acceptItems<RagiumBlocks.Dynamos>()

        output.acceptItems<RagiumBlocks.Frames>()
        output.acceptItems<RagiumBlocks.Machines>()

        output.acceptItems<RagiumBlocks.Casings>()
        output.acceptItems<RagiumBlocks.Devices>()

        output.acceptItems<RagiumBlocks.Drums>()
        // Decorations
        for (items: List<Item> in RagiumBlocks.DECORATIONS.map(HTBuildingBlockSets::getItems)) {
            output.acceptItems(items)
        }

        output.acceptItems<RagiumBlocks.Glasses>()
        output.acceptItems<RagiumBlocks.LEDBlocks>()
    }

    @JvmField
    val INGREDIENTS: DeferredHolder<CreativeModeTab, CreativeModeTab> = register(
        "ingredients",
        "ragi_alloy_ingot",
    ) { _: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
        // Fluid Buckets
        output.acceptItems(RagiumFluidContents.REGISTER.itemEntries)
        // Gems
        output.acceptItems<RagiumItems.Gems>()
        // Ingots
        output.acceptItems<RagiumItems.Compounds>()
        output.acceptItems<RagiumItems.Ingots>()
        output.acceptItems<RagiumItems.Nuggets>()

        output.acceptItems<RagiumItems.Dusts>()
        // Ingredients
        output.accept(RagiumItems.RAGI_COKE)
        output.accept(RagiumItems.COMPRESSED_SAWDUST)
        output.accept(RagiumItems.TAR)
        output.accept(RagiumItems.DEEP_SCRAP)
        output.accept(RagiumItems.ELDRITCH_ORB)
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
        output.acceptItems<RagiumItems.Circuits>()
    }

    @JvmField
    val ITEMS: DeferredHolder<CreativeModeTab, CreativeModeTab> =
        register(
            "items",
            "ragi_ticket",
        ) { _: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
            // Tools
            output.acceptItems<RagiumItems.ForgeHammers>()
            output.accept(RagiumItems.ADVANCED_RAGI_ALLOY_UPGRADE_SMITHING_TEMPLATE)

            output.accept(RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE)
            output.acceptItems<RagiumItems.AzureSteelArmors>()
            output.acceptItems<RagiumItems.AzureSteelTools>()

            output.accept(RagiumItems.DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE)
            output.acceptItems<RagiumItems.DeepSteelArmors>()
            output.acceptItems<RagiumItems.DeepSteelTools>()

            output.accept(RagiumItems.DRILL)

            output.accept(RagiumItems.POTION_BUNDLE)
            output.accept(RagiumItems.SLOT_COVER)
            output.accept(RagiumItems.TRADER_CATALOG)

            output.accept(RagiumItems.RAGI_MAGNET)
            output.accept(RagiumItems.ADVANCED_RAGI_MAGNET)
            output.accept(RagiumItems.RAGI_LANTERN)

            output.accept(RagiumItems.BLAST_CHARGE)

            output.accept(RagiumItems.ENDER_BUNDLE)
            output.accept(RagiumItems.ELDRITCH_EGG)
            // Foods
            output.accept(RagiumItems.Ingots.CHOCOLATE)

            output.accept(RagiumItems.ICE_CREAM)
            output.accept(RagiumItems.ICE_CREAM_SODA)

            output.accept(RagiumItems.MINCED_MEAT)
            output.accept(RagiumItems.Ingots.MEAT)
            output.accept(RagiumItems.Ingots.COOKED_MEAT)
            output.accept(RagiumItems.CANNED_COOKED_MEAT)

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
            // Tickets
            output.acceptItems<RagiumItems.Tickets>()
            output.acceptAll(HTLootTicketHelper.DEFAULT_LOOT_TICKETS.values)
        }

    @JvmStatic
    private fun register(
        name: String,
        icon: String,
        action: CreativeModeTab.DisplayItemsGenerator,
    ): DeferredHolder<CreativeModeTab, CreativeModeTab> = REGISTER.register(name) { _: ResourceLocation ->
        CreativeModeTab
            .builder()
            .title(Component.translatable("itemGroup.${RagiumAPI.MOD_ID}.$name"))
            .icon(DeferredItem.createItem<Item>(RagiumAPI.id(icon))::toStack)
            .displayItems(action)
            .build()
    }

    @JvmStatic
    private fun CreativeModeTab.Output.acceptItems(items: Iterable<ItemLike>) {
        items.forEach(this::accept)
    }

    @JvmStatic
    inline fun <reified I> CreativeModeTab.Output.acceptItems() where I : ItemLike, I : Enum<I> {
        enumEntries<I>().forEach(this::accept)
    }
}
