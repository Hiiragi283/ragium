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
        return stack.stack.canEquip(equipmentSlot, player)
    }

    override fun getStack(): ImmutableItemStack? = player.getItemInHand(hand).toImmutable()

    override fun getCapacity(stack: ImmutableItemStack?): Int = HTItemSlot.getMaxStackSize(stack)

    override fun setStack(stack: ImmutableItemStack?) {
        player.setItemInHand(hand, stack?.stack ?: ItemStack.EMPTY)
    }

    override fun updateCount(stack: ImmutableItemStack?, amount: Int) {
        if (isSameStack(stack)) {
            player.getItemInHand(hand).count = amount
        }
    }
}
