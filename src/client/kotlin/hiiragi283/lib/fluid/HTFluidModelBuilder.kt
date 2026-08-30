package hiiragi283.lib.fluid

import hiiragi283.lib.HTConstants
import hiiragi283.lib.resource.toId
import hiiragi283.lib.resource.vanillaId
import hiiragi283.lib.util.HTDelegates
import hiiragi283.ragium.api.RagiumAPI
import java.awt.Color
import net.minecraft.client.renderer.block.FluidModel
import net.minecraft.client.resources.model.sprite.Material
import net.neoforged.neoforge.client.fluid.CustomFluidRenderer
import net.neoforged.neoforge.client.fluid.FluidTintSource
import net.neoforged.neoforge.client.fluid.FluidTintSources

/**
 * [FluidModel.Unbaked]のビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTFluidModelBuilder {
    /**
     * 液体源のスプライト
     */
    var still: Material by HTDelegates.onceInitialize()

    /**
     * 液体流のスプライト
     */
    var flowing: Material by HTDelegates.onceInitialize()

    /**
     * 液体のオーバーレイのスプライト
     */
    var overlay: Material? = null

    /**
     * 液体の着色ロジック
     */
    var tintSource: FluidTintSource? = null

    /**
     * 特殊なレンダラー
     */
    var customRenderer: CustomFluidRenderer? = null

    /**
     * 半透明な液体のテクスチャを割り当てます。
     * @see net.minecraft.client.renderer.block.FluidStateModelSet.WATER_MODEL
     */
    fun setClear() {
        still = Material(vanillaId(HTConstants.BLOCK, "water_still"))
        flowing = Material(vanillaId(HTConstants.BLOCK, "water_flow"))
        overlay = Material(vanillaId(HTConstants.BLOCK, "water_overlay"))
    }

    /**
     * 不透明な液体のテクスチャを割り当てます。
     */
    fun setDull() {
        still = Material(HTConstants.NEOFORGE.toId(HTConstants.BLOCK, "milk_still"))
        flowing = Material(HTConstants.NEOFORGE.toId(HTConstants.BLOCK, "milk_flowing"))
    }

    fun setMolten() {
        still = Material(RagiumAPI.id(HTConstants.BLOCK, "molten_still"))
        flowing = Material(RagiumAPI.id(HTConstants.BLOCK, "molten_flowing"))
    }

    /**
     * 液体源のスプライトを液体流にコピーします。
     */
    fun copyStillToFlowing() {
        flowing = still
    }

    /**
     * 指定した[色][color]から[FluidTintSource]を指定します。
     */
    fun colorTint(color: Int) {
        tintSource = FluidTintSources.constant(color)
    }

    /**
     * 指定した[色][color]から[FluidTintSource]を指定します。
     */
    fun colorTint(color: Color) {
        colorTint(color.rgb)
    }

    fun build(): FluidModel.Unbaked = FluidModel.Unbaked(still, flowing, overlay, tintSource, customRenderer)
}
