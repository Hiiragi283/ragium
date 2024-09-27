package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.common.block.entity.HTMultiblockController
import hiiragi283.ragium.common.init.RagiumAdvancementCriteria
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineTypes
import hiiragi283.ragium.common.machine.HTBlockPredicate
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.machine.HTMultiblockBuilder
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTBlastFurnaceBlockEntity(pos: BlockPos, state: BlockState, tier: HTMachineTier = HTMachineTier.PRIMITIVE) :
    HTMachineBlockEntityBase(RagiumBlockEntityTypes.BLAST_FURNACE, pos, state, RagiumMachineTypes.BLAST_FURNACE, tier),
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
        RagiumAdvancementCriteria.BUILT_MACHINE.trigger(player, machineType, tier)
    }

    override fun buildMultiblock(builder: HTMultiblockBuilder): HTMultiblockBuilder = builder
        .addLayer(
            -1..1,
            0,
            1..3,
            HTBlockPredicate.block(tier.getHull().block),
        ).addHollow(
            -1..1,
            1,
            1..3,
            HTBlockPredicate.block(tier.getCoil().block),
        ).addHollow(
            -1..1,
            2,
            1..3,
            HTBlockPredicate.block(tier.getCoil().block),
        ).addHollow(
            -1..1,
            3,
            1..3,
            HTBlockPredicate.block(tier.baseBlock),
        )
}