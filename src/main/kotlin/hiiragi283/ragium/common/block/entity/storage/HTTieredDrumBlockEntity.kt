package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.ragium.api.block.attribute.getAttributeTier
import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.fluid.tank.HTBasicFluidTank
import hiiragi283.ragium.common.tier.HTDrumTier
import hiiragi283.ragium.util.HTEnchantmentHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class HTTieredDrumBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTDrumBlockEntity(blockHolder, pos, state) {
    private lateinit var tier: HTDrumTier

    override fun initializeVariables() {
        tier = blockHolder.getAttributeTier()
    }

    override fun createTank(listener: HTContentListener): HTBasicFluidTank = DrumTank(listener)

    private fun getCapacity(): Int =
        HTEnchantmentHelper.processStorageCapacity(this.getLevel()?.random, enchantment, tier.getDefaultCapacity())

    //    DrumTank    //

    /**
     * @see mekanism.common.capabilities.fluid.FluidTankFluidTank
     */
    private inner class DrumTank(listener: HTContentListener) :
        HTBasicFluidTank(
            getCapacity(),
            HTPredicates.alwaysTrueBi(),
            HTPredicates.alwaysTrueBi(),
            HTPredicates.alwaysTrue(),
            listener,
        ) {
        val isCreative: Boolean = tier == HTDrumTier.CREATIVE

        override fun insert(stack: ImmutableFluidStack?, action: HTStorageAction, access: HTStorageAccess): ImmutableFluidStack? {
            val remainder: ImmutableFluidStack?
            if (isCreative && this.getStack() == null && action.execute && access != HTStorageAccess.EXTERNAL) {
                remainder = super.insert(stack, HTStorageAction.SIMULATE, access)
                if (remainder == null) {
                    setStackUnchecked(stack?.copyWithAmount(getCapacity()))
                }
            } else {
                remainder = super.insert(stack, action.combine(!isCreative), access)
            }
            return remainder
        }

        override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): ImmutableFluidStack? =
            super.extract(amount, action.combine(!isCreative), access)

        override fun getCapacity(stack: ImmutableFluidStack?): Int = this@HTTieredDrumBlockEntity.getCapacity()
    }
}
