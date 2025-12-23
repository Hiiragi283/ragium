package hiiragi283.ragium.common.data.texture

import hiiragi283.core.api.data.texture.HTColorPalette
import java.awt.Color

object RagiumMaterialPalette {
    @JvmStatic
    val RAGINITE: HTColorPalette = HTColorPalette(
        arrayOf(
            Color(0xffc0bb),
            Color(0xff777c),
            Color(0xff3351),
            Color(0xe5003a),
            Color(0x990037),
            Color(0x4d0024),
        ),
    )

    @JvmStatic
    val ADVANCED_RAGI_ALLOY: HTColorPalette = HTColorPalette(
        arrayOf(
            Color(0xfff0aa),
            Color(0xffbd55),
            Color(0xff6600),
            Color(0xbf2400),
            Color(0x800004),
            Color(0x40000f),
        ),
    )

    @JvmStatic
    val RAGI_CRYSTAL: HTColorPalette = HTColorPalette(
        arrayOf(
            Color(0xffd4b9),
            Color(0xff8b72),
            Color(0xff2c34),
            Color(0xe5003a),
            Color(0x990047),
            Color(0x4c0034),
        ),
    )
}
