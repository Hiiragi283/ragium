package hiiragi283.ragium.api.gui.component

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.HTStackView
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
interface HTFluidWidget :
    HTStackView<ImmutableFluidStack>,
    HTWidget {
    fun setStack(stack: ImmutableFluidStack)

    companion object {
        @JvmField
        val TANK_ID: ResourceLocation = RagiumAPI.id("textures/gui/tank.png")
    }
}
