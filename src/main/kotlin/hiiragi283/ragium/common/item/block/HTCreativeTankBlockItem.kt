package hiiragi283.ragium.common.item.block

import hiiragi283.lib.data.buildDataPatch
import hiiragi283.lib.fluid.FluidStack
import hiiragi283.lib.fluid.HTFlowingFluidHelper
import hiiragi283.lib.item.HTSubCreativeTabContents
import hiiragi283.ragium.api.data.RagiumDataComponents
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.fluids.SimpleFluidContent
import java.util.function.Consumer

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
            .listElements()
            .mapMulti { fluid, consumer: Consumer<ItemStack> ->
                if (!HTFlowingFluidHelper.isSource(fluid)) return@mapMulti
                val content: SimpleFluidContent = FluidStack(fluid).let(SimpleFluidContent::copyOf)
                if (content.isEmpty) return@mapMulti
                val stack = ItemStack(baseItem, 1, buildDataPatch { set(RagiumDataComponents.FLUID, content) })
                consumer.accept(stack)
            }.forEach(output::accept)
    }

    override fun shouldAddDefault(): Boolean = false
}
