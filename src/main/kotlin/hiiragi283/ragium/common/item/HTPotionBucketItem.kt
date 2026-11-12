package hiiragi283.ragium.common.item

import hiiragi283.lib.item.HTPotionBasedItem
import hiiragi283.lib.item.alchemy.HTBottleType
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.text.Text
import hiiragi283.lib.text.translatableText
import hiiragi283.ragium.common.transfer.fluid.HTCustomBucketFluidHandler
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.transfer.access.ItemAccess
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource

class HTPotionBucketItem(properties: Properties) : HTPotionBasedItem(properties) {
    constructor(content: Fluid, properties: Properties) : this(properties)

    override fun getName(stack: ItemStack): Text {
        val potionName: Text = HTPotionHelper.getPotionName(stack, HTBottleType.DEFAULT)
        return translatableText(super.descriptionId, potionName)
    }

    class BucketHandler(itemAccess: ItemAccess) : HTCustomBucketFluidHandler(itemAccess) {
        override fun getResourceFrom(accessResource: ItemResource, index: Int): FluidResource =
            HTPotionHelper.createFluid(accessResource).mapOrElse(FluidResource::of) { FluidResource.EMPTY }
    }
}
