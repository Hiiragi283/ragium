package hiiragi283.ragium.api.event

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

fun interface HTModifyBlockDropsCallback {
    companion object {
        @JvmField
        val EVENT: Event<HTModifyBlockDropsCallback> =
            EventFactory.createArrayBacked(HTModifyBlockDropsCallback::class.java) { listeners: Array<HTModifyBlockDropsCallback> ->
                HTModifyBlockDropsCallback {
                        state: BlockState,
                        world: ServerWorld,
                        pos: BlockPos,
                        blockEntity: BlockEntity?,
                        breaker: Entity?,
                        tool: ItemStack,
                        drops: List<ItemStack>,
                    ->
                    var newDrops: List<ItemStack> = drops
                    for (listener: HTModifyBlockDropsCallback in listeners) {
                        newDrops = listener.modify(state, world, pos, blockEntity, breaker, tool, drops)
                        if (newDrops != drops) break
                    }
                    newDrops
                }
            }
    }

    fun modify(
        state: BlockState,
        world: ServerWorld,
        pos: BlockPos,
        blockEntity: BlockEntity?,
        breaker: Entity?,
        tool: ItemStack,
        drops: List<ItemStack>,
    ): List<ItemStack>
}
