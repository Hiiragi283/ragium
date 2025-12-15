package hiiragi283.ragium.common.storage.fluid.tank

import hiiragi283.ragium.api.capability.HTFluidCapabilities
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.util.HTContentListener
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem

class HTFluidHandlerItemWrapper private constructor(private val handler: IFluidHandlerItem) :
    HTFluidTank,
    HTContentListener.Empty,
    HTValueSerializable.Empty {
        companion object {
            @JvmStatic
            fun create(stack: ItemStack): HTFluidHandlerItemWrapper? = HTFluidCapabilities.getCapability(stack)?.let(::create)

            @JvmStatic
            fun create(stack: ImmutableItemStack?): HTFluidHandlerItemWrapper? = HTFluidCapabilities.getCapability(stack)?.let(::create)

            @JvmStatic
            fun create(handler: IFluidHandlerItem): HTFluidHandlerItemWrapper? = when (handler.tanks) {
                1 -> HTFluidHandlerItemWrapper(handler)
                else -> null
            }
        }

        val container: ImmutableItemStack? get() = handler.container.toImmutable()

        override fun getStack(): ImmutableFluidStack? = handler.getFluidInTank(0).toImmutable()

        override fun getCapacity(stack: ImmutableFluidStack?): Int = handler.getTankCapacity(0)

        override fun isValid(stack: ImmutableFluidStack): Boolean = handler.isFluidValid(0, stack.unwrap())

        override fun insert(stack: ImmutableFluidStack?, action: HTStorageAction, access: HTStorageAccess): ImmutableFluidStack? {
            if (stack == null) return null
            val filled: Int = handler.fill(stack.unwrap(), action.toFluid())
            return stack.copyWithAmount(stack.amount() - filled)
        }

        override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): ImmutableFluidStack? =
            handler.drain(amount, action.toFluid()).toImmutable()
    }
