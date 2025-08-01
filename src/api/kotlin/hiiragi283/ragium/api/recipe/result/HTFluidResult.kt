package hiiragi283.ragium.api.recipe.result

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

class HTFluidResult(entry: Either<ResourceLocation, TagKey<Fluid>>, amount: Int, components: DataComponentPatch) :
    HTRecipeResult<Fluid, FluidStack>(entry, amount, components) {
    companion object {
        @JvmField
        val CODEC: Codec<HTFluidResult> = RecordCodecBuilder
            .create { instance ->
                instance
                    .group(
                        Codec
                            .either(ResourceLocation.CODEC, TagKey.hashedCodec(Registries.FLUID))
                            .fieldOf("id")
                            .forGetter(HTFluidResult::entry),
                        ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(HTFluidResult::amount),
                        DataComponentPatch.CODEC
                            .optionalFieldOf("components", DataComponentPatch.EMPTY)
                            .forGetter(HTFluidResult::components),
                    ).apply(instance, ::HTFluidResult)
            }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTFluidResult> = StreamCodec.composite(
            ByteBufCodecs.either(ResourceLocation.STREAM_CODEC, HTTagHelper.streamCodec(Registries.FLUID)),
            HTFluidResult::entry,
            ByteBufCodecs.VAR_INT,
            HTFluidResult::amount,
            DataComponentPatch.STREAM_CODEC,
            HTFluidResult::components,
            ::HTFluidResult,
        )
    }

    override val registry: Registry<Fluid>
        get() = BuiltInRegistries.FLUID

    override fun createStack(holder: Holder<Fluid>, amount: Int, components: DataComponentPatch): FluidStack =
        FluidStack(holder, amount, components)
}
