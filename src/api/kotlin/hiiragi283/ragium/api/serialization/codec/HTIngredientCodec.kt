package hiiragi283.ragium.api.serialization.codec

import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.resources.RegistryOps
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.IngredientType
import net.neoforged.neoforge.registries.NeoForgeRegistries

data object HTIngredientCodec {
    @JvmStatic
    private val CUSTOM_CODEC: Codec<ICustomIngredient> = NeoForgeRegistries.INGREDIENT_TYPES
        .byNameCodec()
        .dispatch(ICustomIngredient::getType, IngredientType<*>::codec)

    @JvmField
    val INSTANCE: Codec<Ingredient> = Codec
        .either(ValueCodec.listOrElement(), CUSTOM_CODEC)
        .xmap(
            { either: Either<List<Ingredient.Value>, ICustomIngredient> ->
                either.map(
                    { values: List<Ingredient.Value> -> Ingredient.fromValues(values.stream()) },
                    ::Ingredient,
                )
            },
            { ingredient: Ingredient ->
                val custom: ICustomIngredient? = ingredient.customIngredient
                if (custom != null) {
                    Either.right(custom)
                } else {
                    Either.left(ingredient.values.toList())
                }
            },
        )

    private data object ValueCodec : Codec<Ingredient.Value> {
        private val eitherCodec: Codec<Either<TagKey<Item>, List<Holder<Item>>>> =
            Codec.either(TagKey.hashedCodec(Registries.ITEM), RegistryFixedCodec.create(Registries.ITEM).listOrElement())

        override fun <T : Any> encode(input: Ingredient.Value, ops: DynamicOps<T>, prefix: T): DataResult<T> {
            if (ops is RegistryOps<T>) {
                val either: Either<TagKey<Item>, List<Holder<Item>>> = when (input) {
                    is Ingredient.TagValue -> Either.left(input.tag())
                    is Ingredient.ItemValue -> Either.right(listOf(input.item().itemHolder))
                    else -> {
                        when (val stack: ItemStack? = input.items.firstOrNull()) {
                            null -> return DataResult.error { "Can't encode empty ingredient" }
                            else -> Either.right(listOf(stack.itemHolder))
                        }
                    }
                }
                return eitherCodec.encode(either, ops, prefix)
            }
            return DataResult.error { "Can't access item registry" }
        }

        override fun <T : Any> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<Ingredient.Value, T>> {
            if (ops is RegistryOps<T>) {
                return eitherCodec
                    .decode(ops, input)
                    .flatMap { pair: Pair<Either<TagKey<Item>, List<Holder<Item>>>, T> ->
                        val result: DataResult<Ingredient.Value> = pair.first
                            .map(
                                { tagKey: TagKey<Item> -> DataResult.success(Ingredient.TagValue(tagKey)) },
                                { holders: List<Holder<Item>> ->
                                    if (holders.isEmpty()) {
                                        DataResult.error { "Can't decode empty list" }
                                    } else {
                                        DataResult.success(Ingredient.ItemValue(ItemStack(holders[0])))
                                    }
                                },
                            )
                        result.map { Pair.of(it, pair.second) }
                    }
            }
            return DataResult.error { "Can't access item registry" }
        }
    }
}
