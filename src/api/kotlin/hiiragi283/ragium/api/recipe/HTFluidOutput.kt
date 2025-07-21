package hiiragi283.ragium.api.recipe

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.tag.HTTagHelper
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

class HTFluidOutput(entry: Either<ResourceLocation, TagKey<Fluid>>, amount: Int, components: DataComponentPatch) :
    HTRecipeOutput<Fluid, FluidStack>(entry, amount, components) {
    companion object {
        @JvmField
        val CODEC: Codec<HTFluidOutput> = RecordCodecBuilder
            .create { instance ->
                instance
                    .group(
                        Codec
                            .either(ResourceLocation.CODEC, TagKey.hashedCodec(Registries.FLUID))
                            .fieldOf("id")
                            .forGetter(HTFluidOutput::entry),
                        ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(HTFluidOutput::amount),
                        DataComponentPatch.CODEC
                            .optionalFieldOf("components", DataComponentPatch.EMPTY)
                            .forGetter(HTFluidOutput::components),
                    ).apply(instance, ::HTFluidOutput)
            }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTFluidOutput> = StreamCodec.composite(
            ByteBufCodecs.either(ResourceLocation.STREAM_CODEC, HTTagHelper.streamCodec(Registries.FLUID)),
            HTFluidOutput::entry,
            ByteBufCodecs.VAR_INT,
            HTFluidOutput::amount,
            DataComponentPatch.STREAM_CODEC,
            HTFluidOutput::components,
            ::HTFluidOutput,
        )
    }

    override val registry: Registry<Fluid>
        get() = BuiltInRegistries.FLUID

    override fun createStack(holder: Holder<Fluid>, amount: Int, components: DataComponentPatch): FluidStack =
        FluidStack(holder, amount, components)
}
