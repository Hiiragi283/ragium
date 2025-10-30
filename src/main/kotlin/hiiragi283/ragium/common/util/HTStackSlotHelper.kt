package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.ImmutableStack
import hiiragi283.ragium.api.stack.getCraftingRemainingItem
import hiiragi283.ragium.api.stack.hasCraftingRemainingItem
import hiiragi283.ragium.api.storage.HTStackSlot
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.capability.HTFluidCapabilities
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.getFluidStack
import hiiragi283.ragium.api.storage.fluid.insertFluid
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import java.util.Optional
import java.util.function.ToIntFunction

object HTStackSlotHelper {
    fun <STACK : ImmutableStack<*, STACK>, SLOT : HTStackSlot<STACK>> moveStack(
        from: SLOT?,
        to: SLOT?,
        amount: Int = from?.getAmount() ?: 0,
        access: HTStorageAccess = HTStorageAccess.INTERNAL,
    ): STACK? {
        if (from == null || to == null || amount <= 0) return null
        val simulatedExtract: STACK = from.extract(amount, HTStorageAction.SIMULATE, access) ?: return null
        val simulatedRemain: STACK? = to.insert(simulatedExtract, HTStorageAction.SIMULATE, access)
        val simulatedAccepted: Int = amount - (simulatedRemain?.amount() ?: 0)

        val extracted: STACK = from.extract(simulatedAccepted, HTStorageAction.EXECUTE, access) ?: return null
        val remainder: STACK? = to.insert(extracted, HTStorageAction.EXECUTE, access)
        if (remainder != null) {
            val leftover: STACK? = from.insert(remainder, HTStorageAction.EXECUTE, access)
            if (leftover != null) {
                RagiumAPI.LOGGER.error("Stack slot $from did not accept leftover stack from $to! Voiding it.")
            }
        }
        return remainder
    }

    //    Item    //

    @JvmStatic
    fun shrinkStack(slot: HTItemSlot.Mutable, ingredient: ToIntFunction<ImmutableItemStack>, action: HTStorageAction): Int {
        val stackIn: ImmutableItemStack? = slot.getStack()
        if (stackIn != null && stackIn.hasCraftingRemainingItem() && stackIn.amount() == 1) {
            if (action.execute) {
                slot.setStack(stackIn.getCraftingRemainingItem())
            }
            return 0
        } else {
            return slot.shrinkStack(ingredient.applyAsInt(stackIn), action)
        }
    }

    /**
     * 指定された[ingredient]から，現在の個数を削除します。
     * @param ingredient 削除する個数を提供する材料
     * @param action [HTStorageAction.EXECUTE]の場合のみ実際に削除を行います。
     * @return 実際に削除された個数
     */
    @JvmStatic
    fun shrinkStack(slot: HTItemSlot.Mutable, ingredient: HTItemIngredient, action: HTStorageAction): Int =
        shrinkStack(slot, ingredient::getRequiredAmount, action)

    /**
     * 指定された[ingredient]から，現在の個数を削除します。
     * @param ingredient 削除する個数を提供する材料
     * @param action [HTStorageAction.EXECUTE]の場合のみ実際に削除を行います。
     * @return [Optional.isEmpty]の場合は`0`，それ以外は実際に削除された個数
     */
    @JvmStatic
    fun shrinkStack(slot: HTItemSlot.Mutable, ingredient: Optional<HTItemIngredient>, action: HTStorageAction): Int =
        ingredient.map { ingredient1 -> shrinkStack(slot, ingredient1, action) }.orElse(0)

    @JvmStatic
    fun insertStacks(
        slots: Iterable<HTItemSlot>,
        stack: ImmutableItemStack?,
        action: HTStorageAction,
        filter: (HTItemSlot, ImmutableItemStack) -> Boolean = HTItemSlot::isValid,
        onBreak: () -> Unit = {},
    ): ImmutableItemStack? {
        var remainder: ImmutableItemStack? = stack
        for (slot: HTItemSlot in slots) {
            if (remainder != null && filter(slot, remainder)) {
                remainder = slot.insert(remainder, action, HTStorageAccess.INTERNAL)
                if (remainder == null) {
                    onBreak()
                    break
                }
            }
        }
        return remainder
    }

    //    Fluid    //

