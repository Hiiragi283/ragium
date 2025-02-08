package hiiragi283.ragium.api.property

import com.mojang.serialization.DataResult

/**
 * 空で不変な[HTPropertyHolder]の実装
 */
object EmptyPropertyHolder : HTPropertyHolder {
    override val hasProperty: Boolean = true

    override fun <T : Any> getResult(key: HTPropertyKey<T>): DataResult<T> = DataResult.error { "Empty Property Holder" }
}
