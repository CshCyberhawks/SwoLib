package cshcyberhawks.swolib.autonomous.paths

import edu.wpi.first.wpilibj2.command.CommandBase

data class AttachedCommand(val command: CommandBase, val attachedCommandType: AttachedCommandType = AttachedCommandType.SYNC)