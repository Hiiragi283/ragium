package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.content.HTRegistryEntryList
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.recipe.HTItemIngredient
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.component.ComponentChanges
import net.minecraft.component.DataComponentTypes
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.item.*
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.Text
import net.minecraft.world.WorldView

//    Item    //

fun itemSettings(): Item.Settings = Item.Settings()

fun Item.Settings.tier(tier: HTMachineTier): Item.Settings = component(HTMachineTier.COMPONENT_TYPE, tier).rarity(tier.rarity)

fun Item.Settings.machine(key: HTMachineKey): Item.Settings = component(HTMachineKey.COMPONENT_TYPE, key).name(key.text)

fun Item.Settings.material(key: HTMaterialKey, prefix: HTTagPrefix): Item.Settings =
    component(HTMaterialKey.COMPONENT_TYPE, key).component(HTTagPrefix.COMPONENT_TYPE, prefix).name(prefix.createText(key))

fun Item.Settings.descriptions(vararg texts: Text): Item.Settings = component(RagiumComponentTypes.DESCRIPTION, texts.toList())

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

fun ItemStack.hasEnchantment(world: WorldView, key: RegistryKey<Enchantment>): Boolean = world
    .getEnchantment(key)
    ?.let(EnchantmentHelper.getEnchantments(this)::getLevel)
    ?.let { it > 0 } == true

fun ItemStack.isOf(item: ItemConvertible): Boolean = isOf(item.asItem())

fun ItemStack.isIn(entryList: HTRegistryEntryList<Item>): Boolean = entryList.storage.map(this::isIn, this::isOf)

val ItemStack.restDamage: Int
    get() = maxDamage - damage

//    ItemUsageContext    //

val ItemUsageContext.blockState: BlockState
    get() = world.getBlockState(blockPos)

val ItemUsageContext.blockEntity: BlockEntity?
    get() = world.getBlockEntity(blockPos)
