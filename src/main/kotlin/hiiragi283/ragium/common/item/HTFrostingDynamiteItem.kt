package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.descriptions
import hiiragi283.ragium.common.entity.HTFrostingDynamiteEntity
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Position
import net.minecraft.world.World

class HTFrostingDynamiteItem(settings: Settings) : HTThrowableItem(settings.descriptions(RagiumTranslationKeys.FROSTING_DYNAMITE)) {
    override fun createEntity(world: World, user: LivingEntity): ProjectileEntity = HTFrostingDynamiteEntity(world, user)

    override fun createEntity(
        world: World,
        pos: Position,
        stack: ItemStack,
        direction: Direction,
    ): ProjectileEntity = HTFrostingDynamiteEntity(world, pos.x, pos.y, pos.z)
}
