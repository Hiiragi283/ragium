package hiiragi283.ragium.common.transfer.item

import net.minecraft.world.item.Items
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.transfer.ItemAccessResourceHandler
import net.neoforged.neoforge.transfer.access.ItemAccess
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource
import java.util.Objects

/**
 * @see net.neoforged.neoforge.transfer.fluid.BucketResourceHandler
 */
abstract class HTCustomBucketItemHandler(itemAccess: ItemAccess) :
    ItemAccessResourceHandler<FluidResource>(itemAccess, 1) {
    override fun getAmountFrom(accessResource: ItemResource, index: Int): Int {
        val resource: FluidResource = getResourceFrom(accessResource, index)
        return when (resource.isEmpty) {
            true -> 0
            false -> FluidType.BUCKET_VOLUME
        }
    }

    override fun update(
        accessResource: ItemResource,
        index: Int,
        newResource: FluidResource,
        newAmount: Int
    ): ItemResource? = when {
        newAmount == 0 -> ItemResource.of(Items.BUCKET)
        newAmount != FluidType.BUCKET_VOLUME -> ItemResource.EMPTY
        else -> update(accessResource, newResource.toStack(newAmount))
    }

    protected open fun update(accessResource: ItemResource, newStack: FluidStack): ItemResource =
        ItemResource.of(newStack.fluidType.getBucket(newStack))

    override fun getCapacity(index: Int, resource: FluidResource): Int {
        Objects.checkIndex(index, size)
        return FluidType.BUCKET_VOLUME
    }
}
