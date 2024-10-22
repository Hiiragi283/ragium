package hiiragi283.ragium.client.model

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.client.util.FLUID_CUBE_TRANSFORM
import hiiragi283.ragium.client.util.getSpriteAndColor
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.model.*
import net.minecraft.client.render.model.json.ModelOverrideList
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import java.util.function.Function
import java.util.function.Supplier

data object HTFluidCubeModel : UnbakedModel, BakedModel {
    @JvmField
    val MODEL_ID: Identifier = RagiumAPI.id("item/fluid_cube")

    @JvmStatic
    private lateinit var sprite: Sprite

    @JvmStatic
    private lateinit var textureGetter: Function<SpriteIdentifier, Sprite>

    //    UnbakedModel    //

    override fun getModelDependencies(): Collection<Identifier> = listOf()

    override fun setParents(modelLoader: Function<Identifier, UnbakedModel>) {
    }

    override fun bake(baker: Baker, textureGetter: Function<SpriteIdentifier, Sprite>, rotationContainer: ModelBakeSettings): BakedModel =
        apply {
            this.textureGetter = textureGetter
        }

    //    BakedModel    //

    override fun getQuads(state: BlockState?, face: Direction?, random: Random): List<BakedQuad> = listOf()

    override fun useAmbientOcclusion(): Boolean = true

    override fun hasDepth(): Boolean = false

    override fun isSideLit(): Boolean = false

    override fun isBuiltin(): Boolean = false

    override fun getParticleSprite(): Sprite = sprite

    override fun getTransformation(): ModelTransformation = FLUID_CUBE_TRANSFORM

    override fun getOverrides(): ModelOverrideList = ModelOverrideList.EMPTY

    //    FabricBakedModel    //

    override fun isVanillaAdapter(): Boolean = false

    override fun emitItemQuads(stack: ItemStack, randomSupplier: Supplier<Random>, context: RenderContext) {
        // render cube
        MinecraftClient
            .getInstance()
            .itemRenderer
            .models
            .getModel(RagiumContents.Misc.EMPTY_FLUID_CUBE.asItem())
            ?.emitItemQuads(stack, randomSupplier, context)
        // render fluid
        val (sprite: Sprite, color: Int) = stack.get(RagiumComponentTypes.FLUID)?.value()?.getSpriteAndColor() ?: return
        Direction.entries.forEach { dir: Direction ->
            val emitter: QuadEmitter = context.emitter
            emitter.square(dir, 0.3f, 0.3f, 0.7f, 0.7f, 0.7f)
            emitter.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV)
            emitter.color(color, color, color, color)
            emitter.emit()
        }
    }
}
