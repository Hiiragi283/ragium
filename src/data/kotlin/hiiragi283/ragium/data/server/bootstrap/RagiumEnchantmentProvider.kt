package hiiragi283.ragium.data.server.bootstrap

import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.setup.RagiumEnchantments
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.item.enchantment.Enchantment

object RagiumEnchantmentProvider : RegistrySetBuilder.RegistryBootstrap<Enchantment> {
    override fun run(context: BootstrapContext<Enchantment>) {
        fun register(key: ResourceKey<Enchantment>, builder: Enchantment.Builder) {
            context.register(key, builder.build(key.location()))
        }

        register(
            RagiumEnchantments.CAPACITY,
            Enchantment.enchantment(
                Enchantment.definition(
                    context.lookup(Registries.ITEM).getOrThrow(RagiumItemTags.CAPACITY_ENCHANTABLE),
                    1,
                    5,
                    Enchantment.constantCost(1),
                    Enchantment.constantCost(41),
                    1,
                    EquipmentSlotGroup.ANY,
                ),
            ),
        )
    }
}
