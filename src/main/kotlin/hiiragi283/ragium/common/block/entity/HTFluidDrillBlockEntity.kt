package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

class HTFluidDrillBlockEntity(pos: BlockPos, state: BlockState, tier: HTMachineTier = HTMachineTier.PRIMITIVE)
/* :
    HTProcessorBlockEntityBase(
        TODO(),
        pos,
        state,
        RagiumMachineTypes.FLUID_DRILL,
        tier,
    ),
    HTMultiblockController {
    override var showPreview: Boolean = false

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult = onUseController(state, world, pos, player)

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
    }

    override fun onValid(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
    ) {
        RagiumAdvancementCriteria.BUILT_MACHINE.trigger(player, machineType, tier)
    }

    override fun buildMultiblock(builder: HTMultiblockBuilder) {
        val cut: Block = Blocks.OXIDIZED_CUT_COPPER
        val slab: Block = Blocks.OXIDIZED_CUT_COPPER_SLAB
        builder.addLayer(-1..1, 0, 1..3, HTMultiblockComponent.block(tier.getHull().block))
        builder.add(-1, 1, 1, HTMultiblockComponent.block(cut))
        builder.add(-1, 1, 3, HTMultiblockComponent.block(cut))
        builder.add(1, 1, 1, HTMultiblockComponent.block(cut))
        builder.add(1, 1, 3, HTMultiblockComponent.block(cut))
        builder.addCross4(-1..1, 1, 1..3, HTMultiblockComponent.block(Blocks.OXIDIZED_CHISELED_COPPER))
        builder.add(-1, 2, 1, HTMultiblockComponent.block(slab))
        builder.add(-1, 2, 3, HTMultiblockComponent.block(slab))
        builder.add(1, 2, 1, HTMultiblockComponent.block(slab))
        builder.add(1, 2, 3, HTMultiblockComponent.block(slab))
        builder.addPillar(0, 2..3, 1, HTMultiblockComponent.block(Blocks.OXIDIZED_COPPER_GRATE))
        builder.addPillar(0, 2..3, 3, HTMultiblockComponent.block(Blocks.OXIDIZED_COPPER_GRATE))
        builder.addPillar(-1, 2..3, 2, HTMultiblockComponent.block(Blocks.OXIDIZED_COPPER_GRATE))
        builder.addPillar(1, 2..3, 2, HTMultiblockComponent.block(Blocks.OXIDIZED_COPPER_GRATE))
        builder.addPillar(0, 4..5, 2, HTMultiblockComponent.block(Blocks.OXIDIZED_COPPER_GRATE))
    }
}*/
