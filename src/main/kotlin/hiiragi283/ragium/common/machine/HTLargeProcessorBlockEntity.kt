package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.extension.getMachineEntity
import hiiragi283.ragium.api.extension.sendPacket
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePacket
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.machine.block.HTProcessorBlockEntityBase
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockComponent
import hiiragi283.ragium.api.tags.RagiumBlockTags
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTLargeProcessorBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntityBase.Large(RagiumBlockEntityTypes.LARGE_PROCESSOR, pos, state) {
    override var key: HTMachineKey = RagiumMachineKeys.ALLOY_FURNACE

    //    HTMultiblockController    //

    override fun buildMultiblock(builder: HTMultiblockBuilder) {
        builder.addLayer(-1..1, -1, 1..3, HTMultiblockComponent.of(tier.getBaseBlock()))
        builder.addHollow(-1..1, 0, 1..3, HTMultiblockComponent.of(tier.getHull()))
        builder.addLayer(-1..1, 1, 1..3, HTMultiblockComponent.of(tier.getStorageBlock()))
        builder.add(0, 0, 2, HTMultiblockComponent.of(RagiumBlockTags.MACHINES))
    }

    override fun beforeValidation(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity?,
    ) {
        super.beforeValidation(state, world, pos, player)
        val parent: HTMachineBlockEntityBase = world.getMachineEntity(pos.offset(facing.opposite, 2)) ?: return
        key = parent.key
        tier = parent.tier
        player?.sendPacket(HTMachinePacket(key, tier, pos))
    }
}
