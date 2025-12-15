package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTSimpleDeferredHolder
import hiiragi283.ragium.api.registry.impl.HTDeferredCreativeTabRegister
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.util.HTCreativeTabHelper
import net.minecraft.world.item.CreativeModeTab

object RagiumIntegrationCreativeTabs {
    @JvmField
    val REGISTER = HTDeferredCreativeTabRegister(RagiumAPI.MOD_ID)

    @JvmField
    val INTEGRATION: HTSimpleDeferredHolder<CreativeModeTab> = REGISTER.registerSimpleTab(
        "integration",
        RagiumTranslation.RAGIUM,
        RagiumItems.getHammer(RagiumMaterialKeys.RAGI_CRYSTAL),
    ) { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
        // Items
        HTCreativeTabHelper.addToDisplay(parameters, output, RagiumIntegrationItems.REGISTER.entries)
    }
}
