package hiiragi283.ragium.data.client.lang

import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.data.PackOutput

class RagiumEnglishLangProvider(output: PackOutput) : HTLangProvider.English(output, RagiumAPI.MOD_ID) {
    override fun addTranslations() {
        // Item
        add(RagiumItems.RAGI_ALLOY_COMPOUND, "Ragi-Alloy Compound")
        add(RagiumItems.RAGIUM_POWDER, "Ragium Powder")
    }
}
