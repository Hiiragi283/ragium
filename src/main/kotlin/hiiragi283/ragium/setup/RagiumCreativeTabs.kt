package hiiragi283.ragium.setup

import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.registry.HTSimpleHolderLike
import hiiragi283.core.api.registry.toItemLike
import hiiragi283.core.common.registry.register.HTDeferredCreativeTabRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import net.minecraft.world.item.CreativeModeTab

/**
 * @see mekanism.common.registries.MekanismCreativeTabs
 */
object RagiumCreativeTabs {
    @JvmField
    val REGISTER = HTDeferredCreativeTabRegister(RagiumAPI.MOD_ID)

    @JvmField
    val COMMON: HTSimpleHolderLike<CreativeModeTab> = REGISTER.registerSimpleTab(
        "common",
        RagiumTranslation.RAGIUM,
        CommonParts.INGOT.createId(RagiumMaterialKeys.RAGI_ALLOY).toItemLike(),
    ) { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
        // Items
        HTDeferredCreativeTabRegister.addToDisplay(parameters, output, RagiumItems.REGISTER.asItemSequence())
        // Blocks
        HTDeferredCreativeTabRegister.addToDisplay(parameters, output, RagiumBlocks.REGISTER.asItemSequence())
        // Fluids
        HTDeferredCreativeTabRegister.addToDisplay(parameters, output, RagiumFluids.REGISTER.asItemSequence())
    }
}
