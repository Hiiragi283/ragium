package hiiragi283.ragium.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.integration.jei.RagiumJEIPlugin
import hiiragi283.ragium.integration.jei.stacks
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.placement.HorizontalAlignment
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.ingredients.IIngredientTypeWithSubtypes
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import mezz.jei.api.recipe.RecipeType
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import kotlin.jvm.optionals.getOrNull

class HTMachineRecipeCategory(val machine: HTMachineKey, val guiHelper: IGuiHelper) : HTRecipeCategory<RecipeHolder<HTMachineRecipe>> {
    companion object {
        @JvmField
        val ITEM_TYPE: IIngredientTypeWithSubtypes<Item, ItemStack> = VanillaTypes.ITEM_STACK

        @JvmField
        val FLUID_TYPE: IIngredientTypeWithSubtypes<Fluid, FluidStack> = NeoForgeTypes.FLUID_STACK
    }

    override fun setRecipe(builder: IRecipeLayoutBuilder, holder: RecipeHolder<HTMachineRecipe>, focuses: IFocusGroup) {
        val recipe: HTMachineRecipe = holder.value
        // inputs
        addItemInput(builder, recipe, 0, 0, 0)
        addItemInput(builder, recipe, 1, 0, 1)
        addItemInput(builder, recipe, 2, 0, 2)
        addFluidInput(builder, recipe, 0, 1, 0)
        addFluidInput(builder, recipe, 1, 1, 1)
        addFluidInput(builder, recipe, 2, 1, 2)
        // catalyst
        addCatalyst(builder, recipe, 1)
        // outputs
        addItemOutput(builder, recipe, 5, 0, 0)
        addItemOutput(builder, recipe, 6, 0, 1)
        addItemOutput(builder, recipe, 7, 0, 2)
        addFluidOutput(builder, recipe, 5, 1, 0)
        addFluidOutput(builder, recipe, 6, 1, 1)
        addFluidOutput(builder, recipe, 7, 1, 2)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RecipeHolder<HTMachineRecipe>, focuses: IFocusGroup) {
        // Recipe Arrow
        builder.addRecipeArrow().setPosition(getPosition(3.5), getPosition(0))
        // Recipe Cost
        val tier: HTMachineTier = recipe.value.machineTier
        builder
            .addText(Component.literal("Recipe Cost: ${tier.processCost} FE"), width - 4, 10)
            .setPosition(getPosition(0), getPosition(2.25))
            .setColor(0xFFFFFF)
            .setShadow(true)
            .setTextAlignment(HorizontalAlignment.LEFT)
        // Recipe tier
        builder
            .addText(tier.text, width - 4, 10)
            .setPosition(getPosition(0), getPosition(2.75))
            .setShadow(true)
            .setTextAlignment(HorizontalAlignment.LEFT)
    }

    private fun addItemInput(
        builder: IRecipeLayoutBuilder,
        recipe: HTMachineRecipe,
        x: Int,
        y: Int,
        index: Int,
    ) {
        val stacks: List<ItemStack> = recipe.itemInputs.getOrNull(index)?.stacks ?: listOf()
        builder
            .addInputSlot(getPosition(x), getPosition(y))
            .setStandardSlotBackground()
            .addIngredients(ITEM_TYPE, stacks)
    }

    private fun addCatalyst(builder: IRecipeLayoutBuilder, recipe: HTMachineRecipe, y: Int) {
        val ingredient: Ingredient = recipe.catalyst.getOrNull() ?: Ingredient.EMPTY
        builder
            .addInputSlot(5 + 9 + 3 * 18, getPosition(y))
            .setStandardSlotBackground()
            .addItemStacks(ingredient.items.toList())
    }

    private fun addFluidInput(
        builder: IRecipeLayoutBuilder,
        recipe: HTMachineRecipe,
        x: Int,
        y: Int,
        index: Int,
    ) {
        val builder1: IRecipeSlotBuilder =
            builder.addInputSlot(getPosition(x), getPosition(y)).setStandardSlotBackground()
        val ingredient: SizedFluidIngredient = recipe.fluidInputs.getOrNull(index) ?: return
        builder1.addIngredients(FLUID_TYPE, ingredient.stacks)
        builder1.setFluidRenderer(ingredient.amount().toLong(), false, 16, 16)
    }

    private fun addItemOutput(
        builder: IRecipeLayoutBuilder,
        recipe: HTMachineRecipe,
        x: Int,
        y: Int,
        index: Int,
    ) {
        val stack: ItemStack = recipe.getItemOutput(index) ?: ItemStack.EMPTY
        builder
            .addOutputSlot(getPosition(x), getPosition(y))
            .setStandardSlotBackground()
            .addItemStack(stack)
    }

    private fun addFluidOutput(
        builder: IRecipeLayoutBuilder,
        recipe: HTMachineRecipe,
        x: Int,
        y: Int,
        index: Int,
    ) {
        val builder1: IRecipeSlotBuilder =
            builder.addInputSlot(getPosition(x), getPosition(y)).setStandardSlotBackground()
        val stack: FluidStack = recipe.getFluidOutput(index) ?: return
        val amount: Long = stack.amount.toLong()
        builder1.addFluidStack(stack.fluid, amount.toLong())
        builder1.setFluidRenderer(amount, false, 16, 16)
    }

    //    IRecipeCategory    //

    override fun getRecipeType(): RecipeType<RecipeHolder<HTMachineRecipe>> = RagiumJEIPlugin.getRecipeType(machine)

    override fun getTitle(): Component = machine.text

    override fun getWidth(): Int = 18 * 8 + 8

    override fun getHeight(): Int = 18 * 3 + 8

    private var iconCache: ItemStack? = null

    override fun getIcon(): IDrawable? {
        val stack: ItemStack = if (iconCache != null) {
            iconCache!!
        } else {
            for (tier: HTMachineTier in HTMachineTier.entries) {
                iconCache = machine.createItemStack(tier)
                if (iconCache != null) break
            }
            iconCache ?: return null
        }
        return guiHelper.createDrawableItemStack(stack)
    }

    override fun isHandled(recipe: RecipeHolder<HTMachineRecipe>): Boolean = !recipe.value.isSpecial

    override fun getRegistryName(recipe: RecipeHolder<HTMachineRecipe>): ResourceLocation = recipe.id

    override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<RecipeHolder<HTMachineRecipe>> =
        codecHelper.getRecipeHolderCodec()
}
