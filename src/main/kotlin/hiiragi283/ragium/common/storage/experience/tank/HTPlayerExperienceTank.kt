package hiiragi283.ragium.common.storage.experience.tank

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.experience.HTExperienceTank
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.util.HTExperienceHelper
import net.minecraft.world.entity.player.Player

class HTPlayerExperienceTank(private val player: Player) :
    HTExperienceTank.Basic(),
    HTContentListener.Empty,
    HTValueSerializable.Empty {
    override fun setAmount(amount: Long) {
        HTExperienceHelper.setPlayerExp(player, amount)
    }

    override fun getAmount(): Long = HTExperienceHelper.getPlayerExp(player)

    override fun getCapacity(): Long = Long.MAX_VALUE
}
