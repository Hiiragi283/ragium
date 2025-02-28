package hiiragi283.ragium.api.recipe.base

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.tag.RagiumItemTags
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.tags.TagKey
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import java.util.function.Predicate

class HTItemIngredient private constructor(val ingredient: Ingredient, val count: Int) : Predicate<ItemStack> {
    companion object {
        @JvmField
        val CODEC: Codec<HTItemIngredient> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    Ingredient.MAP_CODEC_NONEMPTY.forGetter(HTItemIngredient::ingredient),
                    ExtraCodecs.POSITIVE_INT.optionalFieldOf("count", 1).forGetter(HTItemIngredient::count),
                ).apply(instance, ::HTItemIngredient)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemIngredient> = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            HTItemIngredient::ingredient,
            ByteBufCodecs.VAR_INT,
            HTItemIngredient::count,
            ::HTItemIngredient,
        )

        @JvmStatic
        fun of(item: ItemLike, count: Int = 1): HTItemIngredient = HTItemIngredient(Ingredient.of(item), count)

        @JvmStatic
        fun of(prefix: HTTagPrefix, material: HTMaterialKey, count: Int = 1): HTItemIngredient = of(prefix.createTag(material), count)

        @JvmStatic
        fun of(tagKey: TagKey<Item>, count: Int = 1): HTItemIngredient = HTItemIngredient(Ingredient.of(tagKey), count)

        @JvmStatic
        fun of(ingredient: ICustomIngredient, count: Int = 1): HTItemIngredient = HTItemIngredient(ingredient.toVanilla(), count)
    }

    val isEmpty: Boolean
        get() = ingredient.isEmpty || count <= 0

    val matchingStacks: List<ItemStack>
        get() = ingredient.items.map { it.copyWithCount(count) }

    override fun test(stack: ItemStack): Boolean = when {
        this.isEmpty -> stack.isEmpty
        else -> when {
            stack.`is`(RagiumItemTags.IGNORED_IN_INGREDIENT) -> true
            else -> ingredient.test(stack) && stack.count >= count
        }
    }
}
