package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockComponent
import hiiragi283.ragium.common.block.entity.HTMultiblockController
import hiiragi283.ragium.common.init.RagiumAdvancementCriteria
import hiiragi283.ragium.common.init.RagiumMachineTypes
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTSawMillMachineEntity(tier: HTMachineTier) :
    HTProcessorMachineEntity(RagiumMachineTypes.SAW_MILL, tier),
    HTMultiblockController {
    override var showPreview: Boolean = false

    override fun onSucceeded(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
    ) {
        super.onSucceeded(state, world, pos, player)
        RagiumAdvancementCriteria.BUILT_MACHINE.trigger(player, machineType, tier)
    }

    override fun buildMultiblock(builder: HTMultiblockBuilder) {
        builder.add(-1, 0, 0, HTMultiblockComponent.block(tier.getHull().block))
        builder.add(1, 0, 0, HTMultiblockComponent.block(tier.getHull().block))
        builder.add(-1, 0, 1, HTMultiblockComponent.block(Blocks.STONE_SLAB))
        builder.add(0, 0, 1, HTMultiblockComponent.block(Blocks.STONECUTTER))
        builder.add(1, 0, 1, HTMultiblockComponent.block(Blocks.STONE_SLAB))
        builder.addLayer(-1..1, 0, 2..2, HTMultiblockComponent.block(tier.getHull().block))
    }
}
