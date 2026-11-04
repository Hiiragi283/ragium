package hiiragi283.lib.recipe.ingredient

import hiiragi283.lib.data.HTDataComponentHelper
import hiiragi283.lib.registry.isAir
import hiiragi283.lib.registry.isEmpty
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.right
import net.minecraft.core.TypedInstance
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource

/**
 * [TypedInstance]の変換を補助するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTIngredientHelper {

    //    Fluid    //

    /**
     * [FluidInstance]を[ItemResource]または[FluidStack]に変換します。
     * @since 26.1.13
     */
    @JvmName("unwrapAsEitherFluid")
    @JvmStatic
    fun unwrapAsEither(instance: TypedInstance<Fluid>): Either<FluidResource, FluidStack> = when (instance) {
        is FluidInstance -> when (instance) {
            is FluidStack -> instance.copy()
            is FluidStackTemplate -> instance.create()
            else -> FluidStack(instance.typeHolder(), instance.amount(), HTDataComponentHelper.createPatch(instance))
        }.right()

        is FluidResource -> Either.Left(instance)

        else -> Either.Left(FluidResource.of(instance.typeHolder()))
    }

    /**
     * [FluidInstance]を[FluidStack]に変換します。
     * @since 26.1.13
     */
    @JvmName("unwrapAsStackFluid")
    @JvmStatic
    fun unwrapAsStack(instance: TypedInstance<Fluid>): FluidStack = when (instance) {
        is FluidInstance -> when (instance) {
            is FluidStack -> instance.copy()
            is FluidStackTemplate -> instance.create()
            else -> FluidStack(instance.typeHolder(), instance.amount(), HTDataComponentHelper.createPatch(instance))
        }

        is FluidResource -> instance.toStack(FluidType.BUCKET_VOLUME)

        else -> FluidStack(instance.typeHolder(), FluidType.BUCKET_VOLUME)
    }

    /**
     * 液体の[TypedInstance]が空かどうか判定します。
     */
    @JvmName("isEmptyFluid")
    @JvmStatic
    fun isEmpty(instance: TypedInstance<Fluid>): Boolean = when (instance) {
        is FluidInstance -> when (instance) {
            is FluidStack -> instance.isEmpty
            is FluidStackTemplate -> false
            else -> instance.typeHolder().isEmpty || instance.amount() <= 0
        }

        is FluidResource -> instance.isEmpty

        else -> instance.typeHolder().isEmpty
    }

    /**
     * @since 26.1.13
     */
    @JvmName("isEmptyFluids")
    @JvmStatic
    fun isEmpty(instances: Iterable<FluidInstance>): Boolean = instances.none() || instances.all(::isEmpty)

    /**
     * @since 26.1.7
     */
    @JvmStatic
    fun copyWithAmount(instance: FluidInstance, newAmount: Int): FluidInstance = when (instance) {
        is FluidStack -> instance.copyWithAmount(newAmount)
        is FluidStackTemplate -> instance.withAmount(newAmount)
        else -> FluidStackTemplate(instance.typeHolder(), newAmount, HTDataComponentHelper.createPatch(instance))
    }

    /**
     * @since 26.1.11
     */
    @JvmName("matchFluids")
    @JvmStatic
    fun match(ingredients: Iterable<HTFluidIngredient>, inputs: Iterable<FluidInstance>): Boolean {
        val combined: List<FluidStack> = inputs
            .fold(mutableMapOf()) { map: MutableMap<FluidResource, Int>, instance: FluidInstance ->
                val resource: FluidResource = unwrapAsStack(instance).let(FluidResource::of)
                if (!resource.isEmpty) {
                    map.compute(resource) { _, old: Int? -> (old ?: 0) + instance.amount() }
                }
                map
            }.map { (resource: FluidResource, count: Int) -> resource.toStack(count) }
        ingredient@for (ingredient: HTFluidIngredient in ingredients) {
            for (stack: FluidStack in combined) {
                if (ingredient.test(stack)) {
                    stack.amount -= ingredient.getMatchingStack(stack).amount()
                    continue@ingredient
                }
            }
            return false
        }
        return true
    }

    /**
     * @since 26.1.11
     */
    @JvmName("getMatchingFluids")
    @JvmStatic
    fun getMatchingStacks(ingredients: Iterable<HTFluidIngredient>, inputs: Iterable<FluidInstance>): List<FluidInstance> {
        val combined: List<FluidStack> = inputs
            .fold(mutableMapOf()) { map: MutableMap<FluidResource, Int>, instance: FluidInstance ->
                val resource: FluidResource = unwrapAsStack(instance).let(FluidResource::of)
                if (!resource.isEmpty) {
                    map.compute(resource) { _, old: Int? -> (old ?: 0) + instance.amount() }
                }
                map
            }.map { (resource: FluidResource, count: Int) -> resource.toStack(count) }
        val results: MutableList<FluidInstance> = mutableListOf()
        ingredient@for (ingredient: HTFluidIngredient in ingredients) {
            for (stack: FluidStack in combined) {
                if (ingredient.test(stack)) {
                    val stackMatched: FluidInstance = ingredient.getMatchingStack(stack)
                    results += stackMatched
                    stack.amount -= stackMatched.amount()
                    continue@ingredient
                }
            }
            return listOf()
        }
        return results
    }

    //    Item    //

    /**
     * [ItemInstance]を[ItemResource]または[ItemStack]に変換します。
     * @since 26.1.13
     */
    @JvmName("unwrapAsEitherItem")
    @JvmStatic
    fun unwrapAsEither(instance: TypedInstance<Item>): Either<ItemResource, ItemStack> = when (instance) {
        is ItemInstance -> when (instance) {
            is ItemStack -> instance.copy()
            is ItemStackTemplate -> instance.create()
            else -> ItemStack(instance.typeHolder(), instance.count(), HTDataComponentHelper.createPatch(instance))
        }.right()

        is ItemResource -> Either.Left(instance)

        else -> Either.Left(ItemResource.of(instance.typeHolder()))
    }

    /**
     * [ItemInstance]を[ItemStack]に変換します。
     * @since 26.1.13
     */
    @JvmName("unwrapAsStackItem")
    @JvmStatic
    fun unwrapAsStack(instance: TypedInstance<Item>): ItemStack = when (instance) {
        is ItemInstance -> when (instance) {
            is ItemStack -> instance.copy()
            is ItemStackTemplate -> instance.create()
            else -> ItemStack(instance.typeHolder(), instance.count(), HTDataComponentHelper.createPatch(instance))
        }

        is ItemResource -> instance.toStack()

        else -> ItemStack(instance.typeHolder())
    }

    /**
     * アイテムの[TypedInstance]が空かどうか判定します。
     */
    @JvmName("isEmptyItem")
    @JvmStatic
    fun isEmpty(instance: TypedInstance<Item>): Boolean = when (instance) {
        is ItemInstance -> when (instance) {
            is ItemStack -> instance.isEmpty
            is ItemStackTemplate -> false
            else -> instance.typeHolder().isAir || instance.count() <= 0
        }

        is ItemResource -> instance.isEmpty

        else -> instance.typeHolder().isAir
    }

    /**
     * @since 26.1.13
     */
    @JvmName("isEmptyItems")
    @JvmStatic
    fun isEmpty(instances: Iterable<TypedInstance<Item>>): Boolean = instances.none() || instances.all(::isEmpty)

    /**
     * @since 26.1.7
     */
    @JvmStatic
    fun copyWithCount(instance: ItemInstance, newCount: Int): ItemInstance = when (instance) {
        is ItemStack -> instance.copyWithCount(newCount)
        is ItemStackTemplate -> instance.withCount(newCount)
        else -> ItemStackTemplate(instance.typeHolder(), newCount, HTDataComponentHelper.createPatch(instance))
    }

    /**
     * @since 26.1.11
     */
    @JvmName("matchItems")
    @JvmStatic
    fun match(ingredients: Iterable<HTItemIngredient>, inputs: Iterable<ItemInstance>): Boolean {
        val combined: List<ItemStack> = inputs
            .fold(mutableMapOf()) { map: MutableMap<ItemResource, Int>, instance: ItemInstance ->
                val resource: ItemResource = unwrapAsStack(instance).let(ItemResource::of)
                if (!resource.isEmpty) {
                    map.compute(resource) { _, old: Int? -> (old ?: 0) + instance.count() }
                }
                map
            }.map { (resource: ItemResource, count: Int) -> resource.toStack(count) }
        ingredient@for (ingredient: HTItemIngredient in ingredients) {
            for (stack: ItemStack in combined) {
                if (ingredient.test(stack)) {
                    stack.count -= ingredient.getMatchingStack(stack).count()
                    continue@ingredient
                }
            }
            return false
        }
        return true
    }

    /**
     * @since 26.1.11
     */
    @JvmName("getMatchingItems")
    @JvmStatic
    fun getMatchingStacks(ingredients: Iterable<HTItemIngredient>, inputs: Iterable<ItemInstance>): List<ItemInstance> {
        val combined: List<ItemStack> = inputs
            .fold(mutableMapOf()) { map: MutableMap<ItemResource, Int>, instance: ItemInstance ->
                val resource: ItemResource = unwrapAsStack(instance).let(ItemResource::of)
                if (!resource.isEmpty) {
                    map.compute(resource) { _, old: Int? -> (old ?: 0) + instance.count() }
                }
                map
            }.map { (resource: ItemResource, count: Int) -> resource.toStack(count) }
        val results: MutableList<ItemInstance> = mutableListOf()
        ingredient@for (ingredient: HTItemIngredient in ingredients) {
            for (stack: ItemStack in combined) {
                if (ingredient.test(stack)) {
                    val stackMatched: ItemInstance = ingredient.getMatchingStack(stack)
                    results += stackMatched
                    stack.count -= stackMatched.count()
                    continue@ingredient
                }
            }
            return listOf()
        }
        return results
    }
}
