package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.ArmorMaterials
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.sound.SoundEvent

object RagiumArmorMaterials {
    @JvmField
    val STEEL: RegistryEntry<ArmorMaterial> = register(
        "steel",
        ArmorMaterials.IRON,
    ) { Ingredient.fromTag(HTTagPrefix.INGOT.createTag(RagiumMaterialKeys.STEEL)) }

    @JvmField
    val DEEP_STEEL: RegistryEntry<ArmorMaterial> = register(
        "deep_steel",
        ArmorMaterials.DIAMOND,
    ) { Ingredient.fromTag(HTTagPrefix.INGOT.createTag(RagiumMaterialKeys.DEEP_STEEL)) }

    @JvmField
    val STELLA: RegistryEntry<ArmorMaterial> = register(
        "stella",
        ArmorMaterials.NETHERITE,
    ) { Ingredient.ofItems(RagiumItems.Ingredients.STELLA_PLATE) }

    @JvmStatic
    private fun register(name: String, entry: RegistryEntry<ArmorMaterial>, repairment: () -> Ingredient): RegistryEntry<ArmorMaterial> =
        entry.value().let { parent: ArmorMaterial ->
            register(
                name,
                parent.defense,
                parent.enchantability,
                parent.equipSound,
                parent.toughness,
                parent.knockbackResistance,
                repairment,
            )
        }

    @JvmStatic
    private fun register(
        name: String,
        defenceMap: Map<ArmorItem.Type, Int>,
        enchantability: Int,
        equipSound: RegistryEntry<SoundEvent>,
        toughness: Float,
        kResistance: Float,
        repairment: () -> Ingredient,
    ): RegistryEntry<ArmorMaterial> = RagiumAPI.id(name).let {
        Registry.registerReference(
            Registries.ARMOR_MATERIAL,
            it,
            ArmorMaterial(
                defenceMap,
                enchantability,
                equipSound,
                repairment,
                listOf(ArmorMaterial.Layer(it)),
                toughness,
                kResistance,
            ),
        )
    }
}
