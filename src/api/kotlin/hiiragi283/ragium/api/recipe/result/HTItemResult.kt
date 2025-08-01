package hiiragi283.ragium.api.recipe.result

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.RagiumAPI
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
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class HTItemResult(entry: Either<ResourceLocation, TagKey<Item>>, amount: Int, components: DataComponentPatch) :
    HTRecipeResult<Item, ItemStack>(
        entry,
        amount,
        components,
    ) {
    companion object {
        @JvmField
        val CODEC: Codec<HTItemResult> = RecordCodecBuilder
            .create { instance ->
                instance
                    .group(
                        Codec
                            .either(ResourceLocation.CODEC, TagKey.hashedCodec(Registries.ITEM))
                            .fieldOf("id")
                            .forGetter(HTItemResult::entry),
                        ExtraCodecs
                            .intRange(1, 99)
                            .fieldOf("count")
                            .orElse(1)
                            .forGetter(HTItemResult::amount),
                        DataComponentPatch.CODEC
                            .optionalFieldOf("components", DataComponentPatch.EMPTY)
                            .forGetter(HTItemResult::components),
                    ).apply(instance, ::HTItemResult)
            }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemResult> = StreamCodec.composite(
            ByteBufCodecs.either(ResourceLocation.STREAM_CODEC, HTTagHelper.streamCodec(Registries.ITEM)),
            HTItemResult::entry,
            ByteBufCodecs.VAR_INT,
            HTItemResult::amount,
            DataComponentPatch.STREAM_CODEC,
            HTItemResult::components,
            ::HTItemResult,
        )
    }

    override fun getFirstHolderFromId(id: ResourceLocation): DataResult<out Holder<Item>> = super
        .getFirstHolderFromId(id)
        .map { holder: Holder<Item> -> RagiumAPI.getInstance().unifyItemFromId(holder, id) }

    override fun getFirstHolderFromTag(tagKey: TagKey<Item>): DataResult<out Holder<Item>> = super
        .getFirstHolderFromTag(tagKey)
        .map { holder: Holder<Item> -> RagiumAPI.getInstance().unifyItemFromTag(holder, tagKey) }

    override val registry: Registry<Item> get() = BuiltInRegistries.ITEM

    override fun createStack(holder: Holder<Item>, amount: Int, components: DataComponentPatch): ItemStack =
        ItemStack(holder, amount, components)
}
