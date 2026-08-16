package hiiragi283.lib.transfer

import java.util.function.Predicate

/**
 * スロットへのアクセスの種類を表すクラスです。
 *
 * 参照 : [Mekanism - AutomationType](https://github.com/mekanism/Mekanism/blob/26.1/src/api/java/mekanism/api/AutomationType.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
enum class HTTransferAccess {
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
         * 内部でのアクセスのみを通すフィルタ
         */
        @JvmField
        val INTERNAL_ONLY: Predicate<HTTransferAccess> = Predicate { it == INTERNAL }

        /**
         * GUIを介したアクセスのみを通すフィルタ
         */
        @JvmField
        val MANUAL_ONLY: Predicate<HTTransferAccess> = Predicate { it == MANUAL }

        /**
         * 外部からのアクセス以外を通すフィルタ
         */
        @JvmField
        val NOT_EXTERNAL: Predicate<HTTransferAccess> = Predicate { it != EXTERNAL }
    }
}
