package hiiragi283.ragium.api.multiblock

import net.minecraft.core.component.DataComponentHolder
import net.minecraft.core.component.DataComponentMap
import net.neoforged.neoforge.common.util.TriState

/**
 * マルチブロックの判定結果と取得データをまとめたクラス
 */
@ConsistentCopyVisibility
data class HTMultiblockData private constructor(val result: TriState, private val components: DataComponentMap) : DataComponentHolder {
    override fun getComponents(): DataComponentMap = components

    companion object {
        @JvmField
        val FALSE = HTMultiblockData(TriState.FALSE, DataComponentMap.EMPTY)

        @JvmField
        val DEFAULT = HTMultiblockData(TriState.DEFAULT, DataComponentMap.EMPTY)

        @JvmStatic
        fun of(builder: DataComponentMap.Builder): HTMultiblockData = HTMultiblockData(TriState.TRUE, builder.build())
    }
}
