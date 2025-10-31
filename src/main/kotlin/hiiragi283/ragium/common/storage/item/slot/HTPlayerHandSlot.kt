package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

class HTPlayerHandSlot(private val player: Player, private val hand: InteractionHand) :
    HTItemSlot.Basic(),
    HTContentListener.Empty,
    HTValueSerializable.Empty {
    override fun isValid(stack: ImmutableItemStack): Boolean {
        val equipmentSlot: EquipmentSlot = when (hand) {
            InteractionHand.MAIN_HAND -> EquipmentSlot.MAINHAND
            InteractionHand.OFF_HAND -> EquipmentSlot.OFFHAND
        }
        return stack.unwrap().canEquip(equipmentSlot, player)
    }

    override fun getStack(): ImmutableItemStack? = player.getItemInHand(hand).toImmutable()

    override fun getCapacity(stack: ImmutableItemStack?): Int = HTItemSlot.getMaxStackSize(stack)

    override fun setStack(stack: ImmutableItemStack?) {
        setStackUnchecked(stack, true)
    }

    fun setStackUnchecked(stack: ImmutableItemStack?, validate: Boolean = false) {
        if (stack == null) {
            if (this.getStack() == null) return
            player.setItemInHand(hand, ItemStack.EMPTY)
        } else if (!validate || isValid(stack)) {
            player.setItemInHand(hand, stack.unwrap())
        } else {
            error("Invalid stack for hand: $stack ${stack.componentsPatch()}")
        }
    }

    override fun updateAmount(stack: ImmutableItemStack, amount: Int) {
        if (isSameStack(stack)) {
            player.getItemInHand(hand).count = amount
        }
    }
}
