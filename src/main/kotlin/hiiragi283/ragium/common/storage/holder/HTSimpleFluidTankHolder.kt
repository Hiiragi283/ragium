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

    override fun getFluidTank(side: Direction?): List<HTFluidTank> {
        val allTanks: List<HTFluidTank> = listOfNotNull(inputTank, outputTank, generic)
        when (side) {
            null -> return allTanks
            else -> {
                val canInsert: Boolean = canInsert(side)
                val canExtract: Boolean = canExtract(side)
                return when {
                    canInsert && canExtract -> allTanks
                    else -> buildList {
                        if (canInsert) {
                            add(inputTank)
                        }
                        if (canExtract) {
                            add(outputTank)
                        }
                        add(generic)
                    }.filterNotNull()
                }
            }
        }
    }
}
