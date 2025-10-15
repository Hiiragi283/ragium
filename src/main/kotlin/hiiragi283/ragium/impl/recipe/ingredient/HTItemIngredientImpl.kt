package hiiragi283.ragium.impl.recipe.ingredient

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.extension.andThen
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.api.serialization.codec.downCast
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.IngredientType
import net.neoforged.neoforge.registries.NeoForgeRegistries

class HTItemIngredientImpl private constructor(either: Either<HolderSet<Item>, ICustomIngredient>, amount: Int = 1) :
    HTIngredientBase<Item, ItemStack, ICustomIngredient>(either, amount),
    HTItemIngredient {
        companion object {
            @JvmField
            val INGREDIENT_CODEC: BiCodec<RegistryFriendlyByteBuf, ICustomIngredient> = VanillaBiCodecs
                .registryBased(
                    NeoForgeRegistries.INGREDIENT_TYPES,
                ).dispatch(ICustomIngredient::getType, IngredientType<*>::codec, IngredientType<*>::streamCodec)

            @JvmStatic
            private val ENTRY_CODEC: BiCodec<RegistryFriendlyByteBuf, Either<HolderSet<Item>, ICustomIngredient>> = eitherCodec(
                Registries.ITEM,
                INGREDIENT_CODEC,
            )

            @JvmStatic
            private val FLAT_CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemIngredientImpl> = ENTRY_CODEC.xmap(
                ::HTItemIngredientImpl,
                HTItemIngredientImpl::either,
            )

            @JvmStatic
            private val CODEC_WITH_COUNT: BiCodec<RegistryFriendlyByteBuf, HTItemIngredientImpl> = BiCodec.composite(
                ENTRY_CODEC.fieldOf("items"),
                HTItemIngredientImpl::either,
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

            @JvmStatic
            fun of(vararg items: ItemLike, count: Int = 1): HTItemIngredientImpl {
                check(items.isNotEmpty())
                check(count >= 1)
                return of(HolderSet.direct(ItemLike::asItem.andThen(BuiltInRegistries.ITEM::wrapAsHolder), *items), count)
            }

            @JvmStatic
            fun of(holderSet: HolderSet<Item>, count: Int = 1): HTItemIngredientImpl {
                check(count >= 1)
                return HTItemIngredientImpl(Either.left(holderSet), count)
            }

            @JvmStatic
            fun of(ingredient: ICustomIngredient, count: Int = 1): HTItemIngredientImpl {
                check(count >= 1)
                return HTItemIngredientImpl(Either.right(ingredient), count)
            }
        }

        override fun unwrap(): Either<Pair<TagKey<Item>, Int>, List<ItemStack>> = either.map(
            { holderSet: HolderSet<Item> ->
                holderSet.unwrap().map(
                    { Either.left(it to amount) },
                    { holders: List<Holder<Item>> ->
                        Either.right(holders.map { holder: Holder<Item> -> ItemStack(holder, amount) })
                    },
                )
            },
            { ingredient: ICustomIngredient ->
                Either.right(
                    ingredient.items.toList().onEach { stack: ItemStack ->
                        stack.count = this.amount
                    },
                )
            },
        )

        override fun test(stack: ItemStack): Boolean = testOnlyType(stack) && stack.count >= this.amount

        override fun testOnlyType(stack: ItemStack): Boolean =
            either.map(stack::`is`) { ingredient: ICustomIngredient -> ingredient.test(stack) }

        override fun getRequiredAmount(stack: ItemStack): Int = if (test(stack)) this.amount else 0

        override fun hasNoMatchingStacks(): Boolean = either.map(
            { holderSet: HolderSet<Item> -> holderSet.toList().isEmpty() },
            { ingredient: ICustomIngredient -> ingredient.toVanilla().hasNoItems() },
        )
    }
