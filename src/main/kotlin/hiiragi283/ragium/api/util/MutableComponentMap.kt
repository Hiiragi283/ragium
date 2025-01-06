package hiiragi283.ragium.api.util

import net.minecraft.component.ComponentChanges
import net.minecraft.component.ComponentMap
import net.minecraft.component.ComponentMapImpl
import net.minecraft.component.ComponentType

/**
 * ミューテーブルな[ComponentMap]
 * @see [ComponentMapImpl]
 */
interface MutableComponentMap : ComponentMap {
    /**
     * 指定した[type]に[value]を紐づけます。
     * @return 以前に紐づいていた値
     */
    fun <T : Any> set(type: ComponentType<T>, value: T?): T?

    /**
     * 指定した[type]に紐づいた値を削除します。
     * @return [type]に紐づいていた値
     */
    fun <T : Any> remove(type: ComponentType<T>): T?

    /**
     * [ComponentChanges]を返します。
     */
    fun getChanges(): ComponentChanges

    companion object {
        /**
         * 空で不変の[MutableComponentMap]の実装
         */
        @JvmField
        val EMPTY: MutableComponentMap = object : MutableComponentMap {
            override fun <T : Any> set(type: ComponentType<T>, value: T?): T? = null

            override fun <T : Any> remove(type: ComponentType<T>): T? = null

            override fun getChanges(): ComponentChanges = ComponentChanges.EMPTY

            override fun <T : Any> get(type: ComponentType<out T>): T? = null

            override fun getTypes(): Set<ComponentType<*>> = setOf()
        }

        /**
         * 指定した[map]から[MutableComponentMap]を返します。
         * @return [map]が[ComponentMapImpl]を継承していない場合はnull
         */
        @JvmStatic
        fun orNull(map: ComponentMap): MutableComponentMap? = (map as? ComponentMapImpl)?.let(::of)

        /**
         * 指定した[map]から[MutableComponentMap]を返します。
         * @return [map]が[ComponentMapImpl]を継承していない場合は[MutableComponentMap.EMPTY]
         */
        @JvmStatic
        fun orEmpty(map: ComponentMap): MutableComponentMap = orNull(map) ?: EMPTY

        /**
         * 指定した[mapImpl]を[MutableComponentMap]に変換します。
         */
        @JvmStatic
        fun of(mapImpl: ComponentMapImpl): MutableComponentMap = object : MutableComponentMap {
            override fun <T : Any> set(type: ComponentType<T>, value: T?): T? = mapImpl.set(type, value)

            override fun <T : Any> remove(type: ComponentType<T>): T? = mapImpl.remove(type)

            override fun getChanges(): ComponentChanges = mapImpl.changes

            override fun <T : Any> get(type: ComponentType<out T>): T? = mapImpl.get(type)

            override fun getTypes(): Set<ComponentType<*>> = mapImpl.types
        }
    }
}
