package hiiragi283.lib.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.HTJeiDrawables
import hiiragi283.lib.integration.jei.ingredient.HTFluidSlotRenderer
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.lib.resource.toLanguageKey
import hiiragi283.lib.text.Text
import hiiragi283.lib.text.translatableText
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IDrawableWidget
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import mezz.jei.api.recipe.category.IRecipeCategory
import mezz.jei.api.recipe.types.IRecipeType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.RecipeType

/**
 * Hiiragi Seriesで使用される[IRecipeCategory]の拡張クラスです。
 *
 * 参照 : [Mekanism - BaseRecipeCategory](https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/client/recipe_viewer/jei/BaseRecipeCategory.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTBasicRecipeCategory<RECIPE : Any>(
    private val recipeType: IRecipeType<RECIPE>,
    private val title: Text,
    private val icon: IDrawable,
    private val width: Int,
    private val height: Int
) : IRecipeCategory<RECIPE> {
    constructor(
        recipeType: IRecipeType<RECIPE>,
        recipeType1: RecipeType<*>,
        icon: IDrawable,
        width: Int,
        height: Int
    ) : this(
        recipeType,
        translatableText(BuiltInRegistries.RECIPE_TYPE.wrapAsHolder(recipeType1).getKeyOrThrow().toLanguageKey()),
        icon,
        width,
        height
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
    protected fun IRecipeSlotBuilder.setSlotBackground(type: HTBackgroundType): IRecipeSlotBuilder = this
        .setBackground(HTJeiDrawables.getSlot(type), -1, -1)
        .setSlotName(type.name)
        .setCustomRenderer(NeoForgeTypes.FLUID_STACK, HTFluidSlotRenderer)

    // IRecipeExtrasBuilder
    protected fun IRecipeExtrasBuilder.addRecipePlus(x: Int, y: Int = getPosition(0)): IDrawableWidget =
        this.addRecipePlusSignWidget().setPosition(x + 2, y + 2)

    protected fun IRecipeExtrasBuilder.addRecipeArrow(progressData: HTProgressData): IDrawableWidget =
        when (progressData) {
            is HTProgressData.Energy -> this.addRecipeArrowWidget()
            is HTProgressData.Time -> this.addAnimatedRecipeArrowWidget(progressData.value)
        }.setTooltip(progressData.getText())

    protected fun IRecipeExtrasBuilder.addRecipeArrow(recipe: HTProgressRecipe.Simple<*>): IDrawableWidget =
        this.addRecipeArrow(recipe.progressData)
}
