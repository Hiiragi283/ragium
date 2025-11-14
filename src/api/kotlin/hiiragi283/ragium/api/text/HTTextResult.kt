package hiiragi283.ragium.api.text

import net.minecraft.network.chat.Component

/**
 * @see com.mojang.serialization.DataResult
 * @see Result
 */
sealed interface HTTextResult<T> {
    companion object {
        @JvmStatic
        fun <T> success(value: T): HTTextResult<T> = Success(value)

        @JvmStatic
        fun <T> failure(message: Component): HTTextResult<T> = Failure(message)

        @JvmStatic
        fun <T> failure(translation: HTTranslation): HTTextResult<T> = failure(translation.translate())

        @JvmStatic
        fun <T> failure(hasText: HTHasText): HTTextResult<T> = failure(hasText.getText())
    }

    val isSuccess: Boolean get() = this is Success<*>
    val isFailure: Boolean get() = this is Failure<*>

    fun result(): T?

    fun error(): Component?

    fun getOrThrow(): T = getOrThrow(::error)

    fun <E : Throwable> getOrThrow(exception: (String) -> E): T

    fun <R> map(transform: (T) -> R): HTTextResult<R>

    fun <R> flatMap(transform: (T) -> HTTextResult<R>): HTTextResult<R>

    fun <R> fold(onSuccess: (T) -> R, onFailure: (Component) -> R): R

    fun onSuccess(action: (T) -> Unit): HTTextResult<T>

    fun onFailure(action: (Component) -> Unit): HTTextResult<T>

    @JvmInline
    private value class Success<T>(private val value: T) : HTTextResult<T> {
        override fun result(): T = value

        override fun error(): Component? = null

        override fun <E : Throwable> getOrThrow(exception: (String) -> E): T = value

        override fun <R> map(transform: (T) -> R): HTTextResult<R> = Success(transform(value))

        override fun <R> flatMap(transform: (T) -> HTTextResult<R>): HTTextResult<R> = transform(value)

        override fun <R> fold(onSuccess: (T) -> R, onFailure: (Component) -> R): R = onSuccess(value)

        override fun onSuccess(action: (T) -> Unit): HTTextResult<T> {
            action(value)
            return this
        }

        override fun onFailure(action: (Component) -> Unit): HTTextResult<T> = this
    }

    @JvmInline
    private value class Failure<T>(private val message: Component) : HTTextResult<T> {
        override fun result(): T? = null

        override fun error(): Component = message

        override fun <E : Throwable> getOrThrow(exception: (String) -> E): T = throw exception(message.string)

        override fun <R> map(transform: (T) -> R): HTTextResult<R> = Failure(message)

        override fun <R> flatMap(transform: (T) -> HTTextResult<R>): HTTextResult<R> = Failure(message)

        override fun <R> fold(onSuccess: (T) -> R, onFailure: (Component) -> R): R = onFailure(message)

        override fun onSuccess(action: (T) -> Unit): HTTextResult<T> = this

        override fun onFailure(action: (Component) -> Unit): HTTextResult<T> {
            action(message)
            return this
        }
    }
}
