package hiiragi283.ragium.client.model

import hiiragi283.ragium.api.extension.FLUID_CUBE_TRANSFORM
import hiiragi283.ragium.api.extension.getSpriteAndColor
import hiiragi283.ragium.api.model.HTBakedModel
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItems
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.Sprite
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import java.util.function.Supplier

@Environment(EnvType.CLIENT)
data object HTFluidCubeModel : HTBakedModel {
    override lateinit var sprite: Sprite

    override fun getTransformation(): ModelTransformation = FLUID_CUBE_TRANSFORM

    override fun emitItemQuads(stack: ItemStack, randomSupplier: Supplier<Random>, context: RenderContext) {
        // render cube
        MinecraftClient
            .getInstance()
            .itemRenderer
            .models
            .getModel(RagiumItems.EMPTY_FLUID_CUBE.get())
            ?.emitItemQuads(stack, randomSupplier, context)
        // render fluid
        val (sprite: Sprite, color: Int) = stack
            .get(RagiumComponentTypes.FLUID)
            ?.value()
            ?.getSpriteAndColor()
            ?: return
        this.sprite = sprite
        Direction.entries.forEach { dir: Direction ->
            val emitter: QuadEmitter = context.emitter
            emitter.square(dir, 0.3f, 0.3f, 0.7f, 0.7f, 0.7f)
            emitter.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV)
            emitter.color(color, color, color, color)
            emitter.emit()
        }
    }
}
