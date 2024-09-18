package hiiragi283.ragium.common.screen

import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.data.Insets
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class HTBurningBoxScreenHandler(syncId: Int, playerInv: PlayerInventory, ctx: ScreenHandlerContext = ScreenHandlerContext.EMPTY) :
    SyncedGuiDescription(
        RagiumScreenHandlerTypes.BURNING_BOX,
        syncId,
        playerInv,
        getBlockInventory(ctx, 2),
        getBlockPropertyDelegate(ctx),
    ) {
    init {
        val root = WGridPanel()
        setRootPanel(root)
        root.setInsets(Insets.ROOT_PANEL)
        // fuel slot
        root.add(WItemSlot.of(blockInventory, 0), 4, 1)
        // ash slot
        root.add(WItemSlot.of(blockInventory, 1), 4, 2)
        // player inventory
        root.add(createPlayerInventoryPanel(), 0, 3)
        root.validate(this)
    }
}
