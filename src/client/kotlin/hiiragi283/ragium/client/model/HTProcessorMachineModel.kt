package hiiragi283.ragium.client.model

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.machine.property.HTMachinePropertyKeys
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.client.extension.getBlockModel
import hiiragi283.ragium.client.extension.hullModel
import hiiragi283.ragium.common.RagiumContents
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
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

@Environment(EnvType.CLIENT)
data object HTProcessorMachineModel : UnbakedModel, BakedModel {
    @JvmField
    val MODEL_ID: Identifier = RagiumAPI.id("block/dynamic_processor")

    //    UnbakedModel    //

    override fun getModelDependencies(): Collection<Identifier> = listOf()

    override fun setParents(modelLoader: Function<Identifier, UnbakedModel>) {
    }

    override fun bake(baker: Baker, textureGetter: Function<SpriteIdentifier, Sprite>, rotationContainer: ModelBakeSettings): BakedModel =
        this

    //    BakedModel    //

    override fun getQuads(state: BlockState?, face: Direction?, random: Random): List<BakedQuad> = listOf()

    override fun useAmbientOcclusion(): Boolean = true

    override fun hasDepth(): Boolean = false

    override fun isSideLit(): Boolean = true

    override fun isBuiltin(): Boolean = false

    override fun getParticleSprite(): Sprite = getBlockModel(RagiumContents.StorageBlocks.RAGI_STEEL.value).particleSprite

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
        val blockEntity: HTMachineBlockEntityBase = blockView.getMachine(pos) ?: return
        val (key: HTMachineKey, tier: HTMachineTier) = blockEntity.definition
        tier.hullModel.emitBlockQuads(blockView, state, pos, randomSupplier, context)
        val frontDir: Direction =
            blockView.getBlockState(pos).getOrDefault(Properties.HORIZONTAL_FACING, Direction.NORTH)
        emitMachineFront(frontDir, key, context)
    }

    override fun emitItemQuads(stack: ItemStack, randomSupplier: Supplier<Random>, context: RenderContext) {
        stack.machineTier.hullModel.emitItemQuads(stack, randomSupplier, context)
        val key: HTMachineKey = stack.machineKeyOrNull ?: return
        emitMachineFront(Direction.NORTH, key, context)
    }

    @JvmStatic
    private fun emitMachineFront(frontDir: Direction, key: HTMachineKey, context: RenderContext) {
        val properties: HTPropertyHolder = key.entry
        val frontSprite: Sprite = MinecraftClient
            .getInstance()
            .getSpriteAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)
            .apply(properties.getOrDefault(HTMachinePropertyKeys.FRONT_TEX)(key.id))

        val emitter: QuadEmitter = context.emitter
        val texDir: Direction = properties.getOrDefault(HTMachinePropertyKeys.FRONT_MAPPER)(frontDir)
        emitter.square(texDir, 0.0f, 0.0f, 1.0f, 1.0f, -0.01f)
        emitter.spriteBake(frontSprite, MutableQuadView.BAKE_LOCK_UV)
        emitter.color(-1, -1, -1, -1)
        emitter.emit()
    }
}
