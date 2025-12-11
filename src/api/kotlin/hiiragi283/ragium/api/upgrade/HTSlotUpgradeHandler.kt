package hiiragi283.ragium.api.upgrade

import hiiragi283.ragium.api.stack.ImmutableComponentStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.item.HTItemSlot

interface HTSlotUpgradeHandler : HTUpgradeHandler {
    fun getUpgradeSlots(): List<HTItemSlot>

    override fun getUpgrades(): List<ImmutableItemStack> = getUpgradeSlots().mapNotNull(HTItemSlot::getStack)

    override fun isValidUpgrade(upgrade: ImmutableItemStack, existing: List<ImmutableItemStack>): Boolean =
        existing.none { stack: ImmutableItemStack -> ImmutableComponentStack.matches(stack, upgrade) }
}
