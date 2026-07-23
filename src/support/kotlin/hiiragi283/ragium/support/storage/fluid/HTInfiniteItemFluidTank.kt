package hiiragi283.ragium.support.storage.fluid

import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTItemFluidTank
import hiiragi283.core.api.storage.fluid.toResource
import hiiragi283.core.util.HTStorageHelper
import net.minecraft.world.item.ItemStack

class HTInfiniteItemFluidTank(override val container: ItemStack) : HTItemFluidTank {
    override fun isValid(resource: HTFluidResourceType): Boolean = true

    override fun insert(
        resource: HTFluidResourceType?,
        amount: Int,
        action: HTStorageAction,
        access: HTStorageAccess,
    ): Int = amount

    override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int = amount

    override fun getResource(): HTFluidResourceType? = HTStorageHelper.getFluid(container).toResource()

    override fun getCapacity(resource: HTFluidResourceType?): Int = Int.MAX_VALUE

    override fun getAmount(): Int = when (getResource()) {
        null -> 0
        else -> Int.MAX_VALUE
    }
}
