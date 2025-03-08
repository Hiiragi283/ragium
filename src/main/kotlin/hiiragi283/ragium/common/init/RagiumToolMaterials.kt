package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Tier
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block

/**
 * @see [net.minecraft.world.item.Tiers]
 */
enum class RagiumToolMaterials(
    private val durability: Int,
    private val miningSpeed: Float,
    private val attackDamage: Float,
    private val incorrectTag: TagKey<Block>,
    private val enchantability: Int,
    private val repair: () -> Ingredient,
) : Tier {
    RAGI_ALLOY(512, 4f, 1f, BlockTags.INCORRECT_FOR_STONE_TOOL, 5, RagiumMaterials.RAGI_ALLOY),
    EMBER_ALLOY(256, 5f, 2f, BlockTags.INCORRECT_FOR_IRON_TOOL, 15, RagiumMaterials.EMBER_ALLOY),
    STEEL(512, 7f, 2.5f, BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 12, CommonMaterials.STEEL),
    DURALUMIN(2048, 12f, 4f, BlockTags.INCORRECT_FOR_IRON_TOOL, 10, RagiumMaterials.DURALUMIN),
    CUSTOM(32, 12f, 0f, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 0),
    ;

    constructor(
        durability: Int,
        miningSpeed: Float,
        attackDamage: Float,
        incorrectTag: TagKey<Block>,
        enchantability: Int,
        key: HTMaterialKey,
        prefix: HTTagPrefix = HTTagPrefix.INGOT,
    ) : this(
        durability,
        miningSpeed,
        attackDamage,
        incorrectTag,
        enchantability,
        { prefix.createIngredient(key) },
    )

    constructor(
        durability: Int,
        miningSpeed: Float,
        attackDamage: Float,
        incorrectTag: TagKey<Block>,
        enchantability: Int,
    ) : this(
        durability,
        miningSpeed,
        attackDamage,
        incorrectTag,
        enchantability,
        Ingredient::EMPTY,
    )

    override fun getUses(): Int = durability

    override fun getSpeed(): Float = miningSpeed

    override fun getAttackDamageBonus(): Float = attackDamage

    override fun getIncorrectBlocksForDrops(): TagKey<Block> = incorrectTag

    override fun getEnchantmentValue(): Int = enchantability

    override fun getRepairIngredient(): Ingredient = repair()
}
