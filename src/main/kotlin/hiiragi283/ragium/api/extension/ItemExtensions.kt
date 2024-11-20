package hiiragi283.ragium.api.extension

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
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.Text
import net.minecraft.world.WorldView

//    Item    //

fun itemSettings(): Item.Settings = Item.Settings()

fun Item.Settings.machineKey(key: HTMachineKey): Item.Settings = component(HTMachineKey.COMPONENT_TYPE, key)

fun Item.Settings.tier(tier: HTMachineTier): Item.Settings = component(HTMachineTier.COMPONENT_TYPE, tier).rarity(tier.rarity)

fun Item.Settings.materialKey(key: HTMaterialKey): Item.Settings = component(HTMaterialKey.COMPONENT_TYPE, key)

fun Item.Settings.prefix(prefix: HTTagPrefix): Item.Settings = component(HTTagPrefix.COMPONENT_TYPE, prefix)

fun Item.Settings.descriptions(vararg texts: Text): Item.Settings = component(RagiumComponentTypes.DESCRIPTION, texts.toList())

fun Item.Settings.repairment(item: ItemConvertible, count: Int = 1): Item.Settings =
    component(RagiumComponentTypes.REPAIRMENT, HTItemIngredient.of(item, count))

fun Item.Settings.repairment(tagKey: TagKey<Item>, count: Int = 1): Item.Settings =
    component(RagiumComponentTypes.REPAIRMENT, HTItemIngredient.of(tagKey, count))

fun Item.Settings.tieredText(translationKey: String, tier: HTMachineTier): Item.Settings =
    component(DataComponentTypes.ITEM_NAME, tier.createPrefixedText(translationKey)).tier(tier)

//    ItemStack    //

fun buildItemStack(item: ItemConvertible?, count: Int = 1, builderAction: ComponentChanges.Builder.() -> Unit = {}): ItemStack {
    if (item == null) return ItemStack.EMPTY
    val item1: Item = item.asItem()
    if (item1 == Items.AIR) return ItemStack.EMPTY
    val entry: RegistryEntry<Item> = Registries.ITEM.getEntry(item1)
    val changes: ComponentChanges = ComponentChanges.builder().apply(builderAction).build()
    return ItemStack(entry, count, changes)
}

fun ItemStack.hasEnchantment(world: WorldView, key: RegistryKey<Enchantment>): Boolean = world
    .getEnchantment(key)
    ?.let(EnchantmentHelper.getEnchantments(this)::getLevel)
    ?.let { it > 0 } == true

fun ItemStack.isOf(item: ItemConvertible): Boolean = isOf(item.asItem())

//    ItemUsageContext    //

val ItemUsageContext.blockState: BlockState
    get() = world.getBlockState(blockPos)

val ItemUsageContext.blockEntity: BlockEntity?
    get() = world.getBlockEntity(blockPos)
