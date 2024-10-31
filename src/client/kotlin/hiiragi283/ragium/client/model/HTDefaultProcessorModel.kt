package hiiragi283.ragium.client.model

import hiiragi283.ragium.api.extension.getMachineEntity
import hiiragi283.ragium.api.extension.getOrDefault
import hiiragi283.ragium.api.extension.machineTier
import hiiragi283.ragium.api.extension.machineType
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.entity.HTMachineEntity
import hiiragi283.ragium.api.machine.property.HTMachinePropertyKeys
import hiiragi283.ragium.client.util.hullModel
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.Sprite
import net.minecraft.item.ItemStack
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.world.BlockRenderView
import java.util.function.Supplier

@Environment(EnvType.CLIENT)
object HTDefaultProcessorModel : FabricBakedModel {
    override fun isVanillaAdapter(): Boolean = false

    override fun emitBlockQuads(
        blockView: BlockRenderView,
        state: BlockState,
        pos: BlockPos,
        randomSupplier: Supplier<Random>,
        context: RenderContext,
    ) {
        val machineEntity: HTMachineEntity<*> = blockView.getMachineEntity(pos) ?: return
        val (machineType: HTMachineType, tier: HTMachineTier) = machineEntity.definition
        tier.hullModel.emitBlockQuads(blockView, state, pos, randomSupplier, context)
        val frontDir: Direction =
            blockView.getBlockState(pos).getOrDefault(Properties.HORIZONTAL_FACING, Direction.NORTH)
        emitMachineFront(frontDir, machineType, context)
    }

    override fun emitItemQuads(stack: ItemStack, randomSupplier: Supplier<Random>, context: RenderContext) {
        stack.machineTier.hullModel.emitItemQuads(stack, randomSupplier, context)
        emitMachineFront(Direction.NORTH, stack.machineType, context)
    }

    @JvmStatic
    private fun emitMachineFront(frontDir: Direction, type: HTMachineType, context: RenderContext) {
        val frontSprite: Sprite = MinecraftClient
            .getInstance()
            .getSpriteAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)
            .apply(type.getOrDefault(HTMachinePropertyKeys.FRONT_TEX)(type.key.id))

        val emitter: QuadEmitter = context.emitter
        val texDir: Direction = type.getOrDefault(HTMachinePropertyKeys.FRONT_MAPPER)(frontDir)
        emitter.square(texDir, 0.0f, 0.0f, 1.0f, 1.0f, -0.01f)
        emitter.spriteBake(frontSprite, MutableQuadView.BAKE_LOCK_UV)
        emitter.color(-1, -1, -1, -1)
        emitter.emit()
    }
}
