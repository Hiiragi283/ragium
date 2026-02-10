package hiiragi283.ragium.client.datagen

import hiiragi283.core.api.data.HTDynamicResourceProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.client.datagen.lang.RagiumEnglishLangProvider
import hiiragi283.ragium.client.datagen.lang.RagiumJapaneseLangProvider
import hiiragi283.ragium.client.datagen.model.RagiumModelProvider
import net.mehvahdjukaar.moonlight.api.events.AfterLanguageLoadEvent
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask
import java.util.function.Consumer

/**
 * @see hiiragi283.core.client.datagen.HCClientResourceProvider
 */
object RagiumClientResourceProvider : HTDynamicResourceProvider.Client(RagiumAPI.MOD_ID) {
    override fun addDynamicTranslations(afterLanguageLoadEvent: AfterLanguageLoadEvent) {}

    override fun regenerateDynamicAssets(executor: Consumer<ResourceGenTask>) {
        HTDynamicResourceProvider.addMaterialIds(this::addSupportedNamespaces)

        // Lang
        executor.accept(RagiumEnglishLangProvider)
        executor.accept(RagiumJapaneseLangProvider)
        // Model
        executor.accept(RagiumModelProvider)
        // Texture
    }
}
