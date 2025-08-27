package hiiragi283.ragium.common.inventory.container

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.inventory.container.HTContainerWithContextMenu
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.items.IItemHandler

class HTUniversalBundleMenu(containerId: Int, inventory: Inventory, context: IItemHandler) :
    HTContainerWithContextMenu<IItemHandler>(
        RagiumMenuTypes.UNIVERSAL_BUNDLE,
        containerId,
        inventory,
        context,
    ) {
    init {
        // backpack slot
        for (i: Int in (0 until context.slots)) {
            addInputSlot(context, i, HTSlotHelper.getSlotPosX(i % 9), HTSlotHelper.getSlotPosY(i / 9))
        }
        // player inventory
        addPlayerInv(inventory)
    }

    override fun onOpen(player: Player) {
        super.onOpen(player)
        player.level().playSound(null, player.blockPosition(), SoundEvents.WOOL_PLACE, SoundSource.PLAYERS)
    }

    override fun onClose(player: Player) {
        super.onClose(player)
        player.level().playSound(null, player.blockPosition(), SoundEvents.WOOL_BREAK, SoundSource.PLAYERS)
    }
}
