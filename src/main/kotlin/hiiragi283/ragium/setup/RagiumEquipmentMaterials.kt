package hiiragi283.ragium.setup

import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.variant.HTBasicEquipmentMaterial
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.BlockTags
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterials
import net.minecraft.world.item.Tiers
import net.neoforged.neoforge.common.Tags

object RagiumEquipmentMaterials {
    /**
     * @see ArmorMaterials.CHAIN
     * @see Tiers.STONE
     */
    @JvmField
    val RAGI_ALLOY = HTBasicEquipmentMaterial(
        RagiumMaterialKeys.RAGI_ALLOY,
        axeDamage = 7f,
        axeAttackSpeed = -3.2f,
        durability = 256,
        miningSpeed = 4f,
        baseAttackDamage = 1f,
        enchantability = 5,
        incorrectBlockTag = BlockTags.NEEDS_STONE_TOOL,
        defenceMap = mapOf(
            ArmorItem.Type.BOOTS to 1,
            ArmorItem.Type.LEGGINGS to 4,
            ArmorItem.Type.CHESTPLATE to 5,
            ArmorItem.Type.HELMET to 2,
            ArmorItem.Type.BODY to 4,
        ),
        armorMultiplier = 10,
        equipSound = SoundEvents.ARMOR_EQUIP_CHAIN,
    )

    /**
     * @see ArmorMaterials.IRON
     * @see Tiers.IRON
     */
    @JvmField
    val AZURE_STEEL = HTBasicEquipmentMaterial(
        RagiumMaterialKeys.AZURE_STEEL,
        axeDamage = 6f,
        axeAttackSpeed = -3.1f,
        durability = 256 * 4,
        miningSpeed = 6f,
        baseAttackDamage = 2f,
        enchantability = 14,
        incorrectBlockTag = BlockTags.NEEDS_IRON_TOOL,
        defenceMap = mapOf(
            ArmorItem.Type.BOOTS to 2,
            ArmorItem.Type.LEGGINGS to 5,
            ArmorItem.Type.CHESTPLATE to 6,
            ArmorItem.Type.HELMET to 2,
            ArmorItem.Type.BODY to 5,
        ),
        armorMultiplier = 15,
        equipSound = SoundEvents.ARMOR_EQUIP_IRON,
    )

    /**
     * @see ArmorMaterials.DIAMOND
     * @see Tiers.DIAMOND
     */
    @JvmField
    val RAGI_CRYSTAL = HTBasicEquipmentMaterial(
        RagiumMaterialKeys.RAGI_CRYSTAL,
        axeDamage = 5f,
        axeAttackSpeed = -3f,
        durability = 1024 + 512 + 256,
        miningSpeed = 8f,
        baseAttackDamage = 3f,
        enchantability = 10,
        incorrectBlockTag = BlockTags.NEEDS_DIAMOND_TOOL,
        defenceMap = mapOf(
            ArmorItem.Type.BOOTS to 3,
            ArmorItem.Type.LEGGINGS to 6,
            ArmorItem.Type.CHESTPLATE to 8,
            ArmorItem.Type.HELMET to 3,
            ArmorItem.Type.BODY to 11,
        ),
        armorMultiplier = 33,
        equipSound = SoundEvents.ARMOR_EQUIP_DIAMOND,
        toughness = 2f,
    )

    /**
     * @see ArmorMaterials.NETHERITE
     * @see Tiers.NETHERITE
     */
    @JvmField
    val DEEP_STEEL = HTBasicEquipmentMaterial(
        RagiumMaterialKeys.DEEP_STEEL,
        axeDamage = 5f,
        axeAttackSpeed = -3f,
        durability = 2048,
        miningSpeed = 10f,
        baseAttackDamage = 4f,
        enchantability = 15,
        incorrectBlockTag = Tags.Blocks.NEEDS_NETHERITE_TOOL,
        defenceMap = mapOf(
            ArmorItem.Type.BOOTS to 3,
            ArmorItem.Type.LEGGINGS to 6,
            ArmorItem.Type.CHESTPLATE to 8,
            ArmorItem.Type.HELMET to 3,
            ArmorItem.Type.BODY to 11,
        ),
        armorMultiplier = 37,
        equipSound = SoundEvents.ARMOR_EQUIP_NETHERITE,
        toughness = 3f,
        resistance = 0.2f,
    )
}
