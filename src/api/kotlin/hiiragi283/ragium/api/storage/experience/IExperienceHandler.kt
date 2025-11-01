package hiiragi283.ragium.api.storage.experience

/**
 * @see mekanism.api.energy.IStrictEnergyHandler
 */
interface IExperienceHandler {
    fun getExperienceTanks(): Int

    fun getExperienceAmount(tank: Int): Long

    fun getExperienceCapacity(tank: Int): Long

    fun insertExperience(tank: Int, amount: Long, simulate: Boolean): Long

    fun extractExperience(tank: Int, amount: Long, simulate: Boolean): Long
}
