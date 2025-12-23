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
            Color(0xffefaa),
            Color(0xffcd55),
            Color(0xff9900),
            Color(0xbf5e00),
            Color(0x803100),
            Color(0x401200),
        ),
    )
}
