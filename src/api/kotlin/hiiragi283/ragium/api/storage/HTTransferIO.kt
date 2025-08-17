package hiiragi283.ragium.api.storage

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.toDescriptionKey
import net.minecraft.core.Direction
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.StringRepresentable
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs
import java.util.function.BiConsumer
import java.util.function.Function

enum class HTTransferIO(val canInsert: Boolean, val canExtract: Boolean, val color: Int) : StringRepresentable {
    INPUT_ONLY(true, false, 0xFF0033),
    OUTPUT_ONLY(false, true, 0x3300FF),
    BOTH(true, true, 0x00FF33),
    NONE(false, false, 0x333333),
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTTransferIO> = StringRepresentable.fromEnum(HTTransferIO::values)

        @JvmField
        val STREAM_CODEC: StreamCodec<FriendlyByteBuf, HTTransferIO> =
            NeoForgeStreamCodecs.enumCodec(HTTransferIO::class.java)

        @JvmStatic
        fun fromOrdinal(ordinal: Int): HTTransferIO = entries.getOrNull(ordinal) ?: BOTH
    }

    @JvmField
    val translationKey: String = RagiumAPI.id(name.lowercase()).toDescriptionKey("transfer_io")

    val description: MutableComponent get() = Component.translatable(translationKey)

    val nextEntry: HTTransferIO
        get() = when (this) {
            INPUT_ONLY -> OUTPUT_ONLY
            OUTPUT_ONLY -> BOTH
            BOTH -> NONE
            NONE -> INPUT_ONLY
        }

    override fun getSerializedName(): String = name.lowercase()

    //    Provider    //

    fun interface Provider : Function<Direction, HTTransferIO> {
        companion object {
            @JvmField
            val ALWAYS = Provider { BOTH }
        }

        operator fun get(direction: Direction): HTTransferIO = apply(direction)
    }

    //    Receiver    //

    fun interface Receiver : BiConsumer<Direction, HTTransferIO> {
        operator fun set(direction: Direction, transferIO: HTTransferIO) {
            accept(direction, transferIO)
        }
    }
}
