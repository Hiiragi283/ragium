package hiiragi283.ragium.api.storage.experience

import net.minecraft.core.Direction

/**
 * @see mekanism.api.energy.ISidedStrictEnergyHandler
 */
interface HTSidedExperienceHandler : IExperienceHandler {
    fun getExperienceSideFor(): Direction? = null

    fun getExperienceTanks(side: Direction?): Int

    @Deprecated("Use `getExperienceTanks(Direction?) instead`")
    override fun getExperienceTanks(): Int = getExperienceTanks(getExperienceSideFor())

    fun getExperienceAmount(tank: Int, side: Direction?): Long

    @Deprecated("Use `getExperienceAmount(Int, Direction?) instead`")
    override fun getExperienceAmount(tank: Int): Long = getExperienceAmount(tank, getExperienceSideFor())

    fun getExperienceCapacity(tank: Int, side: Direction?): Long

    @Deprecated("Use `getExperienceCapacity(Int, Direction?) instead`")
    override fun getExperienceCapacity(tank: Int): Long = getExperienceCapacity(tank, getExperienceSideFor())

    fun insertExperience(
        tank: Int,
        amount: Long,
        simulate: Boolean,
        side: Direction?,
    ): Long

    @Deprecated("Use `insertExperience(Int, Long, Boolean, Direction?) instead`")
    override fun insertExperience(tank: Int, amount: Long, simulate: Boolean): Long =
        insertExperience(tank, amount, simulate, getExperienceSideFor())

    fun extractExperience(
        tank: Int,
        amount: Long,
        simulate: Boolean,
        side: Direction?,
    ): Long

    @Deprecated("Use `extractExperience(Int, Long, Boolean, Direction?) instead`")
    override fun extractExperience(tank: Int, amount: Long, simulate: Boolean): Long =
        extractExperience(tank, amount, simulate, getExperienceSideFor())
}
