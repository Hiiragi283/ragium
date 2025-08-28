package hiiragi283.ragium.client.gui.screen

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.gui.component.HTNumberEditBox
import hiiragi283.ragium.api.gui.screen.HTFluidScreen
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.item.component.HTTeleportPos
import hiiragi283.ragium.client.network.HTTelepadUpdatePacket
import hiiragi283.ragium.common.block.entity.device.HTTelepadBlockentity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.client.gui.components.EditBox
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.network.PacketDistributor
import org.lwjgl.glfw.GLFW

class HTTelepadScreen(menu: HTBlockEntityContainerMenu<HTTelepadBlockentity>, inventory: Inventory, title: Component) :
    HTBlockEntityContainerScreen<HTTelepadBlockentity>(
        menu,
        inventory,
        title,
    ),
    HTFluidScreen {
    companion object {
        @JvmField
        val LOGGER = LogUtils.getLogger()
    }

    override val texture: ResourceLocation = RagiumAPI.id("textures/gui/container/telepad.png")

    private lateinit var fluidWidget: HTFluidWidget

    private lateinit var editBoxX: HTNumberEditBox<Int>
    private lateinit var editBoxY: HTNumberEditBox<Int>
    private lateinit var editBoxZ: HTNumberEditBox<Int>
    private lateinit var editBoxDim: EditBox

    override fun init() {
        super.init()
        // fluid
        fluidWidget =
            addRenderableWidget(createFluidWidget(0, HTSlotHelper.getSlotPosX(0), HTSlotHelper.getSlotPosY(0)))
        // x
        editBoxX = setupNumberBox(0)
        // y
        editBoxY = setupNumberBox(1)
        // z
        editBoxZ = setupNumberBox(2)
        // dimension
        editBoxDim = setupEditBox(HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosX(3))

        val teleportPos: HTTeleportPos = menu.context.teleportPos ?: return
        editBoxX.number = teleportPos.x
        editBoxY.number = teleportPos.y
        editBoxZ.number = teleportPos.z
        editBoxDim.value = teleportPos.dimension.location().toString()
    }

    /**
     * @see [net.minecraft.client.gui.screens.inventory.AnvilScreen]
     */
    private fun setupNumberBox(y: Int): HTNumberEditBox<Int> {
        val editBox: HTNumberEditBox<Int> = addRenderableWidget(
            HTNumberEditBox(
                { value: String -> value.toIntOrNull() ?: 0 },
                font,
                startX + HTSlotHelper.getSlotPosX(3),
                startY + HTSlotHelper.getSlotPosY(y) - (4 * y),
                18 * 3,
            ),
        )
        editBox.setCanLoseFocus(true)
        editBox.setTextColor(-1)
        editBox.setTextColorUneditable(-1)
        editBox.setMaxLength(10)
        return editBox
    }

    private fun setupEditBox(x: Int, y: Int): EditBox {
        val editBox: EditBox = addRenderableWidget(
            EditBox(
                font,
                startX + x,
                startY + y - 4 * 4,
                18 * 3,
                12,
                Component.empty(),
            ),
        )
        editBox.setCanLoseFocus(true)
        editBox.setTextColor(-1)
        editBox.setTextColorUneditable(-1)
        editBox.setMaxLength(50)
        return editBox
    }

    override fun setInitialFocus() {
        setInitialFocus(editBoxX)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean = when (keyCode) {
        GLFW.GLFW_KEY_ESCAPE -> super.keyPressed(keyCode, scanCode, modifiers)
        else -> when {
            editBoxX.isFocused -> editBoxX.keyPressed(keyCode, scanCode, modifiers)
            editBoxY.isFocused -> editBoxY.keyPressed(keyCode, scanCode, modifiers)
            editBoxZ.isFocused -> editBoxZ.keyPressed(keyCode, scanCode, modifiers)
            editBoxDim.isFocused -> editBoxDim.keyPressed(keyCode, scanCode, modifiers)
            else -> super.keyPressed(keyCode, scanCode, modifiers)
        }
    }

    override fun removed() {
        syncTeleportPos()
        super.removed()
    }

    private fun syncTeleportPos() {
        val x: Int = editBoxX.number
        val y: Int = editBoxY.number
        val z: Int = editBoxZ.number
        val id: ResourceLocation = editBoxDim.value.let(ResourceLocation::tryParse) ?: return
        val dim: ResourceKey<Level> = ResourceKey.create(Registries.DIMENSION, id)
        val teleportPos = HTTeleportPos(dim, x, y, z)
        val blockEntity: HTTelepadBlockentity = menu.context
        blockEntity.updateDestination(teleportPos)
        PacketDistributor.sendToServer(HTTelepadUpdatePacket(blockEntity.blockPos, teleportPos))
    }

    //    HTFluidScreen    //

    override fun setFluidStack(index: Int, stack: FluidStack) {
        fluidWidget.stack = stack
    }

    override fun iterator(): Iterator<HTFluidWidget> = listOf(fluidWidget).iterator()
}
