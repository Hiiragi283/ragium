package hiiragi283.ragium.api.gui.component

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.fluids.FluidStack

@OnlyIn(Dist.CLIENT)
interface HTFluidWidget : HTWidget {
    var stack: FluidStack
    val capacity: Int

    companion object {
        @JvmField
        val TANK_ID: ResourceLocation = RagiumAPI.id("textures/gui/tank.png")
    }
}
