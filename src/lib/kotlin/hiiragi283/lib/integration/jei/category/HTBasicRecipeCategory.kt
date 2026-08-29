package hiiragi283.lib.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.HTJeiDrawables
import hiiragi283.lib.integration.jei.HTJeiRecipeType
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.text.Text
import hiiragi283.lib.util.Either
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.placement.IPlaceable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import mezz.jei.api.recipe.category.IRecipeCategory
import mezz.jei.api.recipe.types.IRecipeType
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack

/**
 * Hiiragi Seriesで使用される[IRecipeCategory]の拡張クラスです。
 *
 * 参照 : [Mekanism - BaseRecipeCategory](https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/client/recipe_viewer/jei/BaseRecipeCategory.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTBasicRecipeCategory<RECIPE : Any>(
    private val guiHelper: IGuiHelper,
    private val recipeType: IRecipeType<RECIPE>,
    private val title: Text,
    private val icon: IDrawable,
    private val width: Int,
    private val height: Int,
) : IRecipeCategory<RECIPE> {
    companion object {
        @JvmStatic
        protected fun createIcon(guiHelper: IGuiHelper, icon: Either<Identifier, ItemStack>): IDrawable = icon.fold(
            { id: Identifier -> guiHelper.drawableBuilder(id, 0, 0, 18, 18).setTextureSize(18, 18).build() },
            { stack: ItemStack -> guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, stack) },
        )
    }

    constructor(guiHelper: IGuiHelper, recipeType: HTJeiRecipeType<RECIPE>, width: Int, height: Int) : this(
        guiHelper,
        recipeType,
        recipeType.getText(),
        createIcon(guiHelper, recipeType.icon),
        width,
        height,
    )

    //    IRecipeCategory    //

    final override fun getRecipeType(): IRecipeType<RECIPE> = recipeType

    final override fun getTitle(): Text = title

    override fun getWidth(): Int = width

    override fun getHeight(): Int = height

    final override fun getIcon(): IDrawable = icon

    abstract override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup)

    abstract override fun getIdentifier(recipe: RECIPE): Identifier?

    abstract override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<RECIPE>

    //    Extensions    //

    /**
     * 指定した[インデックス][index]から座標を返します。
     */
    fun getPosition(index: Int): Int = index * 18

    /**
     * 指定した[インデックス][index]から座標を返します。
     */
    fun getPosition(index: Float): Int = (index * 18).toInt()

    /**
     * 指定した[インデックス][index]から座標を返します。
     */
    fun getPosition(index: Double): Int = (index * 18).toInt()

    // IRecipeSlotBuilder
    protected fun IRecipeSlotBuilder.setSlotBackground(type: HTBackgroundType): IRecipeSlotBuilder = this.setBackground(HTJeiDrawables.getSlot(type, guiHelper), -1, -1).setSlotName(type.name)

    protected fun IRecipeSlotBuilder.setSlotBackground(type: HTBackgroundType, capacity: Int): IRecipeSlotBuilder = this
        .setBackground(HTJeiDrawables.getSlot(type, guiHelper), -1, -1)
        .setSlotName(type.name)
        .setFluidRenderer(fixCapacity(capacity), false, 16, 16)

    protected fun IRecipeSlotBuilder.setTankBackground(type: HTBackgroundType, capacity: Int): IRecipeSlotBuilder = this
        .setBackground(HTJeiDrawables.getTank(type, guiHelper), -1, -1)
        .setSlotName(type.name)
        .setFluidRenderer(fixCapacity(capacity), false, 16, 18 * 3 - 2)

    private fun fixCapacity(capacity: Int): Long = maxOf(capacity, 1).toLong()

    // IRecipeExtrasBuilder
    protected fun IRecipeExtrasBuilder.addRecipePlus(x: Int, y: Int = getPosition(0)): IPlaceable<*> = this.addRecipePlusSign().setPosition(x + 2, y + 2)

    protected fun IRecipeExtrasBuilder.addRecipeArrow(progressData: HTProgressData): IPlaceable<*> = when (progressData) {
        is HTProgressData.Energy -> this.addRecipeArrow()
        is HTProgressData.Time -> this.addAnimatedRecipeArrow(progressData.value)
    }

    protected fun IRecipeExtrasBuilder.addRecipeArrow(recipe: HTProgressRecipe.Simple<*>): IPlaceable<*> = this.addRecipeArrow(recipe.progressData)
}
