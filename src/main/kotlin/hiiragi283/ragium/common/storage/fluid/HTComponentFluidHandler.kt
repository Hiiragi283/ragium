package hiiragi283.ragium.common.storage.fluid

import hiiragi283.ragium.api.extension.negate
import hiiragi283.ragium.api.extension.setOrRemove
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.storage.fluid.HTFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidStorageStack
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.storageCopy
import hiiragi283.ragium.api.storage.fluid.toContent
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.MutableDataComponentHolder
import net.neoforged.neoforge.fluids.SimpleFluidContent
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem

/**
 * [HTFluidHandler]に基づいたコンポーネント向けの実装
 */
open class HTComponentFluidHandler(protected val stack: ItemStack, capacity: Long) :
    IFluidHandlerItem,
    HTFluidHandler {
    protected val tank: HTFluidTank = createTank(capacity)

    protected open fun createTank(capacity: Long): HTFluidTank = ComponentTank(stack, capacity)

    final override fun getContainer(): ItemStack = stack

    override fun getFluidTanks(side: Direction?): List<HTFluidTank> = listOf(tank)

    override fun onContentsChanged() {}

    protected open class ComponentTank(private val parent: MutableDataComponentHolder, private val capacity: Long) : HTFluidTank.Mutable {
        protected val component: DataComponentType<SimpleFluidContent> get() = RagiumDataComponents.FLUID_CONTENT

        override fun getStack(): HTFluidStorageStack = parent.getOrDefault(component, SimpleFluidContent.EMPTY).storageCopy()

        override fun getCapacityAsLong(stack: HTFluidStorageStack): Long = capacity

        override fun isValid(stack: HTFluidStorageStack): Boolean = true

        override fun deserialize(input: HTValueInput) {}

        override fun onContentsChanged() {}

        override fun setStack(stack: HTFluidStorageStack) {
            parent.setOrRemove(component, stack.toContent(), SimpleFluidContent::isEmpty.negate())
        }
    }
}
