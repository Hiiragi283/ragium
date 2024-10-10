package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.fluid.HTFlowableFluid
import hiiragi283.ragium.common.fluid.HTFluidContent
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributeHandler
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.world.World
import java.util.*

object RagiumFluids {
    @JvmField
    val PETROLEUM: HTFluidContent = register("petroleum")

    @JvmStatic
    fun init() {
        PETROLEUM.registerAttributes(object : FluidVariantAttributeHandler {
            override fun getViscosity(variant: FluidVariant, world: World?): Int = 6000

            override fun getFillSound(variant: FluidVariant): Optional<SoundEvent> = Optional.of(SoundEvents.ITEM_BUCKET_FILL_LAVA)

            override fun getEmptySound(variant: FluidVariant): Optional<SoundEvent> = Optional.of(SoundEvents.ITEM_BUCKET_EMPTY_LAVA)
        })
    }

    private fun register(name: String, builderAction: HTFlowableFluid.Settings.() -> Unit = {}): HTFluidContent =
        HTFluidContent.create(RagiumAPI.id(name), builderAction)
}
