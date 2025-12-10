package hiiragi283.ragium.common.crafting

import hiiragi283.ragium.api.registry.builtInRegistryHolder
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.MapBiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.NonNullList
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
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
    private val item: Holder<Item>,
    private val targetTypes: HolderSet<DataComponentType<*>>,
) : ShapelessRecipe(group, category, ItemStack(item), NonNullList.of(Ingredient.EMPTY, Ingredient.of(item.value()))) {
    companion object {
        @JvmField
        val CODEC: MapBiCodec<RegistryFriendlyByteBuf, HTClearComponentRecipe> = MapBiCodec.composite(
            BiCodec.STRING
                .optionalFieldOf("group", "")
                .forGetter(HTClearComponentRecipe::getGroup),
            BiCodec
                .of(CraftingBookCategory.CODEC, CraftingBookCategory.STREAM_CODEC)
                .optionalFieldOf("category", CraftingBookCategory.MISC)
                .forGetter(HTClearComponentRecipe::category),
            VanillaBiCodecs
                .holder(Registries.ITEM)
                .fieldOf("ingredient")
                .forGetter(HTClearComponentRecipe::item),
            VanillaBiCodecs
                .holderSet(Registries.DATA_COMPONENT_TYPE)
                .fieldOf("targets")
                .forGetter(HTClearComponentRecipe::targetTypes),
            ::HTClearComponentRecipe,
        )
    }

    constructor(group: String, category: CraftingBookCategory, item: ItemLike, targetTypes: List<DataComponentType<*>>) : this(
        group,
        category,
        item.builtInRegistryHolder(),
        HolderSet.direct(BuiltInRegistries.DATA_COMPONENT_TYPE::wrapAsHolder, targetTypes),
    )

    override fun assemble(input: CraftingInput, registries: HolderLookup.Provider): ItemStack {
        val item: ItemStack = input.items().firstOrNull()?.copyWithCount(1) ?: return ItemStack.EMPTY
        for (holder: Holder<DataComponentType<*>> in targetTypes) {
            item.remove(holder.value())
        }
        return item
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.CLEAR_COMPONENT
}
