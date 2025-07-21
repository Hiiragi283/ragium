package hiiragi283.ragium.data.server.bootstrap

import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
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
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition
import net.neoforged.neoforge.common.Tags

/**
 * @see [net.minecraft.world.item.enchantment.Enchantments]
 */
object RagiumEnchantmentProvider : RegistrySetBuilder.RegistryBootstrap<Enchantment> {
    override fun run(context: BootstrapContext<Enchantment>) {
        fun register(key: ResourceKey<Enchantment>, builder: Enchantment.Builder) {
            context.register(key, builder.build(key.location()))
        }

        val enchLookup: HolderGetter<Enchantment> = context.lookup(Registries.ENCHANTMENT)
        val itemLookup: HolderGetter<Item> = context.lookup(Registries.ITEM)

        register(
            RagiumEnchantments.CAPACITY,
            Enchantment.enchantment(
                Enchantment.definition(
                    itemLookup.getOrThrow(Tags.Items.ENCHANTABLES),
                    1,
                    5,
                    Enchantment.constantCost(1),
                    Enchantment.constantCost(41),
                    1,
                    EquipmentSlotGroup.ANY,
                ),
            ),
        )
        register(
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
            RagiumEnchantments.SONIC_PROTECTION,
            Enchantment
                .enchantment(
                    Enchantment.definition(
                        itemLookup.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
                        2,
                        1,
                        Enchantment.dynamicCost(10, 10),
                        Enchantment.dynamicCost(25, 10),
                        4,
                        EquipmentSlotGroup.CHEST,
                    ),
                ).exclusiveWith(enchLookup.getOrThrow(EnchantmentTags.ARMOR_EXCLUSIVE))
                .withEffect(
                    EnchantmentEffectComponents.DAMAGE_IMMUNITY,
                    DamageImmunity.INSTANCE,
                    DamageSourceCondition.hasDamageSource(
                        DamageSourcePredicate.Builder
                            .damageType()
                            .tag(TagPredicate.`is`(RagiumCommonTags.DamageTypes.IS_SONIC))
                            .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY)),
                    ),
                ),
        )
    }
}
