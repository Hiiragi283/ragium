package hiiragi283.ragium.client.integration.jade.provider

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.storage.HTAmountView
import hiiragi283.ragium.api.storage.experience.IExperienceStorage
import io.netty.buffer.ByteBuf

class HTExperienceView(private val amount: Long, private val capacity: Long) : HTAmountView.LongSized {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTExperienceView> = BiCodec.composite(
            BiCodecs.NON_NEGATIVE_LONG.fieldOf(RagiumConst.AMOUNT),
            HTExperienceView::getAmount,
            BiCodecs.POSITIVE_LONG.fieldOf(RagiumConst.CAPACITY),
            HTExperienceView::getCapacity,
            ::HTExperienceView,
        )
    }
    constructor(storage: IExperienceStorage) : this(storage.getExpStored(), storage.getMaxExpStored())

    override fun getAmount(): Long = amount

    override fun getCapacity(): Long = capacity
}
