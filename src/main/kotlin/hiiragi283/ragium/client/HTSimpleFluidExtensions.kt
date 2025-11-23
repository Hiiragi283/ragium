package hiiragi283.ragium.client

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.registry.toId
import hiiragi283.ragium.api.registry.vanillaId
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import java.awt.Color

@OnlyIn(Dist.CLIENT)
class HTSimpleFluidExtensions(val stillTex: ResourceLocation, val color: Color? = null, val floatingTex: ResourceLocation = stillTex) :
    IClientFluidTypeExtensions {
    companion object {
        @JvmStatic
        fun clear(color: Color): HTSimpleFluidExtensions = HTSimpleFluidExtensions(
            vanillaId("block", "water_still"),
            color,
            vanillaId("block", "water_flow"),
        )

        @JvmStatic
        fun dull(color: Color): HTSimpleFluidExtensions = HTSimpleFluidExtensions(
            RagiumConst.NEOFORGE.toId("block", "milk_still"),
            color,
            RagiumConst.NEOFORGE.toId("block", "milk_flowing"),
        )
    }

    override fun getStillTexture(): ResourceLocation = stillTex

    override fun getFlowingTexture(): ResourceLocation = floatingTex

    override fun getTintColor(): Int = color?.rgb ?: super.getTintColor()
}
