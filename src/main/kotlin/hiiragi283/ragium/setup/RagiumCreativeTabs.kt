package hiiragi283.ragium.setup

import hiiragi283.core.api.registry.HTDeferredHolder
import hiiragi283.core.common.registry.register.HTDeferredCreativeTabRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.text.RagiumTranslation
import net.minecraft.world.item.CreativeModeTab

/**
 * @see mekanism.common.registries.MekanismCreativeTabs
 */
object RagiumCreativeTabs {
    @JvmField
    val REGISTER = HTDeferredCreativeTabRegister(RagiumAPI.MOD_ID)

    @JvmField
    val COMMON: HTDeferredHolder<CreativeModeTab, CreativeModeTab> = REGISTER.registerSimpleTab(
        "common",
        RagiumTranslation.RAGIUM,
        RagiumItems.RAGIUM_POWDER,
    ) { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
        // Items
        HTDeferredCreativeTabRegister.addToDisplay(parameters, output, RagiumItems.REGISTER.asSequence())
    }
}
