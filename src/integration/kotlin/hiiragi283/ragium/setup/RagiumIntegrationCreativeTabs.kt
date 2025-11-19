package hiiragi283.ragium.setup

import hiiragi283.ragium.RagiumIntegration
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.registry.HTSimpleDeferredHolder
import hiiragi283.ragium.api.registry.impl.HTDeferredCreativeTabRegister
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.util.HTCreativeTabHelper
import net.minecraft.world.item.CreativeModeTab

object RagiumIntegrationCreativeTabs {
    @JvmField
    val REGISTER = HTDeferredCreativeTabRegister(RagiumAPI.MOD_ID)

    @JvmField
    val INTEGRATION: HTSimpleDeferredHolder<CreativeModeTab> = REGISTER.registerSimpleTab(
        "integration",
        RagiumTranslation.RAGIUM,
        RagiumItems.getHammer(RagiumMaterialKeys.RAGI_CRYSTAL),
    ) { _: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
        // Items
        HTCreativeTabHelper.addToDisplay(output, RagiumIntegrationItems.REGISTER.entries)
        // Blocks
        if (RagiumIntegration.isLoaded(RagiumConst.FARMERS_DELIGHT)) {
            HTCreativeTabHelper.addToDisplay(output, RagiumDelightContents.BLOCK_REGISTER.blockEntries)
        }
    }
}
