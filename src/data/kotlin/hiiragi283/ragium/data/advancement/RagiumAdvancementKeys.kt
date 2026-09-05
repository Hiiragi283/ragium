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

    @JvmField
    val SOOTY_IRON: AdvancementKey = create("sooty_iron")

    // Mechanical
    @JvmField
    val MECHANICAL_MACHINE_CASING: AdvancementKey = create("mechanical_machine_casing")

    @JvmField
    val CRUSHER: AdvancementKey = create("crusher")
}
