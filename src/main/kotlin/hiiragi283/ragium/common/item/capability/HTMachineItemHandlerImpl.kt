package hiiragi283.ragium.common.item.capability

import hiiragi283.ragium.api.item.HTMachineItemHandler
import net.neoforged.neoforge.items.ItemStackHandler

/**
 * Ragiumで使用する[ItemStackHandler]の拡張クラス
 * @param callback [ItemStackHandler.onContentsChanged]で呼び出されるブロック
 */
open class HTMachineItemHandlerImpl(size: Int, val callback: () -> Unit) :
    ItemStackHandler(size),
    HTMachineItemHandler {
    override fun onContentsChanged(slot: Int) {
        callback()
    }
}
