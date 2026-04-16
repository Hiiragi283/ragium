package hiiragi283.ragium.common.data

import hiiragi283.core.api.data.HTDynamicResourceProvider
import hiiragi283.ragium.api.RagiumAPI
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask
import java.util.function.Consumer

data object RagiumServerResourceProvider : HTDynamicResourceProvider.Server(RagiumAPI.MOD_ID) {
    override fun regenerateDynamicAssets(executor: Consumer<ResourceGenTask>) {
        // Data Map
    }
}
