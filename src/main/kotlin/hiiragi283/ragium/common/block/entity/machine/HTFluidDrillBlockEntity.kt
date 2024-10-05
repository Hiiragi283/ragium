package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockComponent
import hiiragi283.ragium.common.block.entity.HTMultiblockController
import hiiragi283.ragium.common.init.RagiumAdvancementCriteria
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineTypes
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTFluidDrillBlockEntity(pos: BlockPos, state: BlockState, tier: HTMachineTier = HTMachineTier.PRIMITIVE) :
    HTProcessorBlockEntityBase(
        RagiumBlockEntityTypes.FLUID_DRILL,
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
            HTMultiblockComponent.block(tier.getHull().block),
        ).add(
            -1,
            1,
            1,
            HTMultiblockComponent.block(Blocks.OXIDIZED_CUT_COPPER),
        ).add(
            -1,
            1,
            3,
            HTMultiblockComponent.block(Blocks.OXIDIZED_CUT_COPPER),
        ).add(
            1,
            1,
            1,
            HTMultiblockComponent.block(Blocks.OXIDIZED_CUT_COPPER),
        ).add(
            1,
            1,
            3,
            HTMultiblockComponent.block(Blocks.OXIDIZED_CUT_COPPER),
        ).addCross4(-1..1, 1, 1..3, HTMultiblockComponent.block(Blocks.OXIDIZED_CHISELED_COPPER))
        .add(
            -1,
            2,
            1,
            HTMultiblockComponent.block(Blocks.OXIDIZED_CUT_COPPER_SLAB),
        ).add(
            -1,
            2,
            3,
            HTMultiblockComponent.block(Blocks.OXIDIZED_CUT_COPPER_SLAB),
        ).add(
            1,
            2,
            1,
            HTMultiblockComponent.block(Blocks.OXIDIZED_CUT_COPPER_SLAB),
        ).add(
            1,
            2,
            3,
            HTMultiblockComponent.block(Blocks.OXIDIZED_CUT_COPPER_SLAB),
        ).addPillar(0, 2..3, 1, HTMultiblockComponent.block(Blocks.OXIDIZED_COPPER_GRATE))
        .addPillar(0, 2..3, 3, HTMultiblockComponent.block(Blocks.OXIDIZED_COPPER_GRATE))
        .addPillar(-1, 2..3, 2, HTMultiblockComponent.block(Blocks.OXIDIZED_COPPER_GRATE))
        .addPillar(1, 2..3, 2, HTMultiblockComponent.block(Blocks.OXIDIZED_COPPER_GRATE))
        .addPillar(0, 4..5, 2, HTMultiblockComponent.block(Blocks.OXIDIZED_COPPER_GRATE))
}
