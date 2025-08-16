package hiiragi283.ragium.common.block.entity.dynamo

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumDataMaps
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack

class HTThermalGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTFuelGeneratorBlockEntity(
        RagiumBlockEntityTypes.THERMAL_GENERATOR,
        pos,
        state,
    ) {
    override val energyUsage: Int get() = RagiumAPI.getConfig().getBasicMachineEnergyUsage()

    override fun getFuelValue(stack: ItemStack): Int = stack.getBurnTime(null) / 10

    override fun getFuelStack(value: Int): FluidStack = HTFluidContent.LAVA.toStack(value)

    override fun getRequiredAmount(stack: FluidStack): Int = getRequiredAmount(stack, RagiumDataMaps.THERMAL_FUEL)
}
