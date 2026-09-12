package hiiragi283.ragium.common.item

import hiiragi283.lib.item.HTSubCreativeTabContents
import hiiragi283.lib.text.Text
import hiiragi283.lib.text.translatableText
import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.api.data.recipe.HTOreSlurryDataHelper
import hiiragi283.ragium.common.transfer.item.HTCustomBucketItemHandler
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.transfer.access.ItemAccess
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource

class HTOreSlurryBucketItem(properties: Properties) :
    Item(properties),
    HTSubCreativeTabContents {
    constructor(content: Fluid, properties: Properties) : this(properties)

    override fun getName(itemStack: ItemStack): Component {
        val oreSlurryName: Text = HTOreSlurryDataHelper.getTitle(itemStack) ?: return super.getName(itemStack)
        return translatableText(super.descriptionId, oreSlurryName)
    }

    //    HTSubCreativeTabContents    //

    override fun addItems(
        baseItem: Holder<Item>,
        parameters: CreativeModeTab.ItemDisplayParameters,
        output: CreativeModeTab.Output
    ) {
        parameters.holders()
            .lookupOrThrow(RagiumRegistries.Keys.ORE_SLURRY_DATA)
            .listElements()
            .map(HTOreSlurryDataHelper::createBucket)
            .forEach(output::accept)
    }

    override fun shouldAddDefault(): Boolean = false

    //    BucketHandler    //

    class BucketHandler(itemAccess: ItemAccess) : HTCustomBucketItemHandler(itemAccess) {
        override fun getResourceFrom(accessResource: ItemResource, index: Int): FluidResource =
            HTOreSlurryDataHelper.createFluid(accessResource)?.let(FluidResource::of) ?: FluidResource.EMPTY
    }
}
