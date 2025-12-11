package hiiragi283.ragium.util

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.capability.HTFluidCapabilities
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.ImmutableStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTAmountView
import hiiragi283.ragium.api.storage.HTStackSlot
import hiiragi283.ragium.api.storage.HTStackView
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.storage.fluid.tank.HTFluidHandlerItemWrapper
import net.minecraft.core.HolderLookup
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.Level
import net.minecraft.world.level.redstone.Redstone
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import java.util.function.Consumer
import java.util.function.ToIntFunction

object HTStackSlotHelper {
    fun <STACK : ImmutableStack<*, STACK>, SLOT : HTStackSlot<STACK>> moveStack(
        from: SLOT?,
        to: SLOT?,
        amount: Int = from?.getAmount() ?: 0,
        access: HTStorageAccess = HTStorageAccess.INTERNAL,
    ): HTStackMoveResult<STACK> {
        if (from == null || to == null || amount <= 0) return HTStackMoveResult.failed()
        val simulatedExtract: STACK = from.extract(amount, HTStorageAction.SIMULATE, access) ?: return HTStackMoveResult.failed()
        val simulatedRemain: STACK? = to.insert(simulatedExtract, HTStorageAction.SIMULATE, access)
        val simulatedAccepted: Int = amount - (simulatedRemain?.amount() ?: 0)
        if (simulatedAccepted == 0) return HTStackMoveResult.failed()
        val extracted: STACK = from.extract(simulatedAccepted, HTStorageAction.EXECUTE, access) ?: return HTStackMoveResult.failed()
        val remainder: STACK? = to.insert(extracted, HTStorageAction.EXECUTE, access)
        if (remainder != null) {
            val leftover: STACK? = from.insert(remainder, HTStorageAction.EXECUTE, access)
            if (leftover != null) {
                RagiumAPI.LOGGER.error("Stack slot $from did not accept leftover stack from $to! Voiding it.")
            }
        }
        return HTStackMoveResult.succeeded(remainder)
    }

