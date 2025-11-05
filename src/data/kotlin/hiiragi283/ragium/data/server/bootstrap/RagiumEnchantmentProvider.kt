package hiiragi283.ragium.data.server.bootstrap

import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.setup.RagiumEnchantmentComponents
import hiiragi283.ragium.setup.RagiumEnchantments
import net.minecraft.advancements.critereon.DamageSourcePredicate
import net.minecraft.advancements.critereon.EntityPredicate
import net.minecraft.advancements.critereon.TagPredicate
import net.minecraft.core.HolderGetter
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.DamageTypeTags
import net.minecraft.tags.EnchantmentTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.item.Item
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents
import net.minecraft.world.item.enchantment.LevelBasedValue
import net.minecraft.world.item.enchantment.effects.AddValue
import net.minecraft.world.item.enchantment.effects.DamageImmunity
import net.minecraft.world.item.enchantment.effects.MultiplyValue
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition

/**
 * @see net.minecraft.world.item.enchantment.Enchantments
 */
object RagiumEnchantmentProvider : RegistrySetBuilder.RegistryBootstrap<Enchantment> {
    override fun run(context: BootstrapContext<Enchantment>) {
        val enchLookup: HolderGetter<Enchantment> = context.lookup(Registries.ENCHANTMENT)
        val itemLookup: HolderGetter<Item> = context.lookup(Registries.ITEM)

        register(
            context,
            RagiumEnchantments.CAPACITY,
            Enchantment
                .enchantment(
                    Enchantment.definition(
                        itemLookup.getOrThrow(RagiumModTags.Items.CAPACITY_ENCHANTABLE),
                        2,
                        3,
                        Enchantment.dynamicCost(15, 9),
                        Enchantment.dynamicCost(65, 9),
                        4,
                        EquipmentSlotGroup.ANY,
                    ),
                ).withSpecialEffect(RagiumEnchantmentComponents.CAPACITY, MultiplyValue(LevelBasedValue.perLevel(2f, 1f))),
        )
        register(
            context,
            RagiumEnchantments.RANGE,
            Enchantment
                .enchantment(
                    Enchantment.definition(
                        itemLookup.getOrThrow(RagiumModTags.Items.RANGE_ENCHANTABLE),
                        2,
                        3,
                        Enchantment.dynamicCost(15, 9),
                        Enchantment.dynamicCost(65, 9),
                        4,
                        EquipmentSlotGroup.ANY,
                    ),
                ).withEffect(RagiumEnchantmentComponents.RANGE, MultiplyValue(LevelBasedValue.perLevel(2f, 1f))),
        )
        // Weapon
        register(
            context,
            RagiumEnchantments.NOISE_CANCELING,
            Enchantment
                .enchantment(
                    Enchantment.definition(
                        itemLookup.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                        itemLookup.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                        5,
                        5,
                        Enchantment.dynamicCost(5, 8),
                        Enchantment.dynamicCost(25, 8),
                        2,
                        EquipmentSlotGroup.MAINHAND,
                    ),
                ).exclusiveWith(enchLookup.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
                .withEffect(
                    EnchantmentEffectComponents.DAMAGE,
                    AddValue(LevelBasedValue.perLevel(20f)),
                    LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.THIS,
                        EntityPredicate.Builder
                            .entity()
                            .of(RagiumModTags.EntityTypes.SENSITIVE_TO_NOISE_CANCELLING),
                    ),
                ),
        )
        register(
            context,
            RagiumEnchantments.STRIKE,
            Enchantment
                .enchantment(
                    Enchantment.definition(
                        itemLookup.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                        itemLookup.getOrThrow(RagiumModTags.Items.STRIKE_ENCHANTABLE),
                        5,
                        5,
                        Enchantment.dynamicCost(5, 8),
                        Enchantment.dynamicCost(25, 8),
                        2,
                        EquipmentSlotGroup.MAINHAND,
                    ),
                ).exclusiveWith(enchLookup.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
                .withEffect(RagiumEnchantmentComponents.STRIKE),
        )
        // Armor
        register(
            context,
            RagiumEnchantments.SONIC_PROTECTION,
            Enchantment
                .enchantment(
                    Enchantment.definition(
                        itemLookup.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                        itemLookup.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
                        2,
                        1,
                        Enchantment.dynamicCost(10, 10),
                        Enchantment.dynamicCost(25, 10),
                        4,
                        EquipmentSlotGroup.ANY,
                    ),
                ).exclusiveWith(enchLookup.getOrThrow(EnchantmentTags.ARMOR_EXCLUSIVE))
                .withEffect(
                    EnchantmentEffectComponents.DAMAGE_IMMUNITY,
                    DamageImmunity.INSTANCE,
                    DamageSourceCondition.hasDamageSource(
                        DamageSourcePredicate.Builder
                            .damageType()
                            .tag(TagPredicate.`is`(RagiumModTags.DamageTypes.IS_SONIC))
                            .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY)),
                    ),
                ),
        )
    }

    //    Extensions    //

    private fun register(context: BootstrapContext<Enchantment>, key: ResourceKey<Enchantment>, builder: Enchantment.Builder) {
        context.register(key, builder.build(key.location()))
    }
}
