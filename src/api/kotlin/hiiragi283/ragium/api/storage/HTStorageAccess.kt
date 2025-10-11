package hiiragi283.ragium.api.storage

import net.minecraft.core.Direction

/**
 * スロットへのアクセスの種類を表すクラス
 * @see [mekanism.api.AutomationType]
 */
enum class HTStorageAccess {
    /**
     * 外部からのアクセス
     */
    EXTERNAL,

    /**
     * 内部でのアクセス
     */
    INTERNAL,

    /**
     * GUIを介したアクセス
     */
    MANUAL,
    ;

    companion object {
        /**
         * 指定された[side]から[HTStorageAccess]を返します。
         * @return [side]が`null`の場合は[INTERNAL]，それ以外は[EXTERNAL]
         */
        @JvmStatic
        fun forHandler(side: Direction?): HTStorageAccess = when (side) {
            null -> INTERNAL
            else -> EXTERNAL
        }
    }
}
