package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.rowValues
import hiiragi283.ragium.api.extension.toDescriptionKey
import hiiragi283.ragium.api.registry.HTDoubleDeferredRegister
import hiiragi283.ragium.api.registry.HTVariantKey
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.util.HTLootTicketHelper
import hiiragi283.ragium.util.material.HTVanillaMaterialType
import hiiragi283.ragium.util.material.RagiumMaterialType
import hiiragi283.ragium.util.variant.HTMachineVariant
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import kotlin.enums.enumEntries

object RagiumCreativeTabs {
    @JvmField
    val REGISTER: DeferredRegister<CreativeModeTab> =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RagiumAPI.MOD_ID)

    @JvmStatic
    fun init(eventBus: IEventBus) {
        REGISTER.register(eventBus)

        eventBus.addListener(::modifyCreativeTabs)
    }

    @JvmField
    val BLOCKS: DeferredHolder<CreativeModeTab, CreativeModeTab> =
        register("blocks", { HTMachineVariant.PULVERIZER.asItem() }, RagiumBlocks.REGISTER)
    /*{ _: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
        // Natural Resources
        output.accept(RagiumBlocks.ASH_LOG)
        output.accept(RagiumBlocks.SILT)
        output.accept(RagiumBlocks.CRIMSON_SOIL)
        output.accept(RagiumBlocks.MYSTERIOUS_OBSIDIAN)

        RagiumBlocks.ORES.values.forEach(output::accept)
        output.accept(RagiumBlocks.RESONANT_DEBRIS)
        // Storage Blocks
        RagiumBlocks.MATERIALS.rowValues(HTMaterialVariant.STORAGE_BLOCK).forEach(output::accept)
        // Machines
        output.acceptItems<HTGeneratorVariant>()

        output.acceptItems(RagiumBlocks.FRAMES)
        output.acceptItems<HTMachineVariant>()

        output.acceptItems(RagiumBlocks.CASINGS)
        output.acceptItems<HTDeviceVariant>()

        output.acceptItems<HTDrumVariant>()
        output.accept(RagiumItems.MEDIUM_DRUM_UPGRADE)
        output.accept(RagiumItems.LARGE_DRUM_UPGRADE)
        output.accept(RagiumItems.HUGE_DRUM_UPGRADE)
        // Decorations
        for (variant: HTDecorationVariant in HTDecorationVariant.entries) {
            output.accept(variant.base)
            output.accept(variant.slab)
            output.accept(variant.stairs)
            output.accept(variant.wall)
        }

        RagiumBlocks.MATERIALS.rowValues(HTMaterialVariant.GLASS_BLOCK).forEach(output::accept)
        RagiumBlocks.MATERIALS.rowValues(HTMaterialVariant.TINTED_GLASS_BLOCK).forEach(output::accept)
        output.acceptItems(RagiumBlocks.LED_BLOCKS.values)
    }*/

    @JvmField
    val INGREDIENTS: DeferredHolder<CreativeModeTab, CreativeModeTab> = register(
        "ingredients",
        "ragi_alloy_ingot",
    ) { _: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
        // Fluid Buckets
        output.acceptItems(RagiumFluidContents.REGISTER.itemEntries)
        // Materials
        HTMaterialVariant.MATERIAL_TAB_ORDER
            .flatMap(RagiumItems.MATERIALS::rowValues)
            .forEach(output::accept)
        // Ingredients
        output.accept(RagiumItems.TAR)
        output.accept(RagiumItems.ELDER_HEART)

        output.accept(RagiumItems.LUMINOUS_PASTE)
        output.accept(RagiumItems.LED)

        output.accept(RagiumItems.SOLAR_PANEL)
        output.accept(RagiumItems.REDSTONE_BOARD)

        output.accept(RagiumItems.POLYMER_RESIN)
        output.accept(RagiumItems.getPlate(RagiumMaterialType.PLASTIC))
        output.accept(RagiumItems.SYNTHETIC_FIBER)
        output.accept(RagiumItems.SYNTHETIC_LEATHER)

        output.accept(RagiumItems.CIRCUIT_BOARD)
        output.accept(RagiumItems.BASALT_MESH)
        output.accept(RagiumItems.ADVANCED_CIRCUIT_BOARD)

        HTMaterialVariant.CIRCUIT_TAB_ORDER
            .flatMap(RagiumItems.MATERIALS::rowValues)
            .forEach(output::accept)
        output.accept(RagiumItems.ETERNAL_COMPONENT)
    }

    @JvmField
    val ITEMS: DeferredHolder<CreativeModeTab, CreativeModeTab> =
        register(
            "items",
            "ragi_ticket",
        ) { _: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
            // Tools
            output.accept(RagiumItems.getForgeHammer(RagiumMaterialType.RAGI_ALLOY))
            output.accept(RagiumItems.getForgeHammer(RagiumMaterialType.RAGI_CRYSTAL))
            output.accept(RagiumItems.RAGI_MAGNET)

            output.accept(RagiumItems.ADVANCED_RAGI_MAGNET)

            output.accept(RagiumItems.RAGI_LANTERN)

            output.accept(RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE)
            output.acceptFromTable(RagiumItems.ARMORS, RagiumMaterialType.AZURE_STEEL)
            output.acceptFromTable(RagiumItems.TOOLS, RagiumMaterialType.AZURE_STEEL)

            output.accept(RagiumItems.DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE)
            output.acceptFromTable(RagiumItems.ARMORS, RagiumMaterialType.DEEP_STEEL)
            output.acceptFromTable(RagiumItems.TOOLS, RagiumMaterialType.DEEP_STEEL)

            output.accept(RagiumItems.DRILL)

            output.accept(RagiumItems.POTION_BUNDLE)
            output.accept(RagiumItems.SLOT_COVER)
            output.accept(RagiumItems.TRADER_CATALOG)

            output.accept(RagiumItems.BLAST_CHARGE)

            output.accept(RagiumItems.ENDER_BUNDLE)
            output.accept(RagiumItems.ELDRITCH_EGG)
            // Foods
            output.accept(RagiumItems.getIngot(RagiumMaterialType.CHOCOLATE))

            output.accept(RagiumItems.ICE_CREAM)
            output.accept(RagiumItems.ICE_CREAM_SODA)

            output.accept(RagiumItems.MINCED_MEAT)
            output.accept(RagiumItems.getIngot(RagiumMaterialType.MEAT))
            output.accept(RagiumItems.getIngot(RagiumMaterialType.COOKED_MEAT))
            output.accept(RagiumItems.CANNED_COOKED_MEAT)

            output.accept(RagiumItems.MELON_PIE)

            output.accept(RagiumBlocks.SWEET_BERRIES_CAKE)
            output.accept(RagiumItems.SWEET_BERRIES_CAKE_SLICE)

            output.accept(RagiumItems.RAGI_CHERRY)
            output.accept(RagiumItems.RAGI_CHERRY_JAM)
            output.accept(RagiumItems.FEVER_CHERRY)

            output.accept(RagiumItems.BOTTLED_BEE)
            output.accept(RagiumItems.AMBROSIA)
            // Tickets
            output.accept(RagiumItems.TELEPORT_TICKET)

            output.accept(RagiumItems.RAGI_TICKET)
            output.acceptAll(HTLootTicketHelper.DEFAULT_LOOT_TICKETS.values)
        }

    //    Extensions    //

    @JvmStatic
    private fun register(
        name: String,
        icon: String,
        action: CreativeModeTab.DisplayItemsGenerator,
    ): DeferredHolder<CreativeModeTab, CreativeModeTab> = REGISTER.register(name) { id: ResourceLocation ->
        CreativeModeTab
            .builder()
            .title(Component.translatable(id.toDescriptionKey("itemGroup")))
            .icon(DeferredItem.createItem<Item>(RagiumAPI.id(icon))::toStack)
            .displayItems(action)
            .build()
    }

    @JvmStatic
    private fun register(
        name: String,
        icon: ItemLike,
        register: HTDoubleDeferredRegister<out ItemLike, *>,
    ): DeferredHolder<CreativeModeTab, CreativeModeTab> = REGISTER.register(name) { id: ResourceLocation ->
        CreativeModeTab
            .builder()
            .title(Component.translatable(id.toDescriptionKey("itemGroup")))
            .icon { ItemStack(icon) }
            .displayItems(register.firstEntries)
            .build()
    }

    inline fun <reified V> CreativeModeTab.Output.acceptFromTable(
        table: HTTable<V, HTMaterialType, DeferredItem<*>>,
        material: HTMaterialType,
    ) where V : HTVariantKey, V : Enum<V> {
        enumEntries<V>()
            .mapNotNull(table.column(material)::get)
            .forEach(this::accept)
    }

    @JvmStatic
    private fun CreativeModeTab.Output.acceptItems(items: Iterable<ItemLike>) {
        items.forEach(this::accept)
    }

    //    Events    //

    @JvmStatic
    private fun modifyCreativeTabs(event: BuildCreativeModeTabContentsEvent) {
        fun insertBefore(after: ItemLike, before: ItemLike) {
            event.insertBefore(
                ItemStack(after),
                ItemStack(before),
                CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
            )
        }

        fun insertAfter(before: ItemLike, after: ItemLike) {
            event.insertAfter(
                ItemStack(before),
                ItemStack(after),
                CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
            )
        }

        val key: ResourceKey<CreativeModeTab> = event.tabKey
        // 道具タブに鍛造ハンマーを追加する
        if (key == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            insertAfter(
                Items.IRON_PICKAXE,
                RagiumItems.getForgeHammer(HTVanillaMaterialType.IRON),
            )
            insertAfter(
                Items.DIAMOND_PICKAXE,
                RagiumItems.getForgeHammer(HTVanillaMaterialType.DIAMOND),
            )
            insertAfter(
                Items.NETHERITE_PICKAXE,
                RagiumItems.getForgeHammer(HTVanillaMaterialType.NETHERITE),
            )
        }

        if (INGREDIENTS.`is`(key)) {
            insertAfter(RagiumItems.getDust(RagiumMaterialType.RAGINITE), RagiumItems.RAGI_COKE)
            insertAfter(RagiumItems.getGem(RagiumMaterialType.AZURE), RagiumItems.SILICON)
            insertAfter(RagiumItems.getGem(RagiumMaterialType.ELDRITCH_PEARL), RagiumItems.ELDRITCH_GEAR)
            insertBefore(RagiumItems.getIngot(RagiumMaterialType.DEEP_STEEL), RagiumItems.DEEP_SCRAP)
        }
    }
}
