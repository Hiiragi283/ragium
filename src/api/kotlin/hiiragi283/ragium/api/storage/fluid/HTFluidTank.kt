package hiiragi283.ragium.api.storage.fluid

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.enchantment.HTEnchantmentListener
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.storage.HTSingleVariantStorage
import hiiragi283.ragium.api.storage.HTStackStorage
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

abstract class HTFluidTank :
    HTSingleVariantStorage<HTFluidVariant>(),
    HTEnchantmentListener,
    HTNbtCodec,
    HTStackStorage<FluidStack> {
    fun canExtract(maxAmount: Int): Boolean = extract(resource, maxAmount, true) == maxAmount

    fun extract(maxAmount: Int, simulate: Boolean): Int = extract(resource, maxAmount, simulate)

    //    HTSingleVariantStorage    //

    final override fun getEmptyVariant(): HTFluidVariant = HTFluidVariant.EMPTY

    //    HTStackStorage    //

    final override val stack: FluidStack get() = resource.toStack(amount)

    final override fun replace(stack: FluidStack, shouldUpdate: Boolean) {
        if (stack.isEmpty) {
            clear()
            return
        }
        resource = HTFluidVariant.of(stack)
        amount = stack.amount
        if (shouldUpdate) {
            onContentsChanged()
        }
    }

    final override fun canInsert(stack: FluidStack): Boolean = insert(stack, true) == stack.amount

    override fun insert(stack: FluidStack, simulate: Boolean): Int {
        val inserted: Int = insert(HTFluidVariant.of(stack), stack.amount, simulate)
        if (!simulate) {
            stack.amount -= inserted
        }
        return inserted
    }

    //    Builder    //

    companion object {
        @JvmStatic
        fun create(nbtKey: String, builderAction: Builder.() -> Unit = {}): HTFluidTank = Builder().apply(builderAction).build(nbtKey)

        @JvmStatic
        fun create(nbtKey: String, blockEntity: BlockEntity, builderAction: Builder.() -> Unit = {}): HTFluidTank = Builder()
            .apply {
                callback = blockEntity::setChanged
                builderAction()
            }.build(nbtKey)
    }

    class Builder internal constructor() {
        var capacity: Int = FluidType.BUCKET_VOLUME * 8
        var validator: (HTFluidVariant) -> Boolean = { true }
        var callback: () -> Unit = {}

        fun build(nbtKey: String): HTFluidTank = RagiumAPI.getInstance().buildFluidTank(
            nbtKey,
            capacity,
            validator,
            callback,
        )
    }
}
