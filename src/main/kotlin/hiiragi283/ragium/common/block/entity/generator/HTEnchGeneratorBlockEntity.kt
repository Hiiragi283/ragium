package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.world.level.block.state.BlockState

class HTEnchGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTFuelGeneratorBlockEntity(RagiumBlocks.ENCHANTMENT_GENERATOR, pos, state) {
    override fun getFuelValue(stack: ImmutableItemStack): Int = 0

    override fun getFuelStack(value: Int): ImmutableFluidStack? = RagiumFluidContents.EXPERIENCE.toImmutableStack(value)

    override fun getRequiredAmount(access: RegistryAccess, stack: ImmutableFluidStack?): Int =
        if (RagiumFluidContents.EXPERIENCE.isOf(stack)) 10 else 0
}
