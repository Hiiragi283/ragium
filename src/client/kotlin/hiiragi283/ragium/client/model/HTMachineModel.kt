package hiiragi283.ragium.client.model

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getMachineEntity
import hiiragi283.ragium.api.extension.getOrDefault
import hiiragi283.ragium.api.extension.machineTier
import hiiragi283.ragium.api.extension.machineType
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.entity.HTMachineEntity
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.model.*
import net.minecraft.client.render.model.json.ModelOverrideList
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.item.ItemStack
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.world.BlockRenderView
import java.util.function.Function
import java.util.function.Supplier

data object HTMachineModel : UnbakedModel, BakedModel {
    @JvmField
    val MODEL_ID: Identifier = RagiumAPI.id("block/dynamic_machine")

    @JvmStatic
    private lateinit var frontSprite: Sprite

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

    override fun isSideLit(): Boolean = true

    override fun isBuiltin(): Boolean = false

    override fun getParticleSprite(): Sprite = frontSprite

    override fun getTransformation(): ModelTransformation = ModelHelper.MODEL_TRANSFORM_BLOCK

    override fun getOverrides(): ModelOverrideList = ModelOverrideList.EMPTY

    //    FabricBakedModel    //

    override fun isVanillaAdapter(): Boolean = false

    override fun emitBlockQuads(
        blockView: BlockRenderView,
        state: BlockState,
        pos: BlockPos,
        randomSupplier: Supplier<Random>,
        context: RenderContext,
    ) {
        val machineEntity: HTMachineEntity<*> = blockView.getMachineEntity(pos) ?: return
        val frontDir: Direction =
            blockView.getBlockState(pos).getOrDefault(Properties.HORIZONTAL_FACING, Direction.NORTH)
        emitMachineFront(frontDir, machineEntity.machineType, context)
    }

    override fun emitItemQuads(stack: ItemStack, randomSupplier: Supplier<Random>, context: RenderContext) {
        MinecraftClient
            .getInstance()
            .bakedModelManager
            .getModel(ModelIdentifier(stack.machineTier.getHull().id, ""))
            .emitItemQuads(stack, randomSupplier, context)
        emitMachineFront(Direction.NORTH, stack.machineType, context)
    }

    @JvmStatic
    private fun emitMachineFront(frontDir: Direction, type: HTMachineType, context: RenderContext) {
        val frontId = SpriteIdentifier(
            PlayerScreenHandler.BLOCK_ATLAS_TEXTURE,
            type.getOrDefault(HTMachinePropertyKeys.FRONT_TEX)(type.key.id),
        )
        this.frontSprite = this.textureGetter.apply(frontId)
        val emitter: QuadEmitter = context.emitter
        val texDir: Direction = type.getOrDefault(HTMachinePropertyKeys.FRONT_MAPPER)(frontDir)
        emitter.square(texDir, 0.0f, 0.0f, 1.0f, 1.0f, -0.01f)
        emitter.spriteBake(frontSprite, MutableQuadView.BAKE_LOCK_UV)
        emitter.color(-1, -1, -1, -1)
        emitter.emit()
    }
}
