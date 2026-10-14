package hiiragi283.lib.recipe.handler

import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.item.HTItemSlot
import hiiragi283.lib.transfer.item.toResourcePair
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
data object HTInputHandlerHelper {
    @JvmStatic
    fun forItems(slots: List<HTItemSlot>): HTInputHandler<Item, ItemInstance> = MultiItems(slots)

    /**
     * 複数の[HTItemSlot]を[アイテム][ItemInstance]向けの[HTInputHandler]に変換します。
     * @since 26.1.11
     */
    @JvmRecord
    private data class MultiItems(private val slots: List<HTItemSlot>) : HTInputHandler<Item, ItemInstance> {
        override fun use(input: ItemInstance?, transaction: TransactionContext): HTInputHandler.UseResult {
            if (input == null || HTIngredientHelper.isEmpty(input)) return HTInputHandler.UseResult.EMPTY
            val (resource: ItemResource, amount: Int) = HTIngredientHelper.unwrap(input).toResourcePair()
            var extracted = 0
            for (slot: HTItemSlot in slots) {
                extracted += slot.extract(resource, amount - extracted, transaction, HTTransferAccess.INTERNAL)
                if (extracted == amount) break
            }
            return when (extracted) {
                amount -> HTInputHandler.UseResult.CONSUMED
                else -> HTInputHandler.UseResult.NOT_CONSUMED
            }
        }
    }
}
