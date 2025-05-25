package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.CauldronFluidContent

interface HTCauldronDroppingRecipe : Recipe<HTInteractRecipeInput> {
    val fluid: Fluid
    val minLevel: Int
    val ingredient: Ingredient

    //    Recipe    //

    override fun matches(input: HTInteractRecipeInput, level: Level): Boolean {
        // 大釜が一致しているか判定
        val cauldron: CauldronFluidContent = CauldronFluidContent.getForFluid(fluid) ?: return false
        val state: BlockState = level.getBlockState(input.pos)
        val cauldron1: CauldronFluidContent = CauldronFluidContent.getForBlock(state.block) ?: return false
        if (cauldron != cauldron1) return false
        // 液体量が条件を満たしているか判定
        if (cauldron1.maxLevel < minLevel) return false
        val currentLevel: Int = cauldron1.currentLevel(state)
        if (currentLevel < minLevel) return false
        // 材料が一致してるか判定
        return ingredient.test(input.item)
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    override fun getToastSymbol(): ItemStack = ItemStack(Items.CAULDRON)

    override fun getType(): RecipeType<*> = RagiumAPI.getInstance().getCauldronDroppingRecipeType()
}
