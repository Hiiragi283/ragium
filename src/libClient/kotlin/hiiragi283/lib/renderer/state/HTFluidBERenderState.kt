package hiiragi283.lib.renderer.state

import hiiragi283.lib.transfer.fluid.HTFluidView
import hiiragi283.lib.transfer.fluid.getFluidStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.FluidModel
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.neoforged.neoforge.transfer.fluid.FluidResource

/**
 * Hiiragi Seriesで使用される，液体を描画する[BlockEntityRenderState]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
class HTFluidBERenderState : BlockEntityRenderState() {
    /**
     * 液体のスプライト
     *
     * `null`の場合は描画されません。
     */
    var sprite: TextureAtlasSprite? = null

    /**
     * 液体の色
     */
    var color: Int = -1

    /**
     * 液体の充填率
     */
    var fillingLevel: Float = 0f

    /**
     * 液体が空気より軽いか
     */
    var isLighterThanAir: Boolean = false

    /**
     * [HTFluidView]から情報を取得します。
     * @param view 液体の取得元
     */
    fun extractRenderState(view: HTFluidView) {
        if (view.isEmpty) {
            sprite = null
            color = -1
            fillingLevel = 0f
            isLighterThanAir = false
            return
        }
        val resource: FluidResource = view.resource

        this.fillingLevel = view.currentFilledLevel
        val model: FluidModel = Minecraft.getInstance()
            .modelManager
            .fluidStateModelSet
            .get(resource.fluid.defaultFluidState())
        model.stillMaterial().sprite().let(this::sprite::set)
        model.fluidTintSource()?.colorAsStack(view.getFluidStack())?.let(this::color::set)
        this.isLighterThanAir = resource.fluidType.isLighterThanAir
    }
}
