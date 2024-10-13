package hiiragi283.ragium.client.model

import hiiragi283.ragium.api.machine.HTMachineEntity
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.util.getMachineEntity
import hiiragi283.ragium.api.util.getOrDefault
import hiiragi283.ragium.api.util.machineTier
import hiiragi283.ragium.api.util.machineType
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
        val machineEntity: HTMachineEntity = blockView.getMachineEntity(pos) ?: return
        val type: HTMachineType = machineEntity.machineType
        val tier: HTMachineTier = machineEntity.tier
        val frontDir: Direction =
            blockView.getBlockState(pos).getOrDefault(Properties.HORIZONTAL_FACING, Direction.NORTH)
        emitMachineQuads(frontDir, type, tier, machineEntity, context) {
            it.emitBlockQuads(blockView, state, pos, randomSupplier, context)
        }
    }

    override fun emitItemQuads(stack: ItemStack, randomSupplier: Supplier<Random>, context: RenderContext) {
        emitMachineQuads(Direction.NORTH, stack.machineType, stack.machineTier, null, context) {
            it.emitItemQuads(stack, randomSupplier, context)
        }
    }

    @JvmStatic
    private fun emitMachineQuads(
        frontDir: Direction,
        type: HTMachineType,
        tier: HTMachineTier,
        machine: HTMachineEntity?,
        context: RenderContext,
        hullRenderer: (BakedModel) -> Unit,
    ) {
        // render hull model
        MinecraftClient
            .getInstance()
            .bakedModelManager
            .getModel(ModelIdentifier(tier.getHull().id, ""))
            ?.apply(hullRenderer)
        // render machine front
        val frontId = SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, type.getFrontTex(machine))
        this.frontSprite = this.textureGetter.apply(frontId)
        val emitter: QuadEmitter = context.emitter
        val texDir: Direction = type.getOrDefault(HTMachinePropertyKeys.FRONT_MAPPER)(frontDir)
        emitter.square(texDir, 0.0f, 0.0f, 1.0f, 1.0f, -0.01f)
        emitter.spriteBake(frontSprite, MutableQuadView.BAKE_LOCK_UV)
        emitter.color(-1, -1, -1, -1)
        emitter.emit()
    }
}
