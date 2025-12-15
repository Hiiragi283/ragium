package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTSimpleDeferredHolder
import hiiragi283.ragium.api.registry.impl.HTDeferredCreativeTabRegister
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.util.HTCreativeTabHelper
import net.minecraft.world.item.CreativeModeTab

/**
 * @see mekanism.common.registries.MekanismCreativeTabs
 */
object RagiumCreativeTabs {
    @JvmField
    val REGISTER = HTDeferredCreativeTabRegister(RagiumAPI.MOD_ID)

    @JvmField
    val COMMON: HTSimpleDeferredHolder<CreativeModeTab> = REGISTER.registerSimpleTab(
        "common",
        RagiumTranslation.RAGIUM,
        RagiumItems.getHammer(RagiumMaterialKeys.RAGI_ALLOY),
    ) { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
        // Items
        HTCreativeTabHelper.addToDisplay(parameters, output, RagiumItems.REGISTER.entries)
        // Blocks
        HTCreativeTabHelper.addToDisplay(parameters, output, RagiumBlocks.REGISTER.blockEntries)
        // Fluids
        HTCreativeTabHelper.addToDisplay(parameters, output, RagiumFluidContents.REGISTER.itemEntries)
    }
}
