package hiiragi283.ragium.common.item.block

import hiiragi283.lib.fluid.HTFlowingFluidHelper
import hiiragi283.lib.item.HTSubCreativeTabContents
import hiiragi283.lib.registry.asHolderSequence
import hiiragi283.lib.transfer.fluid.HTFluidTankUtil
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

class HTCreativeTankBlockItem(block: Block, properties: Properties) :
    BlockItem(block, properties),
    HTSubCreativeTabContents {
    override fun addItems(baseItem: Holder<Item>, parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output) {
        parameters.holders
            .lookupOrThrow(Registries.FLUID)
            .asHolderSequence()
            .forEach { fluid ->
                if (!HTFlowingFluidHelper.isSource(fluid)) return@forEach
                val fluidStack = FluidStack(fluid, FluidType.BUCKET_VOLUME)
                if (fluidStack.isEmpty) return@forEach
                val stack = ItemStack(baseItem)
                stack.applyComponents(HTFluidTankUtil.createUpdatedPatch(fluidStack))
                output.accept(stack)
            }
    }
}
