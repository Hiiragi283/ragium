package hiiragi283.ragium.common.block.entity.storage

import com.google.common.primitives.Ints
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.experience.HTExperienceStorage
import hiiragi283.ragium.api.storage.experience.IExperienceStorage
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.fluid.tank.HTFluidStackTank
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState

class HTExpDrumBlockEntity(pos: BlockPos, state: BlockState) : HTDrumBlockEntity(RagiumBlocks.EXP_DRUM, pos, state) {
    override fun createTank(listener: HTContentListener): HTFluidStackTank = HTFluidStackTank.create(listener, Int.MAX_VALUE)

    private val storage = FluidExpStorage()

    override fun getExperienceStorage(direction: Direction?): IExperienceStorage = storage

    //    Save & Read    //

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        storage.serialize(output)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        storage.deserialize(input)
    }

    //    FluidExpStorage    //

    private inner class FluidExpStorage :
        HTExperienceStorage.Basic(),
        HTContentListener.Empty,
        HTValueSerializable.Empty {
        override fun setAmount(amount: Long) {
            val fluidAmount: Int = Ints.saturatedCast(amount * RagiumConfig.COMMON.expConversionRatio.asLong)
            tank.setStackUnchecked(RagiumFluidContents.EXPERIENCE.toStorageStack(fluidAmount))
        }

        override fun getAmount(): Long = tank.getAmount() / RagiumConfig.COMMON.expConversionRatio.asLong

        override fun getCapacity(): Long = tank.getCapacity() / RagiumConfig.COMMON.expConversionRatio.asLong
    }
}
