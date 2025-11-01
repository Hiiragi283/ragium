package hiiragi283.ragium.common.storage.experience.tank

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.experience.HTExperienceTank
import hiiragi283.ragium.api.util.HTContentListener
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class HTBottleExperienceTank(private var parent: ItemStack) :
    HTExperienceTank,
    HTContentListener.Empty,
    HTValueSerializable.Empty {
    companion object {
        const val BOTTLE_AMOUNT: Long = 3L + 5L + 5L
    }

    override fun insert(amount: Long, action: HTStorageAction, access: HTStorageAccess): Long {
        if (parent.count != 1 || amount < BOTTLE_AMOUNT || parent.`is`(Items.EXPERIENCE_BOTTLE)) {
            return 0
        }
        if (action.execute) {
            parent = parent.transmuteCopy(Items.EXPERIENCE_BOTTLE)
        }
        return BOTTLE_AMOUNT
    }

    override fun extract(amount: Long, action: HTStorageAction, access: HTStorageAccess): Long {
        if (parent.count != 1 || amount < BOTTLE_AMOUNT || !parent.`is`(Items.GLASS_BOTTLE)) {
            return 0
        }
        if (action.execute) {
            parent = parent.transmuteCopy(Items.GLASS_BOTTLE)
        }
        return BOTTLE_AMOUNT
    }

    override fun getAmount(): Long = when {
        parent.`is`(Items.EXPERIENCE_BOTTLE) -> BOTTLE_AMOUNT
        else -> 0
    }

    override fun getCapacity(): Long = when {
        parent.`is`(Items.EXPERIENCE_BOTTLE) -> BOTTLE_AMOUNT
        else -> 0
    }
}
