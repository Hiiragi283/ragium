package hiiragi283.ragium.api.gui.component

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
interface HTFluidWidget : HTWidget {
    fun getStack(): ImmutableFluidStack

    fun setStack(stack: ImmutableFluidStack)

    fun getCapacity(): Long

    companion object {
        @JvmField
        val TANK_ID: ResourceLocation = RagiumAPI.id("textures/gui/tank.png")
    }
}
