package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.component.HTRadioactiveComponent
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.recipe.HTItemIngredient
import hiiragi283.ragium.api.recipe.HTItemResult
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.component.ComponentChanges
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.UnbreakableComponent
import net.minecraft.inventory.Inventory
import net.minecraft.item.*
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.Text

//    ItemConvertible    //

@Suppress("DEPRECATION")
val ItemConvertible.registryEntry: RegistryEntry<Item>
    get() = asItem().registryEntry

/**
 * このアイテムが[Items.AIR]と一致するか判定します。
 */
val ItemConvertible.isAir: Boolean
    get() = asItem() == Items.AIR

//    Item    //

/**
 * [Item.Settings]を返します。
 */
fun itemSettings(): Item.Settings = Item.Settings()

/**
 * 指定した[tier]とそのレアリティを設定します。
 */
fun Item.Settings.tier(tier: HTMachineTier): Item.Settings = component(HTMachineTier.COMPONENT_TYPE, tier).rarity(tier.rarity)

/**
 * 指定した[key]とその名前を設定します。
 */
fun Item.Settings.machine(key: HTMachineKey): Item.Settings = component(HTMachineKey.COMPONENT_TYPE, key).name(key.text)

/**
 * 指定した[key]と[prefix]，そしてその名前を設定します。
 */
fun Item.Settings.material(key: HTMaterialKey, prefix: HTTagPrefix): Item.Settings =
    component(HTMaterialKey.COMPONENT_TYPE, key).component(HTTagPrefix.COMPONENT_TYPE, prefix).name(prefix.createText(key))

/**
 * 指定した[texts]を説明文として設定します。
 */
fun Item.Settings.descriptions(texts: List<Text>): Item.Settings = component(RagiumComponentTypes.DESCRIPTION, texts)

/**
 * 指定した[texts]を説明文として設定します。
 */
fun Item.Settings.descriptions(vararg texts: Text): Item.Settings = descriptions(texts.toList())

/**
 * 指定した[key]から[Text.translatable]を説明文として設定します。
 */
fun Item.Settings.descriptions(key: String): Item.Settings = descriptions(Text.translatable(key))

/**
 * 指定した[text]をアイテムの名前に設定します。
 */
fun Item.Settings.name(text: Text): Item.Settings = component(DataComponentTypes.ITEM_NAME, text)

/**
 * 指定した[item]と[count]を道具の修理素材として設定します。
 */
fun Item.Settings.repairment(item: ItemConvertible, count: Int = 1): Item.Settings =
    component(RagiumComponentTypes.REPAIRMENT, HTItemIngredient.of(item, count))

/**
 * 指定した[tagKey]と[count]を道具の修理素材として設定します。
 */
fun Item.Settings.repairment(tagKey: TagKey<Item>, count: Int = 1): Item.Settings =
    component(RagiumComponentTypes.REPAIRMENT, HTItemIngredient.of(tagKey, count))

/**
 * 指定した[translationKey]と[tier]をアイテムの名前として設定します。
 */
fun Item.Settings.tieredText(translationKey: String, tier: HTMachineTier): Item.Settings =
    component(DataComponentTypes.ITEM_NAME, tier.createPrefixedText(translationKey)).tier(tier)

/**
 * 今後の更新で変更および削除される可能性があることを設定します。
 */
fun Item.Settings.maybeRework(): Item.Settings = component(RagiumComponentTypes.REWORK_TARGET, Unit)

/**
 * 指定した[level]を放射能レベルとして設定します。
 */
fun Item.Settings.radioactive(level: HTRadioactiveComponent): Item.Settings = component(HTRadioactiveComponent.COMPONENT_TYPE, level)

fun Item.Settings.unbreakable(showInTooltip: Boolean = true): Item.Settings =
    component(DataComponentTypes.UNBREAKABLE, UnbreakableComponent(showInTooltip)).maxCount(1)

//    ItemStack    //

/**
 * 指定した値から[ItemStack]を返します。
 * @param item [ItemStack]のアイテム
 * @param count [ItemStack]の個数
 * @param builderAction [ItemStack]のコンポーネントを設定するブロック
 * @return [item]がnullまたは[Items.AIR]を返した場合は[ItemStack.EMPTY]
 */
@Suppress("DEPRECATION")
fun buildItemStack(item: ItemConvertible?, count: Int = 1, builderAction: ComponentChanges.Builder.() -> Unit = {}): ItemStack {
    val entry: RegistryEntry<Item> = item?.takeUnless { it.isAir }?.registryEntry ?: return ItemStack.EMPTY
    val changes: ComponentChanges = ComponentChanges.builder().apply(builderAction).build()
    return ItemStack(entry, count, changes)
}

/**
 * 指定した[item]とアイテムが一致するか判定します。
 */
fun ItemStack.isOf(item: ItemConvertible): Boolean = isOf(item.asItem())

/**
 * 残りの耐久値を返します。
 */
val ItemStack.restDamage: Int
    get() = maxDamage - damage

/**
 * 現在の個数が[isMaxCount]と一致するか判定します。
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
 * 指定した[slot]に紐づいた[ItemStack]を変換します。
 * @param transform [ItemStack]を変換するブロック
 */
fun Inventory.modifyStack(slot: Int, transform: (ItemStack) -> ItemStack) {
    val stackIn: ItemStack = getStack(slot)
    setStack(slot, transform(stackIn))
}

fun Inventory.mergeStack(slot: Int, result: HTItemResult) {
    modifyStack(slot, result::merge)
}

/**
 * 指定した[slot]に紐づいた[ItemStack]を返します。
 * @return [slot]が範囲外の場合はnull
 */
fun Inventory.getStackOrNull(slot: Int): ItemStack? = if (slot in 0..size()) getStack(slot) else null

/**
 * 指定した[slot]に紐づいた[ItemStack]を返します。
 * @return [slot]が範囲外の場合は[ItemStack.EMPTY]
 */
fun Inventory.getStackOrEmpty(slot: Int): ItemStack = getStackOrNull(slot) ?: ItemStack.EMPTY

/**
 * インデックスをキー，[ItemStack]を値にもつ[Map]に変換します。
 */
fun Inventory.asMap(): Map<Int, ItemStack> = (0 until size()).associateWith(::getStack)

/**
 * [ItemStack]の[List]に変換します。
 */
fun Inventory.iterateStacks(): List<ItemStack> = (0 until size()).map(::getStack)
