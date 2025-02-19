package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.item.HTSingleEnchantmentAwareItem
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.PickaxeItem
import net.minecraft.world.item.Tier
import net.minecraft.world.item.enchantment.Enchantment

class HTSingleEnchantmentPickaxeItem(
    override val targetEnchantment: ResourceKey<Enchantment>,
    override val targetLevel: Int,
    tier: Tier,
    properties: Properties,
) : PickaxeItem(tier, properties),
    HTSingleEnchantmentAwareItem
