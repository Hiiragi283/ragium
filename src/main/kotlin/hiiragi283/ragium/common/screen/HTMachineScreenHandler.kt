package hiiragi283.ragium.common.screen

import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.data.Insets
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class HTMachineScreenHandler(syncId: Int, playerInv: PlayerInventory, ctx: ScreenHandlerContext = ScreenHandlerContext.EMPTY) :
    SyncedGuiDescription(
        RagiumScreenHandlerTypes.MACHINE,
        syncId,
        playerInv,
        getBlockInventory(ctx, 7),
        getBlockPropertyDelegate(ctx),
    ) {
    init {
        val root = WGridPanel()
        setRootPanel(root)
        root.setInsets(Insets.ROOT_PANEL)
        // input slots
        root.add(WItemSlot.of(blockInventory, 0), 1, 1)
        root.add(WItemSlot.of(blockInventory, 1), 2, 1)
        root.add(WItemSlot.of(blockInventory, 2), 3, 1)
        // catalyst slot
        root.add(WItemSlot.of(blockInventory, 3), 4, 2)
        // output slots
        root.add(WItemSlot.of(blockInventory, 4).setInsertingAllowed(false), 5, 1)
        root.add(WItemSlot.of(blockInventory, 5).setInsertingAllowed(false), 6, 1)
        root.add(WItemSlot.of(blockInventory, 6).setInsertingAllowed(false), 7, 1)
        // player inventory
        root.add(createPlayerInventoryPanel(), 0, 3)
        // right arrow
        /*root.add(
            WBar(
                Identifier.of("textures/block/white_wool.png"),
                Identifier.of("textures/block/red_wool.png"),
                0,
                1,
                WBar.Direction.RIGHT,
            ),
            4,
            1,
        )*/
        root.validate(this)
    }
}
