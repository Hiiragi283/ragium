package hiiragi283.ragium.common.storage.experience

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.experience.IExperienceHandlerItem
import hiiragi283.ragium.api.util.HTContentListener
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class HTBottleExperienceHandler(private var bottle: ItemStack) :
    IExperienceHandlerItem,
    HTContentListener.Empty,
    HTValueSerializable.Empty {
    companion object {
        const val BOTTLE_AMOUNT: Long = 3L + 5L + 5L
    }

    override fun getContainer(): ItemStack = bottle

    override fun getExperienceTanks(): Int = 1

    override fun getExperienceAmount(tank: Int): Long = when {
        bottle.`is`(Items.EXPERIENCE_BOTTLE) -> BOTTLE_AMOUNT
        else -> 0
    }

    override fun getExperienceCapacity(tank: Int): Long = when {
        bottle.`is`(Items.EXPERIENCE_BOTTLE) -> BOTTLE_AMOUNT
        else -> 0
    }

    override fun insertExperience(tank: Int, amount: Long, simulate: Boolean): Long {
        if (bottle.count != 1 || amount < BOTTLE_AMOUNT || bottle.`is`(Items.EXPERIENCE_BOTTLE)) {
            return 0
        }
        if (!simulate) {
            bottle = bottle.transmuteCopy(Items.EXPERIENCE_BOTTLE)
        }
        return BOTTLE_AMOUNT
    }

    override fun extractExperience(tank: Int, amount: Long, simulate: Boolean): Long {
        if (bottle.count != 1 || amount < BOTTLE_AMOUNT || !bottle.`is`(Items.GLASS_BOTTLE)) {
            return 0
        }
        if (!simulate) {
            bottle = bottle.transmuteCopy(Items.GLASS_BOTTLE)
        }
        return BOTTLE_AMOUNT
    }
}
