package hiiragi283.ragium.client.data

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.HTDynamicResourceProvider
import hiiragi283.core.api.resource.itemId
import hiiragi283.core.api.resource.toId
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.setup.RagiumItems
import net.mehvahdjukaar.moonlight.api.events.AfterLanguageLoadEvent
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask
import net.minecraft.world.level.block.Blocks
import java.util.function.Consumer

data object RagiumClientResourceProvider : HTDynamicResourceProvider.Client(RagiumAPI.MOD_ID) {
    override fun addDynamicTranslations(afterLanguageLoadEvent: AfterLanguageLoadEvent?) {}

    override fun regenerateDynamicAssets(executor: Consumer<ResourceGenTask>) {
        // Texture
        resprite(
            RagiumItems.CRYO_CHARGE.itemId,
            HTConst.MINECRAFT.toId(HTConst.ITEM, "wind_charge"),
            Blocks.BLUE_ICE,
        ).let(executor::accept)

        resprite(
            RagiumItems.CRUDE_SILICON.itemId,
            HTConst.MINECRAFT.toId(HTConst.ITEM, "light_gray_dye"),
            CommonMaterialKeys.SILICON,
        ).let(executor::accept)
        resprite(
            RagiumItems.SMOKELESS_POWDER.itemId,
            HTConst.MINECRAFT.toId(HTConst.ITEM, "gunpowder"),
            Blocks.TUFF,
        ).let(executor::accept)
    }
}
