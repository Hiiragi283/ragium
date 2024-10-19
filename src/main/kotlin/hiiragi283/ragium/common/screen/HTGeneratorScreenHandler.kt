package hiiragi283.ragium.common.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.machineInventory
import hiiragi283.ragium.api.machine.HTMachineEntity
import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WBar
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.data.Insets
import net.minecraft.block.entity.AbstractFurnaceBlockEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class HTGeneratorScreenHandler(syncId: Int, playerInv: PlayerInventory, ctx: ScreenHandlerContext = ScreenHandlerContext.EMPTY) :
    SyncedGuiDescription(
        RagiumScreenHandlerTypes.GENERATOR,
        syncId,
        playerInv,
        ctx.machineInventory(2),
        getBlockPropertyDelegate(ctx, HTMachineEntity.MAX_PROPERTIES),
    ) {
    init {
        val root = WGridPanel()
        setRootPanel(root)
        root.setInsets(Insets.ROOT_PANEL)
        // input slot
        root.add(WItemSlot.of(blockInventory, 0).setInputFilter(AbstractFurnaceBlockEntity::canUseAsFuel), 3, 1)
        // output slot
        root.add(WItemSlot.of(blockInventory, 1).setInsertingAllowed(false), 5, 1)
        // player inventory
        root.add(createPlayerInventoryPanel(), 0, 3)
        // right arrow
        root.add(
            WBar(
                RagiumAPI.id("textures/gui/progress_base.png"),
                RagiumAPI.id("textures/gui/progress_bar.png"),
                0,
                1,
                WBar.Direction.RIGHT,
            ),
            4,
            1,
        )
        root.validate(this)
    }
}
