package hiiragi283.ragium.api.storage.experience

import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import net.minecraft.core.Direction

/**
 * @see mekanism.api.energy.IMekanismStrictEnergyHandler
 */
fun interface HTExperienceHandler : HTSidedExperienceHandler {
    fun hasExperienceHandler(): Boolean = true

    fun getExpTanks(side: Direction?): List<HTExperienceTank>

    fun getExpTank(tank: Int, side: Direction?): HTExperienceTank? = getExpTanks(side).getOrNull(tank)

    override fun getExperienceTanks(side: Direction?): Int = getExpTanks(side).size

    override fun getExperienceAmount(tank: Int, side: Direction?): Long = getExpTank(tank, side)?.getAmount() ?: 0

    override fun getExperienceCapacity(tank: Int, side: Direction?): Long = getExpTank(tank, side)?.getCapacity() ?: 0

    override fun insertExperience(
        tank: Int,
        amount: Long,
        simulate: Boolean,
        side: Direction?,
    ): Long = getExpTank(tank, side)?.insert(amount, HTStorageAction.of(simulate), HTStorageAccess.EXTERNAL) ?: 0

    override fun extractExperience(
        tank: Int,
        amount: Long,
        simulate: Boolean,
        side: Direction?,
    ): Long = getExpTank(tank, side)?.extract(amount, HTStorageAction.of(simulate), HTStorageAccess.EXTERNAL) ?: 0
}
