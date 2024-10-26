package hiiragi283.ragium.client.gui.widget

import com.mojang.blaze3d.systems.RenderSystem
import hiiragi283.ragium.api.extension.toFloatColor
import hiiragi283.ragium.api.widget.HTFluidWidget
import hiiragi283.ragium.client.util.getSpriteAndColor
import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.widget.TooltipBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.texture.Sprite
import net.minecraft.registry.Registries
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import kotlin.jvm.optionals.getOrNull

@Environment(EnvType.CLIENT)
class HTClientFluidWidget(storage: SlottedStorage<FluidVariant>, index: Int) : HTFluidWidget(storage, index) {
    override var variant: FluidVariant = storage.getSlot(index).resource

    override var amount: Long = storage.getSlot(index).amount

    private var tick: Int = 0

    override fun tick() {
        if (tick >= 20) {
            onTick()
            tick = 0
        } else {
            tick++
        }
    }

    override fun paint(
        context: DrawContext,
        x: Int,
        y: Int,
        mouseX: Int,
        mouseY: Int,
    ) {
        // render slot
        BackgroundPainter.SLOT.paintBackground(context, x, y, this)
        // render fluid sprite
        RenderSystem.enableDepthTest()
        val (sprite: Sprite, color: Int) = variant.fluid.getSpriteAndColor() ?: return
        val floatColor: Triple<Float, Float, Float> = toFloatColor(color)

        context.drawSprite(
            x + getWidth() / 2 - 8,
            y + getHeight() / 2 - 8,
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

    override fun addTooltip(tooltip: TooltipBuilder) {
        // render fluid name
        tooltip.add(FluidVariantAttributes.getName(variant))
        // render fluid amount
        if (amount >= 0) {
            tooltip.add(Text.literal("Amount: $amount Units").formatted(Formatting.GRAY))
        }
        // render fluid id
        val id: Identifier = Registries.FLUID.getId(variant.fluid)
        tooltip.add(Text.literal(id.toString()).formatted(Formatting.DARK_GRAY))
        // render mod name
        FabricLoader
            .getInstance()
            .getModContainer(id.namespace)
            .getOrNull()
            ?.metadata
            ?.name
            ?.let { name: String ->
                tooltip.add(Text.literal(name).formatted(Formatting.BLUE, Formatting.ITALIC))
            }
    }
}
