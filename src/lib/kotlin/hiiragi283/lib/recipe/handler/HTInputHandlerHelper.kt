package hiiragi283.lib.recipe.handler

import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.transfer.HTResourceSlot
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.fluid.HTFluidTank
import hiiragi283.lib.transfer.fluid.getFluidStack
import hiiragi283.lib.transfer.item.HTItemSlot
import hiiragi283.lib.transfer.item.getItemStack
import hiiragi283.lib.transfer.item.toResourcePair
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
data object HTInputHandlerHelper {
    //    Fluid    //

    /**
     * 単一の[HTFluidTank]を[アイテム][FluidInstance]向けの[HTInputSlot]に変換します。
     * @since 26.1.7
     */
    @JvmStatic
    fun forFluid(tank: HTFluidTank): HTInputSlot<FluidInstance> = SingleFluid(tank)

    private class SingleFluid(tank: HTResourceSlot<FluidResource>) :
        HTResourceInputSlot<Fluid, FluidResource, FluidInstance>(tank) {
        override fun isEmpty(instance: FluidInstance): Boolean = HTIngredientHelper.isEmpty(instance)

        override fun getEmptyStack(): FluidStack = FluidStack.EMPTY

        override fun getAmount(instance: FluidInstance): Int = instance.amount()

        override fun asResource(instance: FluidInstance): FluidResource = when (instance) {
            is FluidStack -> FluidResource.of(instance)
            is FluidStackTemplate -> FluidResource.of(instance)
            else -> FluidResource.of(instance.typeHolder())
        }

        override fun getStoredInput(): FluidStack = slot.getFluidStack()
    }

    //    Item    //

    /**
     * 単一の[HTItemSlot]を[アイテム][ItemInstance]向けの[HTInputSlot]に変換します。
     * @since 26.1.7
     */
    @JvmStatic
    fun forItem(slot: HTItemSlot): HTInputSlot<ItemInstance> = SingleItem(slot)

    private class SingleItem(slot: HTResourceSlot<ItemResource>) :
        HTResourceInputSlot<Item, ItemResource, ItemInstance>(slot) {
        override fun isEmpty(instance: ItemInstance): Boolean = HTIngredientHelper.isEmpty(instance)

        override fun getEmptyStack(): ItemStack = ItemStack.EMPTY

        override fun getAmount(instance: ItemInstance): Int = instance.count()

        override fun asResource(instance: ItemInstance): ItemResource = when (instance) {
            is ItemStack -> ItemResource.of(instance)
            is ItemStackTemplate -> ItemResource.of(instance)
            else -> ItemResource.of(instance.typeHolder())
        }

        override fun getStoredInput(): ItemStack = slot.getItemStack()
    }

    /**
     * 複数の[HTItemSlot]を[アイテム][ItemInstance]向けの[HTInputHandler]に変換します。
     * @since 26.1.11
     */
    @JvmStatic
    fun forItems(slots: List<HTItemSlot>): HTInputHandler<ItemInstance> = MultiItems(slots)

    @JvmRecord
    private data class MultiItems(private val slots: List<HTItemSlot>) : HTInputHandler<ItemInstance> {
        override fun use(input: ItemInstance?, transaction: TransactionContext): HTInputHandler.UseResult {
            if (input == null || HTIngredientHelper.isEmpty(input)) return HTInputHandler.UseResult.EMPTY
            val (resource: ItemResource, amount: Int) = HTIngredientHelper.unwrap(input).toResourcePair()
            var extracted = 0
            for (slot: HTItemSlot in slots) {
                extracted += slot.extract(resource, amount - extracted, transaction, HTTransferAccess.INTERNAL)
                if (extracted == amount) break
            }
            return when (extracted) {
                amount -> HTInputHandler.UseResult.CONSUMED
                else -> HTInputHandler.UseResult.NOT_CONSUMED
            }
        }
    }
}
