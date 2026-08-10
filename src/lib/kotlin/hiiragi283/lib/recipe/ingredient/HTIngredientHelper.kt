package hiiragi283.lib.recipe.ingredient

import hiiragi283.lib.registry.isAir
import hiiragi283.lib.registry.isEmpty
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.left
import hiiragi283.lib.util.right
import hiiragi283.lib.util.unwrap
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
     * [TypedInstance]を[FluidStack]に変換します。
     */
    @JvmName("createFluidStack")
    @JvmStatic
    fun createStack(instance: TypedInstance<Fluid>): FluidStack = unwrap(instance).mapLeft { it.toStack(FluidType.BUCKET_VOLUME) }.unwrap()

    /**
     * [TypedInstance]を[FluidStack]に変換します。
     */
    @JvmName("createFluidStack")
    @JvmStatic
    fun createStack(instance: TypedInstance<Fluid>, amount: Int): FluidStack = unwrap(instance).fold({ it.toStack(amount) }, { it.copyWithAmount(amount) })

    /**
     * [TypedInstance]を[FluidResource]または[FluidStack]に変換します。
     */
    @JvmName("unwrapFluidInstance")
    @JvmStatic
    fun unwrap(instance: TypedInstance<Fluid>): Either<FluidResource, FluidStack> = when (instance) {
        is FluidInstance -> {
            when (instance) {
                is FluidStack -> instance
                is FluidStackTemplate -> instance.create()
                else -> FluidStack(instance.typeHolder(), instance.amount())
            }.right()
        }
        is FluidResource -> instance.left()
        else -> FluidResource.of(instance.typeHolder()).left()
    }

    /**
     * [TypedInstance]が空かどうか判定します。
     */
    @JvmName("isEmptyFluid")
    @JvmStatic
    fun isEmpty(instance: TypedInstance<Fluid>): Boolean = when (instance) {
        is FluidInstance -> {
            when (instance) {
                is FluidStack -> instance.isEmpty
                is FluidStackTemplate -> false
                else -> instance.typeHolder().isEmpty || instance.amount() <= 0
            }
        }
        is FluidResource -> instance.isEmpty
        else -> instance.typeHolder().isEmpty
    }

    //    Item    //

    /**
     * [TypedInstance]を[ItemStack]に変換します。
     */
    @JvmName("createItemStack")
    @JvmStatic
    fun createStack(instance: TypedInstance<Item>): ItemStack = unwrap(instance).mapLeft { it.toStack() }.unwrap()

    /**
     * [TypedInstance]を[ItemStack]に変換します。
     */
    @JvmName("createItemStack")
    @JvmStatic
    fun createStack(instance: TypedInstance<Item>, count: Int): ItemStack = unwrap(instance).fold({ it.toStack(count) }, { it.copyWithCount(count) })

    /**
     * [TypedInstance]を[ItemResource]または[ItemStack]に変換します。
     */
    @JvmName("unwrapItemInstance")
    @JvmStatic
    fun unwrap(instance: TypedInstance<Item>): Either<ItemResource, ItemStack> = when (instance) {
        is ItemInstance -> {
            when (instance) {
                is ItemStack -> instance
                is ItemStackTemplate -> instance.create()
                else -> ItemStack(instance.typeHolder(), instance.count())
            }.right()
        }
        is ItemResource -> instance.left()
        else -> ItemResource.of(instance.typeHolder()).left()
    }

    /**
     * [TypedInstance]が空かどうか判定します。
     */
    @JvmName("isEmptyItem")
    @JvmStatic
    fun isEmpty(instance: TypedInstance<Item>): Boolean = when (instance) {
        is ItemInstance -> {
            when (instance) {
                is ItemStack -> instance.isEmpty
                is ItemStackTemplate -> false
                else -> instance.typeHolder().isAir || instance.count() <= 0
            }
        }
        is ItemResource -> instance.isEmpty
        else -> instance.typeHolder().isAir
    }
}
