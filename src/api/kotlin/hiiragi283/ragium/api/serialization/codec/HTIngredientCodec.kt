package hiiragi283.ragium.api.serialization.codec

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.util.unwrapEither
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.IngredientType
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.CompoundFluidIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredientType
import net.neoforged.neoforge.fluids.crafting.SingleFluidIngredient
import net.neoforged.neoforge.fluids.crafting.TagFluidIngredient
import net.neoforged.neoforge.registries.NeoForgeRegistries

data object HTIngredientCodec {
    //    Item    //

    @JvmStatic
    private val CUSTOM_ITEM_CODEC: Codec<ICustomIngredient> = NeoForgeRegistries.INGREDIENT_TYPES
        .byNameCodec()
        .dispatch(ICustomIngredient::getType, IngredientType<*>::codec)

    @JvmStatic
    private val VALUE_CODEC: Codec<Ingredient.Value> =
        Codec
            .either(TagKey.hashedCodec(Registries.ITEM), BuiltInRegistries.ITEM.holderByNameCodec().listOrElement())
            .xmap(
                { either: Either<TagKey<Item>, List<Holder<Item>>> ->
                    either.map(
                        { tagKey: TagKey<Item> -> Ingredient.TagValue(tagKey) },
                        { holders: List<Holder<Item>> ->
                            if (holders.size == 1) {
                                holders[0].let(::ItemStack).let(Ingredient::ItemValue)
                            } else {
                                holders.map(::ItemStack).let(::StacksValue)
                            }
                        },
                    )
                },
                { value: Ingredient.Value ->
                    when (value) {
                        is Ingredient.TagValue -> Either.left(value.tag())
                        else -> Either.right(value.items.map(ItemStack::getItemHolder))
                    }
                },
            )

    @JvmField
    val ITEM: Codec<Ingredient> = Codec
        .either(VALUE_CODEC.listOrElement(), CUSTOM_ITEM_CODEC)
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

    @JvmRecord
    private data class StacksValue(private val stacks: List<ItemStack>) : Ingredient.Value {
        override fun getItems(): Collection<ItemStack> = stacks
    }

    //    Fluid    //

    @JvmStatic
    private val FLUID_HOLDER_CODEC: Codec<FluidIngredient> =
        Codec
            .either(TagKey.hashedCodec(Registries.FLUID), BuiltInRegistries.FLUID.holderByNameCodec().listOrElement())
            .xmap(
                { either: Either<TagKey<Fluid>, List<Holder<Fluid>>> ->
                    either.map(
                        FluidIngredient::tag,
                        { holders: List<Holder<Fluid>> ->
                            when (holders.size) {
                                0 -> FluidIngredient.empty()
                                1 -> FluidIngredient.single(holders[0])
                                else -> CompoundFluidIngredient(holders.map(FluidIngredient::single))
                            }
                        },
                    )
                },
                { ingredient: FluidIngredient ->
                    when (ingredient) {
                        is TagFluidIngredient -> Either.left(ingredient.tag())
                        else -> Either.right(ingredient.stacks.map(FluidStack::getFluidHolder))
                    }
                },
            )

    @JvmField
    val FLUID: MapCodec<FluidIngredient> = NeoForgeExtraCodecs
        .dispatchMapOrElse(
            NeoForgeRegistries.FLUID_INGREDIENT_TYPES.byNameCodec(),
            FluidIngredient::getType,
            FluidIngredientType<*>::codec,
            FLUID_HOLDER_CODEC.fieldOf(RagiumConst.FLUIDS),
        ).xmap(::unwrapEither) { ingredient: FluidIngredient ->
            when (ingredient) {
                is TagFluidIngredient -> Either.right(ingredient)
                is SingleFluidIngredient -> Either.right(ingredient)
                is CompoundFluidIngredient -> {
                    val children: List<FluidIngredient> = ingredient.children()
                    when {
                        children.all { it is SingleFluidIngredient } -> Either.right(ingredient)
                        else -> Either.left(ingredient)
                    }
                }
                else -> Either.left(ingredient)
            }
        }
}
