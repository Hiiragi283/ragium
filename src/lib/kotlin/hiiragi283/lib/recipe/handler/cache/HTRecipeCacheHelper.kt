package hiiragi283.lib.recipe.handler.cache

import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemInstance
import net.neoforged.neoforge.fluids.FluidInstance
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * Hiiragi Seriesで使用される[HTBaseRecipeCache]の実装をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
data object HTRecipeCacheHelper {
    //    Single    //

    /**
     * 1種類の液体に対する[HTSingleRecipeCache]の実装クラスです。
     */
    class SingleFluid<RECIPE : Predicate<FluidInstance>>(lookup: HTRecipeLookup<RECIPE>) :
        HTSingleRecipeCache<FluidInstance, RECIPE>(lookup) {
        override fun isEmpty(input: FluidInstance): Boolean = HTIngredientHelper.isEmpty(input)
    }

    /**
     * 1種類のアイテムに対する[HTSingleRecipeCache]の実装クラスです。
     */
    class SingleItem<RECIPE : Predicate<ItemInstance>>(lookup: HTRecipeLookup<RECIPE>) :
        HTSingleRecipeCache<ItemInstance, RECIPE>(lookup) {
        override fun isEmpty(input: ItemInstance): Boolean = HTIngredientHelper.isEmpty(input)
    }

    /**
     * 複数のアイテムに対する[HTSingleRecipeCache]の実装クラスです。
     */
    class MultiItem<RECIPE : Predicate<List<ItemInstance>>>(lookup: HTRecipeLookup<RECIPE>) :
        HTSingleRecipeCache<List<ItemInstance>, RECIPE>(lookup) {
        override fun isEmpty(input: List<ItemInstance>): Boolean =
            input.isEmpty() || input.all(HTIngredientHelper::isEmpty)
    }

    //    Double    //

    /**
     * 2種類のアイテムに対する[HTDoubleRecipeCache]の実装クラスです。
     */
    class DoubleItem<RECIPE : BiPredicate<ItemInstance, ItemInstance>>(
        lookup: HTRecipeLookup<RECIPE>,
        val isStrict: Boolean
    ) : HTDoubleRecipeCache<ItemInstance, ItemInstance, RECIPE>(lookup) {
        override fun isEmpty(first: ItemInstance, second: ItemInstance): Boolean = when (isStrict) {
            true -> HTIngredientHelper.isEmpty(first) || HTIngredientHelper.isEmpty(second)
            false -> HTIngredientHelper.isEmpty(first) && HTIngredientHelper.isEmpty(second)
        }
    }

    /**
     * 1種類のアイテムと液体に対する[HTDoubleRecipeCache]の実装クラスです。
     */
    class ItemAndFluid<RECIPE : BiPredicate<ItemInstance, FluidInstance>>(
        lookup: HTRecipeLookup<RECIPE>,
        val isStrict: Boolean
    ) : HTDoubleRecipeCache<ItemInstance, FluidInstance, RECIPE>(lookup) {
        override fun isEmpty(first: ItemInstance, second: FluidInstance): Boolean = when (isStrict) {
            true -> HTIngredientHelper.isEmpty(first) || HTIngredientHelper.isEmpty(second)
            false -> HTIngredientHelper.isEmpty(first) && HTIngredientHelper.isEmpty(second)
        }
    }

    class Located<RECIPE : BiPredicate<ServerLevel, BlockPos>>(lookup: HTRecipeLookup<RECIPE>) :
        HTDoubleRecipeCache<ServerLevel, BlockPos, RECIPE>(lookup) {
        override fun isEmpty(first: ServerLevel, second: BlockPos): Boolean = first.isOutsideBuildHeight(second)
    }
}
