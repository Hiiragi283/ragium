package hiiragi283.ragium.common.fluid

import hiiragi283.lib.fluid.HTFluidType
import hiiragi283.ragium.api.data.recipe.HTOreSlurryDataHelper
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

class HTOreSlurryFluidType(properties: Properties) : HTFluidType(properties) {
    override fun getDescription(stack: FluidStack): Component =
        HTOreSlurryDataHelper.getTitle(stack) ?: super.getDescription(stack)

    override fun getBucket(stack: FluidStack): ItemStack =
        HTOreSlurryDataHelper.createBucket(stack) ?: super.getBucket(stack)
}
