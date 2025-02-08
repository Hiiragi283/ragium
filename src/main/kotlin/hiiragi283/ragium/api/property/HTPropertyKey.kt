package hiiragi283.ragium.api.property

import net.minecraft.resources.ResourceLocation

class HTPropertyKey<T : Any> internal constructor(val id: ResourceLocation, val defaultValue: () -> T) {
    companion object {
        @JvmStatic
        fun <T : Any> simple(id: ResourceLocation): HTPropertyKey<T> =
            HTPropertyKey(id) { error("HTPropertyKey: $id have no default value!") }

        @JvmStatic
        fun <T : Any> withDefault(id: ResourceLocation, value: T): HTPropertyKey<T> = withDefault<T>(id) { value }

        @JvmStatic
        fun <T : Any> withDefault(id: ResourceLocation, value: () -> T): HTPropertyKey<T> = HTPropertyKey(id, value)
    }

    /**
     * デフォルトの値を取得します
     * @throws IllegalStateException デフォルト値を持っていない場合
     */
    @Throws
    fun getDefaultValue(): T = defaultValue()

    /**
     * デフォルトの値を取得します
     * @return [Result]で包まれた値
     */
    fun getDefaultResult(): Result<T> = runCatching(::getDefaultValue)

    /**
     * 指定された[obj]を[T]にキャストします。
     * @return キャストできなかった場合はnull
     */
    @Suppress("UNCHECKED_CAST")
    fun cast(obj: Any?): T? = obj as? T

    override fun toString(): String = "HTPropertyKey[$id]"
}
