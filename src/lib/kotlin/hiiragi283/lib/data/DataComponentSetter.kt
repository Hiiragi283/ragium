package hiiragi283.lib.data

import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentType
import net.neoforged.neoforge.common.MutableDataComponentHolder

/**
 * [DataComponentType]に基づいて値を保存する処理を表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface DataComponentSetter {
    operator fun <T : Any> set(type: DataComponentType<T>, value: T)

    fun <T : Any> remove(type: DataComponentType<T>)

    fun <T : Any> setOrRemove(type: DataComponentType<T>, value: T?) {
        when (value) {
            null -> remove(type)
            else -> set(type, value)
        }
    }
}

/**
 * [DataComponentMap.Builder]を[DataComponentSetter]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun DataComponentSetter(builder: DataComponentMap.Builder): DataComponentSetter = MapDataComponentSetter(builder)

@JvmRecord
private data class MapDataComponentSetter(val builder: DataComponentMap.Builder) : DataComponentSetter {
    override fun <T : Any> set(type: DataComponentType<T>, value: T) {
        builder.set(type, value)
    }

    override fun <T : Any> remove(type: DataComponentType<T>) {
        builder.set(type, null)
    }
}

/**
 * [DataComponentPatch.Builder]を[DataComponentSetter]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun DataComponentSetter(builder: DataComponentPatch.Builder): DataComponentSetter = PatchDataComponentSetter(builder)

@JvmRecord
private data class PatchDataComponentSetter(val builder: DataComponentPatch.Builder) : DataComponentSetter {
    override fun <T : Any> set(type: DataComponentType<T>, value: T) {
        builder.set(type, value)
    }

    override fun <T : Any> remove(type: DataComponentType<T>) {
        builder.remove(type)
    }
}

/**
 * [MutableDataComponentHolder]を[DataComponentSetter]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun DataComponentSetter(holder: MutableDataComponentHolder): DataComponentSetter = HolderDataComponentSetter(holder)

@JvmRecord
private data class HolderDataComponentSetter(val holder: MutableDataComponentHolder) : DataComponentSetter {
    override fun <T : Any> set(type: DataComponentType<T>, value: T) {
        holder.set(type, value)
    }

    override fun <T : Any> remove(type: DataComponentType<T>) {
        holder.remove(type)
    }
}
