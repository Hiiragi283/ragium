package hiiragi283.ragium.common.screen

import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.data.Insets
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class HTAlchemicalInfuserHandler(syncId: Int, playerInv: PlayerInventory, ctx: ScreenHandlerContext = ScreenHandlerContext.EMPTY) :
    SyncedGuiDescription(
        RagiumScreenHandlerTypes.ALCHEMICAL_INFUSER,
        syncId,
        playerInv,
        getBlockInventory(ctx, 5),
        getBlockPropertyDelegate(ctx),
    ) {
    init {
        val root = WGridPanel()
        setRootPanel(root)
        root.setInsets(Insets.ROOT_PANEL)
        // input slots
        root.add(WItemSlot.of(blockInventory, 0), 2, 1)
        root.add(WItemSlot.of(blockInventory, 1), 6, 1)
        root.add(WItemSlot.of(blockInventory, 2), 2, 5)
        root.add(WItemSlot.of(blockInventory, 3), 6, 5)
        // output slots
        root.add(WItemSlot.of(blockInventory, 4).setInsertingAllowed(false), 4, 3)
        // player inventory
        root.add(createPlayerInventoryPanel(), 0, 6)
        root.validate(this)
    }
}
