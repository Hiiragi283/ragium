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
                    RagiumItems.getMaterialItems(HTTagPrefix.DUST).forEach(output::accept)
                    output.accept(RagiumItems.BEE_WAX)

                    RagiumItems.getMaterialItems(HTTagPrefix.RAW_MATERIAL).forEach(output::accept)

                    RagiumItems.getMaterialItems(HTTagPrefix.GEM).forEach(output::accept)
                    RagiumItems.OTHER_GEMS.forEach(output::accept)

                    RagiumItems.getMaterialItems(HTTagPrefix.INGOT).forEach(output::accept)
                    RagiumItems.OTHER_RESOURCES.forEach(output::accept)

                    RagiumItems.getMaterialItems(HTTagPrefix.GEAR).forEach(output::accept)

                    RagiumItems.getMaterialItems(HTTagPrefix.ROD).forEach(output::accept)
                    // Foods
                    output.accept(RagiumBlocks.SPONGE_CAKE)
                    output.accept(RagiumBlocks.SWEET_BERRIES_CAKE)
                    RagiumItems.FOODS.forEach(output::accept)
                    // Tools
                    output.accept(RagiumItems.FORGE_HAMMER)
                    output.accept(RagiumItems.SILKY_PICKAXE)

                    output.accept(RagiumItems.DYNAMITE)
                    output.accept(RagiumItems.SLOT_LOCK)

                    RagiumItems.PRESS_MOLDS.forEach(output::accept)
                    // Circuits
                    RagiumItems.CIRCUITS.forEach(output::accept)
                    // Ingredients
                    output.accept(RagiumBlocks.SHAFT)
                    RagiumItems.REAGENTS.forEach(output::accept)
                    RagiumItems.INGREDIENTS.forEach(output::accept)
                    RagiumItems.RADIOACTIVES.forEach(output::accept)
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
                        // Utilities
                        addAll(RagiumBlocks.ADDONS)
                        addAll(RagiumBlocks.BURNERS_NEW)
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
