package hiiragi283.ragium.data.advancement

import hiiragi283.lib.data.advancement.AdvancementKey
import hiiragi283.ragium.api.RagiumAPI

data object RagiumAdvancementKeys {
    @JvmStatic
    private fun create(path: String): AdvancementKey = AdvancementKey(RagiumAPI.id(path))

    @JvmStatic
    private fun create(vararg path: String): AdvancementKey = AdvancementKey(RagiumAPI.id(*path))

    @JvmField
    val ROOT: AdvancementKey = create("root")
}
