package hiiragi283.ragium.client.screen

import hiiragi283.ragium.api.extension.fluidAmountText
import hiiragi283.ragium.api.extension.getSpriteAndColor
import hiiragi283.ragium.api.extension.id
import hiiragi283.ragium.api.extension.toFloatColor
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.Level
import net.minecraft.world.level.material.Fluid
import net.neoforged.fml.ModList
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

abstract class HTContainerScreen<T : AbstractContainerMenu>(menu: T, playerInventory: Inventory, title: Component) :
    AbstractContainerScreen<T>(menu, playerInventory, title) {
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

    protected val startX: Int
        get() = (width - imageWidth) / 2

    protected val startY: Int
        get() = (height - imageHeight) / 2

    protected fun getSlotPosX(index: Int): Int = 8 + index * 18

    protected fun getSlotPosY(index: Int): Int = 18 + index * 18

    protected fun drawFluid(
        guiGraphics: GuiGraphics,
        stack: FluidStack,
        x: Int,
        y: Int,
    ) {
        if (stack.isEmpty) return
        drawFluid(guiGraphics, stack.fluid, x, y)
    }

    protected fun drawFluid(
        guiGraphics: GuiGraphics,
        fluid: Fluid,
        x: Int,
        y: Int,
    ) {
        val (sprite: TextureAtlasSprite, color: Int) = fluid.getSpriteAndColor()
        val floatColor: Triple<Float, Float, Float> = toFloatColor(color)

        guiGraphics.blit(
            startX + getSlotPosX(x),
            startY + getSlotPosY(y),
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

    protected fun drawTooltip(
        x: Int,
        y: Int,
        mouseX: Int,
        mouseY: Int,
        action: () -> Unit,
    ) {
        val startX1: Int = startX + getSlotPosX(x)
        val startY1: Int = startY + getSlotPosY(y)
        val xRange: IntRange = (startX1..startX1 + 18)
        val yRange: IntRange = (startY1..startY1 + 18)
        if (mouseX in xRange && mouseY in yRange) {
            action()
        }
    }

    protected fun drawFluidTooltip(
        guiGraphics: GuiGraphics,
        stack: FluidStack,
        amount: Long,
        x: Int,
        y: Int,
        mouseX: Int,
        mouseY: Int,
    ) {
        drawTooltip(x, y, mouseX, mouseY) {
            if (stack.isEmpty) return@drawTooltip
            val fluidType: FluidType = stack.fluidType
            guiGraphics.renderComponentTooltip(
                font,
                buildList {
                    // Tooltips
                    add(fluidType.description)
                    // Fluid Amount
                    add(fluidAmountText(amount).withStyle(ChatFormatting.GRAY))
                    // Mod Name
                    val fluidId: ResourceLocation = stack.fluidHolder.id ?: return@buildList
                    ModList.get().getModFileById(fluidId.namespace)?.moduleName()?.let { name: String ->
                        add(Component.literal(name).withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC))
                    }
                },
                mouseX,
                mouseY,
            )
        }
    }

    protected fun drawEnergyTooltip(
        guiGraphics: GuiGraphics,
        key: ResourceKey<Level>,
        x: Int,
        y: Int,
        mouseX: Int,
        mouseY: Int,
    ) {
        drawTooltip(x, y, mouseX, mouseY) {
            /*CLIENT_NETWORK_MAP[key]?.let {
                context.drawTooltip(
                    textRenderer,
                    buildList {
                        add(
                            Text
                                .translatable(
                                    RagiumTranslationKeys.MACHINE_NETWORK_ENERGY,
                                    longText(it.amount).formatted(Formatting.RED),
                                ).formatted(Formatting.GRAY),
                        )
                    },
                    mouseX,
                    mouseY,
                )
            }*/
        }
    }
}
