package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTLangType
import hiiragi283.ragium.api.property.HTPropertyKey
import net.minecraft.util.Rarity

object HTMaterialPropertyKeys {
    @JvmField
    val RARITY: HTPropertyKey.Defaulted<Rarity> =
        HTPropertyKey.ofDefaulted(RagiumAPI.id("rarity")) { Rarity.COMMON }

    @JvmField
    val TRANSLATED_NAME: HTPropertyKey.Defaulted<(HTLangType) -> String> =
        HTPropertyKey.ofDefaulted(RagiumAPI.id("en_name"), value = {
            when (it) {
                HTLangType.EN_US -> "UNDEFINED"
                HTLangType.JA_JP -> "未定義"
            }
        })
}
