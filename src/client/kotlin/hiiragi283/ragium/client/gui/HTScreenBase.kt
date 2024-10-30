package hiiragi283.ragium.client.gui

import hiiragi283.ragium.api.extension.getModName
import hiiragi283.ragium.api.extension.toFloatColor
import hiiragi283.ragium.api.screen.HTScreenHandlerBase
import hiiragi283.ragium.client.util.getSpriteAndColor
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.texture.Sprite
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.fluid.Fluid
import net.minecraft.registry.Registries
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
abstract class HTScreenBase<T : HTScreenHandlerBase>(handler: T, inventory: PlayerInventory, title: Text) :
    HandledScreen<T>(handler, inventory, title) {
    override fun render(
        context: DrawContext,
        mouseX: Int,
        mouseY: Int,
        delta: Float,
    ) {
        super.render(context, mouseX, mouseY, delta)
        drawMouseoverTooltip(context, mouseX, mouseY)
    }

    //    Extensions    //

    protected val startX: Int
        get() = (width - backgroundWidth) / 2

    protected val startY: Int
        get() = (height - backgroundHeight) / 2

    protected fun getSlotPosX(index: Int): Int = 8 + index * 18

    protected fun getSlotPosY(index: Int): Int = 18 + index * 18

    protected fun drawFluid(
        context: DrawContext,
        variant: FluidVariant,
        x: Int,
        y: Int,
    ) {
        if (variant.isBlank) return
        drawFluid(context, variant.fluid, x, y)
    }

    protected fun drawFluid(
        context: DrawContext,
        fluid: Fluid,
        x: Int,
        y: Int,
    ) {
        val (sprite: Sprite, color: Int) = fluid.getSpriteAndColor() ?: return
        val floatColor: Triple<Float, Float, Float> = toFloatColor(color)

        context.drawSprite(
            startX + getSlotPosX(x),
            startY + getSlotPosY(y),
            0,
            sprite.contents.width,
            sprite.contents.height,
            sprite,
            floatColor.first,
            floatColor.second,
            floatColor.third,
            1.0f,
        )
    }

    protected fun drawFluidTooltip(
        context: DrawContext,
        fluid: Fluid,
        amount: Long,
        x: Int,
        y: Int,
        mouseX: Int,
        mouseY: Int,
    ) {
        drawFluidTooltip(
            context,
            FluidVariant.of(fluid),
            amount,
            x,
            y,
            mouseX,
            mouseY,
        )
    }

    protected fun drawFluidTooltip(
        context: DrawContext,
        variant: FluidVariant,
        amount: Long,
        x: Int,
        y: Int,
        mouseX: Int,
        mouseY: Int,
    ) {
        if (variant.isBlank) return
        val startX1: Int = startX + getSlotPosX(x)
        val startY1: Int = startY + getSlotPosY(y)
        val xRange: IntRange = (startX1..startX1 + 18)
        val yRange: IntRange = (startY1..startY1 + 18)
        if (mouseX in xRange && mouseY in yRange) {
            context.drawTooltip(
                textRenderer,
                buildList {
                    // Tooltips
                    addAll(FluidVariantRendering.getTooltip(variant))
                    // Fluid Amount
                    add(Text.literal("Amount: $amount Units").formatted(Formatting.GRAY))
                    // Mod Name
                    val id: Identifier = Registries.FLUID.getId(variant.fluid)
                    getModName(id.namespace)?.let { name: String ->
                        add(Text.literal(name).formatted(Formatting.BLUE, Formatting.ITALIC))
                    }
                },
                mouseX,
                mouseY,
            )
        }
    }
}
