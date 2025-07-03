package hiiragi283.ragium.api.recipe

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.util.HTTagUtil
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
import net.minecraft.util.RandomSource
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class HTItemOutput(
    entry: Either<ResourceLocation, TagKey<Item>>,
    amount: Int,
    components: DataComponentPatch,
    val chance: Float,
) : HTRecipeOutput<Item, ItemStack>(
        entry,
        amount,
        components,
    ) {
    companion object {
        @JvmField
        val CODEC: Codec<HTItemOutput> = RecordCodecBuilder
            .create { instance ->
                instance
                    .group(
                        Codec
                            .either(ResourceLocation.CODEC, TagKey.hashedCodec(Registries.ITEM))
                            .fieldOf("id")
                            .forGetter(HTItemOutput::entry),
                        ExtraCodecs
                            .intRange(1, 99)
                            .fieldOf("count")
                            .orElse(1)
                            .forGetter(HTItemOutput::amount),
                        DataComponentPatch.CODEC
                            .optionalFieldOf("components", DataComponentPatch.EMPTY)
                            .forGetter(HTItemOutput::components),
                        Codec.floatRange(0f, 1f).optionalFieldOf("chance", 1f).forGetter(HTItemOutput::chance),
                    ).apply(instance, ::HTItemOutput)
            }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemOutput> = StreamCodec.composite(
            ByteBufCodecs.either(ResourceLocation.STREAM_CODEC, HTTagUtil.streamCodec(Registries.ITEM)),
            HTItemOutput::entry,
            ByteBufCodecs.VAR_INT,
            HTItemOutput::amount,
            DataComponentPatch.STREAM_CODEC,
            HTItemOutput::components,
            ByteBufCodecs.FLOAT,
            HTItemOutput::chance,
            ::HTItemOutput,
        )
    }

    override val registry: Registry<Item> get() = BuiltInRegistries.ITEM

    override fun createStack(holder: Holder<Item>, amount: Int, components: DataComponentPatch): ItemStack =
        ItemStack(holder, amount, components)

    fun getChancedStack(random: RandomSource): ItemStack = when {
        random.nextFloat() <= chance -> getStackResult().result().orElse(ItemStack.EMPTY)
        else -> ItemStack.EMPTY
    }
}
