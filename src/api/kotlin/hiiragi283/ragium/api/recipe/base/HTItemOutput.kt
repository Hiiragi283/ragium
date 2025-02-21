package hiiragi283.ragium.api.recipe.base

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import java.util.function.Supplier

class HTItemOutput private constructor(private val either: Either<SizedTag, ItemStack>) : Supplier<ItemStack> {
    companion object {
        @JvmField
        val CODEC: Codec<HTItemOutput> =
            Codec.xor(SizedTag.CODEC, ItemStack.CODEC).xmap(::HTItemOutput, HTItemOutput::either)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemOutput> = ByteBufCodecs
            .either(
                SizedTag.STREAM_CODEC,
                ItemStack.STREAM_CODEC,
            ).map(::HTItemOutput, HTItemOutput::either)

        @JvmField
        val STACK_COMPARATOR: Comparator<ItemStack> = compareBy { stack: ItemStack -> stack.itemHolder.idOrThrow }

        //    Left    //

        @JvmStatic
        fun of(prefix: HTTagPrefix, material: HTMaterialKey, count: Int = 1): HTItemOutput = of(prefix.createTag(material), count)

        @JvmStatic
        fun of(tagKey: TagKey<Item>, count: Int = 1): HTItemOutput = HTItemOutput(Either.left(SizedTag(tagKey, count)))

        //    Right    //

        @JvmStatic
        fun of(item: ItemLike, count: Int = 1): HTItemOutput = of(ItemStack(item, count))

        @JvmStatic
        fun of(builder: () -> ItemStack): HTItemOutput = of(builder())

        @JvmStatic
        fun of(stack: ItemStack): HTItemOutput {
            check(!stack.isEmpty) { "Empty ItemStack is not allowed!" }
            return HTItemOutput(Either.right(stack))
        }
    }

    val id: ResourceLocation =
        either.map(
            { sizedTag: SizedTag -> sizedTag.tagKey.location() },
            { stack: ItemStack -> stack.itemHolder.idOrThrow },
        )

    fun copyWithCount(count: Int): HTItemOutput = either.map(
        { sizedTag: SizedTag -> of(sizedTag.tagKey, count) },
        { stack: ItemStack -> of(stack.copyWithCount(count)) },
    )

    fun isValid(defaultValue: Boolean): Boolean = either.map(
        { sizedTag: SizedTag ->
            val lookup: RegistryAccess = RagiumAPI.getInstance().getCurrentLookup() ?: return@map defaultValue
            lookup
                .lookupOrThrow(Registries.ITEM)
                .get(sizedTag.tagKey)
                .map(HolderSet<Item>::isNotEmpty)
                .orElse(false)
        },
        { stack: ItemStack -> !stack.isEmpty },
    )

    override fun get(): ItemStack = either
        .map(
            { sizedTag: SizedTag ->
                sizedTag.ingredient.matchingStacks
                    .sortedWith(STACK_COMPARATOR)
                    .getOrNull(0) ?: ItemStack.EMPTY
            },
            identifyFunction(),
        ).copy()

    private data class SizedTag(val tagKey: TagKey<Item>, val count: Int) {
        companion object {
            @JvmField
            val CODEC: Codec<SizedTag> = RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        TagKey.codec(Registries.ITEM).fieldOf("tag").forGetter(SizedTag::tagKey),
                        ExtraCodecs.intRange(1, 99).optionalFieldOf("count", 1).forGetter(SizedTag::count),
                    ).apply(instance, ::SizedTag)
            }

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SizedTag> = StreamCodec.composite(
                TagKey.codec(Registries.ITEM).toRegistryStream(),
                SizedTag::tagKey,
                ByteBufCodecs.VAR_INT,
                SizedTag::count,
                ::SizedTag,
            )
        }

        val ingredient: HTItemIngredient = HTItemIngredient.of(tagKey, count)
    }
}
