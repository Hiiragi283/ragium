package hiiragi283.ragium.data.advancement

import hiiragi283.lib.data.advancement.AdvancementKey
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConstants

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
    val ASSEMBLER: AdvancementKey = create(RagiumConstants.ASSEMBLER)

    @JvmField
    val CRUSHER: AdvancementKey = create(RagiumConstants.CRUSHER)

    // Heat
    @JvmField
    val HEAT_MACHINE_CASING: AdvancementKey = create("heat_machine_casing")

    @JvmField
    val FREEZER: AdvancementKey = create(RagiumConstants.FREEZER)

    @JvmField
    val BLACK_STEEL: AdvancementKey = create("black_steel")

    @JvmField
    val MELTER: AdvancementKey = create(RagiumConstants.MELTER)
}
