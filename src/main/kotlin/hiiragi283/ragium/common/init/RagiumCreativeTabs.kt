package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.toStack
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

@Suppress("unused")
object RagiumCreativeTabs {
    @JvmField
    val REGISTER: DeferredRegister<CreativeModeTab> =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(
        name: String,
        title: String,
        icon: ItemLike,
        action: MutableList<ItemLike>.(HolderLookup.Provider) -> Unit,
    ): DeferredHolder<CreativeModeTab, CreativeModeTab> = REGISTER.register(name) { _: ResourceLocation ->
        CreativeModeTab
            .builder()
            .title(Component.literal(title))
            .icon(icon::toStack)
            .displayItems { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
                buildList {
                    this.action(parameters.holders)
                }.forEach(output::accept)
            }.build()
    }

    @JvmField
    val COMMON: DeferredHolder<CreativeModeTab, CreativeModeTab> =
        register(
            "common",
            "Ragium",
            RagiumItems.Ingots.RAGI_ALLOY,
        ) { provider: HolderLookup.Provider ->
            // Material Blocks
            add(RagiumItems.CRUDE_OIL_BUCKET)
            addAll(RagiumBlocks.RAGINITE_ORES.getItems())
            addAll(RagiumBlocks.RAGI_CRYSTAL_ORES.getItems())
            add(RagiumBlocks.SILT)
            add(RagiumBlocks.ASH_LOG)

            addAll(RagiumBlocks.StorageBlocks.entries)
            // Material Items
            addAll(RagiumItems.Dusts.entries)
            add(RagiumItems.RAGI_ALLOY_COMPOUND)
            addAll(RagiumItems.Ingots.entries)
            addAll(RagiumItems.RawResources.entries)
            addAll(RagiumItems.MekResources.entries)
            // Decorations
            addAll(RagiumBlocks.RAGI_BRICK_SETS.getItems())
            addAll(RagiumBlocks.AZURE_TILE_SETS.getItems())
            addAll(RagiumBlocks.EMBER_STONE_SETS.getItems())
            addAll(RagiumBlocks.PLASTIC_SETS.getItems())
            addAll(RagiumBlocks.BLUE_NETHER_BRICK_SETS.getItems())

            addAll(RagiumBlocks.GLASSES)
            addAll(RagiumBlocks.LED_BLOCKS.values)
            // Machines
            addAll(RagiumBlocks.CASINGS)
            addAll(RagiumBlocks.MACHINES)
            addAll(RagiumBlocks.DEVICES)
            // Armors
            addAll(RagiumItems.AZURE_STEEL_ARMORS.itemHolders)
            // Tools
            addAll(RagiumItems.RAGI_ALLOY_TOOLS.itemHolders)
            addAll(RagiumItems.AZURE_STEEL_TOOLS.itemHolders)

            add(RagiumItems.ENDER_BUNDLE)
            add(RagiumItems.ITEM_MAGNET)
            add(RagiumItems.TRADER_CATALOG)

            addAll(RagiumItems.Molds.entries)
            // Foods
            add(RagiumBlocks.SPONGE_CAKE)
            add(RagiumBlocks.SPONGE_CAKE_SLAB)
            add(RagiumBlocks.SWEET_BERRIES_CAKE)
            addAll(RagiumItems.FOODS)
            // Ingredients
            add(RagiumItems.MACHINE_TEMPLATE)
            add(RagiumItems.ADVANCED_MACHINE_TEMPLATE)

            add(RagiumItems.ENGINE)
            add(RagiumItems.LED)
            add(RagiumItems.SOLAR_PANEL)
            add(RagiumItems.STONE_BOARD)

            add(RagiumItems.POLYMER_RESIN)
            add(RagiumItems.PLASTIC_PLATE)

            add(RagiumItems.SOAP)
            add(RagiumItems.TAR)
            add(RagiumItems.YELLOW_CAKE)
            add(RagiumItems.YELLOW_CAKE_PIECE)

            // addAll(RagiumItems.TICKETS)
        }
}
