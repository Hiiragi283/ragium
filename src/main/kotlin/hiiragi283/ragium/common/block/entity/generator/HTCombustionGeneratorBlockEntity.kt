package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.data.RagiumDataMaps
import hiiragi283.ragium.common.variant.HTGeneratorVariant
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack

class HTCombustionGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTFuelGeneratorBlockEntity(
        HTGeneratorVariant.COMBUSTION,
        pos,
        state,
    ) {
    override fun getFuelValue(stack: ItemStack): Int = when {
        stack.`is`(ItemTags.COALS) -> 100
        else -> 0
    }

    override fun getFuelStack(value: Int): FluidStack = RagiumFluidContents.CRUDE_OIL.toStack(value)

    override fun getRequiredAmount(access: RegistryAccess, stack: FluidStack): Int =
        RagiumDataMaps.INSTANCE.getCombustionFuel(access, stack.fluidHolder)?.amount ?: 0
}
