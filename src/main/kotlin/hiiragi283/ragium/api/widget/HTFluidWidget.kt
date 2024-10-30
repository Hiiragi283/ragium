package hiiragi283.ragium.api.widget

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.fluid.HTMachineFluidStorage
import io.github.cottonmc.cotton.gui.widget.WWidget
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.minecraft.util.Identifier

abstract class HTFluidWidget(val storage: HTMachineFluidStorage, val index: Int) : WWidget() {
    companion object {
        @JvmField
        val FLUID_UPDATE: Identifier = RagiumAPI.id("fluid_update")

        @JvmField
        val FLUID_CODEC: Codec<ResourceAmount<FluidVariant>> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    FluidVariant.CODEC.fieldOf("resource").forGetter(ResourceAmount<FluidVariant>::resource),
                    Codec.LONG.fieldOf("amount").forGetter(ResourceAmount<FluidVariant>::amount),
                ).apply(instance, ::ResourceAmount)
        }
    }

    abstract var variant: FluidVariant
    abstract var amount: Long

    fun getResourceAmount(): ResourceAmount<FluidVariant> = ResourceAmount(variant, amount)

    var onTick: () -> Unit = { }

    fun setOnTick(action: () -> Unit): HTFluidWidget = apply {
        onTick = action
    }

    override fun canResize(): Boolean = true
}
