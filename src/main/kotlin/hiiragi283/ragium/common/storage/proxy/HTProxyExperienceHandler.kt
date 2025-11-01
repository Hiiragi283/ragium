package hiiragi283.ragium.common.storage.proxy

import hiiragi283.ragium.api.storage.experience.HTSidedExperienceHandler
import hiiragi283.ragium.api.storage.experience.IExperienceHandler
import hiiragi283.ragium.api.storage.holder.HTCapabilityHolder
import net.minecraft.core.Direction

/**
 * [IExperienceHandler]向けの[HTProxyHandler]の実装クラス
 * @param handler ラップ対象の[HTSidedExperienceHandler]
 * @param side 現在の向き
 * @param holder 搬入出の制御
 */
class HTProxyExperienceHandler(private val handler: HTSidedExperienceHandler, side: Direction?, holder: HTCapabilityHolder?) :
    HTProxyHandler(side, holder),
    IExperienceHandler {
    override fun getExperienceTanks(): Int = handler.getExperienceTanks(side)

    override fun getExperienceAmount(tank: Int): Long = handler.getExperienceAmount(tank, side)

    override fun getExperienceCapacity(tank: Int): Long = handler.getExperienceCapacity(tank, side)

    override fun insertExperience(tank: Int, amount: Long, simulate: Boolean): Long = when (readOnlyInsert) {
        true -> 0
        false -> handler.insertExperience(tank, amount, simulate, side)
    }

    override fun extractExperience(tank: Int, amount: Long, simulate: Boolean): Long = when (readOnlyExtract) {
        true -> 0
        false -> handler.extractExperience(tank, amount, simulate, side)
    }
}
