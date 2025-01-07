package hiiragi283.ragium.api.data

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.error
import hiiragi283.ragium.api.util.DelegatedLogger
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtOps
import org.slf4j.Logger
import kotlin.reflect.KMutableProperty0

/**
 * [NbtCompound]えの読み書きを体系化するクラス
 * @param key [NbtCompound]に保存する際のキー
 */
class HTNbtCodec<T : Any>(val key: String, val codec: Codec<T>) {
    companion object {
        @JvmStatic
        private val logger: Logger by DelegatedLogger()
    }

    /**
     * 指定した[nbt]に[value]を書き込みます。
     */
    fun writeTo(nbt: NbtCompound, value: T?) {
        if (value == null) return
        codec
            .encodeStart(NbtOps.INSTANCE, value)
            .ifSuccess { nbt.put(key, it) }
            .ifError(logger::error)
    }

    /**
     * 指定した[nbt]から値を読み取ります。
     * @return 戻り値は[DataResult]で包まれる
     */
    fun readFrom(nbt: NbtCompound): DataResult<T> = codec.parse(NbtOps.INSTANCE, nbt.get(key))

    /**
     * 指定した[nbt]から読み取った値を[setter]に渡します。
     */
    fun readAndSet(nbt: NbtCompound, setter: (T) -> Unit) {
        readFrom(nbt).ifSuccess(setter).ifError(logger::error)
    }

    /**
     * 指定した[nbt]から読み取った値を[property]に渡します。
     */
    fun readAndSet(nbt: NbtCompound, property: KMutableProperty0<T>) {
        readFrom(nbt).ifSuccess(property::set).ifError(logger::error)
    }

    /**
     * 指定した[nbt]から読み取った値を，nullでない場合にのみ[property]に渡します。
     */
    fun readAndSetNullable(nbt: NbtCompound, property: KMutableProperty0<T?>) {
        readFrom(nbt).ifSuccess(property::set).ifError(logger::error)
    }
}
