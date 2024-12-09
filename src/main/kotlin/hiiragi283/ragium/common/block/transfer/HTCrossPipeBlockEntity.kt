package hiiragi283.ragium.common.block.transfer

import com.google.common.base.Predicates
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.util.HTPipeType
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumBlockProperties
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTCrossPipeBlockEntity(pos: BlockPos, state: BlockState) :
    HTTransporterBlockEntityBase(RagiumBlockEntityTypes.CROSS_PIPE, pos, state) {
    init {
        this.tier = HTMachineTier.ADVANCED
    }

    constructor(pos: BlockPos, state: BlockState, type: HTPipeType) : this(pos, state) {
        this.type = type
    }

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        if (world.isClient) return
        // transfer containment
        state.get(RagiumBlockProperties.CROSS_DIRECTION).directions.forEach { direction: Direction ->
            StorageUtil.move(
                getBackItemStorage(world, pos, direction),
                getFrontItemStorage(world, pos, direction),
                Predicates.alwaysTrue(),
                type.getItemCount(tier),
                null,
            )
            StorageUtil.move(
                getBackFluidStorage(world, pos, direction),
                getFrontFluidStorage(world, pos, direction),
                Predicates.alwaysTrue(),
                type.getFluidCount(tier),
                null,
            )
        }
    }
}
