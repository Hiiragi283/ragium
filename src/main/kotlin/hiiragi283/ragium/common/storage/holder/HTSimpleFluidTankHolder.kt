package hiiragi283.ragium.common.storage.holder

import hiiragi283.ragium.api.storage.HTTransferIO
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import net.minecraft.core.Direction

class HTSimpleFluidTankHolder(
    private val transferProvider: HTTransferIO.Provider?,
    private val inputTanks: List<HTFluidTank>,
    private val outputTanks: List<HTFluidTank>,
    private val catalyst: HTFluidTank? = null,
) : HTFluidTankHolder {
    override fun getFluidTank(side: Direction?): List<HTFluidTank> = when {
        side == null -> buildList {
            addAll(inputTanks)
            addAll(outputTanks)
            catalyst?.let(::add)
        }

        canInsert(side) -> inputTanks
        canExtract(side) -> outputTanks
        else -> listOf()
    }

    override fun canInsert(side: Direction?): Boolean = when (side) {
        null -> false
        else -> transferProvider?.apply(side)?.canInsert ?: true
    }

    override fun canExtract(side: Direction?): Boolean = when (side) {
        null -> false
        else -> transferProvider?.apply(side)?.canExtract ?: true
    }
}
