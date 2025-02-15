package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
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
                    RagiumBlocks.GLASSES.forEach(output::accept)
                    RagiumBlocks.LED_BLOCKS.values.forEach(output::accept)
                    output.accept(RagiumBlocks.PLASTIC_BLOCK)

                    // Material Items
                    fun registerPrefix(prefix: HTTagPrefix) {
                        RagiumItems.getMaterialItems(prefix).forEach(output::accept)
                    }

                    registerPrefix(HTTagPrefix.DUST)
                    output.accept(RagiumItems.BEE_WAX)

                    registerPrefix(HTTagPrefix.RAW_MATERIAL)
                    output.accept(RagiumItems.SLAG)

                    registerPrefix(HTTagPrefix.GEM)
                    output.accept(RagiumItems.SILKY_CRYSTAL)
                    output.accept(RagiumItems.CRIMSON_CRYSTAL)
                    output.accept(RagiumItems.WARPED_CRYSTAL)
                    output.accept(RagiumItems.OBSIDIAN_TEAR)

                    output.accept(RagiumItems.RAGI_ALLOY_COMPOUND)
                    registerPrefix(HTTagPrefix.INGOT)

                    registerPrefix(HTTagPrefix.GEAR)

                    registerPrefix(HTTagPrefix.DIRTY_DUST)
                    registerPrefix(HTTagPrefix.CLUMP)
                    registerPrefix(HTTagPrefix.SHARD)
                    registerPrefix(HTTagPrefix.CRYSTAL)
                    // Foods
                    output.accept(RagiumBlocks.SPONGE_CAKE)
                    output.accept(RagiumBlocks.SWEET_BERRIES_CAKE)
                    RagiumItems.FOODS.forEach(output::accept)
                    // Tools
                    output.accept(RagiumItems.FORGE_HAMMER)
                    output.accept(RagiumItems.SILKY_PICKAXE)

                    output.accept(RagiumItems.DEFOLIANT)
                    output.accept(RagiumItems.DYNAMITE)
                    output.accept(RagiumItems.MAGNET)
                    output.accept(RagiumItems.SOAP)

                    output.accept(RagiumItems.ALUMINUM_CAN)

                    RagiumItems.PRESS_MOLDS.values.forEach(output::accept)

                    output.accept(RagiumItems.REDSTONE_LENS)
                    output.accept(RagiumItems.GLOW_LENS)
                    output.accept(RagiumItems.PRISMARINE_LENS)
                    output.accept(RagiumItems.MAGICAL_LENS)
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
                    output.accept(RagiumItems.BROKEN_SPAWNER)

                    RagiumItems.REAGENTS.forEach(output::accept)
                    // Storage
                    output.accept(RagiumBlocks.COPPER_DRUM)
                    // Manual Machines
                    output.accept(RagiumBlocks.MANUAL_GRINDER)
                    output.accept(RagiumBlocks.PRIMITIVE_BLAST_FURNACE)

                    output.accept(RagiumBlocks.DISENCHANTING_TABLE)
                    // Utilities
                    RagiumBlocks.ADDONS.forEach(output::accept)
                    RagiumBlocks.BURNERS.forEach(output::accept)
                    // Machines
                    HTMachineType.entries.forEach(output::accept)
                }.build()
        }
}
