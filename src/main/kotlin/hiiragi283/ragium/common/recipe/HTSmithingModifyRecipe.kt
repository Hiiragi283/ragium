package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.codec.BiCodecs
import hiiragi283.ragium.api.codec.MapBiCodec
import hiiragi283.ragium.setup.RagiumRecipeSerializersImpl
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SmithingRecipe
import net.minecraft.world.item.crafting.SmithingRecipeInput
import net.minecraft.world.level.Level

class HTSmithingModifyRecipe(val template: Ingredient, val addition: Ingredient, val components: DataComponentPatch) : SmithingRecipe {
    companion object {
        @JvmField
        val CODEC: MapBiCodec<RegistryFriendlyByteBuf, HTSmithingModifyRecipe> = MapBiCodec.composite(
            BiCodecs.ingredient(false).fieldOf("template"),
            HTSmithingModifyRecipe::template,
            BiCodecs.ingredient(true).fieldOf("addition"),
            HTSmithingModifyRecipe::addition,
            BiCodecs.COMPONENT_PATCH.fieldOf("components"),
            HTSmithingModifyRecipe::components,
            ::HTSmithingModifyRecipe,
        )
    }

    override fun isTemplateIngredient(stack: ItemStack): Boolean = template.test(stack)

    override fun isBaseIngredient(stack: ItemStack): Boolean = stack.isDamageableItem

    override fun isAdditionIngredient(stack: ItemStack): Boolean = addition.test(stack)

    override fun matches(input: SmithingRecipeInput, level: Level): Boolean =
        template.test(input.template) && !input.base.isEmpty && addition.test(input.addition)

    override fun assemble(input: SmithingRecipeInput, registries: HolderLookup.Provider): ItemStack {
        val stack: ItemStack = input.base.copy()
        stack.applyComponents(components)
        return stack
    }

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack(Items.BARRIER)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializersImpl.SMITHING_MODIFY
}
