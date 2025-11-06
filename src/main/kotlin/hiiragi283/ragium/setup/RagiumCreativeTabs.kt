package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.collection.ImmutableTable
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.registry.HTDeferredRegister
import hiiragi283.ragium.api.registry.HTSimpleDeferredHolder
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.toDescriptionKey
import hiiragi283.ragium.api.variant.HTVariantKey
import hiiragi283.ragium.common.item.HTUniversalBundleItem
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.FoodMaterialKeys
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.tier.HTDrumTier
import hiiragi283.ragium.common.util.HTDefaultLootTickets
import hiiragi283.ragium.common.variant.HTArmorVariant
import hiiragi283.ragium.common.variant.HTHammerToolVariant
import hiiragi283.ragium.common.variant.HTVanillaToolVariant
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent

object RagiumCreativeTabs {
    @JvmField
    val REGISTER: HTDeferredRegister<CreativeModeTab> =
        HTDeferredRegister(Registries.CREATIVE_MODE_TAB, RagiumAPI.MOD_ID)

    @JvmStatic
    fun init(eventBus: IEventBus) {
        REGISTER.register(eventBus)

        eventBus.addListener(::modifyCreativeTabs)
    }

    @JvmField
    val BLOCKS: HTSimpleDeferredHolder<CreativeModeTab> =
        REGISTER.register("blocks") { id: ResourceLocation ->
            CreativeModeTab
                .builder()
                .title(Component.translatable(id.toDescriptionKey("itemGroup")))
                .icon { RagiumBlocks.PULVERIZER.toStack() }
                .displayItems(RagiumBlocks.REGISTER.blockEntries)
                .build()
        }

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
    val INGREDIENTS: HTSimpleDeferredHolder<CreativeModeTab> = register(
        "ingredients",
        "ragi_alloy_ingot",
    ) { _: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
        // Fluid Buckets
        output.acceptItems(RagiumFluidContents.REGISTER.itemEntries)
        // Materials
        output.acceptItems(RagiumItems.MATERIALS.values)
        // Ingredients
        output.accept(RagiumItems.TAR)
        output.accept(RagiumItems.PLATING_CATALYST)

        output.accept(RagiumItems.POTATO_SPROUTS)
        output.accept(RagiumItems.GREEN_CAKE)
        output.accept(RagiumItems.GREEN_CAKE_DUST)
        output.accept(RagiumItems.GREEN_PELLET)

        output.accept(RagiumItems.ELDER_HEART)
        output.accept(RagiumItems.GRAVITATIONAL_UNIT)
        output.accept(RagiumItems.WITHER_DOLl)

        output.accept(RagiumItems.LUMINOUS_PASTE)
        output.accept(RagiumItems.LED)

        output.accept(RagiumItems.SOLAR_PANEL)
        output.accept(RagiumItems.REDSTONE_BOARD)

        output.accept(RagiumItems.POLYMER_RESIN)
        output.accept(RagiumItems.POLYMER_CATALYST)
        output.accept(RagiumItems.getPlate(CommonMaterialKeys.PLASTIC))
        output.accept(RagiumItems.SYNTHETIC_FIBER)
        output.accept(RagiumItems.SYNTHETIC_LEATHER)

        output.accept(RagiumItems.CIRCUIT_BOARD)
        output.accept(RagiumItems.BASALT_MESH)
        output.accept(RagiumItems.ADVANCED_CIRCUIT_BOARD)

        RagiumItems.COILS.values.forEach(output::accept)
        RagiumItems.CIRCUITS.values.forEach(output::accept)
        RagiumItems.COMPONENTS.values.forEach(output::accept)
    }

