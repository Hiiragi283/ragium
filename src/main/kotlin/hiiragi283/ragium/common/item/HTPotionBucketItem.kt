package hiiragi283.ragium.common.item

import hiiragi283.lib.item.HTPotionBasedItem
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.text.Text
import hiiragi283.lib.text.translatableText
import hiiragi283.ragium.common.transfer.item.HTCustomBucketItemHandler
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.transfer.access.ItemAccess
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource

class HTPotionBucketItem(properties: Properties) : HTPotionBasedItem(properties) {
    constructor(content: Fluid, properties: Properties) : this(properties)

    override fun getName(stack: ItemStack): Text {
        val potionName: Text = HTPotionHelper.getContents(stack)?.getText() ?: return super.getName(stack)
        return translatableText(super.descriptionId, potionName)
    }

    class BucketHandler(itemAccess: ItemAccess) : HTCustomBucketItemHandler(itemAccess) {
        override fun getResourceFrom(accessResource: ItemResource, index: Int): FluidResource =
            HTPotionHelper.getContents(accessResource)?.toFluidTemplate().let(FluidResource::of)
    }
}
