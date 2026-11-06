package hiiragi283.lib.recipe.handler.cache

import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.base.HTFluidToRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToRecipe
import hiiragi283.lib.recipe.base.HTItemOrFluidToRecipe
import hiiragi283.lib.recipe.base.HTItemToRecipe
import hiiragi283.lib.recipe.base.HTSizedShapelessRecipe
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import net.minecraft.core.BlockPos
import net.minecraft.core.TypedInstance
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidInstance
import java.util.function.BiPredicate

/**
 * Hiiragi Seriesで使用される[HTBaseRecipeCache]の実装をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
data object HTRecipeCacheHelper {
    //    Single    //

    /**
     * 1種類の液体に対する[HTSingleRecipeCache.Typed]の実装クラスです。
     */
    class SingleFluid<RECIPE : HTFluidToRecipe<*>>(lookup: HTRecipeLookup<RECIPE>) :
        HTSingleRecipeCache.Typed<Fluid, FluidInstance, RECIPE>(lookup, HTFluidToRecipe<*>::ingredient) {
        override fun isEmpty(input: FluidInstance): Boolean = HTIngredientHelper.isEmpty(input)
    }

    /**
     * 1種類のアイテムに対する[HTSingleRecipeCache.Typed]の実装クラスです。
     */
    class SingleItem<RECIPE : HTItemToRecipe<*>>(lookup: HTRecipeLookup<RECIPE>) :
        HTSingleRecipeCache.Typed<Item, ItemInstance, RECIPE>(lookup, HTItemToRecipe<*>::ingredient) {
        override fun isEmpty(input: ItemInstance): Boolean = HTIngredientHelper.isEmpty(input)
    }

    /**
     * 複数のアイテムに対する[HTSingleRecipeCache]の実装クラスです。
     */
    class MultiItem<RECIPE : HTSizedShapelessRecipe>(lookup: HTRecipeLookup<RECIPE>) :
        HTSingleRecipeCache<List<ItemInstance>, RECIPE>(lookup) {
        override fun isEmpty(input: List<ItemInstance>): Boolean = HTIngredientHelper.isEmpty(input)

        override fun matches(recipe: RECIPE, input: List<ItemInstance>): Boolean = recipe.test(input)
    }

    //    Double    //

    /**
     * 2種類のアイテムに対する[HTDoubleRecipeCache]の実装クラスです。
     */
    class DoubleItem<RECIPE : BiPredicate<ItemInstance, ItemInstance>>(lookup: HTRecipeLookup<RECIPE>, val isStrict: Boolean) :
        HTDoubleRecipeCache<ItemInstance, ItemInstance, RECIPE>(lookup) {
        override fun isEmpty(first: ItemInstance, second: ItemInstance): Boolean = when (isStrict) {
            true -> HTIngredientHelper.isEmpty(first) || HTIngredientHelper.isEmpty(second)
            false -> HTIngredientHelper.isEmpty(first) && HTIngredientHelper.isEmpty(second)
        }

        override fun matches(recipe: RECIPE, first: ItemInstance, second: ItemInstance): Boolean = recipe.test(first, second)
    }

    /**
     * 1種類のアイテムと液体に対する[HTDoubleRecipeCache]の実装クラスです。
     */
    class ItemAndFluid<RECIPE : BiPredicate<ItemInstance, FluidInstance>>(lookup: HTRecipeLookup<RECIPE>, val isStrict: Boolean) :
        HTDoubleRecipeCache<ItemInstance, FluidInstance, RECIPE>(lookup) {
        override fun isEmpty(first: ItemInstance, second: FluidInstance): Boolean = when (isStrict) {
            true -> HTIngredientHelper.isEmpty(first) || HTIngredientHelper.isEmpty(second)
            false -> HTIngredientHelper.isEmpty(first) && HTIngredientHelper.isEmpty(second)
        }

        override fun matches(recipe: RECIPE, first: ItemInstance, second: FluidInstance): Boolean = recipe.test(first, second)
    }

    /**
     * 1種類のアイテムと液体に対する[HTDoubleRecipeCache]の実装クラスです。
     */
    class ItemAndFluidN<RECIPE : HTItemAndFluidToRecipe<*>>(lookup: HTRecipeLookup<RECIPE>) :
        HTDoubleRecipeCache.SimpleTyped<Item, ItemInstance, Fluid, FluidInstance, RECIPE>(
            lookup,
            HTItemAndFluidToRecipe<*>::itemIngredient,
            HTItemAndFluidToRecipe<*>::fluidIngredient,
        ) {
        override fun isEmpty(first: ItemInstance, second: FluidInstance): Boolean =
            HTIngredientHelper.isEmpty(first) || HTIngredientHelper.isEmpty(second)
    }

    /**
     * 1種類のアイテムまたは液体に対する[HTSingleRecipeCache]の実装クラスです。
     * @since 26.1.13
     */
    class ItemOrFluid<RECIPE : HTItemOrFluidToRecipe<*>>(lookup: HTRecipeLookup<RECIPE>) :
        HTDoubleRecipeCache.Typed<Item, ItemInstance, Fluid, FluidInstance, RECIPE>(lookup) {
        override fun isEmpty(first: ItemInstance, second: FluidInstance): Boolean =
            HTIngredientHelper.isEmpty(first) || HTIngredientHelper.isEmpty(second)

        override fun matches(recipe: RECIPE, first: ItemInstance, second: FluidInstance): Boolean = recipe.test(first, second)

        override fun findAllHolderForFirst(input: TypedInstance<Item>, context: HTRecipeLookup.Context): Sequence<HTRecipeHolder<RECIPE>> =
            lookup.getAllRecipes(context).filter { (_, recipe: RECIPE) -> recipe.ingredient.fold({ it.testOnlyType(input) }, { false }) }

        override fun findAllHolderForSecond(input: TypedInstance<Fluid>, context: HTRecipeLookup.Context): Sequence<HTRecipeHolder<RECIPE>> =
            lookup.getAllRecipes(context).filter { (_, recipe: RECIPE) -> recipe.ingredient.fold({ false }, { it.testOnlyType(input) }) }
    }

    class Located<RECIPE : BiPredicate<ServerLevel, BlockPos>>(lookup: HTRecipeLookup<RECIPE>) :
        HTDoubleRecipeCache<ServerLevel, BlockPos, RECIPE>(lookup) {
        override fun isEmpty(first: ServerLevel, second: BlockPos): Boolean = first.isOutsideBuildHeight(second)

        override fun matches(recipe: RECIPE, first: ServerLevel, second: BlockPos): Boolean = recipe.test(first, second)
    }
}
