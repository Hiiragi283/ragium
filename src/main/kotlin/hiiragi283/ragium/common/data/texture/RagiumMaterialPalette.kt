package hiiragi283.ragium.common.data.texture

import hiiragi283.core.api.data.texture.HTArrayColorPalette
import hiiragi283.core.api.data.texture.HTColorPalette
import java.awt.Color

object RagiumMaterialPalette {
    @JvmStatic
    val RAGINITE: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0xffbbbd),
            Color(0xff778a),
            Color(0xff3366),
            Color(0xe50052),
            Color(0x990047),
            Color(0x4d002c),
        ),
    )

    @JvmStatic
    val ADVANCED_RAGI_ALLOY: HTColorPalette = HTArrayColorPalette(
        arrayOf(
            Color(0xffdbbb),
            Color(0xffa877),
            Color(0xff6633),
            Color(0xe52100),
            Color(0x990500),
            Color(0x4d0005),
        ),
    )
}
