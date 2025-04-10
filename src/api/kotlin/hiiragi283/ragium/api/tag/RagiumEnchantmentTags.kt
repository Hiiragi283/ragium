package hiiragi283.ragium.api.tag

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.enchantment.Enchantment

object RagiumEnchantmentTags {
    @JvmStatic
    private fun create(path: String): TagKey<Enchantment> = TagKey.create(Registries.ENCHANTMENT, RagiumAPI.id(path))

    @JvmField
    val CAPACITY: TagKey<Enchantment> = create("capacity")

    @JvmField
    val CHARGING: TagKey<Enchantment> = create("charging")

    @JvmField
    val COOLING: TagKey<Enchantment> = create("cooling")

    @JvmField
    val EFFICIENCY: TagKey<Enchantment> = create("efficiency")

    @JvmField
    val EXTRA_OUTPUT: TagKey<Enchantment> = create("extra_output")

    @JvmField
    val HEATING: TagKey<Enchantment> = create("heating")

    @JvmField
    val POWER_SAVING: TagKey<Enchantment> = create("power_saving")

    @JvmField
    val RANGE: TagKey<Enchantment> = create("range")
}
