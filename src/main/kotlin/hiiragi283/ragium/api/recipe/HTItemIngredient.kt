package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.simpleOrComplex
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import java.util.function.Predicate

class HTItemIngredient(
    val ingredient: Ingredient,
    val count: Int = 1,
) : Predicate<ItemStack> {
    companion object {
        @JvmField
        val CODEC: Codec<HTItemIngredient> =
            simpleOrComplex(
                Ingredient.CODEC.xmap(::HTItemIngredient, HTItemIngredient::ingredient),
                RecordCodecBuilder.create<HTItemIngredient> { instance ->
                    instance
                        .group(
                            Ingredient.CODEC.fieldOf("items").forGetter(HTItemIngredient::ingredient),
                            Codec.intRange(0, 99).optionalFieldOf("count", 1).forGetter(HTItemIngredient::count),
                        ).apply(instance, ::HTItemIngredient)
                },
            ) { ingredient -> ingredient.count == 1 }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemIngredient> =
            StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC,
                HTItemIngredient::ingredient,
                ByteBufCodecs.VAR_INT,
                HTItemIngredient::count,
                ::HTItemIngredient,
            )

        @JvmStatic
        fun of(
            item: ItemLike,
            count: Int = 1,
        ): HTItemIngredient = HTItemIngredient(Ingredient.of(item), count)

        @JvmStatic
        fun of(
            items: Collection<ItemLike>,
            count: Int = 1,
        ): HTItemIngredient = HTItemIngredient(Ingredient.of(items.stream()), count)

        @JvmStatic
        fun of(
            tagKey: TagKey<Item>,
            provider: HolderLookup.Provider,
            count: Int = 1,
        ): HTItemIngredient = of(tagKey, provider.lookupOrThrow(Registries.ITEM), count)

        @JvmStatic
        fun of(
            tagKey: TagKey<Item>,
            lookup: HolderLookup.RegistryLookup<Item>,
            count: Int = 1,
        ): HTItemIngredient = of(lookup.getOrThrow(tagKey), count)

        @JvmStatic
        fun of(
            items: HolderSet<Item>,
            count: Int = 1,
        ): HTItemIngredient = HTItemIngredient(Ingredient.of(items), count)
    }

    val isEmpty: Boolean
        get() = ingredient.isEmpty || count <= 0

    override fun test(stack: ItemStack): Boolean = ingredient.test(stack) && stack.count >= count
}
