package hiiragi283.ragium.api.util

import com.mojang.serialization.DataResult
import net.minecraft.text.Text

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

    fun <T : Any> map(supplier: () -> T): DataResult<T>

    fun <T : Any> mapOrElse(success: () -> T, error: (Text) -> T): T

    fun toBoolean(): Boolean = mapOrElse({ true }, { false })

    fun flatMap(supplier: () -> HTUnitResult): HTUnitResult

    fun <T : Any> dataMap(supplier: () -> DataResult<T>): DataResult<T>

    fun ifSuccess(action: () -> Unit): HTUnitResult

    fun ifError(action: (Text) -> Unit): HTUnitResult

    val isSuccess: Boolean

    val isError: Boolean
        get() = !isSuccess

    //    Success    //

    private data object Success : HTUnitResult {
        override fun <T : Any> map(supplier: () -> T): DataResult<T> = DataResult.success(supplier())

        override fun <T : Any> mapOrElse(success: () -> T, error: (Text) -> T): T = success()

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

        override fun <T : Any> mapOrElse(success: () -> T, error: (Text) -> T): T = error(message)

        override fun flatMap(supplier: () -> HTUnitResult): HTUnitResult = this

        override fun <T : Any> dataMap(supplier: () -> DataResult<T>): DataResult<T> =
            DataResult.error(this@Error.supplier.invoke()::getString)

        override fun ifSuccess(action: () -> Unit): HTUnitResult = this

        override fun ifError(action: (Text) -> Unit): HTUnitResult = apply { action(message) }

        override val isSuccess: Boolean = false
    }
}
