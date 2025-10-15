package hiiragi283.ragium.impl.recipe.ingredient

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.api.serialization.codec.downCast
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

internal class HTItemIngredientImpl(holderSet: HolderSet<Item>, amount: Int = 1) :
    HTIngredientBase<Item, ItemStack>(holderSet, amount),
    HTItemIngredient {
    companion object {
        @JvmStatic
        private val ENTRY_CODEC: BiCodec<RegistryFriendlyByteBuf, HolderSet<Item>> = VanillaBiCodecs.holderSet(Registries.ITEM)

        @JvmStatic
        private val FLAT_CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemIngredientImpl> =
            ENTRY_CODEC.xmap(::HTItemIngredientImpl, HTItemIngredientImpl::holderSet)

        @JvmStatic
        private val CODEC_WITH_COUNT: BiCodec<RegistryFriendlyByteBuf, HTItemIngredientImpl> = BiCodec.composite(
            ENTRY_CODEC.fieldOf("items"),
            HTItemIngredientImpl::holderSet,
            BiCodecs.POSITIVE_INT.optionalFieldOf("count", 1),
            HTItemIngredientImpl::amount,
            ::HTItemIngredientImpl,
        )

        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemIngredient> = BiCodecs
            .xor(CODEC_WITH_COUNT, FLAT_CODEC)
            .xmap(Either<HTItemIngredientImpl, HTItemIngredientImpl>::unwrap) { itemIngredient: HTItemIngredientImpl ->
                when (itemIngredient.amount) {
                    1 -> Either.right(itemIngredient)
                    else -> Either.left(itemIngredient)
                }
            }.downCast()

            /*fun of(ingredient: ICustomIngredient, count: Int = 1): HTItemIngredientImpl {
                check(count >= 1)
                return HTItemIngredientImpl(Either.right(ingredient), count)
            }*/
    }

    override fun unwrap(): Either<Pair<TagKey<Item>, Int>, List<ItemStack>> = holderSet.unwrap().map(
        { Either.left(it to amount) },
        { holders: List<Holder<Item>> ->
            Either.right(holders.map { holder: Holder<Item> -> ItemStack(holder, amount) })
        },
    )

    override fun test(stack: ItemStack): Boolean = testOnlyType(stack) && stack.count >= this.amount

    override fun testOnlyType(stack: ItemStack): Boolean = stack.`is`(holderSet)
}
