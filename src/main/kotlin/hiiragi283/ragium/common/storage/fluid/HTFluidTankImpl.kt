package hiiragi283.ragium.common.storage.fluid

import hiiragi283.ragium.api.block.entity.HTEnchantableBlockEntity
import hiiragi283.ragium.api.storage.HTFluidTank
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.common.init.RagiumEnchantments
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import kotlin.math.min

class HTFluidTankImpl(
    private val nbtKey: String,
    private var capacity: Int,
    private val validator: (FluidStack) -> Boolean,
    private val callback: Runnable,
) : HTFluidTank {
    private val baseCapacity: Int = capacity
    private var stackIn: FluidStack = FluidStack.EMPTY

    //    HTFluidTank    //

    override fun setFluid(stack: FluidStack) {
        stackIn = stack
    }

    override fun getFluid(): FluidStack = stackIn

    override fun getCapacity(): Int = capacity

    override fun isFluidValid(stack: FluidStack): Boolean = validator(stack)

    override fun canFill(stack: FluidStack): Boolean = validator(stack)

    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int {
        if (resource.isEmpty) return 0
        if (!isFluidValid(resource)) return 0
        // For simulation
        if (action.simulate()) {
            return when {
                stackIn.isEmpty -> min(capacity, resource.amount)
                FluidStack.isSameFluidSameComponents(stackIn, resource) -> min(capacity - stackIn.amount, resource.amount)
                else -> 0
            }
        } else {
            // For execution
            if (stackIn.isEmpty) {
                fluid = resource.copyWithAmount(min(capacity, resource.amount))
                onContentsChanged()
                return stackIn.amount
            } else if (FluidStack.isSameFluidSameComponents(stackIn, resource)) {
                var filled: Int = capacity - fluid.amount
                if (resource.amount < filled) {
                    stackIn.grow(resource.amount)
                    filled = resource.amount
                } else {
                    stackIn.amount = capacity
                }
                if (filled > 0) onContentsChanged()
                return filled
            } else {
                return 0
            }
        }
    }

    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack {
        val drained: Int = min(maxDrain, fluid.amount)
        val result: FluidStack = fluid.copyWithAmount(drained)
        if (drained > 0) {
            shrinkStack(drained, action.simulate())
            if (action.execute()) {
                onContentsChanged()
            }
        }
        return result
    }

    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
        if (resource.isEmpty) return FluidStack.EMPTY
        if (FluidStack.isSameFluidSameComponents(resource, stackIn)) return FluidStack.EMPTY
        return drain(resource.amount, action)
    }

    override fun updateCapacity(blockEntity: HTEnchantableBlockEntity) {
        val level: Int = blockEntity.getEnchantmentLevel(RagiumEnchantments.CAPACITY) + 1
        capacity = level * baseCapacity
    }

    override fun interactWithFluidStorage(player: Player, storageIO: HTStorageIO): Boolean =
        super.interactWithFluidStorage(player, storageIO)

    override fun onContentsChanged() {
        callback.run()
    }

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        FluidStack.OPTIONAL_CODEC
            .encodeStart(registryOps, stackIn)
            .ifSuccess { nbt.put(nbtKey, it) }
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        FluidStack.OPTIONAL_CODEC
            .parse(registryOps, nbt.get(nbtKey))
            .ifSuccess(::setFluid)
    }
}
