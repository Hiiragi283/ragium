package hiiragi283.ragium.api.property

/**
 * ミューテーブルな[HTPropertyHolder]
 * @see [HTPropertyHolderBuilder]
 */
interface HTMutablePropertyHolder : HTPropertyHolder {
    /**
     * 指定した[key]と[value]をセットします。
     */
    operator fun <T : Any> set(key: HTPropertyKey<T>, value: T)

    /**
     * 指定した[key]に紐づいた値を削除します。
     */
    fun remove(key: HTPropertyKey<*>)
}

/**
 * 指定した[keys]を[Unit]に対してセットします。
 */
fun HTMutablePropertyHolder.add(vararg keys: HTPropertyKey<Unit>) {
    keys.forEach { set(it, Unit) }
}

/**
 * 指定した[key]と[value]を，[value]が`null`でない場合はセットします。
 */
fun <T : Any> HTMutablePropertyHolder.setIfNonNull(key: HTPropertyKey<T>, value: T?) {
    value?.let { set(key, it) }
}

/**
 * 指定した[key]に紐づいた値が[filter]に一致した場合に削除します。
 */
fun <T : Any> HTMutablePropertyHolder.removeIf(key: HTPropertyKey<T>, filter: (T) -> Boolean) {
    val existValue: T = get(key) ?: return
    if (filter(existValue)) {
        remove(key)
    }
}

fun <T : Any> HTMutablePropertyHolder.computeIfAbsent(key: HTPropertyKey<T>, mapping: () -> T): T =
    get(key) ?: mapping().apply { set(key, this) }
