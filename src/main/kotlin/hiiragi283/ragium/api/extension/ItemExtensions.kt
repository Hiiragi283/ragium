package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.recipe.HTItemIngredient
import hiiragi283.ragium.api.recipe.HTItemResult
import hiiragi283.ragium.api.util.HTRegistryEntryList
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.component.ComponentChanges
import net.minecraft.component.DataComponentTypes
import net.minecraft.inventory.Inventory
import net.minecraft.item.*
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.Text

//    Item    //

fun itemSettings(): Item.Settings = Item.Settings()

fun Item.Settings.tier(tier: HTMachineTier): Item.Settings = component(HTMachineTier.COMPONENT_TYPE, tier).rarity(tier.rarity)

fun Item.Settings.machine(key: HTMachineKey): Item.Settings = component(HTMachineKey.COMPONENT_TYPE, key).name(key.text)

fun Item.Settings.material(key: HTMaterialKey, prefix: HTTagPrefix): Item.Settings =
    component(HTMaterialKey.COMPONENT_TYPE, key).component(HTTagPrefix.COMPONENT_TYPE, prefix).name(prefix.createText(key))

fun Item.Settings.descriptions(vararg texts: Text): Item.Settings = component(RagiumComponentTypes.DESCRIPTION, texts.toList())

fun Item.Settings.descriptions(key: String): Item.Settings = descriptions(Text.translatable(key))

fun Item.Settings.name(text: Text): Item.Settings = component(DataComponentTypes.ITEM_NAME, text)

fun Item.Settings.repairment(item: ItemConvertible, count: Int = 1): Item.Settings =
    component(RagiumComponentTypes.REPAIRMENT, HTItemIngredient.of(item, count))

fun Item.Settings.repairment(tagKey: TagKey<Item>, count: Int = 1): Item.Settings =
    component(RagiumComponentTypes.REPAIRMENT, HTItemIngredient.of(tagKey, count))

fun Item.Settings.tieredText(translationKey: String, tier: HTMachineTier): Item.Settings =
    component(DataComponentTypes.ITEM_NAME, tier.createPrefixedText(translationKey)).tier(tier)

val Item.isAir: Boolean
    get() = this == Items.AIR

val Item.nonAirOrNull: Item?
    get() = takeUnless { it.isAir }

//    ItemStack    //

@Suppress("DEPRECATION")
fun buildItemStack(item: ItemConvertible?, count: Int = 1, builderAction: ComponentChanges.Builder.() -> Unit = {}): ItemStack {
    val entry: RegistryEntry<Item> = item?.asItem()?.nonAirOrNull?.registryEntry ?: return ItemStack.EMPTY
    val changes: ComponentChanges = ComponentChanges.builder().apply(builderAction).build()
    return ItemStack(entry, count, changes)
}

fun ItemStack.isOf(item: ItemConvertible): Boolean = isOf(item.asItem())

fun ItemStack.isIn(entryList: HTRegistryEntryList<Item>): Boolean = entryList.storage.map(this::isIn, this::isOf)

/**
 * Get remaining durability from [this] item stack
 */
val ItemStack.restDamage: Int
    get() = maxDamage - damage

/**
 * Check [this] item stack count is maximum
 */
val ItemStack.isMaxCount: Boolean
    get() = count == maxCount

//    ItemUsageContext    //

val ItemUsageContext.blockState: BlockState
    get() = world.getBlockState(blockPos)

val ItemUsageContext.blockEntity: BlockEntity?
    get() = world.getBlockEntity(blockPos)

//    Inventory    //

/**
 * Modify [ItemStack] in given [slot]
 */
fun Inventory.modifyStack(slot: Int, mapping: (ItemStack) -> ItemStack) {
    val stackIn: ItemStack = getStack(slot)
    setStack(slot, mapping(stackIn))
}

/**
 * @see [HTItemResult.merge]
 */
fun Inventory.mergeStack(slot: Int, result: HTItemResult) {
    modifyStack(slot, result::merge)
}

fun Inventory.getStackOrNull(slot: Int): ItemStack? = if (slot in 0..size()) getStack(slot) else null

fun Inventory.getStackOrEmpty(slot: Int): ItemStack = getStackOrNull(slot) ?: ItemStack.EMPTY

fun Inventory.asMap(): Map<Int, ItemStack> = (0 until size()).associateWith(::getStack)

fun Inventory.iterateStacks(): List<ItemStack> = (0 until size()).map(::getStack)
