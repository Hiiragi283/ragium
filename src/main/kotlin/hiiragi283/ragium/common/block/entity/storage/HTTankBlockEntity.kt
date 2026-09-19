package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.lib.transfer.fluid.HTBasicFluidTank
import hiiragi283.lib.transfer.fluid.HTFluidTank
import hiiragi283.lib.transfer.holder.HTResourceSlotHolder
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.api.util.HTStorageHelper
import hiiragi283.ragium.common.block.entity.HTConfigurableBlockEntity
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.transfer.fluid.HTVariableFluidTank
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentMap
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.fluids.SimpleFluidContent

class HTTankBlockEntity(pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(RagiumBlockEntityTypes.TANK.get(), pos, state) {
    lateinit var tank: HTBasicFluidTank
        private set

    override fun createFluidHandler(listener: Runnable): HTResourceSlotHolder<HTFluidTank> {
        tank = HTVariableFluidTank.create(RagiumConfig.SERVER.tankCapacity, listener)
        return object : HTResourceSlotHolder<HTFluidTank> {
            override fun getSlots(side: Direction?): List<HTFluidTank> = listOf(tank)

            override fun canInsert(side: Direction?): Boolean = true

            override fun canExtract(side: Direction?): Boolean = true
        }
    }

    override fun markDirtyComparator() {
        level?.updateNeighbourForOutputSignal(blockPos, blockState.block)
    }

    override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int =
        HTStorageHelper.calculateRedstoneLevel(tank)

    //    Sync    //

    override fun applyImplicitComponents(components: DataComponentGetter) {
        super.applyImplicitComponents(components)
        components.get(RagiumDataComponents.FLUID)?.copy()?.let(tank::setStack)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        val content: SimpleFluidContent = tank.getStackCopy().let(SimpleFluidContent::copyOf)
        if (!content.isEmpty) {
            components.set(RagiumDataComponents.FLUID, content)
        }
    }

    override fun writeReducedUpdateTag(output: ValueOutput) {
        super.writeReducedUpdateTag(output)
        tank.serialize(output)
    }

    override fun readUpdateTag(input: ValueInput) {
        tank.deserialize(input)
    }

    //    Ticking    //

    private var oldScale: Float = 0f

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 保持する量の変化があれば更新させる
        val scale: Float = tank.currentFilledLevel
        if (scale != this.oldScale) {
            this.oldScale = scale
            return true
        }
        return false
    }
}
