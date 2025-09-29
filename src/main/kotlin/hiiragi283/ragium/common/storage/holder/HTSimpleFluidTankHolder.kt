package hiiragi283.ragium.common.storage.holder

import hiiragi283.ragium.api.storage.HTAccessConfiguration
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import net.minecraft.core.Direction

class HTSimpleFluidTankHolder(
    transferProvider: HTAccessConfiguration.Holder?,
    private val inputTank: HTFluidTank?,
    private val outputTank: HTFluidTank?,
    private val generic: HTFluidTank?,
) : HTSimpleCapabilityHolder(transferProvider),
    HTFluidTankHolder {
    companion object {
        @JvmStatic
        fun input(transferProvider: HTAccessConfiguration.Holder?, tank: HTFluidTank): HTSimpleFluidTankHolder =
            HTSimpleFluidTankHolder(transferProvider, tank, null, null)

        @JvmStatic
        fun output(transferProvider: HTAccessConfiguration.Holder?, tank: HTFluidTank): HTSimpleFluidTankHolder =
            HTSimpleFluidTankHolder(transferProvider, null, tank, null)

        @JvmStatic
        fun generic(transferProvider: HTAccessConfiguration.Holder?, tank: HTFluidTank): HTSimpleFluidTankHolder =
            HTSimpleFluidTankHolder(transferProvider, null, null, tank)
    }

    override fun getFluidTank(side: Direction?): List<HTFluidTank> = when {
        side == null -> listOfNotNull(inputTank, outputTank, generic)
        canInsert(side) -> listOfNotNull(inputTank, generic)
        canExtract(side) -> listOfNotNull(outputTank, generic)
        else -> listOf()
    }
}
