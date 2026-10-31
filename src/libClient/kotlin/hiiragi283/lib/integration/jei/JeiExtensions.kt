package hiiragi283.lib.integration.jei

import hiiragi283.lib.recipe.display.HTDisplayableContent
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.ingredients.IIngredientType
import mezz.jei.api.recipe.IFocus
import mezz.jei.api.recipe.types.IRecipeType
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.display.SlotDisplay
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

//    IRecipeType    //

/**
 * 新しい[IRecipeType]のインスタンスを作成します。
 * @param T JEIに登録するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
inline fun <reified T : Any> IRecipeType(id: Identifier): IRecipeType<T> = IRecipeType.create(id, T::class.java)

//    IIngredientAcceptor    //

/**
 * [HTDisplayableContent]からプレビューを登録します。
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
fun IRecipeSlotBuilder.add(content: HTDisplayableContent): IRecipeSlotBuilder = this.add(content.display())

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
fun IRecipeSlotBuilder.addOptional(slot: SlotDisplay?): IRecipeSlotBuilder = apply { slot?.let(this::add) }

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
fun IRecipeSlotBuilder.addOptional(slot: Optional<SlotDisplay>): IRecipeSlotBuilder = apply { slot.ifPresent(this::add) }

//    IFocus    //

fun <T : Any> IFocus<*>.getIngredient(type: IIngredientType<T>): T? = this.typedValue.getIngredient(type).getOrNull()
