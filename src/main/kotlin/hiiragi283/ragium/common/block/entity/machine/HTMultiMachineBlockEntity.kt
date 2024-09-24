package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.common.block.entity.HTMultiblockController
import hiiragi283.ragium.common.init.RagiumAdvancementCriteria
import hiiragi283.ragium.common.machine.HTMachineType
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class HTMultiMachineBlockEntity(private val machineType: HTMachineType.Multi, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(machineType, pos, state),
    HTMultiblockController {
    override var showPreview: Boolean = false

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult = onUseController(state, world, pos, player)

    override fun onValid(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
    ) {
        RagiumAdvancementCriteria.BUILD_MULTIBLOCK.trigger(player, machineType)
    }

    override fun getDisplayName(): Text = machineType.text
}
