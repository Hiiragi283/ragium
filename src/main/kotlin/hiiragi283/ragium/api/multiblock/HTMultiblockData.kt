package hiiragi283.ragium.api.multiblock

import hiiragi283.ragium.api.property.HTPropertyHolder
import net.fabricmc.fabric.api.util.TriState

/**
 * マルチブロックの判定結果と取得データをまとめたクラス
 */
@ConsistentCopyVisibility
data class HTMultiblockData private constructor(val result: TriState, private val delegate: HTPropertyHolder) :
    HTPropertyHolder by delegate {
        companion object {
            @JvmField
            val FALSE = HTMultiblockData(TriState.FALSE, HTPropertyHolder.Empty)

            @JvmField
            val DEFAULT = HTMultiblockData(TriState.DEFAULT, HTPropertyHolder.Empty)

            @JvmStatic
            fun of(holder: HTPropertyHolder): HTMultiblockData = HTMultiblockData(TriState.TRUE, holder)
        }
    }
