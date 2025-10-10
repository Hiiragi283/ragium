package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.MapBiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.ShapelessRecipe
import net.minecraft.world.level.ItemLike

class HTClearComponentRecipe(
    group: String,
    category: CraftingBookCategory,
    private val item: Item,
    private val targetTypes: List<DataComponentType<*>>,
) : ShapelessRecipe(group, category, ItemStack(item), NonNullList.of(Ingredient.EMPTY, Ingredient.of(item))) {
    companion object {
        @JvmField
        val CODEC: MapBiCodec<RegistryFriendlyByteBuf, HTClearComponentRecipe> = MapBiCodec.composite(
            BiCodec.STRING.optionalFieldOf("group", ""),
            HTClearComponentRecipe::getGroup,
            BiCodec
                .of(
                    CraftingBookCategory.CODEC,
                    CraftingBookCategory.STREAM_CODEC,
                ).optionalFieldOf("category", CraftingBookCategory.MISC),
            HTClearComponentRecipe::category,
            VanillaBiCodecs.registryBased(BuiltInRegistries.ITEM).fieldOf("ingredient"),
            HTClearComponentRecipe::item,
            VanillaBiCodecs.registryBased(BuiltInRegistries.DATA_COMPONENT_TYPE).listOrElement().fieldOf("targets"),
            HTClearComponentRecipe::targetTypes,
            ::HTClearComponentRecipe,
        )
    }

    constructor(group: String, category: CraftingBookCategory, item: ItemLike, targetTypes: List<DataComponentType<*>>) : this(
        group,
        category,
        item.asItem(),
        targetTypes,
    )

    override fun assemble(input: CraftingInput, registries: HolderLookup.Provider): ItemStack {
        val item: ItemStack = input.items().firstOrNull()?.copy() ?: return ItemStack.EMPTY
        targetTypes.forEach(item::remove)
        return item
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.CLEAR_COMPONENT
}
