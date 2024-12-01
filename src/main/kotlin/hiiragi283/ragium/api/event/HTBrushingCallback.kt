package hiiragi283.ragium.api.event

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.world.World

fun interface HTBrushingCallback {
    companion object {
        @JvmField
        val EVENT: Event<HTBrushingCallback> =
            EventFactory.createArrayBacked(HTBrushingCallback::class.java) { listeners: Array<HTBrushingCallback> ->
                HTBrushingCallback { world: World, player: PlayerEntity, stack: ItemStack, hitResult: BlockHitResult ->
                    for (callback: HTBrushingCallback in listeners) {
                        if (callback.onBrush(world, player, stack, hitResult)) {
                            return@HTBrushingCallback true
                        }
                    }
                    false
                }
            }
    }

    fun onBrush(
        world: World,
        player: PlayerEntity,
        stack: ItemStack,
        hitResult: BlockHitResult,
    ): Boolean
}
