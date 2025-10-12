package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.storage.fluid.ImmutableFluidStack
import hiiragi283.ragium.api.storage.item.ImmutableItemStack
import hiiragi283.ragium.api.storage.item.isOf
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.variant.HTGeneratorVariant
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.world.level.block.state.BlockState

class HTNuclearReactorBlockEntity(pos: BlockPos, state: BlockState) :
    HTFuelGeneratorBlockEntity(
        HTGeneratorVariant.NUCLEAR_REACTOR,
        pos,
        state,
    ) {
    override fun getFuelValue(stack: ImmutableItemStack): Int = when {
        stack.isOf(RagiumModTags.Items.IS_NUCLEAR_FUEL) -> (tank.getCapacityAsInt(tank.getStack()) * 0.9).toInt()
        else -> 0
    }

    override fun getFuelStack(value: Int): ImmutableFluidStack = RagiumFluidContents.GREEN_FUEL.toStorageStack(value)

    override fun getRequiredAmount(access: RegistryAccess, stack: ImmutableFluidStack): Int =
        RagiumDataMaps.INSTANCE.getNuclearFuel(access, stack.holder())
}
