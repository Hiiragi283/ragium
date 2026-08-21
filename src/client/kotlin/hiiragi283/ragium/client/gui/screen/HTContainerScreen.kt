package hiiragi283.ragium.client.gui.screen

import hiiragi283.lib.gui.HTGuiAccess
import hiiragi283.lib.gui.sync.HTChangeType
import hiiragi283.lib.gui.sync.HTSyncType
import hiiragi283.lib.gui.sync.HTSyncablePayload
import hiiragi283.lib.gui.sync.HTSyncableSlot
import hiiragi283.lib.text.Text
import hiiragi283.ragium.gui.menu.HTContainerMenu
import hiiragi283.ragium.network.HTUpdateMenuPacket
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.core.RegistryAccess
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.client.network.ClientPacketDistributor

abstract class HTContainerScreen<MENU : HTContainerMenu<*>>(menu: MENU, inventory: Inventory, title: Text, width: Int, height: Int) :
    AbstractContainerScreen<MENU>(menu, inventory, title, width, height),
    HTGuiAccess {
    override fun containerTick() {
        super.containerTick()
        val player: Player = menu.inventory.player
        val access: RegistryAccess = player.registryAccess()
        HTUpdateMenuPacket
            .create(
                menu.containerId,
                menu.trackedSlots
                    .mapIndexedNotNull { index: Int, (slot: HTSyncableSlot, syncType: HTSyncType) ->
                        if (!syncType.allowC2S) return@mapIndexedNotNull null
                        val changeType: HTChangeType = slot.getChange() ?: return@mapIndexedNotNull null
                        val payload: HTSyncablePayload = slot.createPayload(access, changeType) ?: return@mapIndexedNotNull null
                        index to payload
                    }
                    // .onEach { (index: Int, payload: HTSyncablePayload) -> HiiragiCoreAPI.LOGGER.debug("Index: {}, Payload: {}", index, payload) }
                    .toMap(),
            )?.let(ClientPacketDistributor::sendToServer)
    }

    //    HTGuiAccess    //

    override val carried: ItemStack
        get() = menu.carried

    //    Extensions    //

    val startX: Int get() = (width - imageWidth) / 2

    val startY: Int get() = (height - imageHeight) / 2
}
