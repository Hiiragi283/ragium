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
            addAll(RagiumBlocks.RAGINITE_ORES.getItems())
            addAll(RagiumBlocks.RAGI_CRYSTAL_ORES.getItems())
            addAll(RagiumBlocks.STORAGE_BLOCKS.values)
            // Decorations
            addAll(RagiumBlocks.RAGI_BRICK_SETS.getItems())
            addAll(RagiumBlocks.AZURE_TILE_SETS.getItems())
            addAll(RagiumBlocks.PLASTIC_SETS.getItems())
            addAll(RagiumBlocks.BLUE_NETHER_BRICK_SETS.getItems())

            addAll(RagiumBlocks.GLASSES)
            addAll(RagiumBlocks.LED_BLOCKS.values)

            // Material Items
            addAll(RagiumItems.Dusts.entries)
            add(RagiumItems.RAGI_ALLOY_COMPOUND)
            add(RagiumItems.AZURE_STEEL_COMPOUND)
            addAll(RagiumItems.Ingots.entries)
            addAll(RagiumItems.RawResources.entries)
            addAll(RagiumItems.MekResources.entries)
            // Foods
            addAll(RagiumItems.FOODS)
            // Ingredients
            addAll(RagiumBlocks.CASINGS)

            // add(RagiumItems.ENGINE)
            // add(RagiumItems.LED)
            // add(RagiumItems.SOLAR_PANEL)

            // add(RagiumItems.SOAP)
            // add(RagiumItems.TAR)
            // add(RagiumItems.YELLOW_CAKE)
            // add(RagiumItems.YELLOW_CAKE_PIECE)

            // addAll(RagiumItems.TICKETS)
        }

    @JvmField
    val UTILITIES: DeferredHolder<CreativeModeTab, CreativeModeTab> =
        register(
            "utilities",
            "Ragium - Utilities",
            RagiumBlocks.SWEET_BERRIES_CAKE,
        ) { provider: HolderLookup.Provider ->
            // Foods
            add(RagiumBlocks.SPONGE_CAKE)
            add(RagiumBlocks.SPONGE_CAKE_SLAB)
            add(RagiumBlocks.SWEET_BERRIES_CAKE)
            addAll(RagiumItems.FOODS)
            // Armors
            // Tools
        }
}
