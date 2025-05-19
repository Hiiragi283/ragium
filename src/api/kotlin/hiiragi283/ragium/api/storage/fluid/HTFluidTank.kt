package hiiragi283.ragium.api.storage.fluid

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.enchantment.HTEnchantmentListener
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.storage.HTSingleVariantStorage
import hiiragi283.ragium.api.storage.HTStorageIO
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil

abstract class HTFluidTank(private val validator: (HTFluidVariant) -> Boolean, private val callback: () -> Unit) :
    HTSingleVariantStorage<HTFluidVariant>(),
    HTEnchantmentListener,
    HTNbtCodec {
    val stack: FluidStack get() = resource.toStack(amount)

    fun canInsert(variant: HTFluidVariant, amount: Int): Boolean = insert(variant, amount, true) == amount

    fun canInsert(fluid: Fluid, amount: Int): Boolean = insert(fluid, amount, true) == amount

    fun canInsert(stack: FluidStack): Boolean = insert(stack, true) == stack.amount

    fun canExtract(maxAmount: Int): Boolean = extract(resource, maxAmount, true) == maxAmount

    fun insert(fluid: Fluid, amount: Int, simulate: Boolean): Int = insert(HTFluidVariant.of(fluid), amount, simulate)

    fun insert(stack: FluidStack, simulate: Boolean): Int {
        val inserted: Int = insert(HTFluidVariant.of(stack), stack.amount, simulate)
        if (!simulate) {
            stack.amount -= inserted
        }
        return inserted
    }

    fun extract(maxAmount: Int, simulate: Boolean): Int = extract(resource, maxAmount, simulate)

    fun interactWithFluidStorage(player: Player, storageIO: HTStorageIO): Boolean =
        FluidUtil.interactWithFluidHandler(player, InteractionHand.MAIN_HAND, storageIO.wrapFluidTank(this))

    //    HTSingleVariantStorage    //

    override fun getEmptyVariant(): HTFluidVariant = HTFluidVariant.EMPTY

    override fun isValid(variant: HTFluidVariant): Boolean = validator(variant)

    override fun onContentsChanged() {
        callback()
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
        var capacity: Int = 8000
        var validator: (HTFluidVariant) -> Boolean = { true }
        var callback: () -> Unit = {}

        fun build(nbtKey: String): HTFluidTank = RagiumAPI.getInstance().buildFluidTank(nbtKey, capacity, validator, callback)
    }
}