    @JvmStatic
    fun shrinkStack(tank: HTFluidTank, ingredient: ToIntFunction<ImmutableFluidStack>, action: HTStorageAction): Int =
        tank.extract(ingredient.applyAsInt(tank.getStack()), action, HTStorageAccess.INTERNAL)?.amount() ?: 0

    /**
     * 指定された[ingredient]から，現在の数量を削除します。
     * @param ingredient 削除する数量を提供する材料
     * @param action [HTStorageAction.EXECUTE]の場合のみ実際に削除を行います。
     * @return 実際に削除された数量
     */
    @JvmStatic
    fun shrinkStack(tank: HTFluidTank, ingredient: HTFluidIngredient, action: HTStorageAction): Int =
        shrinkStack(tank, ingredient::getRequiredAmount, action)

    /**
     * 指定された[ingredient]から，現在の数量を削除します。
     * @param ingredient 削除する数量を提供する材料
     * @param action [HTStorageAction.EXECUTE]の場合のみ実際に削除を行います。
     * @return [Optional.isEmpty]の場合は`0`，それ以外は実際に削除された数量
     */
    @JvmStatic
    fun shrinkStack(tank: HTFluidTank, ingredient: Optional<HTFluidIngredient>, action: HTStorageAction): Int =
        ingredient.map { ingredient1 -> shrinkStack(tank, ingredient1, action) }.orElse(0)

    /**
     * @see net.neoforged.neoforge.fluids.FluidUtil.interactWithFluidHandler
     * @see mekanism.common.util.FluidUtils.handleTankInteraction
     */
    @JvmStatic
    fun interact(
        player: Player,
        hand: InteractionHand,
        stack: ItemStack,
        tank: HTFluidTank,
    ): Boolean {
        if (!HTFluidCapabilities.hasCapability(stack)) return false
        val handler: IFluidHandlerItem = HTFluidCapabilities.getCapability(stack.copyWithCount(1)) ?: return false
        val firstFluid: FluidStack = when (tank.getStack() == null) {
            true -> handler.drain(Int.MAX_VALUE, HTStorageAction.SIMULATE.toFluid())
            false -> handler.drain(tank.getFluidStack().copyWithAmount(Int.MAX_VALUE), HTStorageAction.SIMULATE.toFluid())
        }
        if (firstFluid.isEmpty) {
            val stackIn: ImmutableFluidStack? = tank.getStack()
            if (stackIn != null) {
                val filled: Int = handler.fill(stackIn.stack, HTStorageAction.of(player.isCreative).toFluid())
                val container: ItemStack = handler.container
                if (filled > 0) {
                    if (stack.count == 1) {
                        player.setItemInHand(hand, container)
                    } else if (stack.count > 1 && player.inventory.add(container)) {
                        stack.shrink(1)
                    } else {
                        player.drop(container, false, true)
                        stack.shrink(1)
                    }
                    tank.extract(filled, HTStorageAction.EXECUTE, HTStorageAccess.MANUAL)
                    return true
                }
            }
        } else {
            val remainder: FluidStack = tank.insertFluid(firstFluid, HTStorageAction.SIMULATE, HTStorageAccess.MANUAL)
            val remainderAmount: Int = remainder.amount
            val storedAmount: Int = firstFluid.amount
            if (remainderAmount < storedAmount) {
                var filled = false
                val drained: FluidStack = handler.drain(
                    firstFluid.copyWithAmount(storedAmount - remainderAmount),
                    HTStorageAction.of(player.isCreative).toFluid(),
                )
                if (!drained.isEmpty) {
                    val container: ItemStack = handler.container
                    if (player.isCreative) {
                        filled = true
                    } else if (!container.isEmpty) {
                        if (stack.count == 1) {
                            player.setItemInHand(hand, container)
                            filled = true
                        } else if (player.inventory.add(container)) {
                            stack.shrink(1)
                            filled = true
                        }
                    } else {
                        stack.shrink(1)
                        if (stack.isEmpty) {
                            player.setItemInHand(hand, ItemStack.EMPTY)
                        }
                        filled = true
                    }
                    if (filled) {
                        tank.insertFluid(drained, HTStorageAction.EXECUTE, HTStorageAccess.MANUAL)
                        return true
                    }
                }
            }
        }
        return false
    }
}
