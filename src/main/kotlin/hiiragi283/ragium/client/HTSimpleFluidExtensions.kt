package hiiragi283.ragium.client

import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import java.awt.Color

class HTSimpleFluidExtensions(val stillTex: ResourceLocation, val color: Color? = null, val floatingTex: ResourceLocation = stillTex) :
    IClientFluidTypeExtensions {
    override fun getStillTexture(): ResourceLocation = stillTex

    override fun getFlowingTexture(): ResourceLocation = floatingTex

    override fun getTintColor(): Int = color?.rgb ?: super.getTintColor()
}
