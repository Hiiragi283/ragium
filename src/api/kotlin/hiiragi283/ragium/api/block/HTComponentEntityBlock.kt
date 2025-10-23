package hiiragi283.ragium.api.block

import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.HitResult

abstract class HTComponentEntityBlock(type: HTDeferredBlockEntityType<*>, properties: Properties) : HTEntityBlock(type, properties) {
    override fun getCloneItemStack(
        state: BlockState,
        target: HitResult,
        level: LevelReader,
        pos: BlockPos,
        player: Player,
    ): ItemStack {
        val stack: ItemStack = super.getCloneItemStack(state, target, level, pos, player)
        level.getBlockEntity(pos)?.collectComponents()?.let(stack::applyComponents)
        return stack
    }
}
