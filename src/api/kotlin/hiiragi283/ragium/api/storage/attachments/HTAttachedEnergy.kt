package hiiragi283.ragium.api.storage.attachments

import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import io.netty.buffer.ByteBuf

/**
 * @see mekanism.common.attachments.containers.energy.AttachedEnergy
 */
@JvmRecord
data class HTAttachedEnergy(override val containers: List<Int>) : HTAttachedContainers<Int, HTAttachedEnergy> {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTAttachedEnergy> = BiCodecs.NON_NEGATIVE_INT
            .listOf()
            .xmap(::HTAttachedEnergy, HTAttachedEnergy::containers)

        @JvmField
        val EMPTY = HTAttachedEnergy(listOf())

        @JvmStatic
        fun create(size: Int): HTAttachedEnergy = HTAttachedEnergy(List(size) { 0 })
    }

    override fun create(containers: List<Int>): HTAttachedEnergy = HTAttachedEnergy(containers)
}
