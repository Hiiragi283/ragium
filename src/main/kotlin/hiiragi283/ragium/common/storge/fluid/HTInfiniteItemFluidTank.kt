package hiiragi283.ragium.common.storge.fluid

import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResource
import hiiragi283.core.impl.storage.fluid.HTItemFluidTank
import hiiragi283.core.setup.HCDataComponents
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.SimpleFluidContent

class HTInfiniteItemFluidTank(override val container: ItemStack) : HTItemFluidTank {
    override fun isValid(resource: HTFluidResourceType): Boolean = true

    override fun insert(
        resource: HTFluidResourceType?,
        amount: Int,
        action: HTStorageAction,
        access: HTStorageAccess,
    ): Int = amount

    override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int = amount

    override fun getResource(): HTFluidResourceType? =
        container.getOrDefault(HCDataComponents.FLUID, SimpleFluidContent.EMPTY).copy().toResource()

    override fun getCapacity(resource: HTFluidResourceType?): Int = Int.MAX_VALUE

    override fun getAmount(): Int = when (getResource()) {
        null -> 0
        else -> Int.MAX_VALUE
    }
}
