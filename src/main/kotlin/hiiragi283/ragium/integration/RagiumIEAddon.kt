package hiiragi283.ragium.integration

import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler
import blusunrize.immersiveengineering.api.tool.RailgunHandler
import hiiragi283.ragium.api.addon.HTAddon
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.extension.asServerLevel
import hiiragi283.ragium.api.registry.HTDeferredFluid
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumVirtualFluids
import hiiragi283.ragium.common.item.HTThrowableItem
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.WeatheringCopper
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent

@HTAddon("immersiveengineering")
object RagiumIEAddon : RagiumAddon {
    //    RagiumAddon    //

    override val priority: Int = 0

    override fun onCommonSetup(event: FMLCommonSetupEvent) {
        // Dynamite
        RailgunHandler.registerProjectile(
            { Ingredient.of(RagiumItemTags.DYNAMITES) },
            object : RailgunHandler.IRailgunProjectile {
                override fun getProjectile(shooter: Player?, ammo: ItemStack, defaultProjectile: Entity): Entity? {
                    val dynamiteItem: HTThrowableItem? = ammo.item as? HTThrowableItem
                    if (dynamiteItem != null && shooter != null) {
                        val dynamite: Projectile = dynamiteItem.throwProjectile(shooter.level(), shooter, ammo)
                        dynamite.shootFromRotation(shooter, shooter.xRot, shooter.yRot, 0f, 3f, 0f)
                        ammo.consume(1, shooter)
                        return dynamite
                    }
                    return super.getProjectile(shooter, ammo, defaultProjectile)
                }
            },
        )

        chemicalThrower()
    }

    private fun chemicalThrower() {
        fun register(holder: HTDeferredFluid<*>, effect: ChemthrowerHandler.ChemthrowerEffect) {
            ChemthrowerHandler.registerEffect(holder.commonTag, effect)
        }

        //  Oxygen
        register(
            RagiumVirtualFluids.OXYGEN.fluidHolder,
            object : ChemthrowerHandler.ChemthrowerEffect() {
                override fun applyToEntity(
                    target: LivingEntity,
                    shooter: Player?,
                    thrower: ItemStack,
                    fluid: Fluid,
                ) {}

                override fun applyToBlock(
                    world: Level,
                    mop: HitResult,
                    shooter: Player?,
                    thrower: ItemStack,
                    fluid: Fluid,
                ) {
                    val level: ServerLevel = world.asServerLevel() ?: return
                    val blockHit: BlockHitResult = mop as? BlockHitResult ?: return
                    val pos: BlockPos = blockHit.blockPos
                    val state: BlockState = level.getBlockState(pos)
                    val block: WeatheringCopper = state.block as? WeatheringCopper ?: return
                    block.changeOverTime(state, level, pos, level.random)
                }
            },
        )
    }
}
