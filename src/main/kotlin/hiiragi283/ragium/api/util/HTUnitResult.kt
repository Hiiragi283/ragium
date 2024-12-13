package hiiragi283.ragium.api.util

import com.mojang.serialization.DataResult
import net.minecraft.text.Text

/**
 * Simple replacement for [DataResult]<[Unit]>
 */
sealed interface HTUnitResult {
    companion object {
        @JvmStatic
        fun success(): HTUnitResult = Success

        @JvmStatic
        fun errorString(message: () -> String): HTUnitResult = error { Text.literal(message()) }

        @JvmStatic
        fun error(message: () -> Text): HTUnitResult = Error(message)

        @JvmStatic
        fun fromBoolString(value: Boolean, message: () -> String): HTUnitResult = if (value) success() else errorString(message)

        @JvmStatic
        fun fromBool(value: Boolean, message: () -> Text): HTUnitResult = if (value) success() else error(message)

        @JvmStatic
        fun fromNullString(value: Any?, message: () -> String): HTUnitResult = value?.let { success() } ?: error(message)

        @JvmStatic
        fun fromNull(value: Any?, message: () -> Text): HTUnitResult = value?.let { success() } ?: error(message)
    }

    /**
     * Transform into [DataResult]
     */
    fun <T : Any> map(supplier: () -> T): DataResult<T>

    /**
     * Transform into [T] value
     */
    fun <T> mapOrElse(success: () -> T, error: (Text) -> T): T

    /**
     * Transform into [Boolean]
     */
    fun toBoolean(): Boolean = mapOrElse({ true }, { false })

    /**
     * Transform into [T] value if [isSuccess], or null if [isError]
     */
    fun <T : Any> toValue(value: T): T? = mapOrElse({ value }, { null })

    /**
     * Transform into other [HTUnitResult]
     */
    fun flatMap(supplier: () -> HTUnitResult): HTUnitResult

    /**
     * Transform into [DataResult]
     */
    fun <T : Any> dataMap(supplier: () -> DataResult<T>): DataResult<T>

    /**
     * Called if [isSuccess]
     */
    fun ifSuccess(action: () -> Unit): HTUnitResult

    /**
     * Called if [isError]
     */
    fun ifError(action: (Text) -> Unit): HTUnitResult

    val isSuccess: Boolean

    val isError: Boolean
        get() = !isSuccess

    //    Success    //

    private data object Success : HTUnitResult {
        override fun <T : Any> map(supplier: () -> T): DataResult<T> = DataResult.success(supplier())

        override fun <T> mapOrElse(success: () -> T, error: (Text) -> T): T = success()

        override fun flatMap(supplier: () -> HTUnitResult): HTUnitResult = supplier()

        override fun <T : Any> dataMap(supplier: () -> DataResult<T>): DataResult<T> = supplier()

        override fun ifSuccess(action: () -> Unit): HTUnitResult = apply { action() }

        override fun ifError(action: (Text) -> Unit): HTUnitResult = this

        override val isSuccess: Boolean = true
    }

    //    Error    //

    private class Error(val supplier: () -> Text) : HTUnitResult {
        val message: Text
            get() = supplier()

        override fun <T : Any> map(supplier: () -> T): DataResult<T> = DataResult.error(this@Error.supplier.invoke()::getString)

        override fun <T> mapOrElse(success: () -> T, error: (Text) -> T): T = error(message)

        override fun flatMap(supplier: () -> HTUnitResult): HTUnitResult = this

        override fun <T : Any> dataMap(supplier: () -> DataResult<T>): DataResult<T> =
            DataResult.error(this@Error.supplier.invoke()::getString)

        override fun ifSuccess(action: () -> Unit): HTUnitResult = this

        override fun ifError(action: (Text) -> Unit): HTUnitResult = apply { action(message) }

        override val isSuccess: Boolean = false
    }
}
