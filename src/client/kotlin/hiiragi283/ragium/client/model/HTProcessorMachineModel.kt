package hiiragi283.ragium.client.model

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntityBase
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.model.HTBakedModel
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.api.property.HTPropertyKey
import hiiragi283.ragium.common.init.RagiumBlocks
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.Sprite
import net.minecraft.item.ItemStack
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.world.BlockRenderView
import java.util.function.Supplier

@Environment(EnvType.CLIENT)
enum class HTProcessorMachineModel(val frontKey: HTPropertyKey.Defaulted<(Identifier) -> Identifier>) : HTBakedModel {
    INACTIVE(HTMachinePropertyKeys.FRONT_TEX),
    ACTIVE(HTMachinePropertyKeys.ACTIVE_FRONT_TEX),
    ;

    override val sprite: Sprite
        get() = getBlockModel(RagiumBlocks.StorageBlocks.RAGI_STEEL.get()).particleSprite

    override fun getTransformation(): ModelTransformation = ModelHelper.MODEL_TRANSFORM_BLOCK

    override fun emitBlockQuads(
        blockView: BlockRenderView,
        state: BlockState,
        pos: BlockPos,
        randomSupplier: Supplier<Random>,
        context: RenderContext,
    ) {
        val blockEntity: HTMachineBlockEntityBase = blockView.getMachineEntity(pos) ?: return
        val (key: HTMachineKey, tier: HTMachineTier) = blockEntity.definition
        tier.hullModel.emitBlockQuads(blockView, state, pos, randomSupplier, context)
        val frontDir: Direction =
            blockView.getBlockState(pos).getOrDefault(Properties.HORIZONTAL_FACING, Direction.NORTH)
        emitMachineFront(frontDir, key, context)
    }

    override fun emitItemQuads(stack: ItemStack, randomSupplier: Supplier<Random>, context: RenderContext) {
        stack.machineTier.hullModel.emitItemQuads(stack, randomSupplier, context)
        val key: HTMachineKey = stack.get(HTMachineKey.COMPONENT_TYPE) ?: return
        emitMachineFront(Direction.NORTH, key, context)
    }

    private fun emitMachineFront(frontDir: Direction, key: HTMachineKey, context: RenderContext) {
        val properties: HTPropertyHolder = key.getEntryOrNull() ?: return
        val frontSprite: Sprite = MinecraftClient
            .getInstance()
            .getSpriteAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)
            .apply(properties.getOrDefault(frontKey)(key.id))

        val emitter: QuadEmitter = context.emitter
        val texDir: Direction = properties.getOrDefault(HTMachinePropertyKeys.FRONT_MAPPER)(frontDir)
        emitter.square(texDir, 0.0f, 0.0f, 1.0f, 1.0f, -0.01f)
        emitter.spriteBake(frontSprite, MutableQuadView.BAKE_LOCK_UV)
        emitter.color(-1, -1, -1, -1)
        emitter.emit()
    }
}
