package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumCreativeTabs {
    @JvmField
    val REGISTER: DeferredRegister<CreativeModeTab> =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RagiumAPI.MOD_ID)

    @JvmField
    val COMMON: DeferredHolder<CreativeModeTab, CreativeModeTab> =
        REGISTER.register("common") { _: ResourceLocation ->
            CreativeModeTab
                .builder()
                .title(Component.literal("Ragium"))
                .icon { RagiumItems.getMaterialItem(HTTagPrefix.INGOT, RagiumMaterials.RAGIUM).toStack() }
                .displayItems { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
                    output.accept(RagiumItems.CRUDE_OIL_BUCKET)
                    // Material Blocks
                    output.accept(RagiumBlocks.SOUL_MAGMA_BLOCK)
                    RagiumBlocks.ORES.values.forEach(output::accept)
                    RagiumBlocks.STORAGE_BLOCKS.values.forEach(output::accept)
                    output.accept(RagiumBlocks.SLAG_BLOCK)
                    // Decorations
                    output.accept(RagiumBlocks.CHEMICAL_GLASS)
                    output.accept(RagiumBlocks.OBSIDIAN_GLASS)
                    RagiumBlocks.LED_BLOCKS.values.forEach(output::accept)
                    output.accept(RagiumBlocks.PLASTIC_BLOCK)
                    // Material Items
                    RagiumItems.getMaterialItems(HTTagPrefix.DUST).forEach(output::accept)
                    output.accept(RagiumItems.BEE_WAX)

                    RagiumItems.getMaterialItems(HTTagPrefix.RAW_MATERIAL).forEach(output::accept)
                    output.accept(RagiumItems.SLAG)

                    RagiumItems.getMaterialItems(HTTagPrefix.GEM).forEach(output::accept)
                    output.accept(RagiumItems.SILKY_CRYSTAL)
                    output.accept(RagiumItems.CRIMSON_CRYSTAL)
                    output.accept(RagiumItems.WARPED_CRYSTAL)
                    output.accept(RagiumItems.OBSIDIAN_TEAR)

                    output.accept(RagiumItems.RAGI_ALLOY_COMPOUND)
                    RagiumItems.getMaterialItems(HTTagPrefix.INGOT).forEach(output::accept)

                    RagiumItems.getMaterialItems(HTTagPrefix.GEAR).forEach(output::accept)
                    // Foods
                    output.accept(RagiumBlocks.SPONGE_CAKE)
                    output.accept(RagiumBlocks.SWEET_BERRIES_CAKE)
                    RagiumItems.FOODS.forEach(output::accept)
                    // Tools
                    output.accept(RagiumItems.FORGE_HAMMER)
                    output.accept(RagiumItems.SILKY_PICKAXE)

                    output.accept(RagiumItems.DEFOLIANT)
                    output.accept(RagiumItems.DYNAMITE)
                    output.accept(RagiumItems.SLOT_LOCK)
                    output.accept(RagiumItems.SOAP)

                    output.accept(RagiumItems.ALUMINUM_CAN)

                    RagiumItems.PRESS_MOLDS.values.forEach(output::accept)
                    // Circuits
                    output.accept(RagiumItems.POLYMER_RESIN)
                    output.accept(RagiumItems.PLASTIC_PLATE)
                    output.accept(RagiumItems.CIRCUIT_BOARD)

                    output.accept(RagiumItems.BASIC_CIRCUIT)
                    output.accept(RagiumItems.ADVANCED_CIRCUIT)
                    output.accept(RagiumItems.ELITE_CIRCUIT)
                    output.accept(RagiumItems.ULTIMATE_CIRCUIT)
                    // Ingredients
                    output.accept(RagiumBlocks.SHAFT)
                    output.accept(RagiumItems.MACHINE_CASING)
                    output.accept(RagiumItems.CHEMICAL_MACHINE_CASING)
                    output.accept(RagiumItems.PRECISION_MACHINE_CASING)

                    output.accept(RagiumItems.ENGINE)
                    output.accept(RagiumItems.LED)
                    output.accept(RagiumItems.SOLAR_PANEL)

                    output.accept(RagiumItems.YELLOW_CAKE)
                    output.accept(RagiumItems.YELLOW_CAKE_PIECE)

                    output.accept(RagiumItems.RAGI_TICKET)

                    RagiumItems.REAGENTS.forEach(output::accept)
                }.build()
        }

    @JvmField
    val MACHINE: DeferredHolder<CreativeModeTab, CreativeModeTab> =
        REGISTER.register("machine") { _: ResourceLocation ->
            CreativeModeTab
                .builder()
                .title(Component.literal("Ragium - Machine"))
                .icon { ItemStack(RagiumBlocks.MANUAL_GRINDER) }
                .displayItems { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
                    buildList {
                        // Storage
                        add(RagiumBlocks.COPPER_DRUM)
                        // Manual Machines
                        add(RagiumBlocks.MANUAL_GRINDER)
                        add(RagiumBlocks.PRIMITIVE_BLAST_FURNACE)

                        add(RagiumBlocks.DISENCHANTING_TABLE)
                        // Utilities
                        addAll(RagiumBlocks.ADDONS)
                        addAll(RagiumBlocks.BURNERS)
                    }.forEach(output::accept)

                    // Machines
                    RagiumAPI
                        .getInstance()
                        .getMachineRegistry()
                        .blocks
                        .forEach(output::accept)
                }.build()
        }
}
