package hiiragi283.lib.integration.jei

import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.ingredient.HTStackPreview
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.recipe.types.IRecipeType
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

//    IRecipeType    //

/**
 * @param T JEIに登録するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
typealias HTRecipeHolderType<T> = IRecipeType<HTRecipeHolder<T>>

/**
 * 新しい[IRecipeType]のインスタンスを作成します。
 * @param T JEIに登録するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
inline fun <reified T : Any> IRecipeType(id: Identifier): IRecipeType<T> = IRecipeType.create(id, T::class.java)

//    IIngredientAcceptor    //

// Fluid

/**
 * 液体を登録します。
 * @param stack 登録する液体
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun IRecipeSlotBuilder.add(stack: FluidStack): IRecipeSlotBuilder = this.add(NeoForgeTypes.FLUID_STACK, stack)

/**
 * 液体を登録します。
 * @param stacks 登録する液体の一覧
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun IRecipeSlotBuilder.addFluidStacks(stacks: Iterable<FluidStack>): IRecipeSlotBuilder =
    this.addIngredients(NeoForgeTypes.FLUID_STACK, stacks.toList())

/**
 * 液体を登録します。
 * @param ingredient 登録する液体の一覧
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmName("addFluids")
fun IRecipeSlotBuilder.add(ingredient: HTStackPreview<FluidStack>): IRecipeSlotBuilder =
    this.addFluidStacks(ingredient.getPreviewStacks(this.contextMap))

/**
 * 液体を登録します。
 * @param result 登録する液体
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun IRecipeSlotBuilder.add(result: HTFluidResult): IRecipeSlotBuilder = this.add(result.create())

// Item

/**
 * アイテムを登録します。
 * @param ingredient 登録するアイテムの一覧
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
@JvmName("addItems")
fun IRecipeSlotBuilder.add(ingredient: HTStackPreview<ItemStack>): IRecipeSlotBuilder =
    addItemStacks(ingredient.getPreviewStacks(this.contextMap))

/**
 * アイテムを登録します。
 * @param result 登録するアイテム
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun IRecipeSlotBuilder.add(result: HTItemResult): IRecipeSlotBuilder = this.add(result.create())
