package hiiragi283.ragium.api.recipe.base

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.*
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import net.neoforged.neoforge.registries.DeferredHolder
import java.util.function.Supplier

class HTFluidOutput private constructor(private val either: Either<SizedTag, FluidStack>) : Supplier<FluidStack> {
    companion object {
        @JvmField
        val CODEC: Codec<HTFluidOutput> =
            Codec.xor(SizedTag.CODEC, FluidStack.CODEC).xmap(::HTFluidOutput, HTFluidOutput::either)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTFluidOutput> = ByteBufCodecs
            .either(
                SizedTag.STREAM_CODEC,
                FluidStack.STREAM_CODEC,
            ).map(::HTFluidOutput, HTFluidOutput::either)

        @JvmField
        val STACK_COMPARATOR: Comparator<FluidStack> = compareBy { stack: FluidStack -> stack.fluidHolder.idOrThrow }

        //    Left    //

        @JvmStatic
        fun of(holder: DeferredHolder<Fluid, *>, amount: Int = 1000): HTFluidOutput = of(holder.commonTag, amount)

        @JvmStatic
        fun of(tagKey: TagKey<Fluid>, amount: Int = 1000): HTFluidOutput = HTFluidOutput(Either.left(SizedTag(tagKey, amount)))

        //    Right    //

        @JvmStatic
        fun of(fluid: Fluid, amount: Int = 1000): HTFluidOutput = of(FluidStack(fluid, amount))

        @JvmStatic
        fun of(builder: () -> FluidStack): HTFluidOutput = of(builder())

        @JvmStatic
        fun of(stack: FluidStack): HTFluidOutput {
            check(!stack.isEmpty) { "Empty FluidStack is not allowed!" }
            return HTFluidOutput(Either.right(stack))
        }
    }

    val id: ResourceLocation =
        either.map(
            { sizedTag: SizedTag -> sizedTag.tagKey.location() },
            { stack: FluidStack -> stack.fluidHolder.idOrThrow },
        )

    fun copyWithCount(count: Int): HTFluidOutput = either.map(
        { sizedTag: SizedTag -> of(sizedTag.tagKey, count) },
        { stack: FluidStack -> of(stack.copyWithAmount(count)) },
    )

    fun isValid(defaultValue: Boolean): Boolean = either.map(
        { sizedTag: SizedTag ->
            val lookup: RegistryAccess = RagiumAPI.getInstance().getCurrentLookup() ?: return@map defaultValue
            lookup
                .lookupOrThrow(Registries.FLUID)
                .get(sizedTag.tagKey)
                .map(HolderSet<Fluid>::isNotEmpty)
                .orElse(false)
        },
        constFunction2(true),
    )

    override fun get(): FluidStack = either
        .map(
            { sizedTag: SizedTag ->
                sizedTag.ingredient.fluids
                    .sortedWith(STACK_COMPARATOR)
                    .getOrNull(0) ?: FluidStack.EMPTY
            },
            identifyFunction(),
        ).copy()

    private data class SizedTag(val tagKey: TagKey<Fluid>, val count: Int) {
        companion object {
            @JvmField
            val CODEC: Codec<SizedTag> = RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        TagKey.codec(Registries.FLUID).fieldOf("tag").forGetter(SizedTag::tagKey),
                        ExtraCodecs.POSITIVE_INT.optionalFieldOf("count", 1).forGetter(SizedTag::count),
                    ).apply(instance, ::SizedTag)
            }

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SizedTag> = StreamCodec.composite(
                TagKey.codec(Registries.FLUID).toRegistryStream(),
                SizedTag::tagKey,
                ByteBufCodecs.VAR_INT,
                SizedTag::count,
                ::SizedTag,
            )
        }

        val ingredient: SizedFluidIngredient = SizedFluidIngredient.of(tagKey, count)
    }
}
