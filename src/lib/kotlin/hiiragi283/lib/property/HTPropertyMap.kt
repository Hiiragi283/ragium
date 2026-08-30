package hiiragi283.lib.property

/**
 * [Map]に基づく[HTPropertyGetter]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
class HTPropertyMap private constructor(private val map: Map<HTPropertyKey<*>, Any>) : HTPropertyGetter {
    override fun contains(key: HTPropertyKey<*>): Boolean = key in map

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(key: HTPropertyKey<T>): T? = map[key] as? T

    fun toMutable(): Mutable = Mutable(map.toMutableMap())

    /**
     * 可変な[HTPropertyGetter]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.2
     */
    class Mutable(private val map: MutableMap<HTPropertyKey<*>, Any>, private val delegate: HTPropertyMap) : HTPropertyGetter by delegate {
        constructor(map: MutableMap<HTPropertyKey<*>, Any>) : this(map, HTPropertyMap(map))

        constructor() : this(hashMapOf())

        /**
         * 指定した[key]と[value]を追加します。
         * @return 以前に紐づいていた値
         */
        @Suppress("UNCHECKED_CAST")
        fun <T : Any> put(key: HTPropertyKey<T>, value: T?): T? {
            if (value == null) return remove(key)
            return map.put(key, value) as? T
        }

        /**
         * 指定した[key]と[value]を追加します。
         */
        operator fun <T : Any> set(key: HTPropertyKey<T>, value: T) {
            put(key, value)
        }

        /**
         * 指定した[key]を削除します。
         */
        @Suppress("UNCHECKED_CAST")
        fun <T : Any> remove(key: HTPropertyKey<T>): T? = map.remove(key) as? T

        fun toImmutable(): HTPropertyMap = delegate
    }
}
