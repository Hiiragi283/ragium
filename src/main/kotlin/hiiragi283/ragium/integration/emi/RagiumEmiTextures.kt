package hiiragi283.ragium.integration.emi

import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.widget.Bounds
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import net.minecraft.resources.ResourceLocation

object RagiumEmiTextures {
    @JvmField
    val TANK: EmiTexture = create(HTFluidWidget.TANK_ID, 0, 0, 18, 54)

    @JvmStatic
    fun create(
        id: ResourceLocation,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
    ): EmiTexture = EmiTexture(id, x, y, width, height, width, height, width, height)

    @JvmStatic
    fun create(id: ResourceLocation, bounds: Bounds): EmiTexture = create(id, bounds.x, bounds.y, bounds.width, bounds.height)
}
