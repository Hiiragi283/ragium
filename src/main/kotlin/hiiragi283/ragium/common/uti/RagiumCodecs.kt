package hiiragi283.ragium.common.uti

import com.mojang.serialization.Codec

object RagiumCodecs {

    @JvmField
    val UNIT_KT: Codec<Unit> = Codec.unit(Unit)

}