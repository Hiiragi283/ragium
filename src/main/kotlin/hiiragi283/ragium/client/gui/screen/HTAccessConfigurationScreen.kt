package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.client.network.HTUpdateAccessConfigPayload
import hiiragi283.ragium.client.util.HTSpriteRenderHelper
import hiiragi283.ragium.common.block.entity.HTConfigurableBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.inventory.container.HTAccessConfigurationMenu
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.gui.widget.ExtendedButton
import net.neoforged.neoforge.network.PacketDistributor

@OnlyIn(Dist.CLIENT)
class HTAccessConfigurationScreen(menu: HTAccessConfigurationMenu, inventory: Inventory, title: Component) :
    HTContainerScreen<HTAccessConfigurationMenu>(
        null,
        menu,
        inventory,
        title,
    ) {
    private val blockEntity: HTConfigurableBlockEntity = menu.context

    private fun addButton(side: Direction, x: Int, y: Int): ConfigButton = ConfigButton(
        blockEntity.blockPos,
        side,
        startX + HTSlotHelper.getSlotPosX(x) - 4,
        startY + HTSlotHelper.getSlotPosY(y) - 4,
        Component.empty(),
    ).apply {
        tooltip = blockEntity.getAccessConfig(side).let(::createTooltip)
    }

    private fun createTooltip(config: HTAccessConfig): Tooltip = config.translate().let(Tooltip::create)

    override fun init() {
        super.init()

        addRenderableWidget(addButton(Direction.DOWN, 4, 4))
        addRenderableWidget(addButton(Direction.EAST, 6, 2))
        addRenderableWidget(addButton(Direction.NORTH, 4, 2))
        addRenderableWidget(addButton(Direction.SOUTH, 6, 4))
        addRenderableWidget(addButton(Direction.UP, 4, 0))
        addRenderableWidget(addButton(Direction.WEST, 2, 2))
    }

    override fun renderLabels(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
        guiGraphics.drawString(font, title, titleLabelX, titleLabelY, 4210752, false)
    }

    //    Button    //

    private inner class ConfigButton(
        private val pos: BlockPos,
        private val side: Direction,
        x: Int,
        y: Int,
        message: Component,
    ) : ExtendedButton(x, y, 24, 24, message, {}) {
        override fun onPress() {
            super.onPress()
            val value: HTAccessConfig = blockEntity.getAccessConfig(side).nextEntry
            tooltip = createTooltip(value)
            // Client update
            blockEntity.setAccessConfig(side, value)
            // Server update
            PacketDistributor.sendToServer(HTUpdateAccessConfigPayload(pos, side, value))
        }

        override fun renderWidget(
            guiGraphics: GuiGraphics,
            mouseX: Int,
            mouseY: Int,
            partialTick: Float,
        ) {
            if (!visible) return
            // Render background
            HTSpriteRenderHelper.setShaderColor(guiGraphics, blockEntity.getAccessConfig(side).color) {
                guiGraphics.blitSprite(SPRITES.get(this.active, this.isHoveredOrFocused), x, y, width, height)
            }
            // Render icon
            val state: BlockState = Minecraft.getInstance().level?.getBlockState(pos.relative(side)) ?: return
            guiGraphics.renderItem(ItemStack(state.block), x + 4, y + 4)
        }
    }
}
