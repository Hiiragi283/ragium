package hiiragi283.ragium.common.integration.mekanism.storage

import hiiragi283.ragium.api.storage.HTStorageAccess
import mekanism.api.Action
import mekanism.api.chemical.ChemicalStack
import mekanism.api.chemical.ISidedChemicalHandler
import net.minecraft.core.Direction

/**
 * [HTChemicalTank]に基づいた[ISidedChemicalHandler]の拡張インターフェース
 * @see [mekanism.api.chemical.IMekanismChemicalHandler]
 */
fun interface HTChemicalHandler : ISidedChemicalHandler {
    fun hasChemicalHandler(): Boolean = true

    fun getChemicalTanks(side: Direction?): List<HTChemicalTank>

    fun getChemicalTank(tank: Int, side: Direction?): HTChemicalTank? = getChemicalTanks(side).getOrNull(tank)

    override fun getCountChemicalTanks(side: Direction?): Int = getChemicalTanks(side).size

    override fun getChemicalInTank(tank: Int, side: Direction?): ChemicalStack =
        getChemicalTank(tank, side)?.getChemicalStack() ?: ChemicalStack.EMPTY

    override fun setChemicalInTank(tank: Int, stack: ChemicalStack, side: Direction?) {
        (getChemicalTank(tank, side) as? HTChemicalTank.Mutable)?.setChemicalStack(stack)
    }

    override fun getChemicalTankCapacity(tank: Int, side: Direction?): Long =
        getChemicalTank(tank, side)?.getCapacityAsLong(ImmutableChemicalStack.EMPTY) ?: 0

    override fun isValid(tank: Int, stack: ChemicalStack, side: Direction?): Boolean = getChemicalTank(tank, side)?.isValid(stack) ?: false

    override fun insertChemical(
        tank: Int,
        stack: ChemicalStack,
        side: Direction?,
        action: Action,
    ): ChemicalStack =
        getChemicalTank(tank, side)?.insertChemical(stack, MekanismCapabilities.convert(action), HTStorageAccess.INTERNAL) ?: stack

    override fun extractChemical(
        tank: Int,
        amount: Long,
        side: Direction?,
        action: Action,
    ): ChemicalStack =
        getChemicalTank(tank, side)?.extractChemical(amount.toInt(), MekanismCapabilities.convert(action), HTStorageAccess.INTERNAL)
            ?: ChemicalStack.EMPTY
}
