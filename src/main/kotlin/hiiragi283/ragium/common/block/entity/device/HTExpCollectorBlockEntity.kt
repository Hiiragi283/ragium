package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.block.entity.HTTickAwareBlockEntity
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.HTFluidTankHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidTankHelper
import hiiragi283.ragium.api.storage.fluid.HTFluidVariant
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.structure.BoundingBox
import net.minecraft.world.phys.AABB
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.fluids.FluidStack

class HTExpCollectorBlockEntity(pos: BlockPos, state: BlockState) :
    HTTickAwareBlockEntity(RagiumBlockEntityTypes.EXP_COLLECTOR, pos, state),
    HTFluidTankHandler {
    private val outputTank: HTFluidTank = HTFluidTank.create(RagiumConstantValues.OUTPUT_TANK, this) {
        validator = { variant: HTFluidVariant -> variant.isOf(RagiumFluidContents.EXPERIENCE.get()) }
        capacity = Int.MAX_VALUE
    }

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        outputTank.writeNbt(writer)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        outputTank.readNbt(reader)
    }

    //    Ticking    //

    override fun onServerTick(level: ServerLevel, pos: BlockPos, state: BlockState): TriState {
        // 20 tickごとに実行する
        if (!canProcess()) return TriState.DEFAULT
        // 範囲内のExp Orbを取得する
        val range = 5
        val expOrbs: List<ExperienceOrb> = level.getEntitiesOfClass(
            ExperienceOrb::class.java,
            AABB.of(
                BoundingBox(
                    blockPos.x - range,
                    blockPos.y - range,
                    blockPos.z - range,
                    blockPos.x + range,
                    blockPos.y + range,
                    blockPos.z + range,
                ),
            ),
        )
        if (expOrbs.isEmpty()) return TriState.DEFAULT
        // それぞれのExp Orbに対して回収を行う
        for (entity: ExperienceOrb in expOrbs) {
            val fluidAmount: Int = entity.value * 20
            val stack: FluidStack = RagiumFluidContents.EXPERIENCE.toStack(fluidAmount)
            if (HTFluidTankHelper.canInsertFluid(outputTank, stack)) {
                HTFluidTankHelper.insertFluid(outputTank, stack, false)
                entity.discard()
            }
        }
        return TriState.TRUE
    }

    override val maxTicks: Int = 20

    //    Fluid    //

    override fun getFluidIoFromSlot(tank: Int): HTStorageIO = HTStorageIO.OUTPUT

    override fun getFluidTank(tank: Int): HTFluidTank? = outputTank

    override fun getTanks(): Int = 1
}
