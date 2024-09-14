package hiiragi283.ragium.common.fluid

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributeHandler
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes
import net.minecraft.fluid.Fluid
import net.minecraft.sound.SoundEvent
import net.minecraft.text.Text
import net.minecraft.world.World
import java.util.*
import kotlin.jvm.optionals.getOrNull

class HTFluidVariantAttributeHandler private constructor(fluid: Fluid) {

    companion object {
        @JvmStatic
        fun create(
            fluid: Fluid,
            builderAction: HTFluidVariantAttributeHandler.() -> Unit,
        ): FluidVariantAttributeHandler =
            HTFluidVariantAttributeHandler(fluid).apply(builderAction).let { builder: HTFluidVariantAttributeHandler ->
                object : FluidVariantAttributeHandler {
                    override fun getName(fluidVariant: FluidVariant): Text = builder.name
                    override fun getFillSound(variant: FluidVariant): Optional<SoundEvent> =
                        Optional.ofNullable(builder.fillSound)

                    override fun getEmptySound(variant: FluidVariant): Optional<SoundEvent> =
                        Optional.ofNullable(builder.emptySound)

                    override fun getLuminance(variant: FluidVariant): Int = builder.luminance
                    override fun getTemperature(variant: FluidVariant): Int = builder.temperature
                    override fun getViscosity(variant: FluidVariant, world: World?): Int = builder.viscosity
                    override fun isLighterThanAir(variant: FluidVariant): Boolean = builder.isLighterThanAir
                }
            }
    }

    private val defaultHandler: FluidVariantAttributeHandler = FluidVariantAttributes.getHandlerOrDefault(fluid)
    private val variant: FluidVariant = FluidVariant.of(fluid)

    var name: Text = defaultHandler.getName(variant)
    var fillSound: SoundEvent? = defaultHandler.getFillSound(variant).getOrNull()
    var emptySound: SoundEvent? = defaultHandler.getEmptySound(variant).getOrNull()
    var luminance: Int = defaultHandler.getLuminance(variant)
    var temperature: Int = defaultHandler.getTemperature(variant)
    var viscosity: Int = defaultHandler.getViscosity(variant, null)
    var isLighterThanAir: Boolean = defaultHandler.isLighterThanAir(variant)

}