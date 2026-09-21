package hiiragi283.lib.renderer

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.world.level.block.entity.BlockEntity

/**
 * Hiiragi Seriesで使用される，[BlockEntityRenderer]の拡張クラスです。
 * @param BE 描画する[BlockEntity]のクラス
 * @param S 描画に必要なデータを保持するクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
abstract class HTBlockEntityRenderer<BE : BlockEntity, S : BlockEntityRenderState>(
    context: BlockEntityRendererProvider.Context
) : BlockEntityRenderer<BE, S>
