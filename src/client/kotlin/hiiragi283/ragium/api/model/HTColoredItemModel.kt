package hiiragi283.ragium.api.model

import hiiragi283.ragium.api.extension.DEFAULT_ITEM_TRANSFORM
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.Sprite
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import java.util.function.Supplier

@Environment(EnvType.CLIENT)
abstract class HTColoredItemModel : HTBakedModel {
    final override lateinit var sprite: Sprite

    abstract fun getSpriteId(stack: ItemStack): Identifier

    abstract fun getColor(stack: ItemStack): Int

    final override fun getTransformation(): ModelTransformation = DEFAULT_ITEM_TRANSFORM

    override fun emitItemQuads(stack: ItemStack, randomSupplier: Supplier<Random>, context: RenderContext) {
        this.sprite = getSprite(getSpriteId(stack))
        val emitter: QuadEmitter = context.emitter
        Direction.entries.forEach { direction: Direction ->
            forEachDirection(emitter, direction, stack)
        }
    }

    protected open fun forEachDirection(emitter: QuadEmitter, direction: Direction, stack: ItemStack) {
        emitTexture(emitter, direction, sprite, getColor(stack), 0.5f)
    }

    protected fun emitTexture(
        emitter: QuadEmitter,
        direction: Direction,
        sprite: Sprite,
        color: Int,
        depth: Float,
    ) {
        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            emitter.square(direction, 0f, 0f, 1f, 1f, depth)
        } else {
            emitter.square(direction, 0f, 0f, 0f, 0f, 0f)
        }
        emitter.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV)
        emitter.color(color, color, color, color)
        emitter.emit()
    }
}
