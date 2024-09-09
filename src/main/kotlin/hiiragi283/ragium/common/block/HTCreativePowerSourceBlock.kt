package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.energy.HTRagiPower
import hiiragi283.ragium.common.energy.HTRagiPowerProvider
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.state.StateManager
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

object HTCreativePowerSourceBlock : Block(
    Settings.create().requiresTool().strength(-1.0F, 3600000.0F).dropsNothing()
), HTRagiPowerProvider {

    init {
        defaultState = stateManager.defaultState.with(HTRagiPower.PROPERTY, HTRagiPower.NONE)
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        val power: HTRagiPower = state.get(HTRagiPower.PROPERTY)
        val stack: ItemStack = player.getStackInHand(player.activeHand)
        return if (!stack.contains(RagiumComponentTypes.DISABLE_CYCLE_POWER)) {
            world.setBlockState(
                pos,
                state.with(
                    HTRagiPower.PROPERTY, when (player.isSneaking) {
                        true -> power.back
                        false -> power.next
                    }
                )
            )
            ActionResult.success(world.isClient)
        } else ActionResult.PASS
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(HTRagiPower.PROPERTY)
    }

    //    RagiPowerProvider    //

    override fun getPower(world: World, pos: BlockPos, state: BlockState, direction: Direction?): HTRagiPower =
        state.get(HTRagiPower.PROPERTY)

}
