package hiiragi283.ragium.common.block.entity

import com.google.common.base.Predicates
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.util.HTPipeType
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumBlockProperties
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage
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
                getBackStorage(world, pos, ItemStorage.SIDED, direction),
                getFrontStorage(world, pos, ItemStorage.SIDED, direction),
                Predicates.alwaysTrue(),
                type.getItemCount(tier),
                null,
            )
            StorageUtil.move(
                getBackStorage(world, pos, FluidStorage.SIDED, direction),
                getFrontStorage(world, pos, FluidStorage.SIDED, direction),
                Predicates.alwaysTrue(),
                type.getFluidCount(tier),
                null,
            )
        }
    }
}
