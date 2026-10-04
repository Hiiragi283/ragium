package hiiragi283.ragium.common.item.block

import hiiragi283.lib.fluid.FluidStack
import hiiragi283.lib.fluid.HTFlowingFluidHelper
import hiiragi283.lib.item.HTSubCreativeTabContents
import hiiragi283.lib.registry.asHolderSequence
import hiiragi283.ragium.api.util.HTStorageHelper
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

class HTCreativeTankBlockItem(block: Block, properties: Properties) :
    BlockItem(block, properties),
    HTSubCreativeTabContents {
    override fun addItems(
        baseItem: Holder<Item>,
        parameters: CreativeModeTab.ItemDisplayParameters,
        output: CreativeModeTab.Output
    ) {
        parameters.holders
            .lookupOrThrow(Registries.FLUID)
            .asHolderSequence()
            .forEach { fluid ->
                if (!HTFlowingFluidHelper.isSource(fluid)) return@forEach
                val fluidStack = FluidStack(fluid)
                if (fluidStack.isEmpty) return@forEach
                val stack = ItemStack(baseItem)
                HTStorageHelper.updateFluid(stack, fluidStack)
                output.accept(stack)
            }
    }
}
