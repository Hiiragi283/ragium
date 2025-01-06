package hiiragi283.ragium.api.event

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.world.World

/**
 * プレイヤーのインベントリで毎tick呼ばれるイベント
 *
 * [net.minecraft.item.Item.inventoryTick]の最後にフックされています。
 */
fun interface HTInventoryTickCallback {
    companion object {
        @JvmField
        val EVENT: Event<HTInventoryTickCallback> =
            EventFactory.createArrayBacked(HTInventoryTickCallback::class.java) { callbacks: Array<out HTInventoryTickCallback> ->
                HTInventoryTickCallback { stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean ->
                    repeat(callbacks.size) { i: Int ->
                        callbacks[i].inventoryTick(stack, world, entity, slot, selected)
                    }
                }
            }
    }

    /**
     * @param stack 対象となる[ItemStack]
     * @param world [entity]がいるワールド
     * @param entity インベントリを保有しているエンティティ
     * @param slot [stack]が入っているスロットのインデックス
     * @param selected プレイヤーが[slot]を選択中かどうか
     */
    fun inventoryTick(
        stack: ItemStack,
        world: World,
        entity: Entity,
        slot: Int,
        selected: Boolean,
    )
}
