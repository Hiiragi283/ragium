package hiiragi283.lib.serialization.value

import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.common.util.ValueIOSerializable
import org.jetbrains.annotations.ApiStatus

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.14
 */
interface EmptyValueIOSerializable : ValueIOSerializable {
    @ApiStatus.NonExtendable
    override fun serialize(output: ValueOutput): Unit = Unit

    @ApiStatus.NonExtendable
    override fun deserialize(input: ValueInput): Unit = Unit
}
