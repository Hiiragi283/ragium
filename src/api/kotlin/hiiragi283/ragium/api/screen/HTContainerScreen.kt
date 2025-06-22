package hiiragi283.ragium.api.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.fluidCapacityText
import hiiragi283.ragium.api.extension.getSpriteAndColor
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.extension.intText
import hiiragi283.ragium.api.extension.toFloatColor
import hiiragi283.ragium.api.inventory.HTContainerMenu
import hiiragi283.ragium.api.inventory.HTSlotPos
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.neoforged.fml.ModList
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforgespi.language.IModInfo

abstract class HTContainerScreen<T : HTContainerMenu>(menu: T, inventory: Inventory, title: Component) :
    AbstractContainerScreen<T>(menu, inventory, title) {
    override fun render(
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    ) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        renderTooltip(guiGraphics, mouseX, mouseY)
    }

    //    Extensions    //

    protected val startX: Int get() = (width - imageWidth) / 2

    protected val startY: Int get() = (height - imageHeight) / 2

    protected fun renderFluid(
        guiGraphics: GuiGraphics,
        stack: FluidStack,
        x: Double,
        y: Double,
    ) {
        if (stack.isEmpty) return
        val (sprite: TextureAtlasSprite, color: Int) = stack.getSpriteAndColor()
        val floatColor: Triple<Float, Float, Float> = toFloatColor(color)

        guiGraphics.blit(
            startX + HTSlotPos.getSlotPosX(x),
            startY + HTSlotPos.getSlotPosY(y),
            0,
            sprite.contents().width(),
            sprite.contents().height(),
            sprite,
            floatColor.first,
            floatColor.second,
            floatColor.third,
            1.0f,
        )
    }

    protected fun renderTooltip(
        x: Int,
        y: Int,
        mouseX: Int,
        mouseY: Int,
        rangeX: Int = 18,
        rangeY: Int = 18,
        action: () -> Unit,
    ) {
        val startX1: Int = startX + x
        val startY1: Int = startY + y
        val xRange: IntRange = (startX1..startX1 + rangeX)
        val yRange: IntRange = (startY1..startY1 + rangeY)
        if (mouseX in xRange && mouseY in yRange) {
            action()
        }
    }

    protected fun renderFluidTooltip(
        guiGraphics: GuiGraphics,
        stack: FluidStack,
        capacity: Int,
        x: Double,
        y: Double,
        mouseX: Int,
        mouseY: Int,
    ) {
        renderTooltip(HTSlotPos.getSlotPosX(x), HTSlotPos.getSlotPosY(y), mouseX, mouseY) {
            guiGraphics.renderComponentTooltip(
                font,
                buildList {
                    // Tooltips
                    add(
                        when {
                            stack.isEmpty -> Component.literal("Empty")
                            else -> stack.hoverName
                        },
                    )
                    // Fluid Amount
                    add(intText(stack.amount).withStyle(ChatFormatting.GRAY))
                    // Fluid Capacity
                    add(fluidCapacityText(capacity).withStyle(ChatFormatting.GRAY))
                    // Mod Name
                    val firstMod: IModInfo = ModList
                        .get()
                        .getModFileById(stack.fluidHolder.idOrThrow.namespace)
                        .mods
                        .firstOrNull() ?: return@buildList
                    add(Component.literal(firstMod.displayName).withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC))
                },
                mouseX,
                mouseY,
            )
        }
    }

    protected fun renderEnergyTooltip(
        guiGraphics: GuiGraphics,
        x: Int,
        y: Int,
        mouseX: Int,
        mouseY: Int,
    ) {
        val network: IEnergyStorage = RagiumAPI
            .getInstance()
            .getEnergyNetworkManager()
            .getNetworkFromKey(menu.level.dimension())
            ?: return
        renderTooltip(x, y, mouseX, mouseY, rangeY = 18 * 3) {
            guiGraphics.renderTooltip(
                font,
                Component
                    .literal("${network.energyStored}/${network.maxEnergyStored} FE")
                    .withStyle(ChatFormatting.GRAY),
                mouseX,
                mouseY,
            )
        }
    }
}
