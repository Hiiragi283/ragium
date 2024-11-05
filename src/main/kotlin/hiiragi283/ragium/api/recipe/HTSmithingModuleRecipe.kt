package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.tags.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.item.HTCrafterHammerItem
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.SmithingRecipe
import net.minecraft.recipe.input.SmithingRecipeInput
import net.minecraft.registry.RegistryWrapper
import net.minecraft.world.World

object HTSmithingModuleRecipe : SmithingRecipe {
    @JvmField
    val CODEC: MapCodec<HTSmithingModuleRecipe> = MapCodec.unit(HTSmithingModuleRecipe)

    @JvmField
    val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTSmithingModuleRecipe> =
        PacketCodec.unit(HTSmithingModuleRecipe)

    override fun matches(input: SmithingRecipeInput, world: World): Boolean = testTemplate(input.template) && testBase(input.base)

    override fun craft(input: SmithingRecipeInput, lookup: RegistryWrapper.WrapperLookup): ItemStack {
        val module: ItemStack = input.template.copy()
        val base: ItemStack = input.base.copy()
        val baseComponent: HTCrafterHammerItem.Component =
            base.getOrDefault(HTCrafterHammerItem.Component.COMPONENT_TYPE, HTCrafterHammerItem.Component.DEFAULT)
        HTCrafterHammerItem.Behavior.entries
            .firstOrNull { it.asItem() == module.item }
            ?.let { behavior: HTCrafterHammerItem.Behavior ->
                base.set(HTCrafterHammerItem.Component.COMPONENT_TYPE, baseComponent.copy(behavior = behavior))
            }
        return base
    }

    override fun getResult(registriesLookup: RegistryWrapper.WrapperLookup): ItemStack = RagiumItems.CRAFTER_HAMMER.defaultStack

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MODULE_INSTALL

    override fun testTemplate(stack: ItemStack): Boolean = stack.isIn(RagiumItemTags.TOOL_MODULES)

    override fun testBase(stack: ItemStack): Boolean = stack.isOf(RagiumItems.CRAFTER_HAMMER)

    override fun testAddition(stack: ItemStack): Boolean = false
}
