package hiiragi283.ragium.api.property

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.util.DataFunction
import net.minecraft.resources.ResourceLocation
import java.util.function.Supplier

class HTPropertyKey<T : Any> internal constructor(
    val id: ResourceLocation,
    val hasDefaultValue: Boolean,
    val defaultValue: Supplier<T>,
    private val validator: DataFunction<T>,
) {
    companion object {
        @JvmStatic
        fun <T : Any> builder(id: ResourceLocation): Builder<T> = Builder<T>(id)

        @JvmStatic
        fun <T : Any> simple(id: ResourceLocation): HTPropertyKey<T> = builder<T>(id).build()
    }

    /**
     * デフォルトの値を取得します
     * @throws IllegalStateException デフォルト値を持っていない場合
     */
    @Throws
    fun getDefaultValue(): T = defaultValue.get()

    /**
     * デフォルトの値を取得します
     * @return [Result]で包まれた値
     */
    fun getDefaultResult(): Result<T> = runCatching(::getDefaultValue)

    /**
     * 指定した[value]を評価します。
     * @return [DataResult]で包まれた値
     */
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
        private var validator: DataFunction<T> = DataFunction.success()

        fun setDefaultValue(defaultValue: Supplier<T>): Builder<T> = apply {
            this.defaultValue = defaultValue
            this.hasDefaultValue = true
        }

        fun setValidation(validator: DataFunction<T>): Builder<T> = apply {
            this.validator = validator
        }

        fun build(): HTPropertyKey<T> = HTPropertyKey(id, hasDefaultValue, defaultValue, validator)
    }
}
