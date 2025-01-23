package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
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
                .icon { ItemStack(RagiumItems.Ingots.RAGIUM) }
                .displayItems { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
                    // Material Blocks
                    RagiumBlocks.Ores.entries.forEach(output::accept)
                    RagiumBlocks.StorageBlocks.entries.forEach(output::accept)
                    // Material Items
                    RagiumItems.Dusts.entries.forEach(output::accept)
                    output.accept(RagiumItems.BEE_WAX)
                    output.accept(RagiumItems.DEEPANT)

                    RagiumItems.Gears.entries.forEach(output::accept)

                    output.accept(RagiumItems.RESIDUAL_COKE)
                    RagiumItems.RawResources.entries.forEach(output::accept)
                    output.accept(RagiumItems.SLAG)
                    output.accept(RagiumItems.CRIMSON_CRYSTAL)
                    output.accept(RagiumItems.WARPED_CRYSTAL)

                    RagiumItems.Ingots.entries.forEach(output::accept)

                    RagiumItems.Rods.entries.forEach(output::accept)
                    // Foods
                    RagiumItems.FOODS.forEach(output::accept)
                    // Tools
                    output.accept(RagiumItems.FORGE_HAMMER)
                    output.accept(RagiumItems.SLOT_LOCK)
                    RagiumItems.PRESS_MOLDS.forEach(output::accept)
                    RagiumItems.CATALYSTS.forEach(output::accept)
                    // Circuits
                    RagiumItems.Plastics.entries.forEach(output::accept)
                    output.accept(RagiumItems.CIRCUIT_BOARD)
                    RagiumItems.Circuits.entries.forEach(output::accept)
                    // Ingredients
                    RagiumItems.FluidCubes.entries.forEach(output::accept)

                    buildList {
                        addAll(RagiumItems.INGREDIENTS)

                        remove(RagiumItems.BEE_WAX)
                        remove(RagiumItems.CIRCUIT_BOARD)
                        remove(RagiumItems.CRIMSON_CRYSTAL)
                        remove(RagiumItems.DEEPANT)
                        remove(RagiumItems.RESIDUAL_COKE)
                        remove(RagiumItems.SLAG)
                        remove(RagiumItems.WARPED_CRYSTAL)
                    }.forEach(output::accept)
                    RagiumItems.Radioactives.entries.forEach(output::accept)
                }.build()
        }

    @JvmField
    val MACHINE: DeferredHolder<CreativeModeTab, CreativeModeTab> =
        REGISTER.register("machine") { _: ResourceLocation ->
            CreativeModeTab
                .builder()
                .title(Component.literal("Ragium - Machine"))
                .icon { ItemStack(RagiumBlocks.Hulls.ELITE) }
                .displayItems { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
                    buildList {
                        // Components
                        addAll(RagiumBlocks.Grates.entries)
                        addAll(RagiumBlocks.Casings.entries)
                        addAll(RagiumBlocks.Hulls.entries)
                        addAll(RagiumBlocks.Coils.entries)

                        add(RagiumBlocks.SHAFT)
                        // Crate
                        // Drum
                        addAll(RagiumBlocks.Drums.entries)
                        // Manual Machines
                        add(RagiumBlocks.MANUAL_GRINDER)
                        // Utilities
                        add(RagiumBlocks.ENERGY_NETWORK_INTERFACE)
                    }.forEach(output::accept)

                    // Machines
                    HTMachineTier.entries.forEach { tier: HTMachineTier ->
                        RagiumAPI
                            .getInstance()
                            .machineRegistry
                            .keys
                            .mapNotNull { it.createItemStack(tier) }
                            .forEach(output::accept)
                    }

                    buildList {
                        addAll(RagiumBlocks.LEDBlocks.entries)
                        add(RagiumBlocks.PLASTIC_BLOCK)
                        addAll(RagiumBlocks.Decorations.entries)
                    }.forEach(output::accept)
                }.build()
        }
}
