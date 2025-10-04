package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.variant.HTGeneratorVariant
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack

class HTNuclearGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTFuelGeneratorBlockEntity(
        HTGeneratorVariant.NUCLEAR,
        pos,
        state,
    ) {
    override fun getFuelValue(stack: ItemStack): Int = when {
        stack.`is`(RagiumModTags.Items.IS_NUCLEAR_FUEL) -> (tank.getCapacityAsInt(tank.getStack()) * 0.9).toInt()
        else -> 0
    }

    override fun getFuelStack(value: Int): FluidStack = RagiumFluidContents.GREEN_FUEL.toStack(value)

    override fun getRequiredAmount(access: RegistryAccess, stack: FluidStack): Int =
        RagiumDataMaps.INSTANCE.getNuclearFuel(access, stack.fluidHolder)
}
