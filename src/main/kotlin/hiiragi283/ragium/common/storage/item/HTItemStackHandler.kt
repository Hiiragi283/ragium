package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.core.Direction
import net.minecraft.network.RegistryFriendlyByteBuf

open class HTItemStackHandler(protected var slots: List<HTItemSlot>, private val listener: HTContentListener?) : HTItemHandler {
    companion object {
        @JvmStatic
        fun <HANDLER : HTItemHandler> codec(handlerFactory: (List<HTItemSlot>) -> HANDLER): BiCodec<RegistryFriendlyByteBuf, HANDLER> =
            HTItemStackSlot.SIMPLE_CODEC.listOf().xmap(handlerFactory) { handler: HANDLER ->
                handler.getItemSlots(handler.getItemSideFor())
            }
    }

    override fun getItemSlots(side: Direction?): List<HTItemSlot> = slots

    override fun onContentsChanged() {
        listener?.onContentsChanged()
    }
}
