package hiiragi283.ragium.common.block.entity.dynamo

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumDataMaps
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.BlockPos
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack

class HTCombustionGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTFuelGeneratorBlockEntity(
        TODO(),
        pos,
        state,
    ) {
    override val energyUsage: Int get() = RagiumAPI.getConfig().getAdvancedMachineEnergyUsage()

    override fun getFuelValue(stack: ItemStack): Int = when {
        stack.`is`(ItemTags.COALS) -> 160
        else -> 0
    }

    override fun getFuelStack(value: Int): FluidStack = RagiumFluidContents.CRUDE_OIL.toStack(value)

    override fun getRequiredAmount(stack: FluidStack): Int = getRequiredAmount(stack, RagiumDataMaps.COMBUSTION_FUEL)
}
