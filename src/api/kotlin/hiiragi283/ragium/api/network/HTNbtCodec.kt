package hiiragi283.ragium.api.network

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.data.BiCodec
import net.minecraft.nbt.CompoundTag
import net.neoforged.neoforge.common.util.INBTSerializable

interface HTNbtCodec {
    /**
     * 指定した[writer]に値を書き込みます。
     */
    fun writeNbt(writer: Writer) {}

    /**
     * 指定した[reader]から値を読み取ります。
     */
    fun readNbt(reader: Reader) {}

    //    Writer    //

    interface Writer {
        fun <T : Any> write(codec: BiCodec<*, T>, key: String, value: T)

        fun <T : Any> writeNullable(codec: BiCodec<*, T>, key: String, value: T?) {
            if (value == null) return
            write(codec, key, value)
        }

        fun write(key: String, serializable: INBTSerializable<CompoundTag>)
    }

    //    Reader    //

    interface Reader {
        fun <T : Any> read(codec: BiCodec<*, T>, key: String): DataResult<T> = read(codec.codec, key)

        fun <T : Any> read(codec: Codec<T>, key: String): DataResult<T>

        fun read(key: String, serializable: INBTSerializable<CompoundTag>)
    }
}
