package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.extension.setShaderColor
import hiiragi283.ragium.api.gui.component.HTProgressWidget
import hiiragi283.ragium.api.gui.screen.HTDefinitionContainerScreen
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.storage.HTTransferIO
import hiiragi283.ragium.client.network.HTTransferIOUpdatePayload
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotConfigurationMenu
import hiiragi283.ragium.common.storage.HTTransferIOCache
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.gui.widget.ExtendedButton
import net.neoforged.neoforge.network.PacketDistributor

@OnlyIn(Dist.CLIENT)
class HTSlotConfigurationScreen(menu: HTSlotConfigurationMenu, inventory: Inventory, title: Component) :
    HTDefinitionContainerScreen<HTSlotConfigurationMenu>(
        menu,
        inventory,
        title,
    ) {
    override val texture: ResourceLocation? = null

    override fun addProgressBar(consumer: (HTProgressWidget) -> Unit) {
    }

    private fun addButton(direction: Direction, x: Int, y: Int): IOButton = IOButton(
        menu.pos,
        direction,
        startX + HTSlotHelper.getSlotPosX(x) - 4,
        startY + HTSlotHelper.getSlotPosY(y) - 4,
        Component.empty(),
    ).apply {
        getTransferIO(direction)?.description?.let(Tooltip::create)?.let(this::setTooltip)
    }

    private fun getTransferCache(): HTTransferIOCache? = (menu.blockEntity as? HTMachineBlockEntity)?.transferIOCache

    private fun getTransferIO(direction: Direction): HTTransferIO? = getTransferCache()?.get(direction)

    override fun init() {
        super.init()

        addRenderableWidget(addButton(Direction.DOWN, 4, 4))
        addRenderableWidget(addButton(Direction.EAST, 6, 2))
        addRenderableWidget(addButton(Direction.NORTH, 4, 2))
        addRenderableWidget(addButton(Direction.SOUTH, 6, 4))
        addRenderableWidget(addButton(Direction.UP, 4, 0))
        addRenderableWidget(addButton(Direction.WEST, 2, 2))

        removeWidget(energyWidget)
    }

    override fun renderLabels(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
        guiGraphics.drawString(font, title, titleLabelX, titleLabelY, 4210752, false)
    }

    //    Button    //

    private inner class IOButton(
        private val pos: BlockPos,
        private val direction: Direction,
        x: Int,
        y: Int,
        message: Component,
    ) : ExtendedButton(x, y, 24, 24, message, {}) {
        override fun onPress() {
            super.onPress()
            val transferIO1: HTTransferIO = getTransferIO(direction)?.nextEntry ?: return
            tooltip = Tooltip.create(transferIO1.description)
            getTransferCache()?.set(direction, transferIO1)
            PacketDistributor.sendToServer(HTTransferIOUpdatePayload(pos, direction, transferIO1))
        }

        override fun renderWidget(
            guiGraphics: GuiGraphics,
            mouseX: Int,
            mouseY: Int,
            partialTick: Float,
        ) {
            if (!visible) return
            // Render background
            val transferIO: HTTransferIO = getTransferIO(direction) ?: return
            setShaderColor(guiGraphics, transferIO.color) {
                guiGraphics.blitSprite(SPRITES.get(this.active, this.isHoveredOrFocused), x, y, width, height)
            }
            // Render icon
            val minecraft: Minecraft = Minecraft.getInstance()
            val state: BlockState = minecraft.level?.getBlockState(pos.relative(direction)) ?: return
            guiGraphics.renderItem(ItemStack(state.block), x + 4, y + 4)
        }
    }
}
