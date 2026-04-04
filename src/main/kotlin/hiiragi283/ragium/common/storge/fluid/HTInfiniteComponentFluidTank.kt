package hiiragi283.ragium.common.storge.fluid

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.fluid.toResource
import hiiragi283.core.common.storage.HTCapabilityCodec
import hiiragi283.core.common.storage.component.HTComponentHandler
import net.minecraft.world.item.ItemStack

/**
 * @see hiiragi283.core.common.storage.fluid.HTComponentFluidTank
 * @see hiiragi283.ragium.common.block.entity.storage.HTCreativeTankBlockEntity.CreativeFluidTank
 */
class HTInfiniteComponentFluidTank(private val attachedTo: ItemStack) :
    HTFluidTank,
    HTContentListener.Empty,
    HTValueSerializable.Empty {
    constructor(context: HTComponentHandler.ContainerContext) : this(context.attachedTo)

    override fun isValid(resource: HTFluidResourceType): Boolean = false

    override fun insert(
        resource: HTFluidResourceType?,
        amount: Int,
        action: HTStorageAction,
        access: HTStorageAccess,
    ): Int = amount

    override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int = amount

    override fun getResource(): HTFluidResourceType? = HTCapabilityCodec.FLUID.getOrCreate(attachedTo, 1)[0].toResource()

    override fun getCapacity(resource: HTFluidResourceType?): Int = Int.MAX_VALUE

    override fun getAmount(): Int = when (getResource()) {
        null -> 0
        else -> Int.MAX_VALUE
    }
}
