package hiiragi283.ragium.api.function

import hiiragi283.ragium.api.storage.HTStorageAccess
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * @see mekanism.api.functions.ConstantPredicates
 */
@Suppress("UNCHECKED_CAST")
object HTPredicates {
    @JvmStatic
    private val ALWAYS_TRUE: Predicate<Any> = Predicate { true }

    @JvmStatic
    private val ALWAYS_TRUE_BI: BiPredicate<Any, Any> = BiPredicate { _, _ -> true }

    @JvmStatic
    private val ALWAYS_FALSE: Predicate<Any> = Predicate { false }

    @JvmStatic
    private val ALWAYS_FALSE_BI: BiPredicate<Any, Any> = BiPredicate { _, _ -> false }

    @JvmStatic
    private val INTERNAL_ONLY: BiPredicate<Any, HTStorageAccess> =
        BiPredicate { _, access -> access == HTStorageAccess.INTERNAL }

    @JvmStatic
    private val NOT_EXTERNAL: BiPredicate<Any, HTStorageAccess> =
        BiPredicate { _, access -> access != HTStorageAccess.EXTERNAL }

    @JvmStatic
    private val MANUAL_ONLY: BiPredicate<Any, HTStorageAccess> =
        BiPredicate { _, access -> access == HTStorageAccess.MANUAL }

    @JvmStatic
    fun <T> alwaysTrue(): Predicate<T> = ALWAYS_TRUE as Predicate<T>

    @JvmStatic
    fun <T, U> alwaysTrueBi(): BiPredicate<T, U> = ALWAYS_TRUE_BI as BiPredicate<T, U>

    @JvmStatic
    fun <T> alwaysFalse(): Predicate<T> = ALWAYS_FALSE as Predicate<T>

    @JvmStatic
    fun <T, U> alwaysFalseBi(): BiPredicate<T, U> = ALWAYS_FALSE_BI as BiPredicate<T, U>

    @JvmStatic
    fun <T> internalOnly(): BiPredicate<T, HTStorageAccess> = INTERNAL_ONLY as BiPredicate<T, HTStorageAccess>

    @JvmStatic
    fun <T> notExternal(): BiPredicate<T, HTStorageAccess> = NOT_EXTERNAL as BiPredicate<T, HTStorageAccess>

    @JvmStatic
    fun <T> manualOnly(): BiPredicate<T, HTStorageAccess> = MANUAL_ONLY as BiPredicate<T, HTStorageAccess>
}
