package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.block.entity.HTSingleMachineBlockEntity
import hiiragi283.ragium.common.recipe.HTMachineType
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTSingleMachineBlock(private val machineType: HTMachineType) : HTBlockWithEntity(Settings.create()) {

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult = when (world.isClient) {
        true -> ActionResult.SUCCESS
        false -> {
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos))
            ActionResult.CONSUME
        }
    }

    override fun getTranslationKey(): String = machineType.translationKey

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity =
        HTSingleMachineBlockEntity(pos, state, machineType)

    override fun <T : BlockEntity> getTicker(
        world: World,
        state: BlockState,
        type: BlockEntityType<T>,
    ): BlockEntityTicker<T>? = HTBlockEntityTicker.validateTicker(
        type,
        HTMachineType.Variant.SINGLE.blockEntityType,
        HTSingleMachineBlockEntity.TICKER
    )
}