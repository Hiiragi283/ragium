package hiiragi283.ragium.common.screen

import hiiragi283.ragium.api.machine.HTMachinePacket
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.cotton.gui.widget.WTabPanel
import io.github.cottonmc.cotton.gui.widget.data.Insets
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.Items
import net.minecraft.screen.ScreenHandlerContext

class HTSimpleProcessorScreenHandler(
    syncId: Int,
    playerInv: PlayerInventory,
    packet: HTMachinePacket,
    ctx: ScreenHandlerContext = ScreenHandlerContext.EMPTY,
) : HTProcessorScreenHandlerBase(
        RagiumScreenHandlerTypes.SIMPLE_PROCESSOR,
        syncId,
        playerInv,
        packet,
        HTMachineRecipe.SizeType.SIMPLE,
        ctx,
    ) {
    override fun initMainPanel(rootTab: WTabPanel) {
        val mainPanel = WGridPanel()
        mainPanel.setInsets(Insets.ROOT_PANEL)
        mainPanel.add(WLabel(machineText), 0, 0)
        // input slots
        addItemSlot(mainPanel, 0, 2, 1, true)
        addItemSlot(mainPanel, 1, 3, 1, true)
        // catalyst slot
        addItemSlot(mainPanel, 2, 4, 2, true)
        // output slots
        addItemSlot(mainPanel, 3, 5, 1, false)
        addItemSlot(mainPanel, 4, 6, 1, false)
        // Add to tab
        completePanel(mainPanel)
        rootTab.add(mainPanel) {
            it.icon(ItemIcon(machineType.createItemStack(tier)))
            it.tooltip(machineText)
        }
    }

    override fun initFluidPanel(rootTab: WTabPanel) {
        val fluidPanel = WGridPanel()
        fluidPanel.setInsets(Insets.ROOT_PANEL)
        fluidPanel.add(WLabel(machineText), 0, 0)
        // input slots
        addFluidSlot(fluidPanel, 0, 3, 1)
        // output slots
        addFluidSlot(fluidPanel, 1, 5, 1)
        // Add to tab
        completePanel(fluidPanel)
        rootTab.add(fluidPanel) {
            it.icon(ItemIcon(Items.BUCKET))
            it.tooltip(machineText)
        }
    }
}
