package hiiragi283.ragium.api.property

import com.mojang.serialization.DataResult
import net.minecraft.resources.ResourceLocation
import java.util.function.Function
import java.util.function.Supplier

class HTPropertyKey<T : Any> internal constructor(
    val id: ResourceLocation,
    val hasDefaultValue: Boolean,
    val defaultValue: Supplier<T>,
    private val validator: Function<T, DataResult<T>>,
) {
    companion object {
        @JvmStatic
        fun <T : Any> builder(id: ResourceLocation): Builder<T> = Builder<T>(id)

        @JvmStatic
        fun <T : Any> simple(id: ResourceLocation): HTPropertyKey<T> = builder<T>(id).build()
    }

    @Throws
    fun getDefaultValue(): T = defaultValue.get()

    fun getDefaultResult(): Result<T> = runCatching(::getDefaultValue)

    fun validate(value: T): DataResult<T> = validator.apply(value)

    /**
     * 指定された[obj]を[T]にキャストします。
     * @return キャストできなかった場合はnull
     */
    @Suppress("UNCHECKED_CAST")
    fun cast(obj: Any?): T? = obj as? T

    override fun toString(): String = "HTPropertyKey[$id]"

    class Builder<T : Any>(val id: ResourceLocation) {
        private var hasDefaultValue: Boolean = false
        private var defaultValue: Supplier<T> = Supplier { error("HTPropertyKey: $id have no default value!") }
        private var validator: Function<T, DataResult<T>> = Function(DataResult<T>::success)

        fun setDefaultValue(defaultValue: Supplier<T>): Builder<T> = apply {
            this.defaultValue = defaultValue
            this.hasDefaultValue = true
        }

        fun setValidation(validator: Function<T, DataResult<T>>): Builder<T> = apply {
            this.validator = validator
        }

        fun build(): HTPropertyKey<T> = HTPropertyKey(id, hasDefaultValue, defaultValue, validator)
    }
}
