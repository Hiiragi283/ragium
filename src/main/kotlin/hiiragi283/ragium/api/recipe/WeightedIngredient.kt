package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.util.toList
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.tag.TagKey
import java.util.function.Predicate

class WeightedIngredient private constructor(val ingredient: Ingredient, val count: Int = 1) : Predicate<ItemStack> {
    companion object {
        @JvmField
        val EMPTY = WeightedIngredient(Ingredient.EMPTY, 0)

        @JvmField
        val CODEC: Codec<WeightedIngredient> =
            RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        Ingredient.ALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(WeightedIngredient::ingredient),
                        Codec
                            .intRange(1, 99)
                            .optionalFieldOf("count", 1)
                            .forGetter(WeightedIngredient::count),
                    ).apply(instance, Companion::of)
            }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, WeightedIngredient> =
            PacketCodec.tuple(
                Ingredient.PACKET_CODEC,
                WeightedIngredient::ingredient,
                PacketCodecs.INTEGER,
                WeightedIngredient::count,
                Companion::of,
            )

        @JvmField
        val LIST_PACKET_CODEC: PacketCodec<RegistryByteBuf, List<WeightedIngredient>> = PACKET_CODEC.toList()

        @JvmStatic
        fun of(item: ItemConvertible, count: Int = 1): WeightedIngredient = of(Ingredient.ofItems(item), count)

        @JvmStatic
        fun of(tagKey: TagKey<Item>, count: Int = 1): WeightedIngredient = of(Ingredient.fromTag(tagKey), count)

        @JvmStatic
        fun of(ingredient: Ingredient, count: Int = 1): WeightedIngredient {
            check(count > 0) { "Ingredient weight must be larger than 0!" }
            return when (ingredient) {
                Ingredient.EMPTY -> EMPTY
                else -> WeightedIngredient(ingredient, count)
            }
        }
    }

    val matchingStacks: Array<out ItemStack>
        get() = ingredient.matchingStacks.onEach { stack: ItemStack -> stack.count = count }

    //    Predicate    //

    override fun test(stack: ItemStack): Boolean = when {
        stack.isEmpty -> this == EMPTY
        else -> ingredient.test(stack) && stack.count >= this.count
    }

    //    Any    //

    override fun toString(): String = "weightedIngredient[count=$count, ingredient=${ingredient.matchingStacks.joinToString(", ")}]"
}
