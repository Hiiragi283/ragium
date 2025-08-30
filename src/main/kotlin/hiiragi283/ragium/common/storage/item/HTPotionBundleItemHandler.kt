package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.inventory.HTMenuCallback
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.inventory.slot.HTContainerItemSlot
import hiiragi283.ragium.api.storage.item.HTComponentItemHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.MutableDataComponentHolder

class HTPotionBundleItemHandler(parent: MutableDataComponentHolder, size: Int) :
    HTComponentItemHandler(parent, size),
    HTMenuCallback {
    override fun createSlot(slot: Int): HTItemSlot = PotionSlot(slot)

    override fun openMenu(player: Player) {
        player.level().playSound(null, player.blockPosition(), SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS)
    }

    override fun closeMenu(player: Player) {
        player.level().playSound(null, player.blockPosition(), SoundEvents.BOTTLE_EMPTY, SoundSource.PLAYERS)
    }

    private inner class PotionSlot(private val slot: Int) : HTItemSlot {
        override fun getStack(): ItemStack = getStackForContents(contents, slot)

        override fun setStack(stack: ItemStack) {
            updateContents(contents, stack, slot)
        }

        override fun getLimit(stack: ItemStack): Int = 1

        override fun isItemValid(stack: ItemStack): Boolean = stack.isEmpty || stack.`is`(Items.POTION)

        override fun createContainerSlot(): Slot =
            HTContainerItemSlot(this, HTSlotHelper.getSlotPosX(slot % 9), HTSlotHelper.getSlotPosY(slot / 9), null)

        override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        }

        override fun onContentsChanged() {}
    }
}
