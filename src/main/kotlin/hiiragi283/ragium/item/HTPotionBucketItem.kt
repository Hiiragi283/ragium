package hiiragi283.ragium.item

import hiiragi283.lib.item.HTPotionBasedItem
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.text.Text
import hiiragi283.lib.text.translatableText
import java.util.Objects
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.transfer.ItemAccessResourceHandler
import net.neoforged.neoforge.transfer.access.ItemAccess
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource

class HTPotionBucketItem(content: Fluid, properties: Properties) : HTPotionBasedItem(properties) {
    override fun getName(stack: ItemStack): Text {
        val potionName: Text = HTPotionHelper.getContents(stack)?.getText() ?: return super.getName(stack)
        return translatableText(super.descriptionId, potionName)
    }

    /**
     * @see net.neoforged.neoforge.transfer.fluid.BucketResourceHandler
     */
    class BucketHandler(itemAccess: ItemAccess) : ItemAccessResourceHandler<FluidResource>(itemAccess, 1) {
        override fun getResourceFrom(accessResource: ItemResource, index: Int): FluidResource = HTPotionHelper.getContents(accessResource)?.toFluidTemplate().let(FluidResource::of)

        override fun getAmountFrom(accessResource: ItemResource, index: Int): Int {
            val resource: FluidResource = getResourceFrom(accessResource, index)
            return when (resource.isEmpty) {
                true -> 0
                false -> FluidType.BUCKET_VOLUME
            }
        }

        override fun update(accessResource: ItemResource, index: Int, newResource: FluidResource, newAmount: Int): ItemResource = when {
            newAmount == 0 -> ItemResource.of(Items.BUCKET)
            newAmount != FluidType.BUCKET_VOLUME -> ItemResource.EMPTY
            else -> {
                val newStack: FluidStack = newResource.toStack(newAmount)
                ItemResource.of(newStack.fluidType.getBucket(newStack))
            }
        }

        override fun getCapacity(index: Int, resource: FluidResource): Int {
            Objects.checkIndex(index, size)
            return FluidType.BUCKET_VOLUME
        }
    }
}
