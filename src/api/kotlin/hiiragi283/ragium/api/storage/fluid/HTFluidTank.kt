package hiiragi283.ragium.api.storage.fluid

import com.google.common.base.Predicates
import com.google.common.util.concurrent.Runnables
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.storage.HTSingleVariantStorage
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.util.HTEnchantmentListener
import hiiragi283.ragium.api.util.HTNbtCodec
import net.minecraft.tags.TagKey
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil

abstract class HTFluidTank(private val validator: (HTFluidVariant) -> Boolean, private val callback: Runnable) :
    HTSingleVariantStorage<HTFluidVariant>(),
    HTEnchantmentListener,
    HTNbtCodec {
    val stack: FluidStack get() = resource.toStack(amount)

    fun canInsert(variant: HTFluidVariant, amount: Int): Boolean = insert(variant, amount, true) == amount

    fun canInsert(stack: FluidStack): Boolean = insert(stack, true) == stack.amount

    fun canExtract(maxAmount: Int): Boolean = extract(resource, maxAmount, true) == maxAmount

    fun insert(stack: FluidStack, simulate: Boolean): Int = insert(HTFluidVariant.of(stack), stack.amount, simulate)

    fun extract(maxAmount: Int, simulate: Boolean): Int = extract(resource, maxAmount, simulate)

    fun interactWithFluidStorage(player: Player, storageIO: HTStorageIO): Boolean =
        FluidUtil.interactWithFluidHandler(player, InteractionHand.MAIN_HAND, storageIO.wrapFluidTank(this))

    //    HTSingleVariantStorage    //

    override fun getEmptyVariant(): HTFluidVariant = HTFluidVariant.EMPTY

    override fun isValid(variant: HTFluidVariant): Boolean = validator(variant)

    override fun onContentsChanged() {
        callback.run()
    }

    //    Builder    //

    class Builder {
        private var capacity: Int = 8000
        private var validator: (HTFluidVariant) -> Boolean = Predicates.alwaysTrue<HTFluidVariant>()::test
        private var callback: Runnable = Runnables.doNothing()

        fun setCapacity(capacity: Int): Builder = apply {
            this.capacity = capacity
        }

        fun setValidator(tagKey: TagKey<Fluid>): Builder = setValidator { variant: HTFluidVariant -> variant.isIn(tagKey) }

        fun setValidator(validator: (HTFluidVariant) -> Boolean): Builder = apply {
            this.validator = validator
        }

        fun setCallback(callback: Runnable): Builder = apply {
            this.callback = callback
        }

        fun build(nbtKey: String): HTFluidTank = RagiumAPI.getInstance().buildFluidTank(nbtKey, capacity, validator, callback)
    }
}
