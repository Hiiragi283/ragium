package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.RagiumDataMaps
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.util.variant.HTGeneratorVariant
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack

class HTThermalGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTFuelGeneratorBlockEntity(
        HTGeneratorVariant.THERMAL,
        pos,
        state,
    ) {
    override fun getFuelValue(stack: ItemStack): Int = stack.getBurnTime(null) / 10

    override fun getFuelStack(value: Int): FluidStack = HTFluidContent.LAVA.toStack(value)

    override fun getRequiredAmount(stack: FluidStack): Int = getRequiredAmount(stack, RagiumDataMaps.THERMAL_FUEL)
}
