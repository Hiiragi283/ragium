package hiiragi283.ragium.client

import hiiragi283.ragium.api.extension.vanillaId
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import java.awt.Color

@OnlyIn(Dist.CLIENT)
class HTSimpleFluidExtensions(val stillTex: ResourceLocation, val color: Color? = null, val floatingTex: ResourceLocation = stillTex) :
    IClientFluidTypeExtensions {
    constructor(color: Color) : this(vanillaId("block/water_still"), color, vanillaId("block/water_flow"))

    override fun getStillTexture(): ResourceLocation = stillTex

    override fun getFlowingTexture(): ResourceLocation = floatingTex

    override fun getTintColor(): Int = color?.rgb ?: super.getTintColor()
}
