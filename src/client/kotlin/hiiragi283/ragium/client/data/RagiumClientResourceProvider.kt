package hiiragi283.ragium.client.data

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.HTDynamicResourceProvider
import hiiragi283.core.api.resource.itemId
import hiiragi283.core.api.resource.toId
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
            HTConst.MINECRAFT.toId(HTConst.ITEM, "wind_charge.png"),
            Blocks.BLUE_ICE,
        ).let(executor::accept)
    }
}
