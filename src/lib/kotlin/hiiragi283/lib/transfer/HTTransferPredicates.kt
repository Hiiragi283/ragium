package hiiragi283.lib.transfer

import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * ストレージ周りで使用する条件をまとめたクラスです。
 *
 * 参照 : [Mekanism - AutomationType](https://github.com/mekanism/Mekanism/blob/26.1/src/api/java/mekanism/api/functions/ConstantPredicates.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@Suppress("UNCHECKED_CAST")
data object HTTransferPredicates {
    @JvmStatic
    private val ALWAYS_TRUE: Predicate<Any> = Predicate { true }

    @JvmStatic
    private val ALWAYS_TRUE_BI: BiPredicate<Any, Any> = BiPredicate { _, _ -> true }

    @JvmStatic
    private val ALWAYS_FALSE: Predicate<Any> = Predicate { false }

    @JvmStatic
    private val ALWAYS_FALSE_BI: BiPredicate<Any, Any> = BiPredicate { _, _ -> false }

    @JvmStatic
    private val INTERNAL_ONLY: BiPredicate<Any, HTTransferAccess> = BiPredicate { _, access -> access == HTTransferAccess.INTERNAL }

    @JvmStatic
    private val NOT_EXTERNAL: BiPredicate<Any, HTTransferAccess> = BiPredicate { _, access -> access != HTTransferAccess.EXTERNAL }

    @JvmStatic
    private val MANUAL_ONLY: BiPredicate<Any, HTTransferAccess> = BiPredicate { _, access -> access == HTTransferAccess.MANUAL }

    @JvmStatic
    fun <T> alwaysTrue(): Predicate<T> = ALWAYS_TRUE as Predicate<T>

    @JvmStatic
    fun <T, U> alwaysTrueBi(): BiPredicate<T, U> = ALWAYS_TRUE_BI as BiPredicate<T, U>

    @JvmStatic
    fun <T> alwaysFalse(): Predicate<T> = ALWAYS_FALSE as Predicate<T>

    @JvmStatic
    fun <T, U> alwaysFalseBi(): BiPredicate<T, U> = ALWAYS_FALSE_BI as BiPredicate<T, U>

    @JvmStatic
    fun <T> internalOnly(): BiPredicate<T, HTTransferAccess> = INTERNAL_ONLY as BiPredicate<T, HTTransferAccess>

    @JvmStatic
    fun <T> notExternal(): BiPredicate<T, HTTransferAccess> = NOT_EXTERNAL as BiPredicate<T, HTTransferAccess>

    @JvmStatic
    fun <T> manualOnly(): BiPredicate<T, HTTransferAccess> = MANUAL_ONLY as BiPredicate<T, HTTransferAccess>
}
