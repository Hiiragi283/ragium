package hiiragi283.ragium.common.storage.experience

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.experience.HTExperienceStorage
import hiiragi283.ragium.api.storage.experience.IExperienceStorageItem
import hiiragi283.ragium.api.util.HTContentListener
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

/**
 * @see net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper
 */
class HTBottleExperienceStorage(private var bottle: ItemStack) :
    HTExperienceStorage,
    IExperienceStorageItem,
    HTContentListener.Empty,
    HTValueSerializable.Empty {
    companion object {
        const val BOTTLE_AMOUNT: Long = 3L + 5L + 5L
    }

    override fun insertExp(amount: Long, action: HTStorageAction, access: HTStorageAccess): Long {
        if (bottle.count != 1 || amount < BOTTLE_AMOUNT || bottle.`is`(Items.EXPERIENCE_BOTTLE)) {
            return 0
        }
        if (action.execute) {
            bottle = bottle.transmuteCopy(Items.EXPERIENCE_BOTTLE)
        }
        return BOTTLE_AMOUNT
    }

    override fun extractExp(amount: Long, action: HTStorageAction, access: HTStorageAccess): Long {
        if (bottle.count != 1 || amount < BOTTLE_AMOUNT || !bottle.`is`(Items.GLASS_BOTTLE)) {
            return 0
        }
        if (action.execute) {
            bottle = bottle.transmuteCopy(Items.GLASS_BOTTLE)
        }
        return BOTTLE_AMOUNT
    }

    override fun getAmount(): Long = when {
        bottle.`is`(Items.EXPERIENCE_BOTTLE) -> BOTTLE_AMOUNT
        else -> 0
    }

    override fun getCapacity(): Long = when {
        bottle.`is`(Items.EXPERIENCE_BOTTLE) -> BOTTLE_AMOUNT
        else -> 0
    }

    override fun getContainer(): ItemStack = bottle
}
