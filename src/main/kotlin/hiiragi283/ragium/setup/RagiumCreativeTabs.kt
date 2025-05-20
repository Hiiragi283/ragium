package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.toStack
import hiiragi283.ragium.common.util.HTBuildingBlockSets
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

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
            RagiumItems.RAGI_ALLOY_HAMMER,
        ) { provider: HolderLookup.Provider ->
            // Fluid Buckets
            addAll(RagiumFluidContents.REGISTER.itemEntries)
            // Natural Resources
            add(RagiumBlocks.ASH_LOG)
            add(RagiumBlocks.SILT)
            add(RagiumBlocks.MYSTERIOUS_OBSIDIAN)
            // Gems
            addAll(RagiumBlocks.RAGI_CRYSTAL_ORES.getItems())
            add(RagiumBlocks.RAGI_CRYSTAL_BLOCK)
            add(RagiumItems.RAGI_CRYSTAL)

            add(RagiumBlocks.CRIMSON_CRYSTAL_BLOCK)
            add(RagiumItems.CRIMSON_CRYSTAL)
            add(RagiumBlocks.CRIMSON_SOIL)

            add(RagiumBlocks.WARPED_CRYSTAL_BLOCK)
            add(RagiumItems.WARPED_CRYSTAL)
            // Ingots
            add(RagiumBlocks.RAGI_ALLOY_BLOCK)
            add(RagiumItems.RAGI_ALLOY_COMPOUND)
            add(RagiumItems.RAGI_ALLOY_INGOT)
            add(RagiumItems.RAGI_ALLOY_NUGGET)
            add(RagiumItems.RAGI_ALLOY_HAMMER)
            
            add(RagiumBlocks.ADVANCED_RAGI_ALLOY_BLOCK)
            add(RagiumItems.ADVANCED_RAGI_ALLOY_COMPOUND)
            add(RagiumItems.ADVANCED_RAGI_ALLOY_INGOT)
            add(RagiumItems.ADVANCED_RAGI_ALLOY_NUGGET)

            add(RagiumBlocks.AZURE_STEEL_BLOCK)
            add(RagiumItems.AZURE_STEEL_COMPOUND)
            add(RagiumItems.AZURE_STEEL_INGOT)
            add(RagiumItems.AZURE_STEEL_NUGGET)
            add(RagiumItems.AZURE_SHARD)
            addAll(RagiumItems.AZURE_STEEL_ARMORS.itemHolders)
            addAll(RagiumItems.AZURE_STEEL_TOOLS.itemHolders)

            add(RagiumBlocks.DEEP_STEEL_BLOCK)
            add(RagiumItems.DEEP_STEEL_INGOT)

            add(RagiumBlocks.CHEESE_BLOCK)
            add(RagiumItems.CHEESE_INGOT)

            add(RagiumBlocks.CHOCOLATE_BLOCK)
            add(RagiumItems.CHOCOLATE_INGOT)
            // Dusts
            addAll(RagiumBlocks.RAGINITE_ORES.getItems())
            add(RagiumItems.RAGINITE_DUST)
            add(RagiumItems.RAGI_COKE)

            add(RagiumItems.SAWDUST)
            add(RagiumItems.COMPRESSED_SAWDUST)

            add(RagiumItems.ASH_DUST)
            add(RagiumItems.OBSIDIAN_DUST)
            add(RagiumItems.SALTPETER_DUST)
            add(RagiumItems.SULFUR_DUST)

            add(RagiumItems.TAR)
            // Decorations
            RagiumBlocks.DECORATIONS.map(HTBuildingBlockSets::getItems).forEach(::addAll)

            addAll(RagiumBlocks.GLASSES)
            addAll(RagiumBlocks.LED_BLOCKS.values)
            // Machines
            addAll(RagiumBlocks.CASINGS)
            addAll(RagiumBlocks.MACHINES)
            addAll(RagiumBlocks.DEVICES)
            addAll(RagiumBlocks.DRUMS)
            // Tools
            add(RagiumItems.ENDER_BUNDLE)
            add(RagiumItems.ITEM_MAGNET)
            add(RagiumItems.EXP_MAGNET)
            add(RagiumItems.TRADER_CATALOG)
            add(RagiumItems.RAGI_EGG)
            add(RagiumItems.RAGI_LANTERN)

            addAll(RagiumItems.Molds.entries)
            // Tickets
            add(RagiumItems.BLANK_TICKET)

            add(RagiumItems.RAGI_TICKET)
            add(RagiumItems.AZURE_TICKET)
            add(RagiumItems.DEEP_TICKET)
            add(RagiumItems.TELEPORT_TICKET)
            add(RagiumItems.ETERNAL_TICKET)
            // Foods
            add(RagiumItems.ICE_CREAM)
            add(RagiumItems.ICE_CREAM_SODA)

            add(RagiumItems.MINCED_MEAT)
            add(RagiumItems.MEAT_INGOT)
            add(RagiumItems.COOKED_MEAT_INGOT)
            add(RagiumItems.CANNED_COOKED_MEAT)
            add(RagiumBlocks.COOKED_MEAT_ON_THE_BONE)

            add(RagiumItems.MELON_PIE)

            add(RagiumBlocks.SPONGE_CAKE)
            add(RagiumBlocks.SPONGE_CAKE_SLAB)
            add(RagiumBlocks.SWEET_BERRIES_CAKE)
            add(RagiumItems.SWEET_BERRIES_CAKE_PIECE)

            add(RagiumItems.RAGI_CHERRY)
            add(RagiumItems.RAGI_CHERRY_JAM)
            add(RagiumItems.FEVER_CHERRY)

            add(RagiumItems.BOTTLED_BEE)
            add(RagiumItems.EXP_BERRIES)
            add(RagiumItems.WARPED_WART)
            add(RagiumItems.AMBROSIA)
            // Ingredients
            add(RagiumItems.LED)
            add(RagiumItems.SOLAR_PANEL)
            add(RagiumItems.STONE_BOARD)

            add(RagiumItems.POLYMER_RESIN)
            add(RagiumItems.PLASTIC_PLATE)
            add(RagiumItems.BASIC_CIRCUIT)
            add(RagiumItems.ADVANCED_CIRCUIT)
            add(RagiumItems.CRYSTAL_PROCESSOR)
        }
}
