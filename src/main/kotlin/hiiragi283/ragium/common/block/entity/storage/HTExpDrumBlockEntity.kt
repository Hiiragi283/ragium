package hiiragi283.ragium.common.block.entity.storage

import com.google.common.primitives.Ints
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.experience.HTExperienceTank
import hiiragi283.ragium.api.storage.holder.HTExperienceTankHolder
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.common.storage.fluid.tank.HTFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTBasicExperienceTankHolder
import hiiragi283.ragium.common.util.HTExperienceHelper
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState

class HTExpDrumBlockEntity(pos: BlockPos, state: BlockState) : HTDrumBlockEntity(RagiumBlocks.EXP_DRUM, pos, state) {
    override fun createTank(listener: HTContentListener): HTFluidStackTank = HTFluidStackTank.create(listener, Int.MAX_VALUE)

    lateinit var expTank: HTExperienceTank.Basic
        private set

    override fun initializeExperienceHandler(listener: HTContentListener): HTExperienceTankHolder {
        val builder: HTBasicExperienceTankHolder.Builder = HTBasicExperienceTankHolder.builder(this)
        expTank = builder.addSlot(HTAccessConfig.BOTH, FluidExperienceTank(tank))
        return builder.build()
    }

    //    Ticking    //

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        var result: Boolean = super.onUpdateServer(level, pos, state)
        if (!result && HTExperienceHelper.moveExperience(slot, slot::setStackUnchecked, expTank)) {
            result = true
        }
        return result
    }

    //    FluidExperienceTank    //

    private class FluidExperienceTank(private val tank: HTFluidStackTank) :
        HTExperienceTank.Basic(),
        HTValueSerializable.Empty {
        override fun setAmount(amount: Long) {
            val fluidAmount: Int = Ints.saturatedCast(amount * RagiumConfig.COMMON.expConversionRatio.asLong)
            tank.setStackUnchecked(RagiumFluidContents.EXPERIENCE.toStorageStack(fluidAmount))
        }

        override fun getAmount(): Long = tank.getAmount() / RagiumConfig.COMMON.expConversionRatio.asLong

        override fun getCapacity(): Long = tank.getCapacity() / RagiumConfig.COMMON.expConversionRatio.asLong

        override fun onContentsChanged() {
            tank.onContentsChanged()
        }
    }
}
