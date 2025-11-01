package hiiragi283.ragium.api.storage.experience

/**
 * @see net.neoforged.neoforge.energy.IEnergyStorage
 */
interface IExperienceStorage {
    fun insertExp(amount: Long, simulate: Boolean): Long

    fun extractExp(amount: Long, simulate: Boolean): Long

    fun getExpStored(): Long

    fun getMaxExpStored(): Long
}
