package hiiragi283.ragium.api.event

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.world.World

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

    fun inventoryTick(
        stack: ItemStack,
        world: World,
        entity: Entity,
        slot: Int,
        selected: Boolean,
    )
}
