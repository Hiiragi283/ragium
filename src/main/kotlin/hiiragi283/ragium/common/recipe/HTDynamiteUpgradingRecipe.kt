package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.component.HTExplosionComponent
import hiiragi283.ragium.api.extension.isOf
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItemsNew
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.SmithingRecipe
import net.minecraft.recipe.input.SmithingRecipeInput
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.ItemTags
import net.minecraft.world.World

object HTDynamiteUpgradingRecipe : SmithingRecipe {
    override fun testTemplate(stack: ItemStack): Boolean = stack.isOf(RagiumItemsNew.Dynamites.SIMPLE)

    override fun testBase(stack: ItemStack): Boolean = stack.isOf(Items.GUNPOWDER)

    override fun testAddition(stack: ItemStack): Boolean = stack.isIn(ItemTags.WOOL)

    override fun matches(input: SmithingRecipeInput, world: World): Boolean =
        testTemplate(input.template) && (testBase(input.base) || testAddition(input.addition))

    override fun craft(input: SmithingRecipeInput, lookup: RegistryWrapper.WrapperLookup): ItemStack {
        val result: ItemStack = input.template.copyWithCount(1)
        val explosion: HTExplosionComponent =
            result.getOrDefault(RagiumComponentTypes.DYNAMITE, HTExplosionComponent.DEFAULT)
        if (explosion.power >= 16f) return ItemStack.EMPTY
        val newPower: Float = explosion.power + (if (testBase(input.base)) 1f else 0f)
        val canDestroy: Boolean = !testAddition(input.addition)
        result.set(RagiumComponentTypes.DYNAMITE, HTExplosionComponent(newPower, canDestroy))
        return result
    }

    override fun getResult(registriesLookup: RegistryWrapper.WrapperLookup): ItemStack = ItemStack(RagiumItemsNew.Dynamites.SIMPLE)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.DYNAMITE_UPGRADE
}
