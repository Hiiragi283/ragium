package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.variant.HTEquipmentMaterial
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.neoforged.neoforge.registries.RegisterEvent

object RagiumArmorMaterials {
    @JvmStatic
    fun register(helper: RegisterEvent.RegisterHelper<ArmorMaterial>) {
        register(helper, RagiumEquipmentMaterials.AZURE_STEEL)
        register(helper, RagiumEquipmentMaterials.DEEP_STEEL)
    }

    @JvmStatic
    private fun register(helper: RegisterEvent.RegisterHelper<ArmorMaterial>, material: HTEquipmentMaterial) {
        val id: ResourceLocation = RagiumAPI.id(material.asMaterialName())
        helper.register(
            id,
            ArmorMaterial(
                ArmorItem.Type.entries.associateWith(material::getArmorDefence),
                material.enchantmentValue,
                material.getEquipSound(),
                { material.repairIngredient },
                listOf(ArmorMaterial.Layer(id)),
                material.getToughness(),
                material.getKnockbackResistance(),
            ),
        )
    }
}
