package hiiragi283.ragium.common.screen

import hiiragi283.ragium.api.machine.HTMachinePacket
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WTabPanel
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.Items
import net.minecraft.screen.ScreenHandlerContext

class HTLargeMachineScreenHandler(
    syncId: Int,
    playerInv: PlayerInventory,
    packet: HTMachinePacket,
    ctx: ScreenHandlerContext = ScreenHandlerContext.EMPTY,
) : HTProcessorScreenHandlerBase(
        RagiumScreenHandlerTypes.LARGE_MACHINE,
        syncId,
        playerInv,
        packet,
        HTMachineRecipe.SizeType.LARGE,
        ctx,
    ) {
    override fun initMainPanel(rootTab: WTabPanel) {
        val mainPanel: WGridPanel = createPanel()
        // input slots
        addItemSlot(mainPanel, 0, 1, 1, true)
        addItemSlot(mainPanel, 1, 2, 1, true)
        addItemSlot(mainPanel, 2, 3, 1, true)
        // catalyst slot
        addItemSlot(mainPanel, 3, 4, 2, true)
        // output slots
        addItemSlot(mainPanel, 4, 5, 1, false)
        addItemSlot(mainPanel, 5, 6, 1, false)
        addItemSlot(mainPanel, 6, 7, 1, false)
        // Add to tab
        completePanel(mainPanel)
        rootTab.add(mainPanel) {
            it.icon(ItemIcon(machineType.createItemStack(tier)))
            it.tooltip(machineText)
        }
    }

    override fun initFluidPanel(rootTab: WTabPanel) {
        val fluidPanel: WGridPanel = createPanel()
        // input slots
        addFluidSlot(fluidPanel, 0, 2, 1)
        addFluidSlot(fluidPanel, 1, 3, 1)
        // output slots
        addFluidSlot(fluidPanel, 2, 5, 1)
        addFluidSlot(fluidPanel, 3, 6, 1)
        // Add to tab
        completePanel(fluidPanel)
        rootTab.add(fluidPanel) {
            it.icon(ItemIcon(Items.BUCKET))
            it.tooltip(machineText)
        }
    }
}
