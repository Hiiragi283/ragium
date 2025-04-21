package hiiragi283.ragium.setup

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.prefix.HTTagPrefix
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
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
    ADVANCED_RAGI_ALLOY(256, 5f, 2f, BlockTags.INCORRECT_FOR_IRON_TOOL, 15, RagiumMaterials.ADVANCED_RAGI_ALLOY),
    STEEL(512, 7f, 2.5f, BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 12, RagiumMaterials.AZURE_STEEL),
    CUSTOM(32, 12f, 0f, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 0),
    ;

    constructor(
        durability: Int,
        miningSpeed: Float,
        attackDamage: Float,
        incorrectTag: TagKey<Block>,
        enchantability: Int,
        key: HTMaterialKey,
        prefix: HTTagPrefix = HTTagPrefixes.INGOT,
    ) : this(
        durability,
        miningSpeed,
        attackDamage,
        incorrectTag,
        enchantability,
        { Ingredient.of(prefix.createItemTag(key)) },
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