    @JvmField
    val ITEMS: HTSimpleDeferredHolder<CreativeModeTab> =
        register(
            "items",
            "ragi_ticket",
        ) { _: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
            // Tools
            // Raginite
            output.accept(RagiumItems.WRENCH)
            output.accept(RagiumItems.getTool(HTHammerToolVariant, RagiumMaterialKeys.RAGI_ALLOY))
            output.accept(RagiumItems.MAGNET)

            output.accept(RagiumItems.ADVANCED_MAGNET)

            output.accept(RagiumItems.getTool(HTHammerToolVariant, RagiumMaterialKeys.RAGI_CRYSTAL))
            output.accept(RagiumItems.DYNAMIC_LANTERN)
            output.accept(RagiumItems.NIGHT_VISION_GOGGLES)
            // Azure
            output.accept(RagiumItems.AZURE_STEEL_UPGRADE_SMITHING_TEMPLATE)
            output.acceptFromTable(RagiumItems.ARMORS, HTArmorVariant.entries, RagiumMaterialKeys.AZURE_STEEL)
            output.acceptFromTable(RagiumItems.TOOLS, HTVanillaToolVariant.entries, RagiumMaterialKeys.AZURE_STEEL)
            output.accept(RagiumItems.getTool(HTHammerToolVariant, RagiumMaterialKeys.AZURE_STEEL))
            // Molten
            output.accept(RagiumItems.BLAST_CHARGE)

            output.accept(RagiumItems.TELEPORT_KEY)

            output.accept(RagiumItems.ELDRITCH_EGG)

            DyeColor.entries.map(HTUniversalBundleItem::createBundle).forEach(output::accept)
            // Deep
            output.accept(RagiumItems.DEEP_STEEL_UPGRADE_SMITHING_TEMPLATE)
            output.acceptFromTable(RagiumItems.ARMORS, HTArmorVariant.entries, RagiumMaterialKeys.DEEP_STEEL)
            output.acceptFromTable(RagiumItems.TOOLS, HTVanillaToolVariant.entries, RagiumMaterialKeys.DEEP_STEEL)
            output.accept(RagiumItems.getTool(HTHammerToolVariant, RagiumMaterialKeys.DEEP_STEEL))
            // Other
            output.accept(RagiumItems.DRILL)

            output.accept(RagiumItems.POTION_BUNDLE)
            output.accept(RagiumItems.SLOT_COVER)
            output.accept(RagiumItems.TRADER_CATALOG)
            // Foods
            output.accept(RagiumItems.getIngot(FoodMaterialKeys.CHOCOLATE))

            output.accept(RagiumItems.ICE_CREAM)
            output.accept(RagiumItems.ICE_CREAM_SODA)

            output.accept(RagiumItems.getDust(FoodMaterialKeys.RAW_MEAT))
            output.accept(RagiumItems.getIngot(FoodMaterialKeys.RAW_MEAT))
            output.accept(RagiumItems.getIngot(FoodMaterialKeys.COOKED_MEAT))
            output.accept(RagiumItems.CANNED_COOKED_MEAT)

            output.accept(RagiumItems.MELON_PIE)

            output.accept(RagiumBlocks.SWEET_BERRIES_CAKE)
            output.accept(RagiumItems.SWEET_BERRIES_CAKE_SLICE)

            output.accept(RagiumItems.RAGI_CHERRY)
            output.accept(RagiumItems.FEVER_CHERRY)

            output.accept(RagiumItems.BOTTLED_BEE)
            output.accept(RagiumItems.AMBROSIA)
            // Tickets
            output.accept(RagiumItems.LOOT_TICKET)
            output.acceptAll(HTDefaultLootTickets.getDefaultLootTickets().values)
        }

    //    Extensions    //

    @JvmStatic
    private fun register(
        name: String,
        icon: String,
        action: CreativeModeTab.DisplayItemsGenerator,
    ): HTSimpleDeferredHolder<CreativeModeTab> = REGISTER.register(name) { id: ResourceLocation ->
        CreativeModeTab
            .builder()
            .title(Component.translatable(id.toDescriptionKey("itemGroup")))
            .icon(HTDeferredItem<Item>(RagiumAPI.id(icon))::toStack)
            .displayItems(action)
            .build()
    }

    fun <V : HTVariantKey> CreativeModeTab.Output.acceptFromTable(
        table: ImmutableTable<V, HTMaterialKey, HTDeferredItem<*>>,
        variants: Iterable<V>,
        key: HTMaterialLike,
    ) {
        variants
            .mapNotNull(table.column(key.asMaterialKey())::get)
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
                RagiumItems.getTool(HTHammerToolVariant, VanillaMaterialKeys.IRON),
            )
            insertAfter(
                Items.DIAMOND_PICKAXE,
                RagiumItems.getTool(HTHammerToolVariant, VanillaMaterialKeys.DIAMOND),
            )
            insertAfter(
                Items.NETHERITE_PICKAXE,
                RagiumItems.getTool(HTHammerToolVariant, VanillaMaterialKeys.NETHERITE),
            )
        }

        if (BLOCKS.`is`(key)) {
            for (tier: HTDrumTier in HTDrumTier.entries) {
                insertAfter(tier.getBlock(), tier.getMinecartItem())
            }
        }

        if (INGREDIENTS.`is`(key)) {
            insertAfter(RagiumItems.getDust(RagiumMaterialKeys.RAGINITE), RagiumItems.RAGI_COKE)
            insertBefore(RagiumItems.getIngot(RagiumMaterialKeys.RAGI_ALLOY), RagiumItems.RAGI_ALLOY_COMPOUND)

            insertAfter(RagiumItems.getDust(VanillaMaterialKeys.WOOD), RagiumItems.COMPRESSED_SAWDUST)
            insertAfter(RagiumItems.COMPRESSED_SAWDUST, RagiumItems.RESIN)

            insertAfter(RagiumItems.getGem(RagiumMaterialKeys.ELDRITCH_PEARL), Items.ECHO_SHARD)
            insertAfter(Items.ECHO_SHARD, RagiumItems.ECHO_STAR)
        }
    }
}
