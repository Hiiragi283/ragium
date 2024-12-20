package hiiragi283.ragium.api.data

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtOps
import kotlin.reflect.KMutableProperty0

class HTNbtCodec<T : Any>(val key: String, val codec: Codec<T>) {
    fun writeTo(nbt: NbtCompound, value: T) {
        codec
            .encodeStart(NbtOps.INSTANCE, value)
            .ifSuccess { nbt.put(key, it) }
    }

    fun readFrom(nbt: NbtCompound): DataResult<T> = codec.parse(NbtOps.INSTANCE, nbt.get(key))

    fun readAndSet(nbt: NbtCompound, setter: (T) -> Unit) {
        readFrom(nbt).ifSuccess(setter)
    }

    fun readAndSet(nbt: NbtCompound, property: KMutableProperty0<T>) {
        readFrom(nbt).ifSuccess(property::set)
    }
}
