package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.item.enchantment.Enchantment
import net.neoforged.neoforge.registries.holdersets.AnyHolderSet

object RagiumEnchantments {
    @JvmField
    val CAPACITY: ResourceKey<Enchantment> = create("capacity")

    @JvmStatic
    private fun create(path: String): ResourceKey<Enchantment> = ResourceKey.create(Registries.ENCHANTMENT, RagiumAPI.id(path))

    @JvmStatic
    fun boostrap(context: BootstrapContext<Enchantment>) {
        fun register(key: ResourceKey<Enchantment>, builder: Enchantment.Builder) {
            context.register(key, builder.build(key.location()))
        }

        register(
            CAPACITY,
            Enchantment.enchantment(
                Enchantment.definition(
                    AnyHolderSet(BuiltInRegistries.ITEM.asLookup()),
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
