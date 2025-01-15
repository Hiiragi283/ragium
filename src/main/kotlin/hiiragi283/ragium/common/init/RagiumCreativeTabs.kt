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
    val INGREDIENT: DeferredHolder<CreativeModeTab, CreativeModeTab> =
        REGISTER.register("ingredient") { _: ResourceLocation ->
            CreativeModeTab
                .builder()
                .title(Component.literal("Ragium - Ingredient"))
                .icon { ItemStack(RagiumItems.Ingots.RAGIUM) }
                .displayItems { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
                    RagiumBlocks.StorageBlocks.entries.forEach(output::accept)
                    RagiumBlocks.LEDBlocks.entries.forEach(output::accept)

                    RagiumItems.MATERIALS.forEach(output::accept)

                    RagiumItems.FOODS.forEach(output::accept)

                    RagiumItems.Circuits.entries.forEach(output::accept)
                    RagiumItems.PressMolds.entries.forEach(output::accept)
                    RagiumItems.Catalysts.entries.forEach(output::accept)
                    RagiumItems.FluidCubes.entries.forEach(output::accept)

                    RagiumItems.INGREDIENTS.forEach(output::accept)
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
                        addAll(RagiumBlocks.Grates.entries)
                        addAll(RagiumBlocks.Casings.entries)
                        addAll(RagiumBlocks.Hulls.entries)
                        addAll(RagiumBlocks.Coils.entries)
                    }.forEach(output::accept)

                    output.accept(RagiumBlocks.ENERGY_NETWORK_INTERFACE)

                    // Machine
                    HTMachineTier.entries.forEach { tier: HTMachineTier ->
                        RagiumAPI
                            .getInstance()
                            .machineRegistry
                            .keys
                            .mapNotNull { it.createItemStack(tier) }
                            .forEach(output::accept)
                    }

                    RagiumBlocks.Decorations.entries.forEach(output::accept)
                }.build()
        }

    @JvmField
    val STORAGE: DeferredHolder<CreativeModeTab, CreativeModeTab> =
        REGISTER.register("storage") { _: ResourceLocation ->
            CreativeModeTab
                .builder()
                .title(Component.literal("Ragium - Storage"))
                .icon { ItemStack(RagiumBlocks.Drums.ELITE) }
                .displayItems { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->

                    RagiumBlocks.Drums.entries.forEach(output::accept)
                }.build()
        }
}
