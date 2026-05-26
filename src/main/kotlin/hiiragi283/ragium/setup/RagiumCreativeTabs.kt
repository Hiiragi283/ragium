package hiiragi283.ragium.setup

import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.registry.HTDeferredCreativeTabRegister
import hiiragi283.core.api.registry.HTDeferredItem
import hiiragi283.core.api.registry.HTSimpleDeferredHolder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item

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
        HTDeferredItem<Item>(CommonParts.INGOT.createId(RagiumMaterialKeys.RAGI_ALLOY)),
    ) { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
        // Items
        HTDeferredCreativeTabRegister.addToDisplay(parameters, output, RagiumItems.REGISTER.asSequence())
        // Blocks
        HTDeferredCreativeTabRegister.addToDisplay(parameters, output, RagiumBlocks.REGISTER.asItemSequence())
        // Fluids
        HTDeferredCreativeTabRegister.addToDisplay(parameters, output, RagiumFluids.REGISTER.asItemSequence())
    }
}
