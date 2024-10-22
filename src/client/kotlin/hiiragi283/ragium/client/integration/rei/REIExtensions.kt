package hiiragi283.ragium.client.integration.rei

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.property.HTPropertyKey
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipe
import hiiragi283.ragium.client.integration.rei.display.HTMachineRecipeDisplay
import hiiragi283.ragium.client.integration.rei.display.HTMachineRecipeDisplayNew
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.entry.EntryIngredient
import me.shedaniel.rei.api.common.entry.EntryStack
import me.shedaniel.rei.api.common.util.EntryIngredients
import me.shedaniel.rei.api.common.util.EntryStacks
import me.shedaniel.rei.impl.Internals
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentLevelEntry
import net.minecraft.fluid.Fluid
import net.minecraft.item.EnchantedBookItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys

//    Accessors    //

@Suppress("UnstableApiUsage")
val dynamicRegistry: () -> DynamicRegistryManager
    get() = Internals::getRegistryAccess

//    CategoryIdentifier    //

val HTMachineConvertible.categoryId: CategoryIdentifier<HTMachineRecipeDisplay>
    get() = CategoryIdentifier.of(asMachine().id)

val HTMachineConvertible.categoryIdNew: CategoryIdentifier<HTMachineRecipeDisplayNew>
    get() = CategoryIdentifier.of(asMachine().id.withSuffixedPath("_new"))

//    EntryStack    //

fun HTMachineType.createEntryStack(tier: HTMachineTier): EntryStack<ItemStack> = EntryStacks.of(createItemStack(tier))

fun createEnchantedBook(key: RegistryKey<Enchantment>): EntryStack<ItemStack> = dynamicRegistry()
    .get(RegistryKeys.ENCHANTMENT)
    .getEntry(key)
    .map { EnchantmentLevelEntry(it, 1) }
    .map(EnchantedBookItem::forEnchantment)
    .map(EntryStacks::of)
    .orElse(EntryStacks.of(Items.ENCHANTED_BOOK))

//    Display    //

@JvmField
val INPUT_ENTRIES: HTPropertyKey.Defaulted<(HTMachineRecipe) -> List<EntryIngredient>> =
    HTPropertyKey.Defaulted(
        RagiumAPI.id("input_entries"),
        value = { recipe: HTMachineRecipe ->
            buildList {
                add(recipe.getInput(0)?.entryIngredient ?: EntryIngredient.empty())
                add(recipe.getInput(1)?.entryIngredient ?: EntryIngredient.empty())
                add(recipe.getInput(2)?.entryIngredient ?: EntryIngredient.empty())
                add(EntryIngredients.ofIngredient(recipe.catalyst))
            }
        },
    )

@JvmField
val OUTPUT_ENTRIES: HTPropertyKey.Defaulted<(HTMachineRecipe) -> List<EntryIngredient>> =
    HTPropertyKey.Defaulted(
        RagiumAPI.id("output_entries"),
        value = { recipe: HTMachineRecipe ->
            recipe.outputs.map(HTRecipeResult::entryIngredient)
        },
    )

fun HTMachineType.getInputEntries(recipe: HTMachineRecipe): List<EntryIngredient> = getOrDefault(INPUT_ENTRIES)(recipe)

fun HTMachineType.getOutputEntries(recipe: HTMachineRecipe): List<EntryIngredient> = getOrDefault(OUTPUT_ENTRIES)(recipe)

//    WeightedIngredient    //

val HTIngredient.entryStacks: List<EntryStack<*>>
    get() = matchingStacks.map(EntryStacks::of)

val HTIngredient.entryIngredient: EntryIngredient
    get() = EntryIngredient.of(entryStacks)

val HTItemIngredient.entryStacks: List<EntryStack<*>>
    @JvmName("itemEntryStacks")
    get() = weightedList.map { (item: Item, amount: Long) -> EntryStacks.of(item, amount.toInt()) }

val HTItemIngredient.entryIngredient: EntryIngredient
    @JvmName("itemEntryIngredient")
    get() = EntryIngredient.of(entryStacks)

val HTFluidIngredient.entryStacks: List<EntryStack<*>>
    @JvmName("fluidEntryStacks")
    get() = weightedList.map { (fluid: Fluid, amount: Long) -> EntryStacks.of(fluid, amount) }

val HTFluidIngredient.entryIngredient: EntryIngredient
    @JvmName("fluidEntryIngredient")
    get() = EntryIngredient.of(entryStacks)

//    HTRecipeResult    //

val HTRecipeResult.entryStack: EntryStack<*>
    get() = EntryStacks.of(toStack())

val HTRecipeResult.entryIngredient: EntryIngredient
    get() = EntryIngredient.of(entryStack)

val HTItemResult.entryStack: EntryStack<*>
    @JvmName("itemEntryStacks")
    get() = EntryStacks.of(variant.toStack(amount.toInt()))

val HTItemResult.entryIngredient: EntryIngredient
    @JvmName("itemEntryIngredient")
    get() = EntryIngredient.of(entryStack)

val HTFluidResult.entryStack: EntryStack<*>
    @JvmName("fluidEntryStacks")
    get() = EntryStacks.of(entryValue, amount)

val HTFluidResult.entryIngredient: EntryIngredient
    @JvmName("fluidEntryIngredient")
    get() = EntryIngredient.of(entryStack)
