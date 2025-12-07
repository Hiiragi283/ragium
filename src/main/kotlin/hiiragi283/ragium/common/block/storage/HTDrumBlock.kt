package hiiragi283.ragium.common.block.storage

import hiiragi283.ragium.api.block.type.HTEntityBlockType
import hiiragi283.ragium.common.block.HTTypedEntityBlock
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.HitResult

class HTDrumBlock(type: HTEntityBlockType, properties: Properties) : HTTypedEntityBlock<HTEntityBlockType>(type, properties) {
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
