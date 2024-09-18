package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.util.blockSettings
import net.minecraft.block.Block

object HTCreativePowerSourceBlock : Block(
    blockSettings()
        .requiresTool()
        .strength(-1.0F, 3600000.0F)
        .dropsNothing(),
) {
    /*init {
        defaultState = stateManager.defaultState.with(HTMachineTier.PROPERTY, HTMachineTier.NONE)
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        val tier: HTMachineTier = state.get(HTMachineTier.PROPERTY)
        val stack: ItemStack = player.getStackInHand(player.activeHand)
        return if (!stack.contains(RagiumComponentTypes.DISABLE_CYCLE_POWER)) {
            world.setBlockState(
                pos,
                state.with(
                    HTMachineTier.PROPERTY,
                    when (player.isSneaking) {
                        true -> tier.back
                        false -> tier.next
                    },
                ),
            )
            ActionResult.success(world.isClient)
        } else {
            ActionResult.PASS
        }
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(HTMachineTier.PROPERTY)
    }*/

    //    RagiPowerProvider    //

    /*override fun getPower(world: World, pos: BlockPos, state: BlockState, direction: Direction?): HTMachineTier =
        state.get(HTMachineTier.PROPERTY)*/
}
