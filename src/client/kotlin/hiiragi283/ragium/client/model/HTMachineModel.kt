package hiiragi283.ragium.client.model

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getMachineEntity
import hiiragi283.ragium.api.extension.machineType
import hiiragi283.ragium.api.machine.entity.HTMachineEntity
import hiiragi283.ragium.client.machine.HTClientMachinePropertyKeys
import hiiragi283.ragium.client.util.getBlockModel
import hiiragi283.ragium.common.RagiumContents
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext
import net.minecraft.block.BlockState
import net.minecraft.client.render.model.*
import net.minecraft.client.render.model.json.ModelOverrideList
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.world.BlockRenderView
import java.util.function.Function
import java.util.function.Supplier

@Environment(EnvType.CLIENT)
data object HTMachineModel : UnbakedModel, BakedModel {
    @JvmField
    val MODEL_ID: Identifier = RagiumAPI.id("block/dynamic_machine")

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
        val machineEntity: HTMachineEntity<*> = blockView.getMachineEntity(pos) ?: return
        machineEntity.machineType.ifPresent(HTClientMachinePropertyKeys.STATIC_RENDERER) {
            it.emitBlockQuads(blockView, state, pos, randomSupplier, context)
        }
    }

    override fun emitItemQuads(stack: ItemStack, randomSupplier: Supplier<Random>, context: RenderContext) {
        stack.machineType.ifPresent(HTClientMachinePropertyKeys.STATIC_RENDERER) {
            it.emitItemQuads(stack, randomSupplier, context)
        }
    }
}
