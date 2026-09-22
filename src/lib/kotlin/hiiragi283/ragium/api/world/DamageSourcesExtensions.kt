package hiiragi283.ragium.api.world

import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageSources
import net.minecraft.world.entity.Entity

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
fun DamageSources.chemicalBurn(): DamageSource = this.source(RagiumDamageTypes.CHEMICAL_BURN)

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
fun DamageSources.chemicalBurn(cause: Entity?): DamageSource = this.source(RagiumDamageTypes.CHEMICAL_BURN, cause)

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
fun DamageSources.chemicalBurn(directEntity: Entity?, causeEntity: Entity?): DamageSource =
    this.source(RagiumDamageTypes.CHEMICAL_BURN, directEntity, causeEntity)
