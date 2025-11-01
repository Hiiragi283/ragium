package hiiragi283.ragium.common.storage.experience.tank

import com.google.common.primitives.Ints
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.experience.HTExperienceTank
import net.minecraft.world.entity.ExperienceOrb
import kotlin.math.min

class HTOrbExperienceTank(private val orb: ExperienceOrb) :
    HTExperienceTank.Basic(),
    HTValueSerializable.Empty {
    override fun setAmount(amount: Long) {
        if (amount == 0L) {
            if (orb.value == 0) return
            orb.value = 0
        } else {
            orb.value = Ints.saturatedCast(min(amount, getCapacity()))
        }
        onContentsChanged()
    }

    override fun getAmount(): Long = orb.value.toLong()

    override fun getCapacity(): Long = Long.MAX_VALUE

    override fun onContentsChanged() {
        if (orb.value <= 0) {
            orb.discard()
        }
    }
}
