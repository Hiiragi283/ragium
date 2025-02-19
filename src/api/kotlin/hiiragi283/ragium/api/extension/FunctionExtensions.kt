package hiiragi283.ragium.api.extension

//    Consumer    //

/**
 * 何も処理しないブロックを返します。
 */
fun <T> emptyConsumer2(): (T) -> Unit = {}

//    Function    //

/**
 * 常に指定した値を返すブロックを返します。
 */
fun <T, U> constFunction2(value: U): (T) -> U = { value }

/**
 * 常に指定した値を返すブロックを返します。
 */
fun <T, U, V> constFunction3(value: V): (T, U) -> V = { _: T, _: U -> value }

/**
 * 指定した[map]をブロックに変換します。
 */
fun <K : Any, V : Any> mapFunction(map: Map<K, V>): (K) -> V? = map::get

/**
 * 指定した[map]をブロックに変換します。
 * @param default [Map.get]がnullの場合に使う値
 */
fun <K : Any, V : Any> mapFunction(map: Map<K, V>, default: V): (K) -> V = { map.getOrDefault(it, default) }

/**
 * 渡された値をそのまま返すブロックを返します。
 */
fun <T> identifyFunction(): (T) -> T = { it }

//    Runnable    //

/**
 * 何も処理しないブロックを返します。
 */
fun runNothing(): () -> Unit = {}
