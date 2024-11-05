package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.property.HTPropertyKey
import net.minecraft.util.Rarity

object HTMaterialPropertyKeys {
    @JvmField
    val RARITY: HTPropertyKey.Defaulted<Rarity> =
        HTPropertyKey.ofDefaulted(RagiumAPI.id("rarity")) { Rarity.COMMON }

    @JvmField
    val TRANSLATED_NAME: HTPropertyKey.Defaulted<List<String>> =
        HTPropertyKey.ofDefaulted(RagiumAPI.id("translated_name"), listOf("UNDEFINED", "未定義"))
}
