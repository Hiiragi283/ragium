package hiiragi283.ragium.api.network

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import net.minecraft.nbt.CompoundTag
import net.neoforged.neoforge.common.util.INBTSerializable

interface HTNbtCodec {
    /**
     * 指定した[writer]に値を書き込みます。
     */
    fun writeNbt(writer: Writer)

    /**
     * 指定した[reader]から値を読み取ります。
     */
    fun readNbt(reader: Reader)

    //    Writer    //

    interface Writer {
        fun <T : Any> write(codec: Codec<T>, key: String, value: T)

        fun write(key: String, serializable: INBTSerializable<CompoundTag>)
    }

    //    Reader    //

    interface Reader {
        fun <T : Any> read(codec: Codec<T>, key: String): DataResult<T>

        fun read(key: String, serializable: INBTSerializable<CompoundTag>)
    }
}