    @JvmStatic
    fun <STACK : ImmutableStack<*, STACK>> canInsertStack(slot: HTStackSlot<STACK>, stack: STACK, exactMatch: Boolean): Boolean {
        val remainder: STACK? = slot.insert(stack, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
        return when (exactMatch) {
            true -> remainder == null
            false -> remainder == null || remainder.amount() < stack.amount()
        }
    }

    @JvmStatic
    fun <STACK : ImmutableStack<*, STACK>, INPUT : RecipeInput> canInsertStack(
        slot: HTStackSlot<STACK>,
        input: INPUT,
        level: Level,
        transform: (INPUT, HolderLookup.Provider) -> STACK?,
    ): Boolean {
        val stack: STACK = transform(input, level.registryAccess()) ?: return true
        return canInsertStack(slot, stack, true)
    }

    @JvmStatic
    fun <STACK : ImmutableStack<*, STACK>> shrinkStack(
        slot: HTStackSlot<STACK>,
        ingredient: ToIntFunction<STACK>,
        action: HTStorageAction,
    ): Int {
        val stackIn: STACK = slot.getStack() ?: return 0
        return slot.extract(ingredient.applyAsInt(stackIn), action, HTStorageAccess.INTERNAL)?.amount() ?: 0
    }

    @JvmStatic
    fun <STACK : ImmutableStack<*, STACK>> canShrinkStack(slot: HTStackSlot<STACK>, amount: Int, exactMatch: Boolean): Boolean {
        val extracted: Int = slot.extract(amount, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)?.amount() ?: return false
        return when (exactMatch) {
            true -> extracted == amount
            false -> extracted > 0
        }
    }

    @JvmStatic
    fun <STACK : ImmutableStack<*, STACK>> canShrinkStack(
        slot: HTStackSlot<STACK>,
        ingredient: ToIntFunction<STACK>,
        exactMatch: Boolean,
    ): Boolean {
        val amount: Int = slot.getStack()?.let(ingredient::applyAsInt) ?: return false
        val extracted: Int = slot.extract(amount, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)?.amount() ?: return false
        return when (exactMatch) {
            true -> extracted == amount
            false -> extracted > 0
        }
    }

    /**
     * @see net.neoforged.neoforge.items.ItemHandlerHelper.calcRedstoneFromInventory
     * @see mekanism.common.util.MekanismUtils.redstoneLevelFromContents
     */
    @JvmStatic
    fun <STACK : ImmutableStack<*, STACK>> calculateRedstoneLevel(views: Iterable<HTStackView<STACK>>): Int {
        var amountSum = 0
        var capacitySum = 0
        for (view: HTStackView<STACK> in views) {
            amountSum += view.getAmount()
            capacitySum += view.getCapacity(view.getStack())
        }
        return calculateRedstoneLevel(amountSum, capacitySum)
    }

    /**
     * @see mekanism.common.util.MekanismUtils.redstoneLevelFromContents
     */
    @JvmStatic
    fun calculateRedstoneLevel(amount: Int, capacity: Int): Int {
        val level: Float = when (capacity) {
            0 -> 0f
            else -> amount / capacity.toFloat()
        }
        return Mth.lerpDiscrete(level, Redstone.SIGNAL_NONE, Redstone.SIGNAL_MAX)
    }

    @JvmStatic
    fun calculateRedstoneLevel(view: HTAmountView<*>): Int =
        Mth.lerpDiscrete(view.getStoredLevelAsFloat(), Redstone.SIGNAL_NONE, Redstone.SIGNAL_MAX)

    //    Item    //

    @JvmStatic
    inline fun shrinkItemStack(
        slot: HTItemSlot,
        remainderGetter: (ImmutableItemStack) -> ItemStack,
        stackSetter: (ImmutableItemStack) -> Unit,
        amount: Int,
        action: HTStorageAction,
    ): Int {
        val stackIn: ImmutableItemStack = slot.getStack() ?: return 0
        if (action.execute) {
            stackIn
                .let(remainderGetter)
                .let(ItemStack::toImmutable)
                ?.let(stackSetter)
        }
        return slot.extract(amount, action, HTStorageAccess.INTERNAL)?.amount() ?: 0
    }

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
        val stackIn: ImmutableFluidStack? = tank.getStack()
        val firstFluid: ImmutableFluidStack? = when (stackIn == null) {
            true -> handler.drain(Int.MAX_VALUE, HTStorageAction.SIMULATE.toFluid())
            false -> handler.drain(stackIn.unwrap().copyWithAmount(Int.MAX_VALUE), HTStorageAction.SIMULATE.toFluid())
        }.toImmutable()
        if (firstFluid == null) {
            if (stackIn != null) {
                val filled: Int = handler.fill(stackIn.unwrap(), HTStorageAction.of(player.isCreative).toFluid())
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
            val remainder: ImmutableFluidStack? = tank.insert(firstFluid, HTStorageAction.SIMULATE, HTStorageAccess.MANUAL)
            val remainderAmount: Int = remainder?.amount() ?: 0
            val storedAmount: Int = firstFluid.amount()
            if (remainderAmount < storedAmount) {
                var filled = false
                val drained: ImmutableFluidStack? = handler
                    .drain(
                        firstFluid.unwrap().copyWithAmount(storedAmount - remainderAmount),
                        HTStorageAction.of(player.isCreative).toFluid(),
                    ).toImmutable()
                if (drained != null) {
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
                        tank.insert(drained, HTStorageAction.EXECUTE, HTStorageAccess.MANUAL)
                        return true
                    }
                }
            }
        }
        return false
    }

    @JvmStatic
    fun moveFluid(from: HTItemSlot, containerSetter: Consumer<ImmutableItemStack?>, to: HTFluidTank): Boolean {
        val stack: ImmutableItemStack = from.getStack() ?: return false
        if (!HTFluidCapabilities.hasCapability(stack)) return false
        val wrapper: HTFluidHandlerItemWrapper = HTFluidHandlerItemWrapper.create(stack.copyWithAmount(1)) ?: return false
        return moveFluid(from, containerSetter, wrapper, to)
    }

    @JvmStatic
    fun moveFluid(
        slot: HTItemSlot,
        containerSetter: Consumer<ImmutableItemStack?>,
        from: HTFluidHandlerItemWrapper,
        to: HTFluidTank,
    ): Boolean {
        val result: HTStackMoveResult<ImmutableFluidStack> = moveStack(from, to)
        if (result.succeeded) {
            val container: ImmutableItemStack? = from.container
            if (container != null) {
                if (container.amount() == 1) {
                    containerSetter.accept(container)
                } else {
                    slot.extract(1, HTStorageAction.EXECUTE, HTStorageAccess.MANUAL)
                }
            }
        }
        return result.succeeded
    }
}
