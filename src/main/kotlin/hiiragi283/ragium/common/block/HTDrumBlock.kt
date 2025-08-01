package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.common.block.entity.HTDrumBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.HitResult

class HTDrumBlock<BE : HTDrumBlockEntity>(type: HTDeferredBlockEntityType<BE>, properties: Properties) :
    HTEntityBlock<BE>(type, properties) {
    companion object {
        @JvmStatic
        fun create(type: HTDeferredBlockEntityType<out HTDrumBlockEntity>): (Properties) -> HTDrumBlock<out HTDrumBlockEntity> =
            { prop: Properties -> HTDrumBlock(type, prop) }
    }

    override fun initDefaultState(): BlockState = stateDefinition.any()

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
