package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.extension.setShaderColor
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.storage.HTAccessConfiguration
import hiiragi283.ragium.client.network.HTUpdateAccessConfigPayload
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.inventory.HTAccessConfigurationMenu
import hiiragi283.ragium.common.util.HTPacketHelper
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

@OnlyIn(Dist.CLIENT)
class HTAccessConfigurationScreen(menu: HTAccessConfigurationMenu, inventory: Inventory, title: Component) :
    HTContainerScreen<HTAccessConfigurationMenu>(
        menu,
        inventory,
        title,
    ) {
    private val blockEntity: HTMachineBlockEntity = menu.context

    override val texture: ResourceLocation? = null

    private fun addButton(side: Direction, x: Int, y: Int): ConfigButton = ConfigButton(
        blockEntity.blockPos,
        side,
        startX + HTSlotHelper.getSlotPosX(x) - 4,
        startY + HTSlotHelper.getSlotPosY(y) - 4,
        Component.empty(),
    ).apply {
        tooltip = blockEntity.getAccessConfiguration(side).let(::createTooltip)
    }
    
    private fun createTooltip(config: HTAccessConfiguration): Tooltip = config.translationKey.let(Component::translatable).let(Tooltip::create)

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
            val value: HTAccessConfiguration = blockEntity.getAccessConfiguration(side).nextEntry
            tooltip = createTooltip(value)
            // Client update
            blockEntity.setAccessConfiguration(side, value)
            // Server update
            HTPacketHelper.sendToServer(HTUpdateAccessConfigPayload(pos, side, value))
        }

        override fun renderWidget(
            guiGraphics: GuiGraphics,
            mouseX: Int,
            mouseY: Int,
            partialTick: Float,
        ) {
            if (!visible) return
            // Render background
            setShaderColor(guiGraphics, blockEntity.getAccessConfiguration(side).color) {
                guiGraphics.blitSprite(SPRITES.get(this.active, this.isHoveredOrFocused), x, y, width, height)
            }
            // Render icon
            val state: BlockState = Minecraft.getInstance().level?.getBlockState(pos.relative(side)) ?: return
            guiGraphics.renderItem(ItemStack(state.block), x + 4, y + 4)
        }
    }
}
